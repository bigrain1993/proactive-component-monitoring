/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.body.migration;

import java.io.IOException;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;
import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.core.UniqueID;
import org.objectweb.proactive.core.body.BodyImpl;
import org.objectweb.proactive.core.body.MetaObjectFactory;
import org.objectweb.proactive.core.body.UniversalBody;
import org.objectweb.proactive.core.body.reply.ReplyReceiver;
import org.objectweb.proactive.core.body.request.RequestReceiver;
import org.objectweb.proactive.core.event.MigrationEventListener;
import org.objectweb.proactive.core.jmx.notification.NotificationType;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.core.runtime.ProActiveRuntime;
import org.objectweb.proactive.core.security.InternalBodySecurity;
import org.objectweb.proactive.core.security.SecurityContext;
import org.objectweb.proactive.core.security.crypto.Session;
import org.objectweb.proactive.core.security.exceptions.CommunicationForbiddenException;
import org.objectweb.proactive.core.security.exceptions.SecurityNotAvailableException;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;


public class MigratableBody extends BodyImpl implements Migratable, java.io.Serializable {

    /**
     * 
     */
    protected static Logger bodyLogger = ProActiveLogger.getLogger(Loggers.BODY);
    protected static Logger migrationLogger = ProActiveLogger.getLogger(Loggers.MIGRATION);

    //
    // -- PROTECTED MEMBERS -----------------------------------------------
    //

    /** The object responsible for the migration */
    protected MigrationManager migrationManager;

    /** signal that the body has just migrated */
    protected transient boolean hasJustMigrated;

    //
    // -- CONSTRUCTORS -----------------------------------------------
    //
    public MigratableBody() {
    }

    public MigratableBody(Object reifiedObject, String nodeURL, MetaObjectFactory factory)
            throws ActiveObjectCreationException {
        super(reifiedObject, nodeURL, factory);
        this.migrationManager = factory.newMigrationManagerFactory().newMigrationManager();
    }

    //
    // -- PUBLIC METHODS -----------------------------------------------
    //

    //
    // -- implements Migratable -----------------------------------------------
    //
    public boolean hasJustMigrated() {
        return this.hasJustMigrated;
    }

    public UniversalBody migrateTo(Node node) throws MigrationException {
        return internalMigrateTo(node, false);
    }

    public UniversalBody cloneTo(Node node) throws MigrationException {
        return internalMigrateTo(node, true);
    }

    public void addMigrationEventListener(MigrationEventListener listener) {
        if (migrationManager != null) {
            migrationManager.addMigrationEventListener(listener);
        }
    }

    public void removeMigrationEventListener(MigrationEventListener listener) {
        if (migrationManager != null) {
            migrationManager.removeMigrationEventListener(listener);
        }
    }

    public MigrationManager getMigrationManager() {
        return migrationManager;
    }

    //
    // -- PROTECTED METHODS -----------------------------------------------
    //

    /**
     * Signals that the activity of this body, managed by the active thread has just started.
     */
    @Override
    protected void activityStarted() {
        super.activityStarted();

        if (migrationLogger.isDebugEnabled()) {
            migrationLogger.debug("Body run on node " + nodeURL + " migration=" + hasJustMigrated);
        }
        if (bodyLogger.isDebugEnabled()) {
            bodyLogger.debug("Body run on node " + nodeURL + " migration=" + hasJustMigrated);
        }
        if (hasJustMigrated) {
            if (migrationManager != null) {
                migrationManager.startingAfterMigration(this);
            }
            hasJustMigrated = false;
        }
    }

    protected void setRequestReceiver(RequestReceiver requestReceiver) {
        this.requestReceiver = requestReceiver;
    }

    protected void setReplyReceiver(ReplyReceiver replyReceiver) {
        this.replyReceiver = replyReceiver;
    }

    protected void setHasMigrated() {
        this.hasJustMigrated = true;
    }

    protected RequestReceiver getRequestReceiver() {
        return this.requestReceiver;
    }

    protected ReplyReceiver getReplyReceiver() {
        return this.replyReceiver;
    }

    //
    // -- PRIVATE METHODS -----------------------------------------------
    //
    private UniversalBody internalMigrateTo(Node node, boolean byCopy) throws MigrationException {
        UniqueID savedID = null;
        UniversalBody migratedBody = null;

        /*
         * Waits until all the sending queues are empty It is not efficient to migrate with some
         * requests in the sending queue because we will have to serialize them two times (migration
         * then sending) while only one is useful (sending then migration)
         */
        if (super.sendingQueue != null) {
            try {
                super.getSendingQueue().waitForAllSendingQueueEmpty();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }

        if (!isAlive()) {
            throw new MigrationException("Attempt to migrate a dead body that has been terminated");
        }

        if (!isActive()) {
            throw new MigrationException("Attempt to migrate a non active body");
        }

        // PROACTIVE-277
        Class reifiedClass = this.localBodyStrategy.getReifiedObject().getClass();
        if (reifiedClass.isMemberClass() && !Modifier.isStatic(reifiedClass.getModifiers())) {
            // this active object has been turned active from an instance of a non static member class
            // cannot be migrated dur to reference to the enclosing instance
            throw new MigrationException("Attempt to migrate a instance of a non static member class");
        }

        // PROACTIVE-731
        if (this.requestReceiver.hasThreadsForImmediateService()) {
            // This active object has running threads for IS that cannot be migrated.
            throw new MigrationException("Cannot migrate an object with active immediate service threads");
        }

        try {
            // check node with Manager
            node = migrationManager.checkNode(node);
        } catch (MigrationException me) {
            // JMX Notification

            if (mbean != null) {
                mbean.sendNotification(NotificationType.migrationExceptionThrown, me);
            }

            // End JMX Notification
            throw me;
        }

        // get the name of the node
        String saveNodeURL = nodeURL;
        nodeURL = node.getNodeInformation().getURL();

        // security checks
        try {
            ProActiveRuntime runtimeDestination = node.getProActiveRuntime();

            if (this.securityManager != null) {
                Session session = this.securityManager.initiateSession(runtimeDestination);

                if (!session.getSecurityContext().isMigration()) {
                    ProActiveLogger.getLogger(Loggers.SECURITY).info(
                            "NOTE : Security manager forbids the migration");
                    return this;
                }
            } else {
                // no local security but need to check if distant runtime accepts migration
                SecurityContext scDistant = runtimeDestination.getPolicy(this.getEntities(),
                        runtimeDestination.getEntities());
                if (!scDistant.isMigration()) {
                    ProActiveLogger.getLogger(Loggers.SECURITY).info(
                            "NOTE : Security manager forbids the migration");
                    return this;
                }
            }
        } catch (SecurityNotAvailableException e1) {
            bodyLogger.debug("Security not available");
        } catch (CommunicationForbiddenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            nodeURL = node.getNodeInformation().getURL();

            // stop accepting communication
            blockCommunication();

            // save the id
            savedID = bodyID;
            if (byCopy) {
                // if moving by copy we have to create a new unique ID
                // the bodyID will be automatically recreate when deserialized
                bodyID = null;
            }

            // security
            // save opened sessions
            if (this.isSecurityOn) {
                openedSessions = securityManager.getOpenedConnexion();
            }

            // Set copyMode tag in all futures
            // those futures are going to be serialized for migration (i.e. no AC registration)
            this.getFuturePool().setCopyMode(true);

            // try to migrate
            migratedBody = migrationManager.migrateTo(node, this);

            if (isSecurityOn) {
                this.internalBodySecurity.setDistantBody(migratedBody);
            }

            // ****************************************************************
            // Javier dixit: This is the moment to update the location on the FT Manager
            // ****************************************************************
            if (this.ftmanager != null) {
                this.ftmanager.updateLocationAtServer(savedID, migratedBody);
            }
        } catch (MigrationException e) {
            openedSessions = null;
            nodeURL = saveNodeURL;
            bodyID = savedID;
            localBodyStrategy.getFuturePool().setCopyMode(false);
            if (this.isSecurityOn) {
                this.internalBodySecurity.setDistantBody(null);
            }
            acceptCommunication();
            throw e;
        }

        if (!byCopy) {
            this.migrationManager.changeBodyAfterMigration(this, migratedBody);
            activityStopped(false);
        } else {
            bodyID = savedID;
            nodeURL = saveNodeURL;
        }
        acceptCommunication();

        return migratedBody;
    }

    //
    // -- SERIALIZATION METHODS -----------------------------------------------
    //
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        if (migrationLogger.isDebugEnabled()) {
            migrationLogger.debug("stream =  " + out);
        }
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        if (migrationLogger.isDebugEnabled()) {
            migrationLogger.debug("stream =  " + in);
        }
        in.defaultReadObject();
        hasJustMigrated = true;
        if (this.isSecurityOn) {
            internalBodySecurity = new InternalBodySecurity(null);
            // securityManager.setBody(this);
        }
    }

    /*
     * @see org.objectweb.proactive.core.body.LocalBodyStrategy#getNextSequenceID()
     */
    public long getNextSequenceID() {
        return localBodyStrategy.getNextSequenceID();
    }
}

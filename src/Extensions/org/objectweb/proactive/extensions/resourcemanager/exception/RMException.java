/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2007 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 */
package org.objectweb.proactive.extensions.resourcemanager.exception;

import org.objectweb.proactive.extensions.resourcemanager.RMFactory;
import org.objectweb.proactive.extensions.resourcemanager.frontend.RMConnection;


/**
 * Exceptions Generated by the RM
 *
 * @see RMConnection
 * @see RMFactory
 *
 * @author ProActive team.
 * @version 3.9
 * @since ProActive 3.9
 *
 */
public class RMException extends Exception {

    /**
    * Attaches a message to the Exception
    * @param message message attached
    */
    public RMException(String message) {
        super(message);
    }

    /**
     * Empty constructor
     */
    public RMException() {
        super();
    }

    /**
     * Attaches a message and a cause to the Exception
     * @param message message attached
     * @param cause the cause
     */
    public RMException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Attaches a cause to the Exception
     * @param cause the cause
     */
    public RMException(Throwable cause) {
        super(cause);
    }
}
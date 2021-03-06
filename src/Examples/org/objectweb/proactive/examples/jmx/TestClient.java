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
package org.objectweb.proactive.examples.jmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.jmx.ProActiveConnection;
import org.objectweb.proactive.core.jmx.client.ClientConnector;
import org.objectweb.proactive.core.util.URIBuilder;


/**
 *  This example connects remotely a MBean Server and shows all registered MBeans on this server.
 * @author The ProActive Team
 *
 */
public class TestClient implements NotificationListener, Serializable {
    private transient ClientConnector cc;
    private transient ProActiveConnection connection;
    private transient JMXConnector connector;
    private String url;
    private static final String default_url = "//localhost/serverName";
    private ConnectionListener listener;

    public static void main(String[] args) {
        new TestClient();
    }

    /**
     * Default Constructor : read the url and connect to the MBean Server via The ProActive Connector.
     * When connected, gets and show  the JMX domains one can explore
     */
    public TestClient() {
        System.out.println("Enter the name of the JMX MBean Server :  [default is '" + default_url + "']");
        System.out.println("(Type \"exit\" to quit)");
        this.url = read();
        try {
            connect();
            getMBeanInformations();
        } catch (Exception e) {
            System.out
                    .println("Cannot contact the connector, have you started one ? (see connector.[sh|bat])");
            e.printStackTrace();
        }
    }

    private String read() {
        String what = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            what = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (what.equals("exit")) {
            System.out.println("Good Bye !");
            System.exit(0);
        }
        return what;
    }

    private void connect() throws Exception {
        if ("".equals(url.trim())) {
            url = default_url;
        }

        System.out.println("Connecting to : " + this.url);
        String serverName = URIBuilder.getNameFromURI(url);

        if ((serverName == null) || serverName.equals("")) {
            serverName = "serverName";
        }
        this.cc = new ClientConnector(this.url, serverName);
        this.cc.connect();
        this.connection = cc.getConnection();
        this.connector = cc.getConnector();
        /* add a connector listener */
        this.connector.addConnectionNotificationListener(this, null, null);

        this.listener = (ConnectionListener) PAActiveObject.newActive(ConnectionListener.class.getName(),
                new Object[] { this.connection });

    }

    private void domains() {
        System.out.println();
        System.out.println("Registered Domains :");
        String[] domains = (String[]) connection.getDomainsAsynchronous().getObject();
        for (int i = 0; i < domains.length; i++) {
            System.out.println("\t [ " + i + " ] " + domains[i]);
        }
        System.out.println();
        System.out.println("Type the domain number to see all registered MBeans in this domain :");
        String read = read();
        try {
            int index = Integer.parseInt(read);
            mbeans(domains[index]);
        } catch (Exception e) {
            System.out.println();
            System.out.println("Type the domain number to see all registered MBeans in this domain :");
            domains();
        }
    }

    private void mbeans(String domain) {
        try {
            ObjectName on = new ObjectName(domain + ":*");
            Set<ObjectInstance> queryMBeans = connection.queryMBeans(on, null);
            ObjectInstance[] beans = new ObjectInstance[queryMBeans.size()];
            queryMBeans.toArray(beans);
            //			Iterator<ObjectInstance> iterator = queryMBeans.iterator();
            for (int i = 0; i < beans.length; i++) {
                ObjectName beanName = beans[i].getObjectName();

                System.out.println(" [ " + i + " ] " + beanName);
            }
            System.out.println("[ D ]  Domains list");
            System.out.println();
            System.out.println("Type the mbean number to see its properties :");
            String read = read();
            assert (read != null);
            if (read.toLowerCase().trim().equals("d")) {
                domains();
                return;
            }

            try {
                int index = Integer.parseInt(read);
                beanProperties(beans[index].getObjectName());
            } catch (Exception e) {
                System.out.println();
                System.out.println("Type the mbean number to see its properties :");
                mbeans(domain);
            }
            mbeans(domain);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void beanProperties(ObjectName on) {
        System.out.println();
        System.out.println("-------------------- " + on + " ---------------------------- ");

        MBeanInfo beanInfo = (MBeanInfo) connection.getMBeanInfoAsynchronous(on).getObject();
        System.out.println("\tConstructors : ");
        System.out.println();
        MBeanConstructorInfo[] constructorInfos = beanInfo.getConstructors();
        for (int i = 0; i < constructorInfos.length; i++) {
            System.out.print(constructorInfos[i].getName() + " ( ");
            MBeanParameterInfo[] params = constructorInfos[i].getSignature();
            if (params.length == 0) {
                System.out.println(" );");
            } else {
                for (int j = 0; j < params.length; j++) {
                    if (j == (params.length - 1)) {
                        System.out.println(params[j].getType() + "  " + params[j].getName() + "  ); ");
                    } else {
                        System.out.println(params[j].getType() + "  " + params[j].getName() + "  , ");
                    }
                }
            }
        }

        String description = beanInfo.getDescription();
        System.out.println("\tDescription :\t" + description);
        System.out.println();
        System.out.println("\tAttributes :");
        MBeanAttributeInfo[] attribs = beanInfo.getAttributes();
        for (int i = 0; i < attribs.length; i++) {
            Object obj = connection.getAttributeAsynchronous(on, attribs[i].getName()).getObject();
            System.out.println("\t\t" + attribs[i].getDescription() + " =\t " + obj);
        }

        System.out.println();

        MBeanOperationInfo[] infos = beanInfo.getOperations();
        if (infos.length != 0) {
            System.out.println("\tOperations :");

            for (int i = 0; i < infos.length; i++) {
                System.out.println("Description = " + infos[i].getDescription());
                System.out.print("\t" + infos[i].getReturnType() + "  ");
                System.out.print(infos[i].getName() + "  ( ");

                MBeanParameterInfo[] params = infos[i].getSignature();
                if (params.length == 0) {
                    System.out.println(" );");
                } else {
                    for (int j = 0; j < params.length; j++) {
                        if (j == (params.length - 1)) {
                            System.out.print(params[j].getType() + "  " + params[j].getName() + " ) ");
                        } else {
                            System.out.print(params[j].getType() + "  " + params[j].getName() + " , ");
                        }
                    }
                    System.out.println();
                }
            }
        }
        System.out.println();

        MBeanNotificationInfo[] notifs = beanInfo.getNotifications();
        if (notifs.length != 0) {
            System.out.println("\tNotifications :");
            try {
                this.connection.addNotificationListener(on, listener, null, null);
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < notifs.length; i++) {
                System.out.println("\t\t" + notifs[i].getDescription() + " : \t\t" + notifs[i].getName());
            }
        }
    }

    private void getMBeanInformations() {
        domains();
    }

    /***********************************************************************************************
     * @see javax.management.NotificationListener#handleNotification(javax.management.Notification,
     *      java.lang.Object)
     */
    public void handleNotification(Notification notification, Object handback) {
        System.out.println("---> Notification = " + notification);
    }
}

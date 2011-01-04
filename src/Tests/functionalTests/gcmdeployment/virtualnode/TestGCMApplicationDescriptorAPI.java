/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2011 INRIA/University of
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
package functionalTests.gcmdeployment.virtualnode;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.extensions.gcmdeployment.PAGCMDeployment;
import org.objectweb.proactive.extensions.gcmdeployment.core.TopologyImpl;
import org.objectweb.proactive.gcmdeployment.GCMApplication;
import org.objectweb.proactive.gcmdeployment.GCMVirtualNode;
import org.objectweb.proactive.gcmdeployment.Topology;

import functionalTests.FunctionalTest;
import functionalTests.gcmdeployment.LocalHelpers;


public class TestGCMApplicationDescriptorAPI extends FunctionalTest {
    static GCMApplication gcma;

    @Before
    public void before() throws FileNotFoundException, ProActiveException {
        gcma = PAGCMDeployment.loadApplicationDescriptor(LocalHelpers.getDescriptor(this));
    }

    @Test
    public void test() throws ProActiveException, FileNotFoundException {

        Assert.assertFalse(gcma.isStarted());
        Assert.assertEquals(2, gcma.getVirtualNodes().size());

        gcma.startDeployment();
        LocalHelpers.waitAllocation();

        Assert.assertTrue(gcma.isStarted());
        Assert.assertEquals(2, gcma.getVirtualNodes().size());

        GCMVirtualNode vn1 = gcma.getVirtualNode("vn1");
        Assert.assertNotNull(vn1);
        List<Node> nodes = vn1.getCurrentNodes();

        // Check reachable
        for (Node node : nodes) {
            node.getActiveObjects();
        }

        gcma.kill();

        // Check unreachable
        for (Node node : nodes) {
            boolean exception = false;
            try {
                node.getActiveObjects();
            } catch (Throwable e) {
                exception = true;
            }
            Assert.assertTrue(exception);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionGetAllNode() {
        gcma.getAllNodes();
    }

    @Test(expected = ProActiveException.class)
    public void testExceptionGetTopology() throws ProActiveException {
        gcma.getTopology();
    }

    @Test(expected = ProActiveException.class)
    public void testExceptionUpdateTopology() throws ProActiveException {
        Topology t = new TopologyImpl();
        gcma.updateTopology(t);
    }

    @Test
    public void testGetVirtualNode() {
        GCMVirtualNode vn = gcma.getVirtualNode("IDontExist");
        Assert.assertNull(vn);
    }

}

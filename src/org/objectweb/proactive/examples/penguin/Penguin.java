/* 
* ################################################################
* 
* ProActive: The Java(TM) library for Parallel, Distributed, 
*            Concurrent computing with Security and Mobility
* 
* Copyright (C) 1997-2002 INRIA/University of Nice-Sophia Antipolis
* Contact: proactive-support@inria.fr
* 
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or any later version.
*  
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
* USA
*  
*  Initial developer(s):               The ProActive Team
*                        http://www.inria.fr/oasis/ProActive/contacts.html
*  Contributor(s): 
* 
* ################################################################
*/ 
package org.objectweb.proactive.examples.penguin;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.ProActive;
import org.objectweb.proactive.core.body.request.BlockingRequestQueue;
import org.objectweb.proactive.ext.migration.Destination;
import org.objectweb.proactive.ext.migration.MigrationStrategyImpl;


public class Penguin implements java.io.Serializable { //, ActivePenguin {

  private boolean onItinerary,initialized;
  private transient PenguinFrame myFrame;
  private PenguinControler controler;
  private Penguin otherPenguin;
  private javax.swing.ImageIcon face;
  private org.objectweb.proactive.ext.migration.MigrationStrategy myStrategy;
  private org.objectweb.proactive.ext.migration.MigrationStrategyManager myStrategyManager;
  private int index;
  private String name;


  /**
   * Empty constructor for ProActive
   */
  public Penguin() {
  }


  public Penguin(Integer ind) {
    this.index = ind.intValue();
    this.name = "Agent " + index;
  }


  public void loop() {
    rebuild();
  }


  public void rebuild() {
    Body body = ProActive.getBodyOnThis();
    myFrame = new PenguinFrame(face, body.getNodeURL(), index);
    //System.out.println("Penguin is here");
    this.sendMessageToControler();
    try {
      Thread.currentThread().sleep(2000);
    } catch (InterruptedException e) {
    }
  }


  public void clean() {
    if (myFrame != null) {
      myFrame.dispose();
      myFrame = null;
    }
  }


  public String toString() {
    return this.name;
  }


  public void initialize(String[] s) {
    try {
      //With this we can load the image from the same location than
      //the classes
      ClassLoader cl = this.getClass().getClassLoader();
      java.net.URL u = cl.getResource("org/objectweb/proactive/examples/penguin/PenguinSmall.jpg");
      face = new javax.swing.ImageIcon(u);
    } catch (Exception e) {
      e.printStackTrace();
    }
    myStrategyManager = new org.objectweb.proactive.ext.migration.MigrationStrategyManagerImpl(
            (org.objectweb.proactive.core.body.migration.Migratable) org.objectweb.proactive.ProActive.getBodyOnThis());
    myStrategyManager.onDeparture("clean");
    myStrategy = new MigrationStrategyImpl();
    for (int i = 0; i < s.length; i++)
      myStrategy.add(s[i], null);
  }


  public void setControler(PenguinControler c) {
    this.controler = c;
    this.initialized = true;
  }


  public void setOther(Penguin penguin) {
    //	System.out.println("setOther " + penguin);
    this.otherPenguin = penguin;
  }


  public Destination nextHop() {
    Destination r = myStrategy.next();
    if (r == null) {
      myStrategy.reset();
      r = myStrategy.next();
    }
    return r;
  }


  public void live(Body b) {
    BlockingRequestQueue queue = b.getRequestQueue();
    if (!initialized) {
      b.serve(queue.blockingRemoveOldest());
    }

    rebuild();
    Destination r = null;
    //first we empty the RequestQueue
    while (!queue.isEmpty()) {
      b.serve(queue.removeOldest());
    }
    while (b.isActive()) {
      if (onItinerary) {
        r = nextHop();
        try {
          ProActive.migrateTo(r.getDestination());
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        b.serve(queue.blockingRemoveOldest());
      }
    }
  }


  public void startItinerary() {
    myStrategy.reset();
    this.onItinerary = true;
  }


  public void stopItinerary() {
    this.onItinerary = false;
  }


  public void continueItinerary() {
    this.onItinerary = true;
  }


  public Message getPosition() {
    return new Message("I am working on node " + ProActive.getBodyOnThis().getNodeURL() + "\n");
  }


  public void callOther() {
    //	System.out.println("Other pinguin is " + this.otherPenguin);
    if (otherPenguin != null)
      this.otherPenguin.callOther();
  }


  public void sendMessageToControler() {
    if (controler != null)
      controler.receiveMessage(ProActive.getBodyOnThis().getNodeURL() + "\n");
  }


  public static void main(String[] args) {
    if (!(args.length > 1)) {
      System.out.println("Usage: java org.objectweb.proactive.examples.penguin.Penguin hostname1/NodeName1 hostname2/NodeName2 ");
      System.exit(-1);
    }
    try {
      Penguin n = (Penguin) org.objectweb.proactive.ProActive.newActive(Penguin.class.getName(), null);
      //	Penguin n2 = (Penguin)org.objectweb.proactive.ProActive.newActive(Penguin.class.getName(), null);
      n.initialize(args);
      //	n2.initialize(args);
      //	n.startItinerary();
      Object[] param = new Object[1];
      param[0] = n;
      PenguinControler controler = (PenguinControler) org.objectweb.proactive.ProActive.newActive(PenguinControler.class.getName(), param, (org.objectweb.proactive.core.node.Node) null);
      //	n.setOther(n2);
      //	n2.setOther(null);
      //	n2.startItinerary();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

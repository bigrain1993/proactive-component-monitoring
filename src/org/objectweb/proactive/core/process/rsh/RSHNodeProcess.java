/**
 * Created on 8 juil. 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */
package org.objectweb.proactive.core.process.rsh;

import org.objectweb.proactive.core.util.MessageLogger;

/**
 * @author rquilici
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class RSHNodeProcess extends RSHJVMProcess {

  /**
   * Constructor for RSHNodeProcess.
   */
  public RSHNodeProcess() {
    this(new StandardOutputMessageLogger());
    setClassname("org.objectweb.proactive.core.runtime.StartRuntime");
  }

  /**
   * Constructor for RSHNodeProcess.
   * @param messageLogger
   */
  public RSHNodeProcess(MessageLogger messageLogger) {
    super(messageLogger);
  }

  /**
   * Constructor for RSHNodeProcess.
   * @param inputMessageLogger
   * @param errorMessageLogger
   */
  public RSHNodeProcess(MessageLogger inputMessageLogger, MessageLogger errorMessageLogger) {
    super(inputMessageLogger, errorMessageLogger);
  }
  
//  protected void handleProcess(java.io.BufferedReader in, java.io.BufferedWriter out, java.io.BufferedReader err) {
//  String s = null;
//  try {
//      while ((s = in.readLine()) != null && (s.indexOf(NODEOK) == -1)) {
//        System.out.println("-----------------"+s);
//      }
//      } catch (Exception e) {
//      e.printStackTrace();
//      }
//   super.handleProcess(in,out,err);
//  }
}

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
package org.objectweb.proactive.core.mop;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Instances of this class represent method calls performed on reified
 * objects. They are generated by a <I>stub object</I>, whose role is to act
 * as a representative for the reified object.
 *
 * @author Julien Vayssi&egrave;re
 */
public final class MethodCall extends Object implements Serializable {

  /**
   * The array holding the argments of the method call
   */
  private Object[] effectiveArguments;
  
  /**
   * The method corresponding to the call
   */
  private Method reifiedMethod;
  
  /**
   * The hypothetic result
   */
  private Object res;
  
  /**
   * The internal ID of the methodcall
   */
  private long id;

  /**
   *	The size of the pool we use for recycling MethodCall objects.
   */
  //MODIFIE LE 25/10/99
  private static int RECYCLE_POOL_SIZE = 30;
  
  /**
   * The pool of recycled methodcall objects
   */
  private static MethodCall[] recyclePool;
  
  /**
   * Position inside the pool
   */
  private static int index;
  
  /**
   *	Indicates if the recycling of MethodCall object is on.
   */
  private static boolean recycleMethodCallObject;


  private static java.util.Hashtable table = new java.util.Hashtable();
 

  /**
   * Initializes the recycling of MethodCall objects to be enabled by default.
   */
  
  static {
    MethodCall.setRecycleMethodCallObject(true);
  }



  /**
   * Sets recycling of MethodCall objects on/off. Note that turning the recycling
   * off and on again results in the recycling pool being flushed, thus damaging
   * performances.
   *
   * @param value	sets the recycling on if <code>true</code>, otherwise turns it off.
   */
  public static synchronized void setRecycleMethodCallObject(boolean value) {
    if (MethodCall.recycleMethodCallObject == value)
      return;
    else {
      MethodCall.recycleMethodCallObject = value;
      if (value) {
        // Creates the recycle poll for MethodCall objects
        MethodCall.recyclePool = new MethodCall[RECYCLE_POOL_SIZE];
        MethodCall.index = 0;
      } else {
        // If we do not want to recycle MethodCall objects anymore,
        // let's free some memory by permitting the reyclePool to be
        // garbage-collecting
        MethodCall.recyclePool = null;
      }
    }
  }


  /**
   * Indicates if the recycling of MethodCall objects is currently running or not.
   * 
   * @return			<code>true</code> if recycling is on, <code>false</code> otherwise
   */
  public static synchronized boolean getRecycleMethodCallObject() {
    return MethodCall.recycleMethodCallObject;
  }


  /**
   *	Factory method for getting MethodCall objects
   *
   *	@param reifiedMethod a <code>Method</code> object that represents
   *	the method whose invocation is reified
   *	@param effectiveArguments   the effective arguments of the call. Arguments
   *	that are of primitive type need to be wrapped
   * 	within an instance of the corresponding wrapper
   *  class (like <code>java.lang.Integer</code> for
   *  primitive type <code>int</code> for example).
   *	@return	a MethodCall object representing an invocation of method
   *	<code>reifiedMethod</code> with arguments <code>effectiveArguments</code>
   */

  public synchronized static MethodCall getMethodCall(Method reifiedMethod, Object[] effectiveArguments) {
    if (MethodCall.getRecycleMethodCallObject()) {
      // Finds a recycled MethodCall object in the pool, cleans it and
      // eventually returns it
      if (MethodCall.index > 0) {
        MethodCall result;

        // gets the object from the pool
        MethodCall.index--;
        result = MethodCall.recyclePool[MethodCall.index];
        MethodCall.recyclePool[MethodCall.index] = null;

        // Refurbishes the object
        result.reifiedMethod = reifiedMethod;
        result.effectiveArguments = effectiveArguments;
        result.res = null;

        return result;
      } else
        return new MethodCall(reifiedMethod, effectiveArguments);
    } else {
      return new MethodCall(reifiedMethod, effectiveArguments);
    }
  }


  /**
   *	Tells the recyclying process that the MethodCall object passed as parameter
   *	is ready for recycling. It is the responsibility of the caller of this
   *	method to make sure that this object can safely be disposed of.
   */

  public synchronized static void setMethodCall(MethodCall mc) {
    if (MethodCall.getRecycleMethodCallObject()) {
      // If there's still one slot left in the pool
      if (MethodCall.recyclePool[MethodCall.index] == null) {
        // Cleans up a MethodCall object
        // It is prefereable to do it here rather than at the moment
        // the object is picked out of the pool, because it allows
        // garbage-collecting the objects referenced in here
        mc.reifiedMethod = null;
        mc.effectiveArguments = null;
        mc.res = null;
        // Inserts the object in the pool
        MethodCall.recyclePool[MethodCall.index] = mc;
        MethodCall.index++;
        if (MethodCall.index == RECYCLE_POOL_SIZE)
          MethodCall.index = RECYCLE_POOL_SIZE - 1;
      }
    }
  }


  /**
   *	Builds a new MethodCall object. This constructor is private to this class
   *	because we want to enforce the use of factory methods for getting fresh
   * instances of this class (see <I>Factory</I> pattern in GoF).
   */

  private MethodCall(Method reifiedMethod, Object[] effectiveArguments) {
    this.reifiedMethod = reifiedMethod;
    this.effectiveArguments = effectiveArguments;
    this.res = null;
  }


  /**
   *	Executes the instance method call represented by this object.
   *
   * @param targetObject	the Object the method is called on
   * @throws MethodCallExecutionFailedException thrown if the refleciton of the
   * call failed.
   * @throws InvocationTargetException thrown if the execution of the reified
   * method terminates abruptly by throwing an exception. The exception
   * thrown by the execution of the reified method is placed inside the
   * InvocationTargetException object.
   * @return the result of the invocation of the method. If the method returns
   * <code>void</code>, then <code>null</code> is returned. If the method
   * returned a primitive type, then it is wrapped inside the appropriate
   * wrapper object.
   */

  public Object execute(Object targetObject) throws InvocationTargetException, MethodCallExecutionFailedException {
    // A test at how non-public methods can be reflected
    if (reifiedMethod.getParameterTypes().length > 0)
      reifiedMethod.setAccessible(true);
    try {
      return reifiedMethod.invoke(targetObject, effectiveArguments);
    } catch (IllegalAccessException e) {
      throw new MethodCallExecutionFailedException("Access rights to the method denied: " + e);
    } catch (IllegalArgumentException e) {
      throw new MethodCallExecutionFailedException("Illegal method arguments: " + e);
    } catch (ExceptionInInitializerError e) {
      throw new MethodCallExecutionFailedException("Cannot invoke method because it triggered the initialization of a class that failed: " + e);
    }
  }

  protected void finalize() {
    MethodCall.setMethodCall(this);
  }

  public Method getReifiedMethod() {
    return reifiedMethod;
  }


  public String getName() {
    return this.reifiedMethod.getName();
  }


  public Object getParameter(int index) {
    return this.effectiveArguments[index];
  }


  /**
   * Make a deep copy of all arguments of the constructor
   */
  public void makeDeepCopyOfArguments() throws java.io.IOException {
    effectiveArguments = (Object[]) Utils.makeDeepCopy(effectiveArguments);
  }


  private Class[] fixBugRead(FixWrapper[] para) {
    Class[] tmp = new Class[para.length];
    //	System.out.println("--- fixBugRead started for " +  para.length +" values");
    for (int i = 0; i < para.length; i++) {
      //	System.out.println("fixBugRead for " + i + " value is " +para[i]);
      tmp[i] = para[i].getWrapped();
    }
    //	System.out.println("--- fixBugRead over");
    return tmp;
  }


  private FixWrapper[] fixBugWrite(Class[] para) {
    FixWrapper[] tmp = new FixWrapper[para.length];
    for (int i = 0; i < para.length; i++) {
      //	System.out.println("fixBugWrite for " + i + " out of " + para.length + " value is " +para[i] );	
      tmp[i] = new FixWrapper(para[i]);
    }
    //	System.out.println("fixBugWrite over");
    return tmp;
  }

  //  public void debugInt(Class[] para)
  //     {
  // 	for (int i = 0; i < para.length; i++)
  // 	    {
  // 	 	if (para[i].getName().equals("int"))
  //  		    {
  //  			para[i] = IntWrapper.class;
  //  		    }
  // 	    }
  //     }

  //     public void debugIntWrapper(Class[] para)
  //     {
  // 	for (int i = 0; i < para.length; i++)
  // 	    {
  // 		if (para[i].getName().equals("org.objectweb.proactive.core.mop.IntWrapper"))
  // 		    {
  // 			// para[i] = int.class;
  // 			para[i] = Integer.TYPE;
  // 		    }
  // 	    }
  //     }


  //
  // PRIVATE METHODS -----------------------------------------------------------------------
  //


  // This method needs to be rewritten with Class being now serializable (as of Java 2)
  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    Class declaringClass;
    String simpleName;
    Class[] parameters;

    //MODIFIE POUR FLORIAN
    //26-08-99

    // 	boolean exception;
    // 	do
    // 	    {
    // 		exception = false;
    // 		try {
    // We want to implement a workaround the Method class
    // not being Serializable
    // 	for (int i = 0; i < this.effectiveArguments.length; i++)
    // 	    {
    // 		System.out.println("Effective argument class = " + this.effectiveArguments[i].getClass());
    // 	    }
    out.writeObject(this.effectiveArguments);

      
    // The Method object needs to be converted
    declaringClass = this.reifiedMethod.getDeclaringClass();
    out.writeObject(declaringClass);

    simpleName = this.reifiedMethod.getName();
    out.writeObject(simpleName);

      
    //MODIFIED 14/02/01 BY FAB
    //	parameters = this.reifiedMethod.getParameterTypes ();
    //	this.debugInt(parameters);
    //out.writeObject(parameters);
    out.writeObject(this.fixBugWrite(this.reifiedMethod.getParameterTypes()));

    out.writeObject(this.res);
    out.writeLong(this.id);

      
    //	out.writeBoolean(this.forwarded);
    // 	} catch (Exception e) 
    // 		    {
    // 			System.out.println("WARNING MethodCall: Exception " +e);
    // 			exception = true;
    // 		    }
    // 	    } while (exception);
  }


  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    Class declaringClass;
    String simpleName;
    Class[] parameters;

    // Reads the effective arguments
    this.effectiveArguments = (Object[])in.readObject();

    // Reads several pieces of data that we need for looking
    // up the method
    declaringClass = (Class)in.readObject();
    simpleName = (String)in.readObject();

    //MODIFIED 14/02/01 BY FAB
    //parameters = (Class[]) in.readObject();
    // this.debugIntWrapper(parameters);
    parameters = this.fixBugRead((FixWrapper[])in.readObject());

  
    // Reads the res instance variable
    this.res = (Object)in.readObject();
    this.id = in.readLong();
        
    //this.forwarded=in.readBoolean();

    // Builds a key
    String key = declaringClass.getName() + simpleName;
    for (int i = 0; i < parameters.length; i++) {
      key = key + parameters[i].getName();
    }

    this.reifiedMethod = (Method)table.get(key);
    if (this.reifiedMethod == null) {
      // Looks up the method
      try {
        this.reifiedMethod = declaringClass.getMethod(simpleName, parameters);
        table.put(key, this.reifiedMethod);
      } catch (NoSuchMethodException e) {
        throw new InternalException("Lookup for method failed: " + e + ". This may be caused by having different versions of the same class on different VMs. Check your CLASSPATH settings.");
      }
    }
  }

}

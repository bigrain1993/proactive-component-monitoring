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
package org.objectweb.proactive.extensions.scheduler.common.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.objectweb.proactive.annotation.PublicAPI;
import org.objectweb.proactive.extensions.scheduler.common.exception.DependenceFailedException;
import org.objectweb.proactive.extensions.scheduler.common.job.GenericInformationsProvider;
import org.objectweb.proactive.extensions.scheduler.common.scripting.Script;
import org.objectweb.proactive.extensions.scheduler.common.scripting.SelectionScript;


/**
 * This class is the super class of the every task that can be integrated in a job.<br>
 * A task contains some properties that can be set but also : <ul>
 * <li>A selection script that can be used to select a specific execution node for this task.</li>
 * <li>A preScript that will be launched before the real task (can be used to set environment vars).</li>
 * <li>A postScript that will be launched just after the end of the real task.
 * (this can be used to unset vars you set in the preScript).</li>
 * </ul>
 * You will also be able to add dependences (if necessary) to
 * this task. The dependences mechanism are best describe below.
 *
 * @see #addDependence(Task)
 *
 * @author The ProActive Team
 * @version 3.9, Sept 14, 2007
 * @since ProActive 3.9
 */
@PublicAPI
public abstract class Task implements Serializable, GenericInformationsProvider {

    /** Number of nodes asked by the user. */
    protected int numberOfNodesNeeded = 1;

    /** Name of the task. */
    protected String name = TaskId.DEFAULT_TASK_NAME;

    /** Description of the task. */
    protected String description = null;

    /** Description of the result of the task */
    protected String resultPreview;

    /** Task user informations */
    protected HashMap<String, String> genericInformations = new HashMap<String, String>();

    /**
     * selection script : can be launched before getting a node in order to
     * verify some computer specificity.
     */
    protected SelectionScript selectionScript;

    /**
     * PreScript : can be used to launch script just before the task
     * execution.
     */
    protected Script<?> preScript;

    /**
     * PostScript : can be used to launch script just after the task
     * execution even if a problem occurs.
     */
    protected Script<?> postScript;

    /** Tell whether or not this task is re-runnable and how many times (0 if not, default 1) */
    protected int rerunnable = 1;

    /** Tell whether this task has a precious result or not. */
    protected boolean preciousResult;

    /** List of dependences if necessary. */
    protected ArrayList<Task> dependences = null;

    /** Restart the task if an error occurred. It will be restart according to the number of reRun remaining */
    protected RestartMode restartOnError = RestartMode.NOWHERE;

    /** whether user wants to use walltime - maximum execution time of the task */
    protected boolean isWallTime = false;

    /** maximum execution time of the task (in miliseconds), the variable is only valid if isWallTime is true */
    protected long wallTime = 0;

    /**
     * Add a dependence to the task. <font color="red">Warning : the dependence order is very
     * important.</font><br>
     * In fact, it is in this order that you will get back the result in the children task.<br>
     * For example : if you add to the task t3, the dependences t1 then t2, the parents of t3 will be t1 and t2 in this order
     * and the parameters of t3 will be the results of t1 and t2 in this order.
     *
     * @param task
     *            the parent task to add to this task.
     */
    public void addDependence(Task task) {
        if (dependences == null) {
            dependences = new ArrayList<Task>();
        }
        if (task instanceof ProActiveTask) {
            throw new DependenceFailedException("Cannot add a ProActive task in a dependence context !");
        }
        dependences.add(task);
    }

    /**
     * Same as the {@link #addDependence(Task)} method, but for a list of dependences.
     *
     * @param tasks
     *            the parent list of tasks to add to this task.
     */
    public void addDependences(List<Task> tasks) {
        if (dependences == null) {
            dependences = new ArrayList<Task>();
        }
        for (Task t : tasks) {
            addDependence(t);
        }
    }

    /**
     * To get the description of this task.
     *
     * @return the description of this task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * To set the description of this task.
     *
     * @param description
     *            the description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the result preview of this task.
     *
     * @return the result preview of this task.
     */
    public String getResultPreview() {
        return resultPreview;
    }

    /**
     * Set the result preview of this task.
     *
     * @param resultPreview
     *            the result preview  to set.
     */
    public void setResultPreview(String resultPreview) {
        this.resultPreview = resultPreview;
    }

    /**
     * To know if the result of this task is precious.
     *
     * @return true if the result is precious, false if not.
     */
    public boolean isPreciousResult() {
        return preciousResult;
    }

    /**
     * Set if the result of this task is precious.
     *
     * @param preciousResult true if the result of this task is precious, false if not.
     */
    public void setPreciousResult(boolean preciousResult) {
        this.preciousResult = preciousResult;
    }

    /**
     * To get the name of this task.
     *
     * @return the name of this task.
     */
    public String getName() {
        return name;
    }

    /**
     * To set the name of this task.
     *
     * @param name
     *            the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * To get the postScript of this task.
     *
     * @return the postScript of this task.
     */
    public Script<?> getPostScript() {
        return postScript;
    }

    /**
     * To set the postScript of this task.
     *
     * @param postScript
     *            the postScript to set.
     */
    public void setPostScript(Script<?> postScript) {
        this.postScript = postScript;
    }

    /**
     * To get the preScript of this task.
     *
     * @return the preScript of this task.
     */
    public Script<?> getPreScript() {
        return preScript;
    }

    /**
     * To set the preScript of this task.
     *
     * @param preScript
     *            the preScript to set.
     */
    public void setPreScript(Script<?> preScript) {
        this.preScript = preScript;
    }

    /**
     * To get number of times this task can be restart if an error occurs.
     *
     * @return the number of times this task can be restart.
     */
    public int getRerunnable() {
        return rerunnable;
    }

    /**
     * To set number of times this task can be restart if an error occurs.
     *
     * @param rerunnable
     *            the number of times this task can be restart.
     */
    public void setRerunnable(int rerunnable) {
        this.rerunnable = rerunnable;
    }

    /**
     * To get the selection script. This is the script that will select a node.
     *
     * @return the selection Script.
     */
    public SelectionScript getSelectionScript() {
        return selectionScript;
    }

    /**
     * To set the selection script.
     *
     * @param selectionScript
     *            the selectionScript to set.
     */
    public void setSelectionScript(SelectionScript selectionScript) {
        this.selectionScript = selectionScript;
    }

    /**
     * To get the list of dependences of the task.
     *
     * @return the the list of dependences of the task.
     */
    public ArrayList<Task> getDependencesList() {
        return dependences;
    }

    /**
     * Get the number of nodes needed for this task. (by default : 1)
     *
     * @return the number Of Nodes Needed
     */
    public int getNumberOfNodesNeeded() {
        return numberOfNodesNeeded;
    }

    /**
     * Returns the restartOnError state.
     * 
     * @return the restartOnError state.
     */
    public RestartMode getRestartOnError() {
        return restartOnError;
    }

    /**
     * Sets the restartOnError to the given restartOnError value.
     *
     * @param restartOnError the restartOnError to set.
     */
    public void setRestartOnError(RestartMode restartOnError) {
        this.restartOnError = restartOnError;
    }

    /**
     * @see org.objectweb.proactive.extensions.scheduler.common.job.GenericInformationsProvider#getGenericInformations()
     */
    public HashMap<String, String> getGenericInformations() {
        return genericInformations;
    }

    /**
     * @see org.objectweb.proactive.extensions.scheduler.common.job.GenericInformationsProvider#addGenericInformation(java.lang.String, java.lang.Object)
     */
    public void addGenericInformation(String key, String genericInformation) {
        this.genericInformations.put(key, genericInformation);
    }

    /**
     * @return the walltime
     */
    public long getWallTime() {
        return wallTime;
    }

    /**
     * @param walltime the walltime to set
     */
    public void setWallTime(long walltime) {
        this.wallTime = walltime;
        isWallTime = true;
    }

    /**
     * @param walltime the strWallTime argument will be parsed and the long value for the walltime will be set
     */
    public void setWallTime(String strWallTime) {
        // parsing string
        if (strWallTime == null || "".equals(strWallTime)) {
            // the value for walltime is incorrect, setting as though there was no walltime
            this.wallTime = 0;
            this.isWallTime = false;
        } else if (strWallTime.length() <= 2) { // length == 1 || length == 2
            // format: ss or s 
            this.wallTime = (new Long(Integer.parseInt(strWallTime) * 1000)).longValue();
            isWallTime = true;
        } else if (strWallTime.length() == 4) {
            // format: m:ss
            int seconds = Integer.parseInt(strWallTime.substring(2, 4));
            int minutes = Integer.parseInt(strWallTime.substring(0, 1));
            this.wallTime = new Long((minutes * 60 + seconds) * 1000);
            isWallTime = true;
        } else if (strWallTime.length() == 5) {
            // format: mm:ss
            int seconds = Integer.parseInt(strWallTime.substring(3, 5));
            int minutes = Integer.parseInt(strWallTime.substring(0, 2));
            this.wallTime = new Long((minutes * 60 + seconds) * 1000);
            isWallTime = true;
        } else if (strWallTime.length() == 7) {
            // format: h:mm:ss
            int seconds = Integer.parseInt(strWallTime.substring(5, 7));
            int minutes = Integer.parseInt(strWallTime.substring(2, 4));
            int hours = Integer.parseInt(strWallTime.substring(0, 1));
            this.wallTime = new Long((hours * 3600 + minutes * 60 + seconds) * 1000);
            isWallTime = true;
        } else if (strWallTime.length() == 8) {
            // format: hh:mm:ss
            int seconds = Integer.parseInt(strWallTime.substring(6, 8));
            int minutes = Integer.parseInt(strWallTime.substring(3, 5));
            int hours = Integer.parseInt(strWallTime.substring(0, 2));
            this.wallTime = new Long((hours * 3600 + minutes * 60 + seconds) * 1000);
            isWallTime = true;
        } else {
            isWallTime = false;
        }
    }

    /**
     * @return the isWallTime
     */
    public boolean isWallTime() {
        return isWallTime;
    }

    /**
     * @param isWallTime the isWallTime to set
     */
    public void setWallTime(boolean isWallTime) {
        this.isWallTime = isWallTime;
    }
}

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
package org.objectweb.proactive.extensions.masterworker.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.objectweb.proactive.extensions.masterworker.interfaces.Master;
import org.objectweb.proactive.extensions.masterworker.interfaces.SubMaster;
import org.objectweb.proactive.extensions.masterworker.interfaces.internal.ResultIntern;


/**
 * <i><font size="-1" color="#FF0000">**For internal use only** </font></i><br>
 * This class is responsible for keeping the completed results.<br/>
 * The class will serve the results in completion order mode or in submission order mode<br/>
 * @author The ProActive Team
 *
 */
public class ResultQueue<R extends Serializable> implements Serializable {

    /**
     * log4j logger of the master
     */
    private static final Logger logger = ProActiveLogger.getLogger(Loggers.MASTERWORKER);
    private static final boolean debug = logger.isDebugEnabled();

    /**
     * current ordering mode
     */
    private SubMaster.OrderingMode mode;

    /**
     * submitted tasks ids (ordered)
     */
    private TreeSet<Long> idsubmitted = new TreeSet<Long>();

    /**
     * sorted set of results (for submission order)
     */
    private SortedSet<ResultIntern<R>> orderedResults = new TreeSet<ResultIntern<R>>();

    /**
     * unordered list of results (for completion order)
     */
    private LinkedList<ResultIntern<R>> unorderedResults = new LinkedList<ResultIntern<R>>();

    /**
     * Creates the result queue with the given ordering mode
     * @param mode ordering mode
     */
    public ResultQueue(final Master.OrderingMode mode) {
        this.mode = mode;
    }

    /**
     * Adds a completed task to the queue
     * @param result result of the completed task
     */
    public void addCompletedTask(final ResultIntern<R> result) {
        if (mode == Master.COMPLETION_ORDER) {
            unorderedResults.add(result);
        } else {
            orderedResults.add(result);
        }
    }

    /**
     * Specifies that a new tasks have been submitted
     * @param taskId id of the pending task
     */
    public void addPendingTask(final Long taskId) {
        idsubmitted.add(taskId);
    }

    /**
     * Tells if all results are currently available in the queue
     * @return the answer
     */
    public boolean areAllResultsAvailable() {
        if (mode == Master.COMPLETION_ORDER) {
            return unorderedResults.size() == idsubmitted.size();
        } else {
            return orderedResults.size() == idsubmitted.size();
        }
    }

    /**
     * Clears the queue
     */
    public void clear() {
        unorderedResults.clear();
        orderedResults.clear();
        idsubmitted.clear();
    }

    /**
     * Count the number of results available for the current reception order
     * @return the number of results available for the current reception order
     */
    public int countAvailableResults() {
        if (mode == Master.COMPLETION_ORDER) {
            return unorderedResults.size();
        } else {
            int resultcount = 0;
            Iterator<ResultIntern<R>> it = orderedResults.iterator();
            Iterator<Long> it2 = idsubmitted.iterator();
            while (it.hasNext() && (it.next().getId() == it2.next())) {
                resultcount++;
            }

            return resultcount;
        }
    }

    /**
     * Counts the number of pending tasks
     * @return the number of pending tasks
     */
    public int countPendingResults() {
        return idsubmitted.size();
    }

    /**
     * Returns the results of all completed tasks (if and only if there are no pending tasks)
     * @return a list containing all completed tasks, if all tasks are completed
     */
    public List<ResultIntern<R>> getAll() {
        if (areAllResultsAvailable()) {

            int size = 0;

            Iterator<ResultIntern<R>> it;
            if (mode == Master.COMPLETION_ORDER) {
                it = unorderedResults.iterator();
                size = unorderedResults.size();
            } else {
                it = orderedResults.iterator();
                size = orderedResults.size();
            }
            List<ResultIntern<R>> answer = new ArrayList<ResultIntern<R>>(size);
            while (it.hasNext()) {
                ResultIntern<R> res = it.next();
                answer.add(res);
                it.remove();
                long taskId = res.getId();
                if (debug) {
                    logger.debug("Result " + taskId + " removed from the result queue.");
                }

                idsubmitted.remove(taskId);
            }

            return answer;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns the the result of the next completed task (depending of the current ResultReceptionOrder)
     * @return the result of the next completed task, if the next task in the current ResultReceptionOrder is available
     */
    public ResultIntern<R> getNext() {
        if (isOneResultAvailable()) {
            ResultIntern<R> answer;
            if (mode == Master.COMPLETION_ORDER) {
                answer = unorderedResults.poll();
            } else {
                Iterator<ResultIntern<R>> it = orderedResults.iterator();
                answer = it.next();
                it.remove();
            }

            idsubmitted.remove(answer.getId());
            return answer;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Returns the results of all k next completed task (depending of the current ResultReceptionOrder)
     * @param k number of compllocalJVMeted tasks to get
     * @return a list containing the results of the k next completed tasks, if the k next tasks in the current ResultReceptionOrder are available
     */
    public List<ResultIntern<R>> getNextK(final int k) {
        if (countAvailableResults() >= k) {
            List<ResultIntern<R>> answer = new ArrayList<ResultIntern<R>>(k);
            Iterator<ResultIntern<R>> it;
            if (mode == Master.COMPLETION_ORDER) {
                it = unorderedResults.iterator();
            } else {
                it = orderedResults.iterator();
            }

            int count = 0;
            while (it.hasNext() && (count < k)) {
                ResultIntern<R> res = it.next();
                answer.add(res);
                it.remove();
                count++;
                idsubmitted.remove(res.getId());
            }

            return answer;
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * Tells that the queue has neither results nor pending tasks
     * @return true if the result queue has neither results nor pending tasks, false otherwise
     */
    public boolean isEmpty() {
        boolean empty = idsubmitted.isEmpty();
        if (mode == Master.COMPLETION_ORDER) {
            return empty && unorderedResults.isEmpty();
        } else {
            return empty && orderedResults.isEmpty();
        }
    }

    /**
     * Tells that there is at least one result available for the current reception order
     * @return true if there is at least one result available for the current reception order, false otherwise
     */
    public boolean isOneResultAvailable() {
        if (mode == Master.COMPLETION_ORDER) {
            return !unorderedResults.isEmpty();
        } else {
            return !orderedResults.isEmpty() && (orderedResults.first().getId() == idsubmitted.first());
        }
    }

    /**
     * Changes the current result ordering mode
     * @param mode the new result ordering mode
     */
    public void setMode(final Master.OrderingMode mode) {
        if ((mode == Master.COMPLETION_ORDER) && (this.mode == Master.SUBMISSION_ORDER)) {
            Iterator<ResultIntern<R>> it = orderedResults.iterator();
            while (it.hasNext()) {
                ResultIntern<R> res = it.next();
                unorderedResults.add(res);
                it.remove();
            }
        } else if ((mode == Master.SUBMISSION_ORDER) && (this.mode == Master.COMPLETION_ORDER)) {
            Iterator<ResultIntern<R>> it = unorderedResults.iterator();
            while (it.hasNext()) {
                ResultIntern<R> res = it.next();
                orderedResults.add(res);
                it.remove();
            }
        }

        this.mode = mode;
    }

    public SubMaster.OrderingMode getMode() {
        return mode;
    }
}

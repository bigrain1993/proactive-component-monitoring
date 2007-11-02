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
package org.objectweb.proactive.ic2d.timit.actions.timeline;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.objectweb.proactive.ic2d.timit.editparts.timeline.TimeLineChartEditPart;


public class ClearTimeLineAction extends Action {
    public static final String CLEAR_TIMELINE_ACTION = "Clear TimeLine";
    private TimeLineChartEditPart durationChartEditPart;

    public ClearTimeLineAction() {
        super.setId(CLEAR_TIMELINE_ACTION);
        super.setImageDescriptor(ImageDescriptor.createFromFile(
                this.getClass(), "delete_obj.gif"));
        super.setToolTipText(CLEAR_TIMELINE_ACTION);
        super.setEnabled(false);
    }

    public final void setTarget(
        final TimeLineChartEditPart durationChartEditPart) {
        super.setEnabled(true);
        this.durationChartEditPart = durationChartEditPart;
    }

    @Override
    public final void run() {
        if (this.durationChartEditPart != null) {
            this.durationChartEditPart.removeAndClearAll();
        }
    }
}
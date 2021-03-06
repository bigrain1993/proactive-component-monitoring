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
package trywithcatch;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class TryCatch extends Anything {
    private Terminal tryTerminal;
    private Block tryBlock;
    private List<Catch> catchBlocks;
    private Block finallyBlock;
    public static boolean addPackageName = false;
    public static final String PACKAGE = "org.objectweb.proactive.api";
    private static final String TRY_WITH_CATCH = "PAException.tryWithCatch";
    private static final String END_TRY_WITH_CATCH = "PAException.endTryWithCatch();";
    private static final String REMOVE_TRY_WITH_CATCH = "PAException.removeTryWithCatch();";

    public TryCatch(Terminal tt, Block tb, List<Catch> cb, Block fb) {
        tryTerminal = tt;
        tryBlock = tb;
        catchBlocks = cb;
        finallyBlock = fb;
    }

    public static void setAddPackageName(boolean value) {
        addPackageName = value;
    }

    private String getPackageName() {
        return addPackageName ? (PACKAGE + ".") : "";
    }

    @Override
    public String toString() {
        return "" + tryTerminal + tryBlock + catchBlocks + finallyBlock;
    }

    @Override
    protected void prettyPrint(int indent) {
        super.prettyPrint(indent);
        System.out.print("tryWithCatch (");
        for (Catch c : catchBlocks) {
            System.out.print(c.getClassName() + ", ");
        }
        System.out.println(")");

        tryTerminal.prettyPrint(indent);

        tryBlock.prettyPrint(indent);

        super.prettyPrint(indent);
        System.out.print("catch blocks:");
        for (Catch c : catchBlocks) {
            System.out.print(c.getBlock() + ", ");
        }

        if (finallyBlock != null) {
            System.out.println();
            super.prettyPrint(indent);
            System.out.print("finally: " + finallyBlock);
        }

        System.out.println();
    }

    private String getTryWithCatchCall() {
        String call = getPackageName() + TRY_WITH_CATCH + "(";
        Catch firstCatch = catchBlocks.get(0);
        if (catchBlocks.size() == 1) {
            call += ("" + firstCatch.getClassName());
        } else {
            call += ("new Class[] {" + firstCatch.getClassName());
            Iterator<Catch> iter = catchBlocks.iterator();
            iter.next(); // The first one is already done
            while (iter.hasNext()) {
                Catch ca = iter.next();
                call += (", " + ca.getClassName());
            }
            call += "}";
        }

        return call + ");";
    }

    @Override
    public void work(Catcher c) throws IOException {
        if (!catchBlocks.isEmpty()) {
            String call = getTryWithCatchCall();
            String indent = Catcher.getNewline(tryTerminal);
            c.addAtOffset(tryTerminal.getLeft(), call + indent);
        }

        tryBlock.work(c);
        String indent = Catcher.getNewline(tryBlock.getEnd());
        c.addAtOffset(tryBlock.getEnd().getLeft(), Catcher.INDENT + getPackageName() + END_TRY_WITH_CATCH +
            indent);

        Iterator<Catch> iter = catchBlocks.iterator();
        Catch ca = null;
        while (iter.hasNext()) {
            ca = iter.next();
            ca.work(c);
        }

        if (finallyBlock == null) {
            Block lastBlock;
            if (ca != null) {
                lastBlock = ca.getBlock();
            } else {
                lastBlock = tryBlock;
            }

            indent = Catcher.getNewline(lastBlock.getEnd());
            c.addAtOffset(lastBlock.getEnd().getRight(), " finally {" + indent + Catcher.INDENT +
                getPackageName() + REMOVE_TRY_WITH_CATCH + indent + "}");
        } else {
            indent = Catcher.getNewline(finallyBlock.getEnd()) + Catcher.INDENT;
            c.addAtOffset(finallyBlock.getStart().getRight(), indent + getPackageName() +
                REMOVE_TRY_WITH_CATCH);
            finallyBlock.work(c);
        }
    }
}

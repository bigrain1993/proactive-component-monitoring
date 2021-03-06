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
package org.objectweb.proactive.extensions.calcium.examples.blast;

import java.io.File;
import java.net.MalformedURLException;


public class BlastParams implements java.io.Serializable {
    public File formatProg;
    public File blastProg;
    public File queryFile; //query file
    public File dbFile; //database file

    //FormatDB
    private boolean isNucleotide; //-p F Input is a nucleotide, not a protein.
    int divideDBInto; //Number of parts the database should be divided into
    long maxDBSize; //maximum database size to accept

    public BlastParams(File queryFile, File dbFile, File formatProg, File blastProg, boolean isNucleotide,
            long maxDBSize) throws MalformedURLException {
        this.formatProg = formatProg;
        this.blastProg = blastProg;

        this.queryFile = queryFile;
        this.dbFile = dbFile;
        this.isNucleotide = isNucleotide;

        this.divideDBInto = 2;
        this.maxDBSize = maxDBSize;

        //this.queryIndexFiles = null;
    }

    public BlastParams(BlastParams param) throws MalformedURLException {
        this(param.queryFile, param.dbFile, param.formatProg, param.blastProg, param.isNucleotide,
                param.maxDBSize);
    }

    public String getBlastParemeterString(File outFile) {
        //-p blastn|blastp|blastx
        return "-p blastn" + " -d " + dbFile.getAbsolutePath() + " -i " + queryFile.getAbsoluteFile() +
            " -o " + outFile.getAbsolutePath();
    }

    public String getFormatQueryString() {
        return getFormatParemeterString(queryFile);
    }

    public String getFormatDBString() {
        return getFormatParemeterString(dbFile);
    }

    private String getFormatParemeterString(File inputFile) {
        return "-i " + inputFile.getAbsoluteFile() + (isNucleotide ? " -p F " : " -o T");
    }
}

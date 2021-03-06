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
package org.objectweb.proactive.core.xml.handler;

import org.objectweb.proactive.core.xml.io.Attributes;


/**
 *
 * Receives SAX event and pass them on
 *
 * @author The ProActive Team
 * @version      0.91
 *
 */
public class BasicUnmarshaller implements UnmarshallerHandler {
    protected Object resultObject;
    protected boolean isResultValid = true;

    //
    // -- CONSTRUCTORS -----------------------------------------------
    //  
    public BasicUnmarshaller() {
    }

    //
    // -- PUBLIC METHODS -----------------------------------------------
    //
    //
    // -- implements UnmarshallerHandler ------------------------------------------------------
    //  
    public Object getResultObject() throws org.xml.sax.SAXException {
        if (!isResultValid) {
            throw new org.xml.sax.SAXException("The result object is not valid");
        }
        Object o = resultObject;
        resultObject = null;
        isResultValid = false;
        return o;
    }

    public void startContextElement(String name, Attributes attributes) throws org.xml.sax.SAXException {
    }

    //
    // -- implements XMLHandler ------------------------------------------------------
    //  
    public void startElement(String name, Attributes attributes) throws org.xml.sax.SAXException {
    }

    public void endElement(String name) throws org.xml.sax.SAXException {
    }

    public void readValue(String value) throws org.xml.sax.SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws org.xml.sax.SAXException {
    }

    public void endPrefixMapping(String prefix) throws org.xml.sax.SAXException {
    }

    //
    // -- PROTECTED METHODS ------------------------------------------------------
    //
    protected void setResultObject(Object value) {
        isResultValid = true;
        resultObject = value;
    }

    protected boolean checkNonEmpty(String s) {
        return (s != null) && (s.length() > 0);
    }

    //
    // -- PRIVATE METHODS ------------------------------------------------------
    //
}

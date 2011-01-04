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
package trywithcatch;

/* The following code was generated by JFlex 1.4.1 on 23/09/05 17:08 */

import trywithcatch.java_cup.runtime.Symbol;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1
 * on 23/09/05 17:08 from the specification file
 * <tt>java.lex</tt>
 */
class Yylex implements trywithcatch.java_cup.runtime.Scanner {

    /** This character denotes the end of file */
    public static final int YYEOF = -1;

    /** initial size of the lookahead buffer */
    private static final int ZZ_BUFFERSIZE = 16384;

    /** lexical states */
    public static final int COMMENT_CPP = 2;
    public static final int STRING = 3;
    public static final int YYINITIAL = 0;
    public static final int COMMENT_C = 1;

    /** 
     * Translates characters to character classes
     */
    private static final String ZZ_CMAP_PACKED = "\12\0\1\37\27\0\1\17\1\0\1\34\5\0\1\16\3\0\1\26"
        + "\1\15\12\35\7\0\1\23\1\36\1\30\14\36\1\21\3\36\1\32"
        + "\2\36\1\27\3\36\1\0\1\40\2\0\1\36\1\0\1\5\1\20"
        + "\1\4\1\31\1\25\1\7\1\20\1\6\1\10\2\20\1\12\1\33"
        + "\1\11\1\22\2\20\1\2\1\20\1\1\1\20\1\24\2\20\1\3" + "\1\20\1\13\1\0\1\14\uff82\0";

    /** 
     * Translates characters to character classes
     */
    private static final char[] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

    /** 
     * Translates DFA states to action switch labels.
     */
    private static final int[] ZZ_ACTION = zzUnpackAction();

    private static final String ZZ_ACTION_PACKED_0 = "\4\0\1\1\4\2\1\3\1\4\1\1\1\5\2\2"
        + "\2\1\1\6\1\1\3\2\1\7\1\10\1\2\2\0" + "\1\11\3\2\1\0\3\2\1\0\1\12\2\2\1\0"
        + "\2\2\1\0\1\13\1\2\1\0\1\2\1\0\1\2" + "\1\0\1\2\1\0\3\2\1\0\3\2\3\0\3\2"
        + "\3\0\3\2\3\0\3\2\3\0\2\2\3\0\2\2" + "\2\0\2\2\2\0\2\2\2\0\2\2\2\0\2\2"
        + "\2\0\1\14\1\2\2\0\1\2\1\14\1\0\1\2" + "\1\0\1\15\1\0\1\15";

    private static int[] zzUnpackAction() {
        int[] result = new int[117];
        int offset = 0;
        offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAction(String packed, int offset, int[] result) {
        int i = 0; /* index in packed string  */
        int j = offset; /* index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do
                result[j++] = value;
            while (--count > 0);
        }
        return j;
    }

    /** 
     * Translates a state to a row index in the transition table
     */
    private static final int[] ZZ_ROWMAP = zzUnpackRowMap();

    private static final String ZZ_ROWMAP_PACKED_0 = "\0\0\0\41\0\102\0\143\0\204\0\245\0\306\0\347"
        + "\0\u0108\0\204\0\204\0\u0129\0\204\0\u014a\0\u016b\0\u018c"
        + "\0\u01ad\0\204\0\u01ce\0\u01ef\0\u0210\0\u0231\0\204\0\204"
        + "\0\u0252\0\u018c\0\u0273\0\306\0\u0294\0\u02b5\0\u02d6\0\u02f7"
        + "\0\u0318\0\u0339\0\u035a\0\u037b\0\306\0\u039c\0\u03bd\0\u03de"
        + "\0\u03ff\0\u0420\0\u0441\0\306\0\u0462\0\u0483\0\u04a4\0\u04c5"
        + "\0\u04e6\0\u0507\0\u0528\0\u0549\0\u056a\0\u058b\0\u05ac\0\u05cd"
        + "\0\u05ee\0\u060f\0\u0630\0\u0651\0\u0672\0\u0693\0\u06b4\0\u06d5"
        + "\0\u06f6\0\u0717\0\u0738\0\u0759\0\u077a\0\u079b\0\u07bc\0\u07dd"
        + "\0\u07fe\0\u081f\0\u0840\0\u0861\0\u0882\0\u08a3\0\u08c4\0\u08e5"
        + "\0\u0906\0\u0927\0\u0948\0\u0969\0\u098a\0\u09ab\0\u09cc\0\u09ed"
        + "\0\u0a0e\0\u0a2f\0\u0a50\0\u0a71\0\u0a92\0\u0ab3\0\u0ad4\0\u0af5"
        + "\0\u0b16\0\u0b37\0\u0b58\0\u0b79\0\u0b9a\0\u0bbb\0\u0bdc\0\u0bfd"
        + "\0\u0c1e\0\u016b\0\u0c3f\0\u0c60\0\u0c81\0\u0ca2\0\204\0\u0cc3"
        + "\0\u0ce4\0\u0d05\0\u016b\0\u0d26\0\204";

    private static int[] zzUnpackRowMap() {
        int[] result = new int[117];
        int offset = 0;
        offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackRowMap(String packed, int offset, int[] result) {
        int i = 0; /* index in packed string  */
        int j = offset; /* index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int high = packed.charAt(i++) << 16;
            result[j++] = high | packed.charAt(i++);
        }
        return j;
    }

    /** 
     * The transition table of the DFA
     */
    private static final int[] ZZ_TRANS = zzUnpackTrans();

    private static final String ZZ_TRANS_PACKED_0 = "\1\5\1\6\2\7\1\10\2\7\1\11\3\7\1\12"
        + "\1\13\1\14\1\5\1\15\1\7\1\16\1\7\1\17" + "\2\7\1\20\2\17\1\7\1\17\1\7\1\17\1\5"
        + "\1\17\20\5\1\21\61\5\1\22\20\5\1\22\20\5" + "\1\23\42\0\1\7\1\24\10\7\5\0\1\7\1\16"
        + "\1\7\1\17\3\7\2\17\1\7\1\17\1\7\1\0" + "\2\17\3\0\12\7\5\0\1\7\1\16\1\7\1\17"
        + "\3\7\2\17\1\7\1\17\1\7\1\0\2\17\3\0" + "\4\7\1\25\5\7\5\0\1\7\1\16\1\7\1\17"
        + "\3\7\2\17\1\7\1\17\1\7\1\0\2\17\3\0" + "\7\7\1\26\2\7\5\0\1\7\1\16\1\7\1\17"
        + "\3\7\2\17\1\7\1\17\1\7\1\0\2\17\17\0" + "\1\27\1\30\23\0\1\17\1\31\10\17\5\0\14\17"
        + "\1\0\2\17\3\0\12\17\5\0\14\17\1\0\2\17" + "\3\0\12\32\5\0\1\32\1\33\1\32\1\0\3\32"
        + "\2\0\1\32\1\0\1\32\22\0\1\22\42\0\1\5" + "\22\0\2\7\1\34\7\7\5\0\1\7\1\16\1\7"
        + "\1\17\3\7\2\17\1\7\1\17\1\7\1\0\2\17" + "\3\0\1\35\11\7\5\0\1\7\1\16\1\7\1\17"
        + "\3\7\2\17\1\7\1\17\1\7\1\0\2\17\3\0" + "\10\7\1\36\1\7\5\0\1\7\1\16\1\7\1\17"
        + "\3\7\2\17\1\7\1\17\1\7\1\0\2\17\3\0" + "\12\17\5\0\2\17\1\37\11\17\1\0\2\17\4\0"
        + "\1\40\37\0\3\7\1\41\6\7\5\0\1\7\1\16" + "\1\7\1\17\3\7\2\17\1\7\1\17\1\7\1\0"
        + "\2\17\3\0\4\7\1\42\5\7\5\0\1\7\1\16" + "\1\7\1\17\3\7\2\17\1\7\1\17\1\7\1\0"
        + "\2\17\3\0\12\17\5\0\3\17\1\43\10\17\1\0" + "\2\17\24\0\1\44\17\0\5\7\1\45\4\7\5\0"
        + "\1\7\1\16\1\7\1\17\3\7\2\17\1\7\1\17" + "\1\7\1\0\2\17\3\0\11\7\1\46\5\0\1\7"
        + "\1\16\1\7\1\17\3\7\2\17\1\7\1\17\1\7" + "\1\0\2\17\3\0\3\17\1\47\6\17\5\0\14\17"
        + "\1\0\2\17\25\0\1\50\16\0\11\7\1\51\5\0" + "\1\7\1\16\1\7\1\17\3\7\2\17\1\7\1\17"
        + "\1\7\1\0\2\17\3\0\1\52\11\17\5\0\14\17" + "\1\0\2\17\6\0\1\53\35\0\2\7\1\54\7\7"
        + "\5\0\1\7\1\16\1\7\1\17\3\7\2\17\1\7" + "\1\17\1\7\1\0\2\17\3\0\7\17\1\55\2\17"
        + "\5\0\14\17\1\0\2\17\3\0\1\56\40\0\12\17" + "\5\0\4\17\1\57\7\17\1\0\2\17\12\0\1\60"
        + "\31\0\12\17\5\0\5\17\1\61\6\17\1\0\2\17" + "\26\0\1\62\15\0\12\17\5\0\6\17\1\63\5\17"
        + "\1\0\2\17\27\0\1\64\14\0\1\65\1\66\10\17" + "\5\0\5\17\1\67\6\17\1\0\2\17\30\0\1\70"
        + "\13\0\1\17\1\71\10\17\5\0\14\17\1\0\2\17" + "\3\0\12\17\5\0\5\17\1\72\6\17\1\0\2\17"
        + "\3\0\10\17\1\73\1\17\5\0\14\17\1\0\2\17" + "\3\0\1\74\1\75\22\0\1\76\14\0\2\17\1\77"
        + "\7\17\5\0\14\17\1\0\2\17\3\0\12\17\5\0" + "\13\17\1\100\1\0\2\17\3\0\12\17\5\0\11\17"
        + "\1\101\2\17\1\0\2\17\4\0\1\102\63\0\1\103" + "\24\0\1\104\30\0\12\17\5\0\7\17\1\105\4\17"
        + "\1\0\2\17\3\0\12\17\5\0\2\17\1\106\11\17" + "\1\0\2\17\3\0\12\17\5\0\12\17\1\107\1\17"
        + "\1\0\2\17\5\0\1\110\70\0\1\111\36\0\1\112" + "\10\0\7\17\1\113\2\17\5\0\14\17\1\0\2\17"
        + "\3\0\12\17\5\0\4\17\1\114\7\17\1\0\2\17" + "\3\0\1\17\1\115\10\17\5\0\14\17\1\0\2\17"
        + "\31\0\1\116\33\0\1\117\50\0\1\120\7\0\1\121" + "\11\17\5\0\14\17\1\0\2\17\3\0\12\17\5\0"
        + "\5\17\1\101\6\17\1\0\2\17\3\0\2\17\1\122" + "\7\17\5\0\14\17\1\0\2\17\12\0\1\123\54\0"
        + "\1\124\16\0\1\125\37\0\5\17\1\126\4\17\5\0" + "\14\17\1\0\2\17\3\0\12\17\5\0\7\17\1\127"
        + "\4\17\1\0\2\17\3\0\1\130\64\0\1\112\16\0" + "\1\131\36\0\12\17\5\0\10\17\1\132\3\17\1\0"
        + "\2\17\3\0\7\17\1\133\2\17\5\0\14\17\1\0" + "\2\17\10\0\1\134\61\0\1\135\12\0\4\17\1\136"
        + "\5\17\5\0\14\17\1\0\2\17\3\0\1\137\11\17" + "\5\0\14\17\1\0\2\17\32\0\1\140\20\0\1\141"
        + "\31\0\1\142\11\17\5\0\14\17\1\0\2\17\3\0" + "\5\17\1\143\4\17\5\0\14\17\1\0\2\17\7\0"
        + "\1\144\34\0\1\145\40\0\3\17\1\146\6\17\5\0" + "\14\17\1\0\2\17\3\0\12\17\5\0\10\17\1\147"
        + "\3\17\1\0\2\17\3\0\1\150\45\0\1\151\33\0" + "\5\17\1\152\4\17\5\0\14\17\1\0\2\17\3\0"
        + "\4\17\1\153\5\17\5\0\14\17\1\0\2\17\6\0" + "\1\154\64\0\1\155\11\0\1\156\11\17\5\0\14\17"
        + "\1\0\2\17\10\0\1\157\37\0\1\160\34\0\3\17" + "\1\161\6\17\5\0\14\17\1\0\2\17\3\0\1\162"
        + "\40\0\5\17\1\163\4\17\5\0\14\17\1\0\2\17" + "\6\0\1\164\42\0\1\165\32\0";

    private static int[] zzUnpackTrans() {
        int[] result = new int[3399];
        int offset = 0;
        offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackTrans(String packed, int offset, int[] result) {
        int i = 0; /* index in packed string  */
        int j = offset; /* index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            value--;
            do
                result[j++] = value;
            while (--count > 0);
        }
        return j;
    }

    /* error codes */
    private static final int ZZ_UNKNOWN_ERROR = 0;
    private static final int ZZ_NO_MATCH = 1;
    private static final int ZZ_PUSHBACK_2BIG = 2;

    /* error messages for the codes above */
    private static final String ZZ_ERROR_MSG[] = { "Unkown internal scanner error",
            "Error: could not match input", "Error: pushback value was too large" };

    /**
     * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
     */
    private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();

    private static final String ZZ_ATTRIBUTE_PACKED_0 = "\4\0\1\11\4\1\2\11\1\1\1\11\4\1\1\11"
        + "\4\1\2\11\1\1\2\0\4\1\1\0\3\1\1\0" + "\3\1\1\0\2\1\1\0\2\1\1\0\1\1\1\0"
        + "\1\1\1\0\1\1\1\0\3\1\1\0\3\1\3\0" + "\3\1\3\0\3\1\3\0\3\1\3\0\2\1\3\0"
        + "\2\1\2\0\2\1\2\0\2\1\2\0\2\1\2\0" + "\2\1\2\0\2\1\2\0\1\1\1\11\1\0\1\1" + "\1\0\1\1\1\0\1\11";

    private static int[] zzUnpackAttribute() {
        int[] result = new int[117];
        int offset = 0;
        offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
        return result;
    }

    private static int zzUnpackAttribute(String packed, int offset, int[] result) {
        int i = 0; /* index in packed string  */
        int j = offset; /* index in unpacked array */
        int l = packed.length();
        while (i < l) {
            int count = packed.charAt(i++);
            int value = packed.charAt(i++);
            do
                result[j++] = value;
            while (--count > 0);
        }
        return j;
    }

    /** the input device */
    private java.io.Reader zzReader;

    /** the current state of the DFA */
    private int zzState;

    /** the current lexical state */
    private int zzLexicalState = YYINITIAL;

    /** this buffer contains the current text to be matched and is
        the source of the yytext() string */
    private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

    /** the textposition at the last accepting state */
    private int zzMarkedPos;

    /** the textposition at the last state to be included in yytext */
    private int zzPushbackPos;

    /** the current text position in the buffer */
    private int zzCurrentPos;

    /** startRead marks the beginning of the yytext() string in the buffer */
    private int zzStartRead;

    /** endRead marks the last character in the buffer, that has been read
        from input */
    private int zzEndRead;

    /** number of newlines encountered up to the start of the matched text */
    private int yyline;

    /** the number of characters up to the start of the matched text */
    private int yychar;

    /**
     * the number of characters from the last newline up to the start of the 
     * matched text
     */
    private int yycolumn;

    /** 
     * zzAtBOL == true <=> the scanner is currently at the beginning of a line
     */
    private boolean zzAtBOL = true;

    /** zzAtEOF == true <=> the scanner is at the EOF */
    private boolean zzAtEOF;

    /** denotes if the user-EOF-code has already been executed */
    private boolean zzEOFDone;

    /* user code: */
    private boolean useNextIdent;

    private Symbol newSymbol(int id) {
        int left = yychar;
        int right = left + yylength();
        return new Symbol(id, left, yycolumn, new Terminal(left, right, yycolumn, yytext()));
    }

    private Symbol newIdent() {
        /*
         * We put '.' in the identifier to include the package name.
         * We only support ASCII characters.
         */
        if (useNextIdent) {
            useNextIdent = false;
            return new Symbol(sym.IDENT, yychar, yychar + yylength(), yytext());
        }

        return null;
    }

    /**
     * Creates a new scanner
     * There is also a java.io.InputStream version of this constructor.
     *
     * @param   in  the java.io.Reader to read input from.
     */
    Yylex(java.io.Reader in) {
        this.zzReader = in;
    }

    /**
     * Creates a new scanner.
     * There is also java.io.Reader version of this constructor.
     *
     * @param   in  the java.io.Inputstream to read input from.
     */
    Yylex(java.io.InputStream in) {
        this(new java.io.InputStreamReader(in));
    }

    /** 
     * Unpacks the compressed character translation table.
     *
     * @param packed   the packed character translation table
     * @return         the unpacked character translation table
     */
    private static char[] zzUnpackCMap(String packed) {
        char[] map = new char[0x10000];
        int i = 0; /* index in packed string  */
        int j = 0; /* index in unpacked array */
        while (i < 110) {
            int count = packed.charAt(i++);
            char value = packed.charAt(i++);
            do
                map[j++] = value;
            while (--count > 0);
        }
        return map;
    }

    /**
     * Refills the input buffer.
     *
     * @return      <code>false</code>, iff there was new input.
     * 
     * @exception   java.io.IOException  if any I/O-Error occurs
     */
    private boolean zzRefill() throws java.io.IOException {

        /* first: make room (if you can) */
        if (zzStartRead > 0) {
            System.arraycopy(zzBuffer, zzStartRead, zzBuffer, 0, zzEndRead - zzStartRead);

            /* translate stored positions */
            zzEndRead -= zzStartRead;
            zzCurrentPos -= zzStartRead;
            zzMarkedPos -= zzStartRead;
            zzPushbackPos -= zzStartRead;
            zzStartRead = 0;
        }

        /* is the buffer big enough? */
        if (zzCurrentPos >= zzBuffer.length) {
            /* if not: blow it up */
            char newBuffer[] = new char[zzCurrentPos * 2];
            System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
            zzBuffer = newBuffer;
        }

        /* finally: fill the buffer with new input */
        int numRead = zzReader.read(zzBuffer, zzEndRead, zzBuffer.length - zzEndRead);

        if (numRead < 0) {
            return true;
        } else {
            zzEndRead += numRead;
            return false;
        }
    }

    /**
     * Closes the input stream.
     */
    public final void yyclose() throws java.io.IOException {
        zzAtEOF = true; /* indicate end of file */
        zzEndRead = zzStartRead; /* invalidate buffer    */

        if (zzReader != null)
            zzReader.close();
    }

    /**
     * Resets the scanner to read from a new input stream.
     * Does not close the old reader.
     *
     * All internal variables are reset, the old input stream 
     * <b>cannot</b> be reused (internal buffer is discarded and lost).
     * Lexical state is set to <tt>ZZ_INITIAL</tt>.
     *
     * @param reader   the new input stream 
     */
    public final void yyreset(java.io.Reader reader) {
        zzReader = reader;
        zzAtBOL = true;
        zzAtEOF = false;
        zzEndRead = zzStartRead = 0;
        zzCurrentPos = zzMarkedPos = zzPushbackPos = 0;
        yyline = yychar = yycolumn = 0;
        zzLexicalState = YYINITIAL;
    }

    /**
     * Returns the current lexical state.
     */
    public final int yystate() {
        return zzLexicalState;
    }

    /**
     * Enters a new lexical state
     *
     * @param newState the new lexical state
     */
    public final void yybegin(int newState) {
        zzLexicalState = newState;
    }

    /**
     * Returns the text matched by the current regular expression.
     */
    public final String yytext() {
        return new String(zzBuffer, zzStartRead, zzMarkedPos - zzStartRead);
    }

    /**
     * Returns the character at position <tt>pos</tt> from the 
     * matched text. 
     * 
     * It is equivalent to yytext().charAt(pos), but faster
     *
     * @param pos the position of the character to fetch. 
     *            A value from 0 to yylength()-1.
     *
     * @return the character at position pos
     */
    public final char yycharat(int pos) {
        return zzBuffer[zzStartRead + pos];
    }

    /**
     * Returns the length of the matched text region.
     */
    public final int yylength() {
        return zzMarkedPos - zzStartRead;
    }

    /**
     * Reports an error that occured while scanning.
     *
     * In a wellformed scanner (no or only correct usage of 
     * yypushback(int) and a match-all fallback rule) this method 
     * will only be called with things that "Can't Possibly Happen".
     * If this method is called, something is seriously wrong
     * (e.g. a JFlex bug producing a faulty scanner etc.).
     *
     * Usual syntax/scanner level error handling should be done
     * in error fallback rules.
     *
     * @param   errorCode  the code of the errormessage to display
     */
    private void zzScanError(int errorCode) {
        String message;
        try {
            message = ZZ_ERROR_MSG[errorCode];
        } catch (ArrayIndexOutOfBoundsException e) {
            message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
        }

        throw new Error(message);
    }

    /**
     * Pushes the specified amount of characters back into the input stream.
     *
     * They will be read again by then next call of the scanning method
     *
     * @param number  the number of characters to be read again.
     *                This number must not be greater than yylength()!
     */
    public void yypushback(int number) {
        if (number > yylength())
            zzScanError(ZZ_PUSHBACK_2BIG);

        zzMarkedPos -= number;
    }

    /**
     * Contains user EOF-code, which will be executed exactly once,
     * when the end of file is reached
     */
    private void zzDoEOF() throws java.io.IOException {
        if (!zzEOFDone) {
            zzEOFDone = true;
            yyclose();
        }
    }

    /**
     * Resumes scanning until the next regular expression is matched,
     * the end of input is encountered or an I/O-Error occurs.
     *
     * @return      the next token
     * @exception   java.io.IOException  if any I/O-Error occurs
     */
    public trywithcatch.java_cup.runtime.Symbol next_token() throws java.io.IOException {
        int zzInput;
        int zzAction;

        // cached fields:
        int zzCurrentPosL;
        int zzMarkedPosL;
        int zzEndReadL = zzEndRead;
        char[] zzBufferL = zzBuffer;
        char[] zzCMapL = ZZ_CMAP;

        int[] zzTransL = ZZ_TRANS;
        int[] zzRowMapL = ZZ_ROWMAP;
        int[] zzAttrL = ZZ_ATTRIBUTE;

        while (true) {
            zzMarkedPosL = zzMarkedPos;

            yychar += zzMarkedPosL - zzStartRead;

            boolean zzR = false;
            for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL; zzCurrentPosL++) {
                switch (zzBufferL[zzCurrentPosL]) {
                    case '\u000B':
                    case '\u000C':
                    case '\u0085':
                    case '\u2028':
                    case '\u2029':
                        yycolumn = 0;
                        zzR = false;
                        break;
                    case '\r':
                        yycolumn = 0;
                        zzR = true;
                        break;
                    case '\n':
                        if (zzR)
                            zzR = false;
                        else {
                            yycolumn = 0;
                        }
                        break;
                    default:
                        zzR = false;
                        yycolumn++;
                }
            }

            zzAction = -1;

            zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

            zzState = zzLexicalState;

            zzForAction: {
                while (true) {

                    if (zzCurrentPosL < zzEndReadL)
                        zzInput = zzBufferL[zzCurrentPosL++];
                    else if (zzAtEOF) {
                        zzInput = YYEOF;
                        break zzForAction;
                    } else {
                        // store back cached positions
                        zzCurrentPos = zzCurrentPosL;
                        zzMarkedPos = zzMarkedPosL;
                        boolean eof = zzRefill();
                        // get translated positions and possibly new buffer
                        zzCurrentPosL = zzCurrentPos;
                        zzMarkedPosL = zzMarkedPos;
                        zzBufferL = zzBuffer;
                        zzEndReadL = zzEndRead;
                        if (eof) {
                            zzInput = YYEOF;
                            break zzForAction;
                        } else {
                            zzInput = zzBufferL[zzCurrentPosL++];
                        }
                    }
                    int zzNext = zzTransL[zzRowMapL[zzState] + zzCMapL[zzInput]];
                    if (zzNext == -1)
                        break zzForAction;
                    zzState = zzNext;

                    int zzAttributes = zzAttrL[zzState];
                    if ((zzAttributes & 1) == 1) {
                        zzAction = zzState;
                        zzMarkedPosL = zzCurrentPosL;
                        if ((zzAttributes & 8) == 8)
                            break zzForAction;
                    }

                }
            }

            // store back cached position
            zzMarkedPos = zzMarkedPosL;

            switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
                case 13: {
                    return newSymbol(sym.END_TRY_WITH_CATCH);
                }
                case 14:
                    break;
                case 12: {
                    return newSymbol(sym.TRY_WITH_CATCH);
                }
                case 15:
                    break;
                case 10: {
                    useNextIdent = true;
                    return newSymbol(sym.CATCH);
                }
                case 16:
                    break;
                case 4: {
                    return newSymbol(sym.BLOCK_END);
                }
                case 17:
                    break;
                case 9: {
                    return newSymbol(sym.TRY);
                }
                case 18:
                    break;
                case 3: {
                    return newSymbol(sym.BLOCK_START);
                }
                case 19:
                    break;
                case 8: {
                    yybegin(COMMENT_C);
                }
                case 20:
                    break;
                case 11: {
                    return newSymbol(sym.FINALLY);
                }
                case 21:
                    break;
                case 7: {
                    yybegin(COMMENT_CPP);
                }
                case 22:
                    break;
                case 5: {
                    yybegin(STRING);
                }
                case 23:
                    break;
                case 2: {
                    Symbol s = newIdent();
                    if (s != null)
                        return s;
                }
                case 24:
                    break;
                case 6: {
                    yybegin(YYINITIAL);
                }
                case 25:
                    break;
                case 1: {
                }
                case 26:
                    break;
                default:
                    if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
                        zzAtEOF = true;
                        zzDoEOF();
                        {
                            return new trywithcatch.java_cup.runtime.Symbol(sym.EOF);
                        }
                    } else {
                        zzScanError(ZZ_NO_MATCH);
                    }
            }
        }
    }

}

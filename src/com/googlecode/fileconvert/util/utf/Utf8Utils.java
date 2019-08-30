package com.googlecode.fileconvert.util.utf;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Utf8Utils {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
            .getLogger(Utf8Utils.class);

    public static String detectUtf8(String w3UrlPart) {
        byte[] bts;
        try {
            bts = w3UrlPart.getBytes("iso-8859-1");
            if (likeMultiByteUtf8(bts)) {
                String t = new String(bts, "UTF-8");
                if (log.isDebugEnabled()) {
                    log.debug(w3UrlPart + ">treat as utf8.[" + t + "]");
                }
                return t;
            } else {
                String t = new String(bts, "GB18030");
                if (log.isDebugEnabled()) {
                    log.debug(w3UrlPart + ">treat as gbxxx [" + t + "]");
                }
                return t;
            }
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    private static boolean likeMultiByteUtf8(byte[] bts) {
        int len = checkUtf8(bts);
        if (len > 2)
            return true;
        return false;
    }


    private static boolean likeEncodedUrl(String urlPart) {
        String r = urlPart.replaceAll("%25", "");
        return r.indexOf("%") > -1;
    }

    static class Utf8Magic {
        protected static final byte bm1 = (byte) 0x80; // 0x0

        protected static final byte bm2 = (byte) 0xE0;// 0xC0;

        protected static final byte bm3 = (byte) 0xF0;// 0xE0;

        protected static final byte bm4 = (byte) 0xF8;// 0xF0;

        protected static final byte tm1 = (byte) 0x0;

        protected static final byte tm2 = (byte) 0xC0;

        protected static final byte tm3 = (byte) 0xE0;

        protected static final byte tm4 = (byte) 0xF0;

        public static byte[] bms = new byte[] { bm1, bm2, bm3, bm4 };

        public static byte[] tms = new byte[] { tm1, tm2, tm3, tm4 };

        public static byte[] maskBits = new byte[] { bm1, bm2, bm3, bm4 };

        public static byte[] magicBits = new byte[] { tm1, tm2, tm3, tm4 };

        public static byte siblingMask = (byte) 0xC0;// 后续字节

        public static byte siblingMagicBits = (byte) 0x80;
        // ut16 only
        // DC00..DFFF; Low Surrogates
        // D800..DB7F; High Surrogates
        public static byte firstLowSurrogates = (byte) 0xDC;
        public static byte lastLowSurrogates = (byte) 0xDF;
        public static byte firstHighSurrogates = (byte) 0xD8;
        public static byte lastHighSurrogates = (byte) 0xDB;
    }

    // private boolean isUtf8(byte[] bs) {
    // return isUtf8(bs, false);
    // }

    public static boolean isMultiByteUtf8(byte[] bs) {
        return isUtf8(bs, true);
    }

    public static boolean isUtf8(byte[] bs, boolean multibyte) {
        // printHex(bs);
        // System.outt.println("length multibyte?" + multibyte);

        StringBuilder sb = null;
        if (log.isDebugEnabled()) {
            sb = new StringBuilder();
        }
        try {
            for (int i = 0; i < bs.length; i++) {
                byte b = bs[i];
                if (log.isDebugEnabled()) {
                    sb.append("\n");
                    sb.append(i).append(" : 0x").append(
                            Integer.toString((0xFF & b), 16)).append("_")
                            .append(Integer.toString((0xFF & b), 2));
                }
                // System.outt.print(i);
                // System.outt.print(" : 0x");
                // System.out.println(Integer.toString(b, 16));
                for (int j = 0; j < Utf8Magic.maskBits.length; j++) {

                    if ((b & Utf8Magic.maskBits[j]) == Utf8Magic.magicBits[j]) {
                        if (j == 0) {
                            // 单字节
                            if (multibyte) {
                                // System.outt.println(" no allow single byte");
                                if (log.isDebugEnabled()) {
                                    sb.append(",not allow single byte");
                                }
                                return false;
                            } else {
                            }
                        } else {
                            // 后面有j个字节，共j + 1 byte
                            for (int k = 0; k < j; k++) {
                                if ((bs[++i] & Utf8Magic.siblingMask) != Utf8Magic.siblingMagicBits) {
                                    // System.outt.println(" not match " +
                                    // Integer.toHexString(0xFF & bs[i]));
                                    if (log.isDebugEnabled()) {
                                        sb
                                                .append(", not match ")
                                                .append(
                                                        Integer.toString(
                                                                0xFF & bs[i],
                                                                16))
                                                .append("_")
                                                .append(
                                                        Integer
                                                                .toString(
                                                                        0xFF & bs[i],
                                                                        2));
                                    }
                                    return false;
                                } else {
                                    // System.outt.println(" match " +
                                    // Integer.toHexString(0xFF & bs[i]));
                                    if (log.isDebugEnabled()) {
                                        sb
                                                .append(", ")
                                                .append(
                                                        Integer.toString(
                                                                0xFF & bs[i],
                                                                16))
                                                .append("_")
                                                .append(
                                                        Integer
                                                                .toString(
                                                                        0xFF & bs[i],
                                                                        2));
                                    }
                                }
                            }
                        }
                        // System.outt.println("match by " + j);
                        if (log.isDebugEnabled()) {
                            sb.append(", match by " + j);
                        }
                        break;
                    } else {
                        if (j >= Utf8Magic.maskBits.length - 1) {
                            // System.outt.println(j);
                            // System.outt.println("no mask match");
                            if (log.isDebugEnabled()) {
                                sb.append(", no mask match ").append(j);
                            }
                            return false;
                        }
                    }
                }
                // System.outt.println();
            }
            return true;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug(sb.toString());
            }
        }
    }

    private static class MatchCtx {

        private static ByteMatcher firstByteMatcher = new Utf8FirstByteMatcher();
        private static ByteMatcher otherByteMatcher = new Utf8OtherByteMatcher();
        private int encLength;
        private int require;
        private int found;

        private int maxByteLen = 0;

        private ByteMatcher matcher;

        private StringBuilder sb;

        public MatchCtx() {
            init();
            if (log.isDebugEnabled()) {
                sb = new StringBuilder(1024);
            }
        }

        private void init() {
            this.matcher = firstByteMatcher;
            this.found = 0;
            this.require = 0;
            this.encLength = 0;
        }

        public ByteMatcher matcher() {
            return matcher;
        }

        public void start(int len) {
            this.encLength = len;
            this.require = len - 1;
            this.matcher = otherByteMatcher;
            if (len == 1) {
                if (maxByteLen == 0)
                    maxByteLen = 1;
                init();
            }
        }

        public void consume() {
            found++;
            if (found >= require) {
                // switch to next character start
                if (encLength > maxByteLen) {
                    maxByteLen = encLength;
                }
                init();
            }
        }

        public int getMaxByteLen() {
            return maxByteLen;
        }

        public void debug(Object... msgs) {
            if (msgs != null) {
                for (Object o : msgs) {
                    sb.append(String.valueOf(o));
                }
            }
        }

        @Override
        public String toString() {
            if (log.isDebugEnabled()) {
                return sb.toString();
            }
            return super.toString();
        }
    }

    private static interface ByteMatcher {
        boolean match(byte bt, MatchCtx ctx);
    }

    private static String hex(byte b) {
        return Integer.toHexString(0xFF & b);
    }

    private static class Utf8FirstByteMatcher implements ByteMatcher {
        public boolean match(byte bt, MatchCtx ctx) {
            // if (log.isDebugEnabled()) {
            // log.debug("match first byte " + hex(bt));
            // }
            if (log.isDebugEnabled()) {
                ctx.debug("[0x", hex(bt), " ");
            }
            for (int i = 0; i < Utf8Magic.magicBits.length; i++) {
                // if (log.isDebugEnabled()) {
                // log.debug("magicBits " + hex(Utf8Magic.magicBits[i]));
                // }
                if ((bt & Utf8Magic.maskBits[i]) == Utf8Magic.magicBits[i]) {
                    if (log.isDebugEnabled()) {
                        ctx.debug(i + 1, ":", hex(Utf8Magic.magicBits[i]));
                    }
                    ctx.start(i + 1);
                    return true;
                }
            }
            if (log.isDebugEnabled()) {
                ctx.debug("^");
            }
            return false;
        }
    }

    private static class Utf8OtherByteMatcher implements ByteMatcher {
        public boolean match(byte bt, MatchCtx ctx) {
            if (log.isDebugEnabled()) {
                ctx.debug(" ", hex(bt));
            }
            if ((bt & Utf8Magic.siblingMask) == Utf8Magic.siblingMagicBits) {
                ctx.consume();
                return true;
            }
            if (log.isDebugEnabled()) {
                ctx.debug("^");
            }
            return false;
        }

    }

    /**
     * @param bs
     * @return 如果是utf-8,那么返回最长的utf8码字节数
     */
    public static int checkUtf8(byte[] bs) {
        MatchCtx ctx = new MatchCtx();
        try {
            for (int i = 0; i < bs.length; i++) {
                byte b = bs[i];
                if (!ctx.matcher().match(b, ctx)) {
                    // not utf8
                    return 0;
                }
            }
        } finally {
            if (log.isDebugEnabled()) {
                log.debug(ctx);
            }
        }
        return ctx.getMaxByteLen();
    }

    private static Map<Character.UnicodeBlock, Boolean> FullWidthBlocks = new HashMap<Character.UnicodeBlock, Boolean>();
    static {
        FullWidthBlocks.put(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS,
                Boolean.TRUE);
        FullWidthBlocks.put(
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A,
                Boolean.TRUE);
        FullWidthBlocks.put(
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B,
                Boolean.TRUE);
        FullWidthBlocks.put(
                Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS,
                Boolean.TRUE);
        FullWidthBlocks.put(
                Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT,
                Boolean.TRUE);
        FullWidthBlocks.put(Character.UnicodeBlock.KANBUN, Boolean.TRUE);

        // Radicals and Strokes
        FullWidthBlocks.put(Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT,
                Boolean.TRUE);
        FullWidthBlocks.put(Character.UnicodeBlock.KANGXI_RADICALS,
                Boolean.TRUE);

        FullWidthBlocks.put(Character.UnicodeBlock.CJK_COMPATIBILITY,
                Boolean.TRUE);// ?
        FullWidthBlocks.put(Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS,
                Boolean.TRUE);// ?

        FullWidthBlocks.put(Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION,
                Boolean.TRUE);
        // Chinese-specific
        FullWidthBlocks.put(Character.UnicodeBlock.BOPOMOFO, Boolean.TRUE);
        FullWidthBlocks.put(Character.UnicodeBlock.BOPOMOFO_EXTENDED,
                Boolean.TRUE);

        // japanese
        FullWidthBlocks.put(Character.UnicodeBlock.HIRAGANA, Boolean.TRUE);
        FullWidthBlocks.put(Character.UnicodeBlock.KATAKANA, Boolean.TRUE);
        FullWidthBlocks.put(
                Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS,
                Boolean.TRUE);

        // korea
        FullWidthBlocks.put(Character.UnicodeBlock.HANGUL_SYLLABLES,
                Boolean.TRUE);
        FullWidthBlocks.put(Character.UnicodeBlock.HANGUL_JAMO, Boolean.TRUE);
        FullWidthBlocks.put(Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO,
                Boolean.TRUE);
    }

    public static boolean isHalfWidth(int codepoint) {
        if (true) {
            return !isFullWidth(codepoint);
        }
        Character.UnicodeBlock ub = null;
        try {
            ub = Character.UnicodeBlock.of(codepoint);
        } catch (Exception e) {
            log.error("cant find a unicode block for " + codepoint, e);
        }
        if (ub != null) {
            if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                //
                if (codepoint >= 0xFF00 && codepoint <= 0xff60) {
                    return false;
                } else if (codepoint >= 0xffe0 && codepoint <= 0xffe6) {
                    return false;
                } else {
                    return true;
                }
            } else if (FullWidthBlocks.containsKey(ub)) {
                return false;
            }
        }
        return true;
    }

    private static int[] fwstarts = new int[] {
            //
            0, 0x00A1, 0x00A4, 0x00A7, 0x00AA, 0x00AD, 0x00B0, 0x00B6, 0x00BC,
            0x00C6, 0x00D0, 0x00D7, 0x00DE, 0x00E6, 0x00E8, 0x00EC, 0x00F0,
            0x00F2, 0x00F7, 0x00FC, 0x00FE, 0x0101, 0x0111, 0x0113, 0x011B,
            0x0126, 0x012B, 0x0131, 0x0138, 0x013F, 0x0144, 0x0148, 0x014D,
            0x0152, 0x0166, 0x016B, 0x01CE, 0x01D0, 0x01D2, 0x01D4, 0x01D6,
            0x01D8, 0x01DA, 0x01DC, 0x0251, 0x0261, 0x02C4, 0x02C7, 0x02C9,
            0x02CD, 0x02D0, 0x02D8, 0x02DD, 0x02DF, 0x0300, 0x0391, 0x03A3,
            0x03B1, 0x03C3, 0x0401, 0x0410, 0x0451, 0x1100, 0x115F, 0x2010,
            0x2013, 0x2018, 0x201C, 0x2020, 0x2024, 0x2030, 0x2032, 0x2035,
            0x203B, 0x203E, 0x2074, 0x207F, 0x2081, 0x20AC, 0x2103, 0x2105,
            0x2109, 0x2113, 0x2116, 0x2121, 0x2126, 0x212B, 0x2153, 0x215B,
            0x2160, 0x2170, 0x2190, 0x21B8, 0x21D2, 0x21D4, 0x21E7, 0x2200,
            0x2202, 0x2207, 0x220B, 0x220F, 0x2211, 0x2215, 0x221A, 0x221D,
            0x2223, 0x2225, 0x2227, 0x222E, 0x2234, 0x223C, 0x2248, 0x224C,
            0x2252, 0x2260, 0x2264, 0x226A, 0x226E, 0x2282, 0x2286, 0x2295,
            0x2299, 0x22A5, 0x22BF, 0x2312, 0x2329, 0x2460, 0x24EB, 0x2550,
            0x2580, 0x2592, 0x25A0, 0x25A3, 0x25B2, 0x25B6, 0x25BC, 0x25C0,
            0x25C6, 0x25CB, 0x25CE, 0x25E2, 0x25EF, 0x2605, 0x2609, 0x260E,
            0x2614, 0x261C, 0x261E, 0x2640, 0x2642, 0x2660, 0x2663, 0x2667,
            0x266C, 0x266F, 0x273D, 0x2776, 0x2E80, 0x2E9B, 0x2F00, 0x2FF0,
            0x3000, 0x3041, 0x3099, 0x3105, 0x3131, 0x3190, 0x31C0, 0x31F0,
            0x3220, 0x3250, 0x3300, 0x3400, 0x4E00, 0xA000, 0xA490, 0xAC00,
            0xE000, 0xF900, 0xFA30, 0xFA70, 0xFE00, 0xFE30, 0xFE54, 0xFE68,
            0xFF01, 0xFFE0, 0xFFFD, 0x20000, 0x2A6D7, 0x2F800, 0x2FA1E,
            0x30000, 0xE0100, 0xF0000, 0x100000 };
    private static int[] fwends = new int[] {
            //
            0, 0x00A1, 0x00A4, 0x00A8, 0x00AA, 0x00AE, 0x00B4, 0x00BA, 0x00BF,
            0x00C6, 0x00D0, 0x00D8, 0x00E1, 0x00E6, 0x00EA, 0x00ED, 0x00F0,
            0x00F3, 0x00FA, 0x00FC, 0x00FE, 0x0101, 0x0111, 0x0113, 0x011B,
            0x0127, 0x012B, 0x0133, 0x0138, 0x0142, 0x0144, 0x014B, 0x014D,
            0x0153, 0x0167, 0x016B, 0x01CE, 0x01D0, 0x01D2, 0x01D4, 0x01D6,
            0x01D8, 0x01DA, 0x01DC, 0x0251, 0x0261, 0x02C4, 0x02C7, 0x02CB,
            0x02CD, 0x02D0, 0x02DB, 0x02DD, 0x02DF, 0x036F, 0x03A1, 0x03A9,
            0x03C1, 0x03C9, 0x0401, 0x044F, 0x0451, 0x1159, 0x115F, 0x2010,
            0x2016, 0x2019, 0x201D, 0x2022, 0x2027, 0x2030, 0x2033, 0x2035,
            0x203B, 0x203E, 0x2074, 0x207F, 0x2084, 0x20AC, 0x2103, 0x2105,
            0x2109, 0x2113, 0x2116, 0x2122, 0x2126, 0x212B, 0x2154, 0x215E,
            0x216B, 0x2179, 0x2199, 0x21B9, 0x21D2, 0x21D4, 0x21E7, 0x2200,
            0x2203, 0x2208, 0x220B, 0x220F, 0x2211, 0x2215, 0x221A, 0x2220,
            0x2223, 0x2225, 0x222C, 0x222E, 0x2237, 0x223D, 0x2248, 0x224C,
            0x2252, 0x2261, 0x2267, 0x226B, 0x226F, 0x2283, 0x2287, 0x2295,
            0x2299, 0x22A5, 0x22BF, 0x2312, 0x232A, 0x24E9, 0x254B, 0x2573,
            0x258F, 0x2595, 0x25A1, 0x25A9, 0x25B3, 0x25B7, 0x25BD, 0x25C1,
            0x25C8, 0x25CB, 0x25D1, 0x25E5, 0x25EF, 0x2606, 0x2609, 0x260F,
            0x2615, 0x261C, 0x261E, 0x2640, 0x2642, 0x2661, 0x2665, 0x266A,
            0x266D, 0x266F, 0x273D, 0x277F, 0x2E99, 0x2EF3, 0x2FD5, 0x2FFB,
            0x303E, 0x3096, 0x30FF, 0x312C, 0x318E, 0x31B7, 0x31CF, 0x321E,
            0x3243, 0x32FE, 0x33FF, 0x4DB5, 0x9FBB, 0xA48C, 0xA4C6, 0xD7A3,
            0xF8FF, 0xFA2D, 0xFA6A, 0xFAD9, 0xFE19, 0xFE52, 0xFE66, 0xFE6B,
            0xFF60, 0xFFE6, 0xFFFD, 0x2A6D6, 0x2F7FF, 0x2FA1D, 0x2FFFD,
            0x3FFFD, 0xE01EF, 0xFFFFD, 0x10FFFD };
    private static int fwlength = fwstarts.length;

    public static boolean isFullWidth(int codePoint) {
        int top, bottom, current;
        bottom = 0;
        top = fwlength;
        current = top / 2;

        while (top - bottom > 1) {
            if (codePoint >= fwstarts[current]) {
                bottom = current;
            } else {
                top = current;
            }
            current = (top + bottom) / 2;
        }
        // System.out.println("current:" + current);
        if (codePoint <= fwends[current]) {
            return true;
        }
        return false;
    }

    public static int viewUnitLen(String str) {
        if (str == null)
            return 0;
        char ch, chl;
        int cnt = str.length();
        int units = 0;
        int codePoint = 0;
        int i = 0;
        for (i = 0; i < cnt;) {
            ch = str.charAt(i++);
            if (Character.isHighSurrogate(ch)) {
                chl = str.charAt(i++);
                codePoint = Character.toCodePoint(ch, chl);
            } else {
                codePoint = ch;
            }
            if (Utf8Utils.isFullWidth(codePoint)) {
                units++;
            } else {
                // System.out.println("halfwidth:" + ch);
            }
            units++;

        }
        return units;
    }


    public static String toHw(String str) {
        if (str == null) {
            return str;
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        char ch;
        boolean lastIsEng = true;
        for (int i = 0; i < len;) {
            ch = str.charAt(i++);
            if ((ch > 0xFF00) && (ch <= 0xFF5E)) {
                // System.out.println("w-- " + ch);
                sb.append((char) (ch - 0xFEE0));
                lastIsEng = true;
            } else if (ch == 0x3002 || ch == 0xFF61) {
                if (lastIsEng) {
                    sb.append('.');
                } else {
                    sb.append(ch);
                }
            } else {
                // System.out.println("h-- " + ch + ", 0x"
                // + Integer.toString(ch, 16));
                sb.append(ch);
                lastIsEng = false;
            }
        }
        return sb.toString();
    }

    public static String stripb(String str, int len) {
        if (str == null)
            return null;
        str = str.trim();

        char ch, chl;
        int cnt = str.length();
        int bytes = 0;
        int codePoint = 0;
        int i = 0;
        for (i = 0; i < cnt;) {
            ch = str.charAt(i++);
            if (Character.isHighSurrogate(ch)) {
                chl = str.charAt(i++);
                codePoint = Character.toCodePoint(ch, chl);
            } else {
                codePoint = ch;
            }
            if (Utf8Utils.isHalfWidth(codePoint)) {
            } else {
                bytes++;
            }
            bytes++;
            if (bytes >= len) {
                break;
            }
        }
        if (i >= cnt) {
            return str;
        }
        return str.substring(0, i);
    }
}
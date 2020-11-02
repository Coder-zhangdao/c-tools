package com.bixuebihui.util.other;

import org.apache.commons.lang.StringEscapeUtils;

public class CMyStringForWeb extends CMyString {
    public static final int JSON_USE_QUOT = 1;        //JSON用双引号引用数据
    public static final int JSON_USE_APOS = 2;        //JSON用单引号引用数据

    /**
     * 过滤转意页面HTML代码
     *
     * @param _sContent
     * @return 过滤后的html
     */
    public static String filterForHTMLValue(String _sContent) {
        if (_sContent == null)
            return "";
        char srcBuff[] = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";
        StringBuffer retBuff = new StringBuffer((int) ((double) nLen * 1.8D));
        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
                case 38: // '&'
                    if (i + 1 < nLen) {
                        cTemp = srcBuff[i + 1];
                        if (cTemp == '#')
                            retBuff.append("&");
                        else
                            retBuff.append("&amp;");
                    } else {
                        retBuff.append("&amp;");
                    }
                    break;

                case 60: // '<'
                    retBuff.append("&lt;");
                    break;

                case 62: // '>'
                    retBuff.append("&gt;");
                    break;

                case 34: // '"'
                    retBuff.append("&quot;");
                    break;

                case 39: // '\''
                    retBuff.append("&acute;");            //&apos; - 高版本标准
                    break;

                case 10: // '\n'
                    retBuff.append("<br/>");
                    break;

                case 13: // '\r'
                    retBuff.append("<br/>");
                    break;

                default:
                    retBuff.append(cTemp);
                    break;
            }
        }

        return retBuff.toString();
    }


    /**
     * 过滤转意JSON格式的数据
     *
     * @param _sContent    源字符串
     * @param datamarktype JSON_USE_QUOT 双引号, JSON_USE_APOS 单引号
     * @return 过滤后的字符串
     */
    public static String filterForJson(String _sContent, int datamarktype) {
        if (_sContent == null)
            return "";
        char srcBuff[] = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";
        StringBuffer retBuff = new StringBuffer((int) ((double) nLen * 1.8D));
        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
                case 34: // '"'
                    if (datamarktype == JSON_USE_QUOT)            //如果JSON数据用单引号是否会出现问题??
                        retBuff.append("\\\"");
                    else
                        retBuff.append("\"");
                    break;

                case 39://'\''
                    if (datamarktype == JSON_USE_APOS)        //如果JSON用单引号引用数据，应将数据中的单引号转意
                        retBuff.append("\\'");
                    else
                        retBuff.append("'");
                    break;

                case 92: // '\\'
                    retBuff.append("\\\\");
                    break;

                case 10: // '\n'
                    retBuff.append("\\n");
                    break;

                case 13: // '\r'
                    retBuff.append("\\r");
                    break;

                case 12: // '\f'
                    retBuff.append("\\f");
                    break;

                case 9: // '\t'
                    retBuff.append("\\t");
                    break;

                case 47: // '/'
                    retBuff.append("\\/");
                    break;

                default:
                    retBuff.append(cTemp);
                    break;
            }
        }

        return retBuff.toString();
    }

    /**
     * 将字符串转换为jSON格式并将其中的html敏感字符做转意
     *
     * @param _sContent 源字符串
     * @return 过滤后的字符串
     */
    public static String filterForJsonHtml(String _sContent) {
        if (_sContent == null)
            return "";
        char srcBuff[] = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";
        StringBuffer retBuff = new StringBuffer((int) ((double) nLen * 1.8D));
        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
                case 34: // '"'
                    retBuff.append("&quot;");                //无论什么引号一律转意为html
                    break;

                case 39: // '\''
                    retBuff.append("&apos;");                //&acute; - 低版本标准
                    break;

                case 92: // '\\'
                    retBuff.append("\\\\");
                    break;

                case 10: // '\n'
                    retBuff.append("<br/>");
                    break;

                case 13: // '\r'
                    retBuff.append("<br/>");
                    break;

                case 12: // '\f'
                    retBuff.append("\\f");
                    break;

                case 9: // '\t'
                    retBuff.append("\\t");
                    break;

                case 47: // '/'
                    retBuff.append("\\/");
                    break;

                case 38: // '&'
                    if (i + 1 < nLen) {
                        cTemp = srcBuff[i + 1];
                        if (cTemp == '#')
                            retBuff.append("&");
                        else
                            retBuff.append("&amp;");
                    } else {
                        retBuff.append("&amp;");
                    }
                    break;

                case 60: // '<'
                    retBuff.append("&lt;");
                    break;

                case 62: // '>'
                    retBuff.append("&gt;");
                    break;

                default:
                    retBuff.append(cTemp);
                    break;
            }
        }

        return retBuff.toString();
    }

    /**
     * 过滤转意字符，仅留纯文本,特殊字符变全角
     *
     * @param _sContent 源字符串
     * @return 转换后的字符串
     */
    public static String filterToText(String _sContent) {
        if (_sContent == null)
            return "";

        _sContent = StringEscapeUtils.unescapeHtml(_sContent);

        char srcBuff[] = _sContent.toCharArray();
        int nLen = srcBuff.length;
        if (nLen == 0)
            return "";


        StringBuffer retBuff = new StringBuffer((int) ((double) nLen * 1.8D));
        for (int i = 0; i < nLen; i++) {
            char cTemp = srcBuff[i];
            switch (cTemp) {
                case 38: // '&'
                    retBuff.append("＆");
                    break;

                case 60: // '<'
                    retBuff.append("〈");
                    break;
                case 62: // '>'
                    retBuff.append("〉");
                    break;

                case 10: // '\n'
                case 13: // '\r'
                    break;

                case 92: // '\\'
                    retBuff.append("＼");
                    break;

                case 34: // '"'
                    retBuff.append("“");
                    break;

                case 39: // '\''
                    retBuff.append("‘");
                    break;

                default:
                    retBuff.append(cTemp);
                    break;
            }
        }

        return retBuff.toString();
    }


}

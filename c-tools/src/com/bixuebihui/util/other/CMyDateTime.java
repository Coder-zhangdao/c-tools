package com.bixuebihui.util.other;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class CMyDateTime
        implements Cloneable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -410914354106815003L;

    public CMyDateTime() {
        m_dtDate = null;
    }

    public CMyDateTime(long p_lDate) {
        m_dtDate = null;
        m_dtDate = new Date(p_lDate);
    }

    public static CMyDateTime now() {
        CMyDateTime mydtNow = new CMyDateTime();
        mydtNow.setDateTimeWithCurrentTime();
        return mydtNow;
    }

    public boolean isNull() {
        return m_dtDate == null;
    }

    public long getTimeInMillis() {
        return m_dtDate != null ? m_dtDate.getTime() : 0L;
    }

    public static int getTimeZoneRawOffset() {
        TimeZone timeZone = TimeZone.getDefault();
        int nOffset = timeZone.getRawOffset();
        return nOffset;
    }

    public long compareTo(Date p_dtAnother) {
        long lMyTime = m_dtDate != null ? m_dtDate.getTime() : 0L;
        long lAnotherTime = p_dtAnother != null ? p_dtAnother.getTime() : 0L;
        return lMyTime - lAnotherTime;
    }

    public long compareTo(CMyDateTime p_mydtAnother) {
        return compareTo(p_mydtAnother.getDateTime());
    }

    public static String extractDateTimeFormat(String _sValue) {
        char[] FORMAT_CHAR = {
                'y', 'M', 'd', 'H', 'm', 's'
        };
        return extractFormat(_sValue, FORMAT_CHAR);
    }

    public static String extractDateFormat(String _sValue) {
        char[] FORMAT_CHAR = {
                'y', 'M', 'd'
        };
        return extractFormat(_sValue, FORMAT_CHAR);
    }

    public static String extractTimeFormat(String _sValue) {
        char[] FORMAT_CHAR = {
                'H', 'm', 's'
        };
        return extractFormat(_sValue, FORMAT_CHAR);
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    private static String extractFormat(String _sValue, char[] _formatChar) {
        if (_sValue == null) {
            return null;
        }
        char[] buffValue = _sValue.trim().toCharArray();
        if (buffValue.length == 0) {
            return null;
        }
        StringBuilder buffFormat = new StringBuilder(19);
        int nAt = 0;
        int nAtField = 0;
        while (nAt < buffValue.length) {
            char aChar = buffValue[nAt++];
            if (Character.isDigit(aChar)) {
                buffFormat.append(_formatChar[nAtField]);
                continue;
            }
            buffFormat.append(aChar);
            if (++nAtField >= _formatChar.length) {
                break;
            }
        }
        return buffFormat.toString();
    }

    public static String formatTimeUsed(long iMillis) {
        if (iMillis <= 0L) {
            return "";
        }
        int iSecond = 0;
        int iMinute = 0;
        StringBuffer sb = new StringBuffer(16);
        iSecond = (int) (iMillis / 1000L);
        iMillis %= 1000L;
        if (iSecond > 0) {
            iMinute = iSecond / 60;
            iSecond %= 60;
        }
        if (iMinute > 0) {
            sb.append(iMinute).append('\u5206');
            if (iSecond < 10) {
                sb.append('0');
            }
            sb.append(iSecond);
        } else {
            sb.append(iSecond).append('.');
            if (iMillis < 10L) {
                sb.append('0').append('0');
            } else if (iMillis < 100L) {
                sb.append('0');
            }
            sb.append(iMillis);
        }
        sb.append('\u79D2');
        return sb.toString();
    }

    public int getYear()
            throws CMyException {
        return get(1);
    }

    public int getMonth()
            throws CMyException {
        return get(2);
    }

    public int getDay()
            throws CMyException {
        return get(3);
    }

    public int getHour()
            throws CMyException {
        return get(4);
    }

    public int getMinute()
            throws CMyException {
        return get(5);
    }

    public int getSecond()
            throws CMyException {
        return get(6);
    }

    public int getDayOfWeek()
            throws CMyException {
        return get(12);
    }

    public static String getStr(Object _dtTime, String _sFormat) {
        if (_dtTime instanceof CMyDateTime) {
            return ((CMyDateTime) _dtTime).toString(_sFormat);
        } else {
            return CMyString.showObjNull(_dtTime);
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public static void main(String[] args) {
        CMyDateTime myDateTime = new CMyDateTime();
        try {
            CMyDateTime now = now();
            System.out.println("now:" + now.toString(DEF_DATETIME_FORMAT_PRG));
            CMyDateTime nowClone = (CMyDateTime) now.clone();
            CMyDateTime execStartTime = nowClone.dateAdd(3, -30);
            System.out.println("now-30:" + execStartTime.toString(DEF_DATETIME_FORMAT_PRG));
            System.out.println("now:" + now.toString(DEF_DATETIME_FORMAT_PRG));
            System.out.println("nowClone:" + nowClone.toString(DEF_DATETIME_FORMAT_PRG));
            now = now();
            execStartTime = now.dateAdd(3, -3);
            System.out.println("now-3:" + execStartTime.toString(DEF_DATETIME_FORMAT_PRG));
            now = now();
            execStartTime = now.dateAdd(4, -3);
            System.out.println("now-3:" + execStartTime.toString(DEF_DATETIME_FORMAT_PRG));
            execStartTime.setDateTimeWithString("2002.1.1 00:00:00", "yyyy.MM.dd HH:mm:ss");
            System.out.println("time:" + execStartTime.toString(DEF_DATETIME_FORMAT_PRG));
            execStartTime.setDateTimeWithString(execStartTime.toString(DEF_DATE_FORMAT_PRG) + " 23:00:00", DEF_DATETIME_FORMAT_PRG);
            CMyDateTime execEndTime = now();
            execEndTime.setDateTimeWithString(execEndTime.toString(DEF_DATE_FORMAT_PRG) + " 24:00:00", DEF_DATETIME_FORMAT_PRG);
            System.out.println("now:" + now.toString());
            System.out.println("execStartTime:" + execStartTime.toString());
            System.out.println("now.compareTo(execStartTime):" + now.compareTo(execStartTime));
            System.out.println("TimeZone = " + getTimeZoneRawOffset());
            myDateTime.setDateTimeWithCurrentTime();
            System.out.println("Start:" + myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));
            long nTime = myDateTime.getTimeInMillis() % 0x36ee80L;
            System.out.print("\nTime=" + nTime);
            Time tempTime = new Time(nTime);
            System.out.print("  " + tempTime.toString());
            System.out.print("\n");
            myDateTime.setDateWithString("2001-04-15", 0);
            System.out.println(myDateTime.getDateTimeAsString("yyyy.MM.dd"));
            myDateTime.setDateWithString("000505", 2);
            System.out.println(myDateTime.getDateTimeAsString("yyyy.MM.dd"));
            myDateTime.setTimeWithString("12:01:02", 0);
            System.out.println(myDateTime.getDateTimeAsString(DEF_TIME_FORMAT_PRG));
            myDateTime.setTimeWithString("00:25", 2);
            System.out.println(myDateTime.getDateTimeAsString(DEF_DATETIME_FORMAT_PRG));
            java.sql.Date dDate = java.sql.Date.valueOf("1978-02-04");
            Time tTime = Time.valueOf("12:00:20");
            System.out.println(myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));
            myDateTime.setDate(dDate);
            System.out.println(myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));
            myDateTime.setTime(tTime);
            System.out.println(myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));
            myDateTime.setDateTimeWithCurrentTime();
            System.out.println("End:" + myDateTime.getDateTimeAsString("yyyy/MM/dd HH:mm:ss"));
            CMyDateTime myDateTime2 = new CMyDateTime();
            int[] nFields = {
                    1, 2, 3, 4, 5, 6, 11, 12
            };
            myDateTime2.setDateTimeWithString("2001-02-07 14:34:00", DEF_DATETIME_FORMAT_PRG);
            myDateTime.setDateTimeWithString("2001-03-07 15:35:01", DEF_DATETIME_FORMAT_PRG);
            for (int i = 0; i < 8; i++) {
                long lDateDiff = myDateTime.dateDiff(nFields[i], myDateTime2.getDateTime());
                System.out.println("DateDiff(" + nFields[i] + ")=" + lDateDiff);
            }

            for (int i = 0; i < 6; i++) {
                System.out.println("get(" + nFields[i] + ")=" + myDateTime.get(nFields[i]));
            }

            System.out.println("getWeek=" + myDateTime.get(12));
            System.out.println("Test for dateAdd()");
            System.out.println("oldDateTime = " + myDateTime.toString());
            myDateTime.dateAdd(1, 12);
            System.out.println("dateAdd(YEAR,12) = " + myDateTime.toString());
            myDateTime.dateAdd(1, -12);
            System.out.println("dateAdd(YEAR,-12) = " + myDateTime.toString());
            myDateTime.dateAdd(2, -3);
            System.out.println("dateAdd(MONTH,-3) = " + myDateTime.toString());
            myDateTime.dateAdd(3, 10);
            System.out.println("dateAdd(DAY,10) = " + myDateTime.toString());
            myDateTime.setDateTimeWithCurrentTime();
            int nWeek = myDateTime.getDayOfWeek();
            myDateTime.dateAdd(3, -nWeek);
            System.out.println("Monday of this week is:" + myDateTime.toString(DEF_DATE_FORMAT_PRG));
            for (int j = 1; j < 7; j++) {
                myDateTime.dateAdd(3, 1);
                System.out.println((j + 1) + " of this week is:" + myDateTime.toString(DEF_DATE_FORMAT_PRG));
            }

            System.out.println("\n\n===== test for CMyDateTime.set() ====== ");
            String[] sValues = {
                    "2002.06.13 12:00:12", "1900.2.4 3:4:5", "1901-03-15 23:05:10", "1978-2-4 5:6:7", "2001/12/31 21:08:22", "1988/2/5 9:1:2", "1986.12.24", "0019.2.8", "2002-12-20", "1999-8-1",
                    "2001/12/21", "2000/1/5", "78.02.04", "89.2.6", "99-12-31", "22-3-6", "01/02/04", "02/5/8"
            };
            for (int j = 0; j < sValues.length; j++) {
                myDateTime.setDateTimeWithString(sValues[j]);
                System.out.println("[" + j + "]" + extractDateTimeFormat(sValues[j]) + "  " + myDateTime.toString());
            }

        } catch (CMyException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public Date getDateTime() {
        return m_dtDate;
    }

    public long dateDiff(int p_nPart, CMyDateTime p_mydtAnother)
            throws CMyException {
        if (p_mydtAnother == null) {
            throw new CMyException(10, "\u65E0\u6548\u7684\u65E5\u671F\u65F6\u95F4\u5BF9\u8C61\u53C2\u6570(CMyDateTime.dateDiff(CMyDateTime))");
        } else {
            return dateDiff(p_nPart, p_mydtAnother.getDateTime());
        }
    }

    public long dateDiff(int p_nPart, Date p_dtAnother)
            throws CMyException {
        if (p_dtAnother == null) {
            throw new CMyException(10, "\u65E0\u6548\u7684\u65E5\u671F\u65F6\u95F4\u53C2\u6570\uFF08CMyDateTime.dateDiff(int,java.util.Date)\uFF09");
        }
        if (isNull()) {
            throw new CMyException(10, "\u65E5\u671F\u65F6\u95F4\u4E3A\u7A7A\uFF08CMyDateTime.dateDiff(int,java.util.Date)\uFF09");
        }
        if (p_nPart == 1) {
            return dateDiff_year(p_dtAnother);
        }
        if (p_nPart == 2) {
            return dateDiff_month(p_dtAnother);
        }
        long lMyTime = m_dtDate != null ? m_dtDate.getTime() : 0L;
        long lAnotherTime = p_dtAnother.getTime();
        long lDiffTime = (lMyTime - lAnotherTime) / 1000L;
        switch (p_nPart) {
            case 3: // '\003'
                return lDiffTime / 0x15180L;

            case 4: // '\004'
                return lDiffTime / 3600L;

            case 5: // '\005'
                return lDiffTime / 60L;

            case 6: // '\006'
                return lDiffTime;

            case 11: // '\013'
                return lDiffTime / 0x15180L / 91L;

            case 12: // '\f'
                return lDiffTime / 0x15180L / 7L;

            case 7: // '\007'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            default:
                throw new CMyException(10, "\u53C2\u6570\u65E0\u6548(CMyDateTime.dateDiff(int,java.util.Date))");
        }
    }

    private long dateDiff_year(Date p_dtAnother) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(m_dtDate);
        int nYear1 = cal.get(Calendar.YEAR);
        int nMonth1 = cal.get(Calendar.MONTH);
        cal.setTime(p_dtAnother);
        int nYear2 = cal.get(Calendar.YEAR);
        int nMonth2 = cal.get(Calendar.MONTH);
        if (nYear1 == nYear2) {
            return 0L;
        }
        if (nYear1 > nYear2) {
            return (nYear1 - nYear2) + (nMonth1 < nMonth2 ? -1 : 0);
        } else {
            return (long) ((nYear1 - nYear2) + (nMonth1 <= nMonth2 ? 0 : 1));
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public long dateDiff_month(Date p_dtAnother) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(m_dtDate);
        int nMonths1 = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        int nDay1 = cal.get(Calendar.DATE);
        cal.setTime(p_dtAnother);
        int nMonths2 = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        int nDay2 = cal.get(Calendar.DATE);
        if (nMonths1 == nMonths2) {
            return 0L;
        }
        if (nMonths1 > nMonths2) {
            return (nMonths1 - nMonths2) + (nDay1 >= nDay2 ? 0 : -1);
        } else {
            return (long) ((nMonths1 - nMonths2) + (nDay1 <= nDay2 ? 0 : 1));
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public int get(int p_nField)
            throws CMyException {
        if (m_dtDate == null) {
            throw new CMyException(20, "\u65E5\u671F\u65F6\u95F4\u4E3A\u7A7A\uFF08CMyDateTime.get\uFF09");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(m_dtDate);
        switch (p_nField) {
            case 1: // '\001'
                return cal.get(Calendar.YEAR);

            case 2: // '\002'
                return cal.get(Calendar.MONTH) + 1;

            case 3: // '\003'
                return cal.get(Calendar.DATE);

            case 4: // '\004'
                return cal.get(Calendar.HOUR_OF_DAY);

            case 5: // '\005'
                return cal.get(Calendar.MINUTE);

            case 6: // '\006'
                return cal.get(Calendar.SECOND);

            case 12: // '\f'
                return cal.get(Calendar.DAY_OF_WEEK);

            case 13: // '\r'
                return cal.getActualMaximum(5);

            case 14: // '\016'
                return getWeekCountsOfMonth(true);

            case 15: // '\017'
                return cal.getActualMaximum(Calendar.DAY_OF_YEAR);

            case 16: // '\020'
                return cal.getActualMaximum(Calendar.WEEK_OF_YEAR);

            case 7: // '\007'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            default:
                throw new CMyException(10, "\u65E0\u6548\u7684\u65E5\u671F\u65F6\u95F4\u57DF\u53C2\u6570\uFF08CMyDateTime.get\uFF09");
        }
    }

    public void setDateTime(Date p_dtDate) {
        m_dtDate = p_dtDate;
    }

    public boolean setDateTimeWithString(String p_sValue, String p_sFormat)
            throws CMyException {
        try {
            SimpleDateFormat dtFormat = new SimpleDateFormat(p_sFormat);
            m_dtDate = dtFormat.parse(p_sValue);
            return true;
        } catch (Exception ex) {
            throw new CMyException(10, "\u65E5\u671F\u65F6\u95F4\u5B57\u7B26\u4E32\u503C\u548C\u683C\u5F0F\u65E0\u6548\uFF08CMyDateTime.setDateTimeWithString\uFF09", ex);
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public CMyDateTime dateAdd(int p_nField, int p_nAdd)
            throws CMyException {
        if (m_dtDate == null) {
            throw new CMyException(20, "\u65E5\u671F\u65F6\u95F4\u4E3A\u7A7A\uFF08CMyDateTime.dateAdd\uFF09");
        }
        int nCalField = 0;
        switch (p_nField) {
            case 1: // '\001'
                nCalField = 1;
                break;

            case 2: // '\002'
                nCalField = 2;
                break;

            case 12: // '\f'
                nCalField = 5;
                p_nAdd *= 7;
                break;

            case 3: // '\003'
                nCalField = 5;
                break;

            case 4: // '\004'
                nCalField = 10;
                break;

            case 5: // '\005'
                nCalField = 12;
                break;

            case 6: // '\006'
                nCalField = 13;
                break;

            case 7: // '\007'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            default:
                throw new CMyException(10, "\u65E0\u6548\u7684\u65E5\u671F\u65F6\u95F4\u57DF\u53C2\u6570\uFF08CMyDateTime.dateAdd\uFF09");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(m_dtDate);
        cal.set(nCalField, cal.get(nCalField) + p_nAdd);
        m_dtDate = cal.getTime();
        return this;
    }

    @Override
    public synchronized Object clone() {
        CMyDateTime newMyDateTime = new CMyDateTime();
        newMyDateTime.m_dtDate = m_dtDate != null ? (Date) m_dtDate.clone() : null;
        return newMyDateTime;
    }

    public void setDateTimeWithRs(ResultSet p_rsData, int p_nFieldIndex)
            throws CMyException {
        try {
            Timestamp tsDateTime = p_rsData.getTimestamp(p_nFieldIndex);
            setDateTimeWithTimestamp(tsDateTime);
        } catch (SQLException ex) {
            throw new CMyException(40, "\u4ECE\u8BB0\u5F55\u96C6\u4E2D\u8BFB\u53D6\u65F6\u95F4\u5B57\u6BB5\u65F6\u51FA\u9519\uFF1ACMyDateTime.setDateTimeWithRs()", ex);
        }
    }

    public void setDateTimeWithRs(ResultSet p_rsData, String p_sFieldName)
            throws CMyException {
        try {
            Timestamp tsDateTime = p_rsData.getTimestamp(p_sFieldName);
            setDateTimeWithTimestamp(tsDateTime);
        } catch (SQLException ex) {
            throw new CMyException(40, "\u4ECE\u8BB0\u5F55\u96C6\u4E2D\u8BFB\u53D6\u65F6\u95F4\u5B57\u6BB5\u65F6\u51FA\u9519\uFF1ACMyDateTime.setDateTimeWithRs()", ex);
        }
    }

    @Override
    public String toString() {
        return toString(DEF_DATETIME_FORMAT_PRG);
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public String toString(String p_sFormat) {
        if (m_dtDate == null) {
            return null;
        }
        try {
            return getDateTimeAsString(p_sFormat);
        } catch (CMyException ex) {
            return null;
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public java.sql.Date toDate() {
        if (m_dtDate == null) {
            return null;
        } else {
            return new java.sql.Date(m_dtDate.getTime());
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public Time toTime() {
        if (m_dtDate == null) {
            return null;
        } else {
            return new Time(m_dtDate.getTime());
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public Timestamp toTimestamp() {
        if (m_dtDate == null) {
            return null;
        } else {
            return new Timestamp(m_dtDate.getTime());
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public void setDateTimeWithCurrentTime() {
        if (m_dtDate == null) {
            m_dtDate = new Date(System.currentTimeMillis());
        } else {
            m_dtDate.setTime(System.currentTimeMillis());
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public void setDateTimeWithTimestamp(Timestamp p_tsDateTime)
            throws CMyException {
        try {
            if (p_tsDateTime == null) {
                m_dtDate = null;
            } else {
                if (m_dtDate == null) {
                    m_dtDate = new Date();
                }
                m_dtDate.setTime(p_tsDateTime.getTime());
            }
        } catch (Exception ex) {
            throw new CMyException(0, "\u4F7F\u7528Timestamp\u5BF9\u8C61\u8BBE\u7F6E\u65E5\u671F\u548C\u65F6\u95F4\u51FA\u9519\uFF1ACMyDateTime.setDateTimeWithTimestamp()", ex);
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public boolean setDate(java.sql.Date p_dDate)
            throws CMyException {
        if (p_dDate == null) {
            return false;
        } else {
            return setDateWithString(p_dDate.toString(), 0);
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public boolean setTime(Time p_tTime)
            throws CMyException {
        if (p_tTime == null) {
            return false;
        } else {
            return setTimeWithString(p_tTime.toString(), 0);
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public boolean setDateWithString(String p_sDateValue, int p_nFormatType)
            throws CMyException {
        boolean blHasSepChar = false;
        int nLen = p_sDateValue.length();
        if (nLen < 6) {
            throw new CMyException(10, "\u65E5\u671F\u5B57\u7B26\u4E32\u65E0\u6548\uFF08CMyDateTime.setDateWithString\uFF09");
        }
        try {
            String sDateValue;
            switch (p_nFormatType) {
                case 1: // '\001'
                    blHasSepChar = nLen >= 10;
                    sDateValue = p_sDateValue.substring(0, 4) + "-" + p_sDateValue.substring(blHasSepChar ? 5 : 4, blHasSepChar ? 7 : 6) + "-" + p_sDateValue.substring(blHasSepChar ? 8 : 6, blHasSepChar ? 10 : 8);
                    break;

                case 2: // '\002'
                    sDateValue = p_sDateValue.charAt(0) >= '5' ? "19" : "20";
                    blHasSepChar = nLen >= 8;
                    sDateValue = sDateValue + p_sDateValue.substring(0, 2) + "-" + p_sDateValue.substring(blHasSepChar ? 3 : 2, blHasSepChar ? 5 : 4) + "-" + p_sDateValue.substring(blHasSepChar ? 6 : 4, blHasSepChar ? 8 : 6);
                    break;

                default:
                    sDateValue = p_sDateValue;
                    break;
            }
            if (m_dtDate == null) {
                return setDateTimeWithString(sDateValue, DEF_DATE_FORMAT_PRG);
            } else {
                String sTimeValue = getDateTimeAsString(DEF_TIME_FORMAT_PRG);
                return setDateTimeWithString(sDateValue + " " + sTimeValue, DEF_DATETIME_FORMAT_PRG);
            }
        } catch (Exception ex) {
            throw new CMyException(10, "\u65E0\u6548\u7684\u65E5\u671F\u5B57\u7B26\u4E32\uFF08CMyException.setDateWithString\uFF09", ex);
        }
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public boolean setTimeWithString(String p_sTimeValue, int p_nFormatType)
            throws CMyException {
        boolean blHasSepChar;
        int nLen = p_sTimeValue.length();
        if (nLen < 4) {
            throw new CMyException(10, "\u65F6\u95F4\u5B57\u7B26\u4E32\u683C\u5F0F\u65E0\u6548\uFF08\uFF09");
        }
        try {
            String sTimeValue;
            switch (p_nFormatType) {
                case 1: // '\001'
                    blHasSepChar = nLen >= 8;
                    sTimeValue = p_sTimeValue.substring(0, 2) + ":" + p_sTimeValue.substring(blHasSepChar ? 3 : 2, blHasSepChar ? 5 : 4) + ":" + p_sTimeValue.substring(blHasSepChar ? 6 : 4, blHasSepChar ? 8 : 6);
                    break;

                case 2: // '\002'
                    blHasSepChar = nLen >= 5;
                    sTimeValue = p_sTimeValue.substring(0, 2) + ":" + p_sTimeValue.substring(blHasSepChar ? 3 : 2, blHasSepChar ? 5 : 4) + ":00";
                    break;

                default:
                    sTimeValue = p_sTimeValue;
                    break;
            }
            if (m_dtDate == null) {
                return setDateTimeWithString(sTimeValue, DEF_TIME_FORMAT_PRG);
            } else {
                String sDateValue = getDateTimeAsString(DEF_DATE_FORMAT_PRG);
                return setDateTimeWithString(sDateValue + " " + sTimeValue, DEF_DATETIME_FORMAT_PRG);
            }
        } catch (Exception ex) {
            throw new CMyException(10, "\u65E0\u6548\u7684\u65F6\u95F4\u5B57\u7B26\u4E32\uFF08CMyException.setTimeWithString\uFF09", ex);
        }
    }

    public String getDateTimeAsString(String p_sFormat)
            throws CMyException {
        if (m_dtDate == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(p_sFormat).format(m_dtDate);
        } catch (Exception ex) {
            throw new CMyException(10, "\u6307\u5B9A\u7684\u65E5\u671F\u65F6\u95F4\u683C\u5F0F\u6709\u9519\uFF08CMyDateTime.getDateTimeAsString\uFF09", ex);
        }
    }

    public String getDateTimeAsString()
            throws CMyException {
        if (m_dtDate == null ) {
            return null;
        }
        try {
            return new SimpleDateFormat(DEF_DATETIME_FORMAT_PRG).format(m_dtDate);
        } catch (Exception ex) {
            throw new CMyException(0, "\u683C\u5F0F\u5316\u65E5\u671F\u65F6\u95F4\u5B57\u7B26\u4E32\u51FA\u9519\uFF08CMyDateTime.getDateTimeAsString()\uFF09", ex);
        }
    }

    public boolean setDateTimeWithString(String _sValue)
            throws CMyException {
        String sFormat = extractDateTimeFormat(_sValue);
        if (_sValue == null) {
            return false;
        } else {
            return setDateTimeWithString(_sValue, sFormat);
        }
    }

    public boolean isLeapYear()
            throws CMyException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(m_dtDate);
        return calendar.isLeapYear(getYear());
    }

    public boolean isToday() {
        CMyDateTime today = now();
        return toString(DEF_DATE_FORMAT_PRG).equals(today.toString(DEF_DATE_FORMAT_PRG));
    }

    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    public int getWeekCountsOfMonth(boolean _bSundayStart)
            throws CMyException {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(m_dtDate);
        int nWeekCounts = calendar.getActualMaximum(4);
        if (_bSundayStart) {
            return nWeekCounts;
        }
        CMyDateTime firstDay = new CMyDateTime();
        firstDay.setDateTime(m_dtDate);
        firstDay.setDateTimeWithString(firstDay.getYear() + "-" + firstDay.getMonth() + "-1");
        if (firstDay.getDayOfWeek() == 6) {
            nWeekCounts++;
        }
        return nWeekCounts;
    }

    @Override
    public boolean equals(Object _another) {
        return _another != null && (_another instanceof CMyDateTime) && ((CMyDateTime) _another).getTimeInMillis() == getTimeInMillis();
    }

    @Override
    public int hashCode() {
        return (int) this.getTimeInMillis();
    }

    private Date m_dtDate;
    public static final int FORMAT_DEFAULT = 0;
    public static final int FORMAT_LONG = 1;
    public static final int FORMAT_SHORT = 2;
    public static final String DEF_DATE_FORMAT_PRG = "yyyy-MM-dd";
    public static final String DEF_TIME_FORMAT_PRG = "HH:mm:ss";
    public static final String DEF_DATETIME_FORMAT_PRG = "yyyy-MM-dd HH:mm:ss";
    public static final String DEF_DATETIME_FORMAT_DB = "YYYY-MM-DD HH24:MI:SS";
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int DAY = 3;
    public static final int HOUR = 4;
    public static final int MINUTE = 5;
    public static final int SECOND = 6;
    public static final int QUATER = 11;
    public static final int WEEK = 12;
    public static final int DAY_OF_MONTH = 13;
    public static final int WEEK_OF_MONTH = 14;
    public static final int DAY_OF_YEAR = 15;
    public static final int WEEK_OF_YEAR = 16;
    public static final long ADAY_MILLIS = 0x5265c00L;
}

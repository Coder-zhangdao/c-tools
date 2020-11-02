
package com.bixuebihui.util.other;

import java.math.BigInteger;


public class CMyBitsValue
        implements Cloneable {

    public CMyBitsValue() {
        value = 0L;
        value = 0L;
    }

    public CMyBitsValue(long _lValue) {
        value = 0L;
        setValue(_lValue);
    }

    public CMyBitsValue(String _sValue) {
        value = 0L;
        setValue(_sValue);
    }

    public CMyBitsValue(CMyBitsValue _myBitsValue) {
        value = 0L;
        copy(_myBitsValue);
    }

    public void copy(CMyBitsValue _myBitsValue) {
        setValue(_myBitsValue.getValue());
    }

    @Override
    public Object clone() {
        return new CMyBitsValue(this);
    }

    public static boolean getBitOfInt(int _value, int _index) {
        if (_index < 0 || _index > 31) {
            return false;
        } else {
            return BigInteger.valueOf(_value).testBit(_index);
        }
    }

    public static boolean getBit(long _value, int _index) {
        if (_index < 0 || _index > 63) {
            return false;
        } else {
            return BigInteger.valueOf(_value).testBit(_index);
        }
    }

    public static long setBit(long _value, int _index, boolean _bValue) {
        if (_index < 0 || _index > 63) {
            return _value;
        }
        BigInteger bigInt = BigInteger.valueOf(_value);
        if (_bValue) {
            bigInt = bigInt.setBit(_index);
        } else {
            bigInt = bigInt.clearBit(_index);
        }
        return bigInt.longValue();
    }

    protected static int setBitOfInt(int _value, int _index, boolean _bValue) {
        if (_index < 0 || _index > 31) {
            return _value;
        } else {
            return (int) setBit(_value, _index, _bValue);
        }
    }

    public long getValue() {
        return value;
    }

    public int getValueAsInt() {
        return (int) value;
    }

    public void setValue(long _nValue) {
        value = _nValue;
    }

    public void setValue(String _sValue) {
        if (_sValue == null) {
            return;
        }
        BigInteger bigInt = BigInteger.valueOf(0L);
        int nLen = _sValue.length();
        int nBitPos = nLen - 1;
        for (int i = 0; i < nLen; i++) {
            if (_sValue.charAt(i) == '1') {
                bigInt = bigInt.setBit(nBitPos);
            }
            nBitPos--;
        }

        value = bigInt.longValue();
    }

    public boolean getBit(int _index) {
        return getBit(value, _index);
    }

    public CMyBitsValue setBit(int _index, boolean _bValue) {
        value = setBit(value, _index, _bValue);
        return this;
    }

    public void and(CMyBitsValue _myBitsValue) {
        if (_myBitsValue == null) {
        } else {
            and(_myBitsValue.getValue());
        }
    }

    public void and(long _value) {
        BigInteger bigInt = BigInteger.valueOf(value);
        bigInt = bigInt.and(BigInteger.valueOf(_value));
        value = bigInt.longValue();
    }

    public void or(CMyBitsValue _myBitsValue) {
        if (_myBitsValue == null) {
        } else {
            or(_myBitsValue.getValue());
        }
    }

    public void or(long _value) {
        BigInteger bigInt = BigInteger.valueOf(value);
        bigInt = bigInt.or(BigInteger.valueOf(_value));
        value = bigInt.longValue();
    }

    public int getRealLength() {
        return BigInteger.valueOf(value).bitLength();
    }

    @Override
    public String toString() {
        return toString(getRealLength());
    }

    public String toString(int _nLength) {
        StringBuffer strBuffer = new StringBuffer(_nLength);
        String sValue = Long.toBinaryString(value);
        if (_nLength > getRealLength()) {
            sValue = CMyString.expandStr(sValue, _nLength, '0', true);
        }
        for (int i = _nLength - 1; i > -1; i--) {
            strBuffer.append(sValue.charAt(i));
        }

        return strBuffer.toString();
    }

    public static void main(String args[]) {
        CMyBitsValue myBitsValue = new CMyBitsValue(0L);
        System.out.println(myBitsValue.toString(10));
        myBitsValue.setBit(31, true);
        System.out.println(myBitsValue.toString());
        myBitsValue.setBit(1, true);
        myBitsValue.setBit(2, false);
        myBitsValue.setBit(3, true);
        myBitsValue.setBit(10, true);
        myBitsValue.setBit(9, false);
        myBitsValue.setBit(8, true);
        myBitsValue.setBit(7, true);
        System.out.println(myBitsValue.toString());
        System.out.println("10=" + myBitsValue.getBit(10));
        System.out.println("1=" + myBitsValue.getBit(1));
        System.out.println("2=" + myBitsValue.getBit(2));
        int nLen = 64;
        myBitsValue.setValue(0L);
        for (int i = 0; i < nLen; i++) {
            myBitsValue.setBit(i, true);
        }

        for (int i = nLen - 1; i >= 0; i--) {
            System.out.print(i % 10 != 0 || i <= 0 ? String.valueOf(i % 10) : " ");
        }

        System.out.print("\n");
        System.out.println(myBitsValue.toString(nLen));
        for (int i = nLen - 1; i >= 0; i--) {
            System.out.print(myBitsValue.getBit(i) ? "1" : "0");
        }

        System.out.println("\n");
        System.out.println(CMyString.expandStr(Long.toBinaryString(0x7fffffffffffffffL), 64, '0', true) + " : MAX=" + 0x7fffffffffffffffL);
        System.out.println(Long.toBinaryString(0x8000000000000000L) + " : MIN=" + 0x8000000000000000L);
        System.out.println("\n========= Test for BigInteger ==========");
        BigInteger bigInt = BigInteger.valueOf(0L);
        bigInt = bigInt.setBit(31).setBit(63);
        System.out.println(bigInt.toString(2));
        long lValue = bigInt.longValue();
        System.out.println(Long.toBinaryString(lValue));
        bigInt = BigInteger.valueOf(lValue);
        System.out.println("\n");
        for (int i = 63; i >= 0; i--) {
            System.out.print(bigInt.testBit(i) ? "1" : "0");
        }

        int nValue = 0;
        nValue = setBitOfInt(nValue, 31, true);
        System.out.println("\n int = " + nValue);
        for (int i = 0; i < 32; i++) {
            nValue = setBitOfInt(nValue, i, (i & 1) == 1);
        }

        nValue = setBitOfInt(nValue, 31, false);
        for (int i = 31; i >= 0; i--) {
            System.out.print(getBitOfInt(nValue, i) ? "1" : "0");
        }

        System.out.println("\n");
        System.out.println(Long.toBinaryString(nValue));
        String sValue = "10101000100011110001";
        myBitsValue.setValue(sValue);
        System.out.println("src = " + sValue);
        System.out.println("dst = " + myBitsValue.toString());
        myBitsValue.or(2L);
        System.out.println("or  = " + myBitsValue.toString());
        myBitsValue.and(5L);
        System.out.println("and = " + myBitsValue.toString());
    }

    protected long value;
}

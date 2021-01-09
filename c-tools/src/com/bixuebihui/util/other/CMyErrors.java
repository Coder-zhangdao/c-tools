// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:54
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   CMyErrors.java

package com.bixuebihui.util.other;

import java.util.Vector;


// Referenced classes of package com.bixuebihui.util:
//            CMyException

public class CMyErrors {

    public CMyErrors() {
        vErrors = null;
        vErrors = new Vector(INIT_SIZE);
    }

    public CMyErrors add(String _sError) {
        if (_sError != null) {
            vErrors.add(_sError);
        }
        return this;
    }

    public CMyErrors add(Exception _ex) {
        if (_ex != null) {
            vErrors.add(_ex);
        }
        return this;
    }

    public int size() {
        return vErrors.size();
    }

    public boolean isEmpty() {
        return vErrors.isEmpty();
    }

    public Vector getErrors() {
        return vErrors;
    }

    public Object getAt(int _index) {
        try {
            return vErrors.get(_index);
        } catch (Exception ex) {
        }
        return null;
    }

    public CMyErrors clear() {
        vErrors.clear();
        return this;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean _bIncludingNo) {
        if (vErrors.size() == 0) {
            return "";
        }
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < vErrors.size(); i++) {
            Object objError = vErrors.get(i);
            if (objError != null) {
                if (_bIncludingNo) {
                    buff.append("(" + i + ")");
                }
                if (objError instanceof String) {
                    buff.append((String) objError).append("\n");
                } else if (objError instanceof CMyException) {
                    buff.append(((CMyException) objError).toString()).append("\n");
                } else if (objError instanceof Exception) {
                    buff.append(((Exception) objError).toString()).append("\n");
                }
            }
        }

        return buff.toString();
    }

    public static void main(String[] args) {
        CMyErrors errors = new CMyErrors();
        CMyException ex = new CMyException(1, "My Exception");
        errors.add("This is an error message!");
        errors.add(new CMyException(41, "CMyException error", ex));
        System.out.println(errors.toString());
    }

    private static final int INIT_SIZE = 5;
    private Vector vErrors;

    public CMyErrors add(CMyErrors _errors) {
        if (_errors == null || _errors.isEmpty()) {
            return this;
        } else {
            vErrors.addAll(_errors.vErrors);
            return this;
        }
    }

    public CMyErrors add(String _sInfo, Exception _ex) {
        add(_sInfo);
        add(_ex);
        return this;
    }
}

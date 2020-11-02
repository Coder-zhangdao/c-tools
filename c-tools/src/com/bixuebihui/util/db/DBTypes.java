// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   DBTypes.java

package com.bixuebihui.util.db;


// Referenced classes of package com.bixuebihui.util.database:
//            OracleDB, SQLServerDB, DB2UDB, SybaseASE,
//            DBType

public class DBTypes {

    public DBTypes() {
    }

    public static DBType getDBType(int _nDBType) {
        if (_nDBType < 1 || _nDBType > allDBTypes.length)
            return null;
        else
            return allDBTypes[_nDBType - 1];
    }

    public static void main(String args[]) {
        DBType dbType = getDBType(1);
    }

    public static final int UNKOWN = 0;
    public static final int ORACLE = 1;
    public static final int SQLSERVER = 2;
    public static final int DB2UDB = 3;
    public static final int SybaseASE = 4;
    public static final OracleDB ORACLEDB;
    public static final SQLServerDB SQLSERVERDB;
    public static final DB2UDB db2Instance;
    public static final SybaseASE dbSybaseASE;
    private static DBType allDBTypes[];

    static {
        ORACLEDB = new OracleDB();
        SQLSERVERDB = new SQLServerDB();
        db2Instance = new DB2UDB();
        dbSybaseASE = new SybaseASE();
        allDBTypes = (new DBType[]{
                ORACLEDB, SQLSERVERDB, db2Instance, dbSybaseASE
        });
    }
}

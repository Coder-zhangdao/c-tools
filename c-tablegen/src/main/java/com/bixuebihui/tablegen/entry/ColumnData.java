package com.bixuebihui.tablegen.entry;

import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;

import java.io.Serializable;

/**
 * Container for details about a table column.
 *
 * @author: J.A.Carter
 * Release 1.1
 * <p>
 * <c) Joe Carter 1998
 * Released under GPL. See LICENSE for full details
 */
public class ColumnData implements Serializable {
    static String[] sqlTypes = {"CHAR", "TINYINT", "BIGINT", "INT",
            "SMALLINT", "FLOAT", "REAL", "DOUBLE",
            "NUMERIC", "DECIMAL", "DATE", "VARCHAR",
            "LONGVARCHAR", "TIMESTAMP", "TIME", "BIT",
            "BINARY", "VARBINARY", "LONGVARBINARY", "NULL",
            "OTHER", "TEXT", "CLOB", "NVARCHAR",
            "NTEXT", "DATETIME", "SYSNAME", "VARBINARY",
            "NCHAR"};
    boolean isAutoIncrement;
    /**
     * 注释
     */
    String remarks;
    //sysname in sql server is nvarchar(128) NOT NULL
    long cid;
    int type;
    //数字类型的位数或字符类型的最大长度
    long columns;
    long decimalDigits = 0;
    String name;
    boolean isNullable;
    String defaultValue;
    String comment;

    /**
     * Standard constructor.
     * Requires name, XOPEN type, number of columns.
     * The isNullable is only used for CHAR(x) and DATE fields.
     *
     * @param remarks
     * @param defaultValue
     */
    public ColumnData(String name, long origType, long columns, boolean isNullable, boolean isAutoIncrement, long decimalDigits, String defaultValue, String remarks) {
        this.name = name;
        this.type = (int) origType;
        this.columns = columns;
        this.isNullable = isNullable;
        this.isAutoIncrement = isAutoIncrement;
        this.decimalDigits = decimalDigits;
        this.defaultValue = defaultValue;
        this.remarks = remarks;

// Uncomment this is you want to see the raw types used
// for the columns. Usefully for debugging the inconsistent
// use in between drivers/databases.
//

        switch (type) {

            case 2:
                if (columns > 1) {
                    //derby numeric(n), added by Xing
                    type = 3;
                } else {
                    type = 16;
                }
                break;
            case 3:
                if (decimalDigits > 0) {
                    //mysql decimal(n, decimalDigits);
                    type = 8;
                }

                break;

            case -1:
                // Sybase
                // TEXT
                type = 22;
                break;

            case -2:
                //MySQL
                //TINYBLOB - use VARBINARY for now. Cobble alert!
                type = 18;
                break;

            case -3:
                // MySQL
                // medium blob / blob to LONGVARBINARY
                type = 19;

// Arse -> Some serious type conflicts here
// This issue needs sorting out. Need a heavy duty
// dig down the JDBC specs. Sigh...

// OOOPS - conflict. MySQL gets priority for now.
                // PostGres
                // abstime / timestamp
//        type = 14;  // use timestamp

// OOOPS - conflict. Postgres gets priority for now.
                //Oracle
                //RAW which maps to VARBINARY
//        type = 18;
                break;


            case -4:
                //Oracle
                //LONG RAW which maps to LONGVARBINARY
                type = 19;
                break;

            case -5:
                // MySQL
                // bigint
                type = 3;
                break;

            case -6:
                // Sybase
                // TINYINT
                type = 2;
                break;

            case -7:
                // Sybase
                // BIT
                type = 16;
                break;

            case 1111:
                // PostGres
                // bytea / int28 / reltime / tinterval
                // type = 3;  // use bigint
                //ORACLE NVARCHAR2
                type = 24;
                break;

            case 91:
                // PostGres
                // date
                type = 11;
                break;

            case 92:
                // PostGres
                // timestamp
                type = 14;
                break;

            case 93:
                // MySQL
                // timestamp
                type = 14;
                break;

            case 2005:
                // Oracle
                // Clob
                type = 23;
                break;
            default:
                break;
        }

        if ((type < 1) || (type > 24)) {
            System.err.println("Warning! - column name : " + name +
                    " is of a type not recognised. Value : " + type);
            System.err.println("Defaulting to string");
            type = 12;
        }
    }

    /**
     * Constructor with column type as a String.
     * Requires name, type, number of columns.
     * The isNullable is only used for CHAR(x) and DATE fields.
     */
    public ColumnData(String name, String coltype, int columns, boolean isNullable) {
        this.name = name;
        this.columns = columns;
        this.isNullable = isNullable;

        int i = 0;
        boolean quit = false;

        // invalid
        this.type = -1;
        while (!quit) {
            if (coltype.toUpperCase().compareTo(sqlTypes[i]) == 0) {
                // starts at 1
                this.type = i + 1;
                quit = true;
            }

            i++;
            if (i >= sqlTypes.length) {
                quit = true;
            }
        }

        if (this.type == -1) {
            System.out.println("Column name : " + name + " Type : " + coltype + " is unknown");
        }
    }

    public static ColumnData valueOf(T_metacolumn src) {
        ColumnData d = new ColumnData(src.getCname(), src.getType(), src.getColumns(), src.getIsnullable(), src.getIsauto_increment(), src.getDecimaldigits(),
                null, src.getDescription());
        d.cid = src.getCid();
        return d;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getColumns() {
        return columns;
    }

    public void setColumns(long columns) {
        this.columns = columns;
    }

    public long getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(long decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (columns ^ (columns >>> 32));
        result = prime * result
                + (int) (decimalDigits ^ (decimalDigits >>> 32));
        result = prime * result + (isAutoIncrement ? 1231 : 1237);
        result = prime * result + (isNullable ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + type;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ColumnData other = (ColumnData) obj;
        if (columns != other.columns) {
            return false;
        }
        if (decimalDigits != other.decimalDigits) {
            return false;
        }
        if (isAutoIncrement != other.isAutoIncrement) {
            return false;
        }
        if (isNullable != other.isNullable) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        return type == other.type;
    }

    /**
     * Returns the name of the column.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Converts to entry to a readable form.
     */
    @Override
    public String toString() {
        String res = "";
        String digits = "";

        if ((type == 1) || (type == 12) || (type == 22) || (type == 24) || (type == 25) || (type == 27) || (type == 29)) {
            digits = "(" + columns + ")";
        } else {
            digits = decimalDigits > 0 ? "(" + decimalDigits + ")" : "";
        }

        if ((type > sqlTypes.length) || (type < 0)) {
            res = "Type : " + type + "  Name : " + name;
        } else {
            res = "Type : " + sqlTypes[type - 1] + digits + " Name : " + name;
        }

        return res+(comment==null?"":": "+comment);
        //return super.toString()+"[type="+type+":"+ sqlTypes[type]+",columns="+columns+",name="+name+",isNullable="+isNullable+"]";
    }


    /**
     * Writes out the equivalent java type of the column sql type.
     */
    public String getJavaType() {
        String jType = null;

        switch (type) {
            case 1:
            case 12:
            case 13:
            case 22:
            case 24:
            case 25:
            case 27:
            case 29:
                jType = "String";
                break;

            case 2:
                jType = "Byte";
                break;

            case 3:
                jType = "Long";
                break;

            case 4:
                // jType = "Integer";
                jType = "Long";
                break;

            case 5:
                jType = "Short";
                break;

            case 6:
            case 8:
                jType = "Double";
                break;

            case 9:
            case 10:
                jType = "java.math.BigDecimal";
                break;

            case 7:
                jType = "Float";
                break;

            case 11:
                jType = "Date";
                // always return Timestamp as it's more accurate
                // and is a superset of Date
                // NOPE - lets follow Suns recommendations, so...
                //jType = "Timestamp";
                break;

            case 14:
            case 26:
                jType = "Timestamp";
                break;

            case 15:
                jType = "Time";
                break;

            case 16:
                jType = "Boolean";
                break;

            case 17:
            case 18:
            case 19:
            case 28:
                jType = "byte[]";
                break;

            case 20:
                jType = "null";
                break;

            case 23:
                jType = "com.bixuebihui.jdbc.ClobString"; //oracle CLOB
                break;
            default:
                System.err.println("Warning - col type : " + type + " is unknown");
                jType = "Object";
                break;
        }

        return jType;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }


/**
 * "Et Si Tu N'existais Pas" lyrics:

 Et si tu n'existais pas
 Dis-moi pourquoi j'existerais
 Pour traîner dans un monde sans toi
 Sans espoir et sans regret
 Et si tu n'existais pas
 J'essaierais d'inventer l'amour
 Comme un peintre qui voit sous ses doigts
 Naître les couleurs du jour
 Et qui n'en revient pas

 Et si tu n'existais pas
 Dis-moi pour qui j'existerais
 Des passantes endormies dans mes bras
 Que je n'aimerais jamais
 Et si tu n'existais pas
 Je ne serais qu'un point de plus
 Dans ce monde qui vient et qui va
 Je me sentirais perdu
 J'aurais besoin de toi

 Et si tu n'existais pas
 Dis-moi comment j'existerais
 Je pourrais faire semblant d'être moi
 Mais je ne serais pas vrai
 Et si tu n'existais pas
 Je crois que je l'aurais trouvé
 Le secret de la vie, le pourquoi
 Simplement pour te créer
 Et pour te regarder

 Et si tu n'existais pas
 Dis-moi pourquoi j'existerais
 Pour traîner dans un monde sans toi
 Sans espoir et sans regret
 Et si tu n'existais pas
 J'essaierais d'inventer l'amour
 Comme un peintre qui voit sous ses doigts
 Naître les couleurs du jour
 Et qui n'en revient pas
 */
}

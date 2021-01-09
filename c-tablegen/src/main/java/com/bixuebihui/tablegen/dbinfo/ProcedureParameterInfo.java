package com.bixuebihui.tablegen.dbinfo;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xwx
 */
public class ProcedureParameterInfo extends DatabaseObjectInfo
{
	public static class  F{
		public static final String procedure_cat="procedure_cat";
		public static final String procedure_schem="procedure_schem";
		public static final String procedure_name="procedure_name";

		public static final String column_name="column_name";
		public static final String column_type="column_type";

		public static final String data_type="data_type";
		public static final String type_name="type_name";

		public static final String precision="precision";
		public static final String length="length";
		public static final String scale="scale";
		public static final String radix="radix";

		public static final String nullable="nullable";

		public static final String remarks="remarks";
		public static final String column_def="column_def";
		public static final String sql_data_type="sql_data_type";
		public static final String sql_datetime_sub="sql_datetime_sub";
		public static final String char_octet_length="char_octet_length";
		public static final String ordinal_position="ordinal_position";
		public static final String is_nullable="is_nullable";

		public static final String ss_type_catalog_name="ss_type_catalog_name";
		public static final String ss_type_schema_name="ss_type_schema_name";

		public static final String ss_udt_catalog_name="ss_udt_catalog_name";
		public static final String ss_udt_schema_name="ss_udt_schema_name";
		public static final String ss_udt_assembly_type_name="ss_udt_assembly_type_name";

		public static final String ss_xml_schemacollection_catalog_name="ss_xml_schemacollection_catalog_name";
		public static final String ss_xml_schemacollection_schema_name="ss_xml_schemacollection_schema_name";
		public static final String ss_xml_schemacollection_name="ss_xml_schemacollection_name";

		public static final String ss_data_type="ss_data_type";
	}

	public static ProcedureParameterInfo mapRow (ResultSet r, int index) throws SQLException
	{
		return new ProcedureParameterInfo(DatabaseObjectType.DATATYPE,
				r.getString(F.procedure_cat), r.getString(F.procedure_schem), r.getString(F.procedure_name),

				r.getString(F.column_name), r.getInt(F.column_type), r.getInt(F.data_type), r.getString(F.type_name),
				r.getLong(F.precision),r.getLong(F.length), r.getInt(F.scale), r.getInt(F.radix), r.getBoolean(F.nullable),
				r.getString(F.remarks), r.getString(F.column_def), r.getString(F.sql_data_type), r.getString(F.sql_datetime_sub),
				r.getString(F.char_octet_length), r.getString(F.ordinal_position), r.getString(F.is_nullable),
				r.getString(F.ss_type_catalog_name), r.getString(F.ss_type_schema_name),
				r.getString(F.ss_udt_catalog_name),
				r.getString(F.ss_type_schema_name), r.getString(F.ss_udt_assembly_type_name),
				r.getString(F.ss_xml_schemacollection_catalog_name),
				r.getString(F.ss_xml_schemacollection_schema_name), r.getString(F.ss_xml_schemacollection_name),
				r.getString(F.ss_data_type)

				);	}
	public String getProcedure_cat() {
		return procedure_cat;
	}

	public String getProcedure_schem() {
		return procedure_schem;
	}

	public String getProcedure_name() {
		return procedure_name;
	}

	public String getColumn_name() {
		return column_name;
	}

	public int getColumn_type() {
		return column_type;
	}

	public int getData_type() {
		return data_type;
	}

	public String getType_name() {
		return type_name;
	}

	public long getPrecision() {
		return precision;
	}

	public long getLength() {
		return length;
	}

	public int getScale() {
		return scale;
	}

	public int getRadix() {
		return radix;
	}

	public boolean getNullable() {
		return nullable;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getColumn_def() {
		return column_def;
	}

	public String getSql_data_type() {
		return sql_data_type;
	}

	public String getSql_datetime_sub() {
		return sql_datetime_sub;
	}

	public String getChar_octet_length() {
		return char_octet_length;
	}

	public String getOrdinal_position() {
		return ordinal_position;
	}

	public String getIs_nullable() {
		return is_nullable;
	}

	public String getSs_type_catalog_name() {
		return ss_type_catalog_name;
	}

	public String getSs_type_schema_name() {
		return ss_type_schema_name;
	}

	public String getSs_udt_catalog_name() {
		return ss_udt_catalog_name;
	}

	public String getSs_udt_schema_name() {
		return ss_udt_schema_name;
	}

	public String getSs_udt_assembly_type_name() {
		return ss_udt_assembly_type_name;
	}

	public String getSs_xml_schemacollection_catalog_name() {
		return ss_xml_schemacollection_catalog_name;
	}

	public String getSs_xml_schemacollection_schema_name() {
		return ss_xml_schemacollection_schema_name;
	}

	public String getSs_xml_schemacollection_name() {
		return ss_xml_schemacollection_name;
	}

	public String getSs_data_type() {
		return ss_data_type;
	}

	public ProcedureParameterInfo( DatabaseObjectType dboType,

			String procedure_cat, String procedure_schem,
			String procedure_name, String column_name, int column_type,
			int data_type, String type_name, long precision,
			long length, int scale, int radix, boolean nullable,
			String remarks, String column_def, String sql_data_type,
			String sql_datetime_sub, String char_octet_length,
			String ordinal_position, String is_nullable,
			String ss_type_catalog_name, String ss_type_schema_name,
			String ss_udt_catalog_name, String ss_udt_schema_name,
			String ss_udt_assembly_type_name,
			String ss_xml_schemacollection_catalog_name,
			String ss_xml_schemacollection_schema_name,
			String ss_xml_schemacollection_name, String ss_data_type) {
		super(procedure_cat, procedure_schem, column_name, dboType);
		this.procedure_cat = procedure_cat;
		this.procedure_schem = procedure_schem;
		this.procedure_name = procedure_name;
		this.column_name = column_name;
		this.column_type = column_type;
		this.data_type = data_type;
		this.type_name = type_name;
		this.precision = precision;
		this.length = length;
		this.scale = scale;
		this.radix = radix;
		this.nullable = nullable;
		this.remarks = remarks;
		this.column_def = column_def;
		this.sql_data_type = sql_data_type;
		this.sql_datetime_sub = sql_datetime_sub;
		this.char_octet_length = char_octet_length;
		this.ordinal_position = ordinal_position;
		this.is_nullable = is_nullable;

		this.ss_type_catalog_name = ss_type_catalog_name;
		this.ss_type_schema_name = ss_type_schema_name;
		this.ss_udt_catalog_name = ss_udt_catalog_name;
		this.ss_udt_schema_name = ss_udt_schema_name;
		this.ss_udt_assembly_type_name = ss_udt_assembly_type_name;
		this.ss_xml_schemacollection_catalog_name = ss_xml_schemacollection_catalog_name;
		this.ss_xml_schemacollection_schema_name = ss_xml_schemacollection_schema_name;
		this.ss_xml_schemacollection_name = ss_xml_schemacollection_name;
		this.ss_data_type = ss_data_type;

	}

	/**
	 * TableColumnInfo
	 *
	 */
	private final String procedure_cat,procedure_schem,procedure_name,column_name,  type_name,  remarks,column_def,sql_data_type,sql_datetime_sub,char_octet_length,ordinal_position,is_nullable,
	ss_type_catalog_name,ss_type_schema_name,ss_udt_catalog_name,ss_udt_schema_name,ss_udt_assembly_type_name,
	ss_xml_schemacollection_catalog_name,ss_xml_schemacollection_schema_name,ss_xml_schemacollection_name,ss_data_type;

	private final int column_type,data_type,  scale,radix;
	private final long precision, length;
	private final boolean nullable;


	public String getColumn_typeDescription(){
		String res;
		switch(column_type){
		case 0:
		default:
			res= "procedureColumnUnknown - nobody knows";
			break;
		case 1:
			res ="procedureColumnIn - IN parameter";
			break;
		case 2:
			res ="procedureColumnInOut - INOUT parameter";
			break;
		case 3:
			res ="procedureColumnOut - OUT parameter";
			break;
		case 4:
			res ="procedureColumnReturn - procedure return value";
			break;
		case 5:
			res="procedureColumnResult - result column in ResultSet";
			break;
		}
		return res;
	}


	@Override
    public String toString(){
		return this.getSimpleName()+" column_type="+this.column_type+" data_type="+this.data_type+" type_name="+this.type_name+" "+getColumn_typeDescription();
	}

	/*
	public ProcedureParameterInfo(String catalog, String schema, String tableName,
							String columnName, int dataType, String typeName,
							int columnSize, int decimalDigits, int radix,
							int isNullAllowed, String remarks, String defaultValue,
							int octetLength, int ordinalPosition,
							String isNullable)
	{
		super(catalog, schema, tableName + '.' + columnName,
				DatabaseObjectType.COLUMN);
		_columnName = columnName;
		_dataType = dataType;
		_typeName = typeName;
		_columnSize = columnSize;
		_decimalDigits = decimalDigits;
		_radix = radix;
		_isNullAllowed = isNullAllowed;
		_remarks = remarks;
		_defaultValue = defaultValue;
		_octetLength = octetLength;
		_ordinalPosition = ordinalPosition;
		_isNullable = isNullable;
	}
*/
}

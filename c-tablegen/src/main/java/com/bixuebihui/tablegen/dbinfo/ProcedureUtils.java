package com.bixuebihui.tablegen.dbinfo;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bixuebihui.tablegen.TableUtils;

public class ProcedureUtils {
	private static final Log mLog = LogFactory.getLog(ProcedureUtils.class);

	/**
     * Retrieves a description of the given catalog's stored procedure parameter
     * and result columns.
     *
     * <P>Only descriptions matching the schema, procedure and
     * parameter name criteria are returned.  They are ordered by
     * PROCEDURE_CAT, PROCEDURE_SCHEM, PROCEDURE_NAME and SPECIFIC_NAME. Within this, the return value,
     * if any, is first. Next are the parameter descriptions in call
     * order. The column descriptions follow in column number order.
     *
     * <P>Each row in the <code>ResultSet</code> is a parameter description or
     * column description with the following fields:
     *  <OL>
     *  <LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be <code>null</code>)
     *  <LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be <code>null</code>)
     *  <LI><B>PROCEDURE_NAME</B> String => procedure name
     *  <LI><B>COLUMN_NAME</B> String => column/parameter name
     *  <LI><B>COLUMN_TYPE</B> Short => kind of column/parameter:
     *      <UL>
     *      <LI> procedureColumnUnknown - nobody knows
     *      <LI> procedureColumnIn - IN parameter
     *      <LI> procedureColumnInOut - INOUT parameter
     *      <LI> procedureColumnOut - OUT parameter
     *      <LI> procedureColumnReturn - procedure return value
     *      <LI> procedureColumnResult - result column in <code>ResultSet</code>
     *      </UL>
     *  <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
     *  <LI><B>TYPE_NAME</B> String => SQL type name, for a UDT type the
     *  type name is fully qualified
     *  <LI><B>PRECISION</B> int => precision
     *  <LI><B>LENGTH</B> int => length in bytes of data
     *  <LI><B>SCALE</B> short => scale -  null is returned for data types where
     * SCALE is not applicable.
     *  <LI><B>RADIX</B> short => radix
     *  <LI><B>NULLABLE</B> short => can it contain NULL.
     *      <UL>
     *      <LI> procedureNoNulls - does not allow NULL values
     *      <LI> procedureNullable - allows NULL values
     *      <LI> procedureNullableUnknown - nullability unknown
     *      </UL>
     *  <LI><B>REMARKS</B> String => comment describing parameter/column
     *  <LI><B>COLUMN_DEF</B> String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be <code>null</code>)
     *      <UL>
     *      <LI> The string NULL (not enclosed in quotes) - if NULL was specified as the default value
     *      <LI> TRUNCATE (not enclosed in quotes)        - if the specified default value cannot be represented without truncation
     *      <LI> NULL                                     - if a default value was not specified
     *      </UL>
     *  <LI><B>SQL_DATA_TYPE</B> int  => reserved for future use
     *  <LI><B>SQL_DATETIME_SUB</B> int  => reserved for future use
     *  <LI><B>CHAR_OCTET_LENGTH</B> int  => the maximum length of binary and character based columns.  For any other datatype the returned value is a
     * NULL
     *  <LI><B>ORDINAL_POSITION</B> int  => the ordinal position, starting from 1, for the input and output parameters for a procedure. A value of 0
     *is returned if this row describes the procedure's return value.  For result set columns, it is the
     *ordinal position of the column in the result set starting from 1.  If there are
     *multiple result sets, the column ordinal positions are implementation
     * defined.
     *  <LI><B>IS_NULLABLE</B> String  => ISO rules are used to determine the nullability for a column.
     *       <UL>
     *       <LI> YES           --- if the column can include NULLs
     *       <LI> NO            --- if the column cannot include NULLs
     *       <LI> empty string  --- if the nullability for the
     * column is unknown
     *       </UL>
     *  <LI><B>SPECIFIC_NAME</B> String  => the name which uniquely identifies this procedure within its schema.
     *  </OL>
     *
     * <P><B>Note:</B> Some databases may not return the column
     * descriptions for a procedure.
     *
     * <p>The PRECISION column represents the specified column size for the given column.
     * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
     * this is the length in bytes. Null is returned for data types where the
     * column size is not applicable.
     * @param catalog a catalog name; must match the catalog name as it
     *        is stored in the database; "" retrieves those without a catalog;
     *        <code>null</code> means that the catalog name should not be used to narrow
     *        the search
     * @param schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search
     * @param procedureNamePattern a procedure name pattern; must match the
     *        procedure name as it is stored in the database
     * @param columnNamePattern a column name pattern; must match the column name
     *        as it is stored in the database
     * @return <code>ResultSet</code> - each row describes a stored procedure parameter or
     *      column
     * @exception SQLException if a database access error occurs
     * @see #getSearchStringEscape
     */

	public static Vector<ProcedureParameterInfo> getProcedureColumns(DatabaseMetaData metaData,IProcedureInfo ti)
			throws SQLException
		{
			ResultSet rs = metaData.getProcedureColumns(ti.getCatalogName(),
														ti.getSchemaName(),
														ti.getSimpleName(),
														"%");
			Vector<ProcedureParameterInfo> v = new Vector<ProcedureParameterInfo>();
			while(rs.next()){
				ProcedureParameterInfo res = ProcedureParameterInfo.mapRow(rs, rs.getRow());
				v.add(res);
			}

			return v;
		}
	public static Vector<ProcedureParameterInfo> getFunctionColumns(DatabaseMetaData metaData,IProcedureInfo ti)
			throws SQLException
		{
			ResultSet rs = metaData.getFunctionColumns(ti.getCatalogName(),
														ti.getSchemaName(),
														ti.getSimpleName(),
														"%");
			Vector<ProcedureParameterInfo> v = new Vector<ProcedureParameterInfo>();
			while(rs.next()){
				ProcedureParameterInfo res = ProcedureParameterInfo.mapRow(rs, rs.getRow());
				v.add(res);
			}

			return v;
		}


	/**
	 *<P>Each procedure description has the the following columns:
     *  <OL>
     *  <LI><B>PROCEDURE_CAT</B> String => procedure catalog (may be <code>null</code>)
     *  <LI><B>PROCEDURE_SCHEM</B> String => procedure schema (may be <code>null</code>)
     *  <LI><B>PROCEDURE_NAME</B> String => procedure name
     *  <LI> reserved for future use
     *  <LI> reserved for future use
     *  <LI> reserved for future use
     *  <LI><B>REMARKS</B> String => explanatory comment on the procedure
     *  <LI><B>PROCEDURE_TYPE</B> short => kind of procedure:
     *      <UL>
     *      <LI> procedureResultUnknown - Cannot determine if  a return value
     *       will be returned
     *      <LI> procedureNoResult - Does not return a return value
     *      <LI> procedureReturnsResult - Returns a return value
     *      </UL>
     *  <LI><B>SPECIFIC_NAME</B> String  => The name which uniquely identifies this
     * procedure within its schema.
     *  </OL>
     * <p>
	 */
	public static Vector<ProcedureInfo> getProcedure(DatabaseMetaData metaData,
			String catalog, String schema, String tableOwner,
			Map<String, String> includeList,
			Map<String, String> excludeList) throws SQLException {

		Vector<ProcedureInfo> procedures = null;

		ResultSet tables = metaData.getProcedures(catalog, schema, "%");


		try {
			procedures = new Vector<ProcedureInfo>();

			String tableName = "";

			// mLog.info("");
			// mLog.info("Classes being created for the following tables...");
			while (tables.next()) {

				// System.out.println("tables.getString(2)="+tables.getString(2));
				// System.out.println("tableOwner="+tableOwner);
				//table.getString(2)=dbo
				//table.getString(1)=lenovoDB
				//table.getString(3)=11111111;1
				//table.getString(4)=-1
				//table.getString(5)=-1
				//table.getString(6)=-1
				//table.getString(7)=null
				//table.getString(8)=null
				String owner = tables.getString(2);

				if (tableOwner != null
						&& owner != null
						&& !tableOwner.toUpperCase().equals(
								owner.toUpperCase())) {
					 mLog.debug("tableOwner is :" + tableOwner + " skip: "
					 + owner);
					continue;
				}

				tableName = tables.getString(3);

				// If we are using a table list
				// then check against that first.
				// If no list then we use everything.
				//
				//if (!procedures.contains(tableName))
				if (TableUtils.matchTableName(includeList, tableName)) {
					if (!TableUtils.isExcluded(excludeList, tableName)) {
						ProcedureInfo p = new ProcedureInfo(tables.getString(1), tables.getString(2), tables.getString(3), tables.getString(7), tables.getInt(8));
						procedures
						.addElement(p);
						String str="Procedure name = "+tableName+" "+tables.getString(4)+" "+tables.getString(5)+" "+tables.getString(6)+" "+tables.getString(7)+" "+tables.getString(8);
						System.out.println(str);
					 mLog.info(str);
					} else {
						// mLog
						// .info("Skip table name contain char $ or in
						// exclude
						// list: "
						// + tableName);
					}
				} else {
					// mLog.info("Skip table not in include list: " +
					// tableName);
				}
				// else if (tablesList.get(tableName) != null)
				// {
				// tableNames.addElement(tableName);
				// mLog.info(tableName);
				// }
			}
		} finally {
			DbUtils.close(tables);
		}
		// mLog.info("");
		return procedures;
	}

	public static Vector<ProcedureInfo> getFunctions(DatabaseMetaData metaData,
			String catalog, String schema, String tableOwner,
			Map<String, String> includeList,
			Map<String, String> excludeList) throws SQLException {

		Vector<ProcedureInfo> procedures = null;

		ResultSet tables = metaData.getFunctions(catalog, schema, "%");


		try {
			procedures = new Vector<ProcedureInfo>();

			String tableName = "";

			// mLog.info("");
			// mLog.info("Classes being created for the following tables...");
			while (tables.next()) {

				// System.out.println("tables.getString(2)="+tables.getString(2));
				// System.out.println("tableOwner="+tableOwner);
				//table.getString(2)=dbo
				//table.getString(1)=lenovoDB
				//table.getString(3)=11111111;1
				//table.getString(4)=-1
				//table.getString(5)=-1
				//table.getString(6)=-1
				//table.getString(7)=null
				//table.getString(8)=null
				String owner = tables.getString(2);

				if (tableOwner != null
						&& owner != null
						&& !tableOwner.toUpperCase().equals(
								owner.toUpperCase())) {
					 mLog.debug("tableOwner is :" + tableOwner + " skip: "
					 + owner);
					continue;
				}

				tableName = tables.getString(3);

				// If we are using a table list
				// then check against that first.
				// If no list then we use everything.
				//
				//if (!procedures.contains(tableName))
				if (TableUtils.matchTableName(includeList, tableName)) {
					if (!TableUtils.isExcluded(excludeList, tableName)) {
						ProcedureInfo p = new ProcedureInfo(tables.getString(1), tables.getString(2), tables.getString(3), tables.getString(7), tables.getInt(8));
						procedures
						.addElement(p);
						String str="Procedure name = "+tableName+" "+tables.getString(4)+" "+tables.getString(5)+" "+tables.getString(6)+" "+tables.getString(7)+" "+tables.getString(8);
						System.out.println(str);
					 mLog.info(str);
					} else {
						// mLog
						// .info("Skip table name contain char $ or in
						// exclude
						// list: "
						// + tableName);
					}
				} else {
					// mLog.info("Skip table not in include list: " +
					// tableName);
				}
				// else if (tablesList.get(tableName) != null)
				// {
				// tableNames.addElement(tableName);
				// mLog.info(tableName);
				// }
			}
		} finally {
			DbUtils.close(tables);
		}
		// mLog.info("");
		return procedures;
	}

}

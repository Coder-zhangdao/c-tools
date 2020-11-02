package com.bixuebihui.tablegen.dbinfo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.IDbHelper;
import org.junit.Before;
import org.junit.Test;

public class ProcedureUtilsTest {
	IDbHelper dbHelper;

	DatabaseMetaData metaData;
	String catalog = null;
	String schema = null;
	String tableOwner = "dbo";
	Map<String, String> includeList = null;
	Map<String, String> excludeList = new HashMap<String, String>();



	@Before
	public void setUp() {
		try {
			 dbHelper= (IDbHelper) BeanFactory.createObjectById("dbHelper");
			metaData = dbHelper.getConnection().getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetProcedureColumns() throws SQLException {
		IProcedureInfo info;

		Vector<ProcedureInfo> res = ProcedureUtils.getProcedure(metaData,
				catalog, schema, tableOwner, includeList, excludeList);
		info = res.get(2);

		Vector<ProcedureParameterInfo> rs = ProcedureUtils.getProcedureColumns(metaData, info);
		//dump(rs);
		for(ProcedureParameterInfo in:rs){
			System.out.println(in);
		}
	}

	@Test
	public void testProcess() throws SQLException{


		Vector<ProcedureInfo> res = ProcedureUtils.getProcedure(metaData,
				catalog, schema, tableOwner, includeList, excludeList);

		for(ProcedureInfo info:res){
			Vector<ProcedureParameterInfo> rs = ProcedureUtils.getProcedureColumns(metaData, info);
			String str = ProcedureGen.process(info, rs, true);
			System.out.println(str);
		}
	}

	@Test
	public void testGetProcedure() throws SQLException {

		Vector<ProcedureInfo> res = ProcedureUtils.getProcedure(metaData,
				catalog, schema, tableOwner, includeList, excludeList);

		System.out.println(res);
	}

	@Test
	public void testCallableStatement() throws SQLException{
		String procedure ="{call sp_xyz(?,?)}";
		Connection con = dbHelper.getConnection();
		CallableStatement cstmt = con.prepareCall(procedure);
		int abc=0;
		cstmt.setInt(1, abc);

		int sqlType =1;
		cstmt.registerOutParameter(2, sqlType );

		cstmt.execute();

		int count = cstmt.getInt(2);

		cstmt.close();
		con.close();

/**
 *
down vote
accepted


You need to get a ResultSet via:

ResultSet rs = stmt.executeQuery("SELECT * FROM setoffunc()");
while (rs.next()) {
   // read results
}
rs.close();
stmt.close();

Or:

// Procedure call.
CallableStatement proc = conn.prepareCall("{ ? = call refcursorfunc() }");
proc.registerOutParameter(1, Types.OTHER);
proc.execute();
ResultSet results = (ResultSet) proc.getObject(1);
while (results.next()) {
    // read results
}
results.close();
proc.close();
 */


	}

}

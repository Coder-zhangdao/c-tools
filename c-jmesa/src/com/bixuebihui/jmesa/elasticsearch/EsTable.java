package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.EasyTable;
import org.jmesa.limit.Limit;
import org.jmesa.limit.LimitFactory;
import org.jmesa.limit.RowSelect;

import java.sql.SQLException;

public class EsTable extends EasyTable {
    EsRequest request;
    public EsTable(String  host,String username, String password,String tableCaption, String indexName) {
        super();
        request =  new EsRequest(host, username, password);
        request.indexName  = indexName;
        this.id = tableCaption;
        this.tableCaption = tableCaption;
    }

    @Override
    public String json( String jsonQuery) throws SQLException {
        try {
            Limit limit = getLimit(tableCaption, jsonQuery);
            ElasticSearchFilter filter = new ElasticSearchFilter();
            getFilter(filter, limit, id);
            RowSelect rowSelect = limit.getRowSelect();
            String query = filter.toEsObject(request.indexName, rowSelect.getRowStart(), rowSelect.getRowEnd() - rowSelect.getRowStart());
            return request.esJsonToTableJson(request.query(query), rowSelect );
        }catch (Exception e){
            throw new SQLException(e);
        }
    }


    public Limit getLimit(String id, String json) {
        LimitFactory limitFactory = new LimitFactory(id, json);
        return  limitFactory.createLimit();
    }

}

package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.EasyTable;
import org.jmesa.limit.Limit;
import org.jmesa.limit.LimitFactory;
import org.jmesa.limit.RowSelect;
import org.jmesa.limit.SortSet;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xwx
 */
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
            LinkedHashMap<String,String> sort = toEsSort(limit.getSortSet());
            String query = filter.toEsObject(rowSelect.getRowStart(),
                    rowSelect.getRowEnd() - rowSelect.getRowStart(), sort);
            return request.esJsonToTableJson(request.query(query), rowSelect );
        }catch (Exception e){
            throw new SQLException(e);
        }
    }

    private LinkedHashMap<String,String> toEsSort(SortSet sortSet) {
        LinkedHashMap<String,String> res = new LinkedHashMap<>();
        if(sortSet.isSorted()) {
            for (org.jmesa.limit.Sort sort :
                    sortSet.getSorts().stream().sorted(Comparator.comparingInt(org.jmesa.limit.Sort::getPosition)).collect(Collectors.toList())) {
                res.put(sort.getProperty(), sort.getOrder().toParam());
            }
        }
        return res;
    }


    public Limit getLimit(String id, String json) {
        LimitFactory limitFactory = new LimitFactory(id, json);
        return  limitFactory.createLimit();
    }

}

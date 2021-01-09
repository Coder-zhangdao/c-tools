/**
 *
 */
package com.bixuebihui.jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>SqlSort class.</p>
 *
 * @author xwx
 * @version $Id: $Id
 */
public class SqlSort {

	List<Sort> sorts = new ArrayList<>();

    /**
     * <p>addSort.</p>
     *
     * @param property a {@link java.lang.String} object.
     * @param order    a {@link java.lang.String} object.
     */
    public void addSort(String property, String order) {
        sorts.add(new Sort(property, order));
    }

    /**
     * <p>Constructor for SqlSort.</p>
     *
     * @param src a {@link SqlSort} object.
     */
    public SqlSort(SqlSort src) {
        //Sort为不变类，多个类引用不会因意外改变造不可预知问题
        this.sorts.addAll(src.sorts);
    }

    /**
     * <p>Constructor for SqlSort.</p>
     */
    public SqlSort() {
    }


    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        if (sorts.size() <= 0) {
            return "";
        }

        StringBuilder criteria = new StringBuilder(" order by ");
        for (Sort sort : sorts) {
            buildCriteria(criteria, sort.getProperty(), sort.getOrder());
        }

        if (criteria.lastIndexOf(",") == criteria.length() - 1) {
            return criteria.substring(0, criteria.length() - 1);
        }
        return criteria.toString();
    }

	private void buildCriteria(StringBuilder criteria, String property,
			String order) {
		if (order.equals(Sort.ASC)) {
			criteria.append(property).append(" ").append(Sort.ASC).append(",");
		} else if (order.equals(Sort.DESC)) {
			criteria.append(property).append(" ").append(Sort.DESC).append(",");
		}
	}

    /**
     * Sort 为不变类， 便于克隆
     */
    public static class Sort {


        public final static String ASC = "asc";
        public final static String DESC = "desc";

        private final String property;
        private final String order;

        public Sort(String property, String order) {
            this.property = property;
            this.order = order;
        }

        public String getProperty() {
            return property;
        }

        public String getOrder() {
            return order;
        }
    }

    /**
     * <p>clear.</p>
     */
    public void clear() {
        if(sorts!=null) {
            this.sorts.clear();
        }
    }

}

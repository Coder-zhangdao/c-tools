package com.bixuebihui.jmesa.elasticsearch.query;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Bool query.
 *
 * @author Nicolas Ruflin <spam@ruflin.com>
 *
 *  https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-bool-query.html
 */
public class Bool extends Query {

        /**
         * Add should part to query.
         *
         * @param  args Query|array, Should query
         *
         * @return this
         */
        public Bool addShould(Query args)
        {
            return this.addQuery("should", args);
        }


        /**
         * Add must part to query.
         *
         * @param args Query|array Must query
         *
         * @return this
         */
        public Bool addMust(Query args)
        {
            return this.addQuery("must", args);
        }


        /**
         * Add must not part to query.
         *
         * @param  args Query|array,  Must not query
         *
         * @return this
         */
        public Bool addMustNot(Query args)
        {
            return this.addQuery("must_not", args);
        }

        /**
         * Sets the filter.
         *
         * @param  filter Filter object
         *
         * @return this
         */
        public Bool addFilter(Query filter)
        {
            return (Bool) this.addParam("filter", filter);
        }

        /**
         * Adds a query to the current object.
         *
         * @param type string     type Query
         * @param args Query|array Query
         *
         * @return this
         */
        protected Bool addQuery(String type, Query args)
        {
            return (Bool) this.addParam(type, args.toArray());
        }

        /**
         * Sets boost value of this query.
         *
         * @param  boost Boost value
         *
         * @return this
         */
        public Bool setBoost(float boost)
        {
            return (Bool) this.setParam("boost", boost);
        }

        /**
         * Sets the minimum number of should clauses to match.
         *
         * @param minimum int|string  Minimum value
         *
         * @return this
         */
        public Bool setMinimumShouldMatch(int minimum)
        {
            return (Bool) this.setParam("minimum_should_match", minimum);
        }

        /**
         * Converts array to an object in case no queries are added.
         *
         * @return array
         */
        @Override
        public Map toArray()
        {
            if (this.getParams()==null) {
                this.setParams(Maps.newHashMap());
            }

            return  super.toArray();
        }
    }

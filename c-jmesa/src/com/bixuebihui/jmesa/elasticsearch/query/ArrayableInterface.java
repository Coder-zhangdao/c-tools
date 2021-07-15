package com.bixuebihui.jmesa.elasticsearch.query;

import java.util.Map;

/**
 * Interface for params.
 *
 *
 * @author Evgeniy Sokolov <ewgraf@gmail.com>
 */
interface ArrayableInterface
{
    /**
     * Converts the object to an array.
     *
     * @return array Object as array
     */
     Map<String, Object> toArray();
}

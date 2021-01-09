package com.bixuebihui.algorithm;

import com.bixuebihui.sql.PooledPreparedStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;

/**
 * <p>RemoveActionImpl class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class RemoveActionImpl implements IRemoveAction {

    private static final Log log = LogFactory.getLog(RemoveActionImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean actionAfterRemove(Object val) {
        if(val instanceof PooledPreparedStatement){
            try {
                ((PooledPreparedStatement)val).getStatement().close();
                log.debug("Debug-RemoveActionImpl-PooledPreparedStatement.close()");
            } catch (SQLException e) {
                log.warn(e);
            }
        }else{
            log.debug("Debug-RemoveActionImpl.actionAfterRemove.close()");
        }
        return false;
    }

}

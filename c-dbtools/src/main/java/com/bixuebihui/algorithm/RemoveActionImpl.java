package com.bixuebihui.algorithm;

import com.bixuebihui.sql.PooledPreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * <p>RemoveActionImpl class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class RemoveActionImpl implements IRemoveAction {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveActionImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean actionAfterRemove(Object val) {
        if(val instanceof PooledPreparedStatement){
            try {
                ((PooledPreparedStatement)val).getStatement().close();
                LOG.debug("Debug-RemoveActionImpl-PooledPreparedStatement.close()");
            } catch (SQLException e) {
                LOG.warn("",e);
            }
        }else{
            LOG.debug("Debug-RemoveActionImpl.actionAfterRemove.close()");
        }
        return false;
    }

}

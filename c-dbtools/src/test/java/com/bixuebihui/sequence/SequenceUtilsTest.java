package com.bixuebihui.sequence;

import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.datasource.DataSourceTest;
import com.bixuebihui.jdbc.DbHelper;
import junit.framework.TestCase;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: leizhimin
 * Date: 2008-4-2 15:31:30
 * Company: LavaSoft(http://lavasoft.blog.51cto.com)
 * Sequence测试(客户端)
 */
public class SequenceUtilsTest extends TestCase {
    final DbHelper dbHelper = new DbHelper();

    protected void setUp() throws SQLException {
        //DbcpDataSource ds = new DbcpDataSource();
        //ds.setDatabaseConfig(DataSourceTest.getConfigDerby());
        BitmechanicDataSource ds = new BitmechanicDataSource();
        ds.setDatabaseConfig(DataSourceTest.getConfigH2());

        dbHelper.setDataSource(ds);
        dbHelper.executeNoQuery(KeyInfo.INSTALL);
    }

    class RunSeq implements Runnable {
        volatile boolean pass = false;

        public void run() {
            System.out.println("----------test(" + this.hashCode() + ")----------");
            long old = 0;
            pass = true;
            for (int i = 0; i < 200; i++) {
                long x = SequenceUtils.getInstance().getNextKeyValue("sdaf", dbHelper);
                if (x <= old) {
                    System.err.println("Error: x=" + x + " old=" + old + " x must bigger than old");
                }
                pass = pass && x > old;
                old = x;
            }

        }

    }

    /**
     * 测试Sequence方法
     *
     * @throws InterruptedException
     */
    public void testGetNextKeyValue() throws InterruptedException {
        synchronized (this) {

            RunSeq r1 = new RunSeq();
            RunSeq r2 = new RunSeq();
            RunSeq r3 = new RunSeq();
            Thread a = new Thread(r1);
            Thread b = new Thread(r2);
            Thread c = new Thread(r3);
            a.start();
            b.start();
            c.start();
            System.out.print("wait threads to finish.");
            while (a.isAlive() || b.isAlive() || c.isAlive()) {
                this.wait(100);
                System.out.print(".");
            }
            assertTrue(r1.pass);
            assertTrue(r2.pass);
            assertTrue(r3.pass);
            System.out.println("Done.");
        }
    }

}

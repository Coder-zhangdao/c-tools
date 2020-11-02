package com.bixuebihui.jdbc.aop;

import com.bixuebihui.jdbc.DbHelper;
import org.aopalliance.intercept.MethodInvocation;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DbHelperAroundAdviceTest {


    @Test
    public void invoke() throws Throwable {

        DbHelperAroundAdvice advice = new DbHelperAroundAdvice();
        MethodInvocation methodInvocation = EasyMock.createMock(MethodInvocation.class);
        EasyMock.expect(methodInvocation.getMethod()).andReturn(DbHelper.class.getMethod("executeNoQuery", String.class));
        EasyMock.expect(methodInvocation.getArguments()).andReturn(new Object[]{"select 1+2"});
        Object a = new Object();
        EasyMock.expect(methodInvocation.proceed()).andReturn(a);
        EasyMock.replay(methodInvocation);
        Object b = advice.invoke(methodInvocation);
        assertEquals(a, b);
        if(advice.isDebug())
            assertEquals(1, DbHelperAroundAdvice.getSqlStatStore().size());
        else
            assertEquals(0, DbHelperAroundAdvice.getSqlStatStore().size());
    }

}

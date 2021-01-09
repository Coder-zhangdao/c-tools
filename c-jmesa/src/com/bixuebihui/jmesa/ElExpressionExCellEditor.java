package com.bixuebihui.jmesa;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jmesa.view.editor.expression.ElExpressionCellEditor;
import org.jmesa.view.editor.expression.Expression;

import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author xwx
 */
public class ElExpressionExCellEditor extends ElExpressionCellEditor {

    protected Map<String, ?> addtionalContext;
    protected String var;

    public ElExpressionExCellEditor(Expression expression, Map<String, ?> context) {
        super(expression);
        var = expression.getVar();
        addtionalContext = context;
    }

    public ElExpressionExCellEditor(String var, Object template, Map<String, ?> context) {
        super(var, template);
        this.var = var;
        addtionalContext = context;
    }


    /**
     * Creates a VariableResolver based on the current row bean and variable name.
     *
     * @param item The row's backing bean.
     */
    @Override
    protected VariableMapper getVariableMapper(Object item) {

        Map<String, Object> context = new HashMap<>(16);

        if (addtionalContext != null) {
            context.putAll(addtionalContext);
        }

        return super.getVariableMapper(item);
    }

    @Override
    protected FunctionMapper getFunctionMapper() {

        return new FunctionMapper() {
            Map<String, Method> map = new Hashtable<>();

            //避免命名冲突，三个参数以上的方法加上参数个数后缀
            @Override
            public Method resolveFunction(String prefix, String localName) {
                if ("fn".equals(prefix)) {
                    if (map.size() == 0) {

                        for (Method m : StringUtils.class.getMethods()) {
                            Class<?>[] types = m.getParameterTypes();
                            String name = changeName(m, types);
                            //System.out.println(name);
                            map.put(name, m);
                        }

                        for (Method m : StringEscapeUtils.class.getMethods()) {
                            Class<?>[] types = m.getParameterTypes();

                            if (types != null && types.length > 0) {
                                if (types[0] == java.io.Writer.class) {
                                    continue; //忽略参数带Writer方法
                                }
                            }

                            String name = changeName(m, types);

                            map.put(name, m);
                        }
                    }
                    return map.get(localName);

                }
                return null;
            }

            private String changeName(Method m, Class<?>[] types) {
                String name = m.getName();
                if (types != null && types.length > 2) {
                    name += types.length;
                }
                return name;
            }

        };
    }


}

package com.bixuebihui.filter;


import java.util.Properties;

/**
 * 对形如 ${abc} 的标记进行简单过滤替换
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class ELFilter implements IFilter {


	private final String basedir;

	private final Properties properties;

    /**
     * <p>Constructor for ELFilter.</p>
     */
    public ELFilter() {
        this(null);
    }

    /**
     * <p>Constructor for ELFilter.</p>
     *
     * @param properties a {@link java.util.Properties} object.
     */
    public ELFilter(Properties properties) {
        this.properties = properties;
        this.basedir = System.getProperty("user.dir");
    }

    /**
     * <p>evaluate.</p>
     *
     * @param expr a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object evaluate(String expr) {
        String value = null;

        if (expr == null) {
            return null;
        }

        String expression = stripTokens(expr);
        if (expression.equals(expr)) {
            int index = expr.indexOf("${");
            if (index >= 0) {
                int lastIndex = expr.indexOf('}', index);
                if (lastIndex >= 0) {
                    String retVal = expr.substring(0, index);

                    if (index > 0 && expr.charAt(index - 1) == '$') {
                        retVal += expr.substring(index + 1, lastIndex + 1);
                    } else {
                        retVal += evaluate(expr.substring(index, lastIndex + 1));
                    }

                    retVal += evaluate(expr.substring(lastIndex + 1));
                    return retVal;
                }
            }

            // Was not an expression
            if (expression.contains("$$")) {
                return expression.replaceAll("\\$\\$", "\\$");
            } else {
                return expression;
            }
        }

        if ("basedir".equals(expression)) {
            value = basedir;
        } else {
            value = System.getProperty(expression);
            if(value==null) value= System.getenv(expression);
        }
        if (value == null && properties != null) {
            // We will attempt to get nab a system property as a way to specify a
            // parameter to a plugins. My particular case here is allowing the surefire
            // plugin to run a single test so I want to specify that class on the cli
            // as a parameter.

            value = properties.getProperty(expression);

        }

        if (value !=null) {
            String val = value;
            int exprStartDelimiter = val.indexOf("${");

            if (exprStartDelimiter >= 0) {
                if (exprStartDelimiter > 0) {
                    value = val.substring(0, exprStartDelimiter)
                            + evaluate(val.substring(exprStartDelimiter));
                } else {
                    value = (String) evaluate(val.substring(exprStartDelimiter));
                }
            }
        }

        return value;
    }

    private String stripTokens(String expr) {
        if (expr.startsWith("${") && (expr.indexOf('}') == (expr.length() - 1))) {
            expr = expr.substring(2, expr.length() - 1);
        }
        return expr;
    }

    /** {@inheritDoc} */
    public String filter(String s) {
        return this.evaluate(s).toString();
    }

}

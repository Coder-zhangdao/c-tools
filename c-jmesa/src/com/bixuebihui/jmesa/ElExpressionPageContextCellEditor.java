package com.bixuebihui.jmesa;

import javax.el.VariableMapper;
import javax.servlet.jsp.PageContext;

import com.sun.el.lang.VariableMapperImpl;
import org.jmesa.view.editor.expression.Expression;

public class ElExpressionPageContextCellEditor extends ElExpressionExCellEditor {

	protected PageContext addtionalContext;
	protected String var;

	public ElExpressionPageContextCellEditor(Expression expression,PageContext context) {
		super(expression, null);
		var = expression.getVar();
		addtionalContext = context;
	}

	public ElExpressionPageContextCellEditor(String var, Object template, PageContext context) {
         super(var, template, null);
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
   	 	addtionalContext.getRequest().setAttribute(var, item);
    	return new ExVariableMapper(super.getVariableMapper(item), addtionalContext);
    }

    public static class ExVariableMapper extends VariableMapperImpl{

    	PageContext pageContext;
		VariableMapper defaultMapper;

		ExVariableMapper(VariableMapper defaultMapper, PageContext pageContext){
			this.pageContext = pageContext;
			this.defaultMapper = defaultMapper;
		}


	}


}

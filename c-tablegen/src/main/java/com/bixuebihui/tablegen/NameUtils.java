package com.bixuebihui.tablegen;

import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NameUtils {
	static char KEYWORD_APPEND_CHAR = '_';

	static Set<String> PROJECT_FIELD_KEYWORD = new HashSet<>(Arrays.asList(new String[]{"length", "start", "draw", "count"}));
	static Set<String> JAVA_KEYWORD = new HashSet<>(Arrays.asList(new String[]{"abstract",
			"assert",
			"boolean",
			"break",
			"byte",
			"case",
			"catch",
			"char",
			"class",
			"const",
			"continue", "default", "do", "double", "else",
			"enum", "extends", "final", "finally", "float",
			"for", "goto", "if", "implements", "import",
			"instanceof", "int", "interface", "long", "native",
			"new", "package", "private", "protected", "public",
			"return", "strictfp", "short", "static", "super",
			"switch", "synchronized", "this", "throw", "throws",
			"transient", "try", "void", "volatile", "while"}));

	public static boolean isYes(String str){
		return "Y".equals(str)
		|| "YES".equals(str);
	}

	public static String columnNameToFieldName(String columnName){
		if (JAVA_KEYWORD.contains(columnName) || (PROJECT_FIELD_KEYWORD.contains(columnName))) {
			columnName = KEYWORD_APPEND_CHAR + columnName;
		}
		return columnName;
	}

    /**
     * Upper cases the first character in a String.
	 * @Since 1.1 snake case to camel case
     * @param p "camel_case"
	 * @return  returns "camelCase"
     */
	public static String firstUp(String p) {
        // return p.substring(0, 1).toUpperCase() + p.substring(1);
        // Guava
		// CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "camel_case"); // returns camelCase
		// CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "CAMEL_CASE"); // returns CamelCase
		// CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, "camelCase"); // returns CAMEL_CASE
		// CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "CamelCase"); // returns camel-case


		return CaseUtils.toCamelCase(p, true, '_');
	}

	public static String firstLow(String p, boolean keepCase) {
		//return keepCase ? (p.substring(0, 1).toLowerCase() + p.substring(1)) :
		// (p.substring(0, 1).toLowerCase() + p.substring(1).toLowerCase());
		return CaseUtils.toCamelCase(p, false, '_');

	}

	public static String getConfigBaseDir(String propertiesFilename) {
		int i = propertiesFilename.lastIndexOf(File.separator) + 1;
		return  propertiesFilename.substring(0, i);
	}

}

/*
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bixuebihui.filters;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Example filter that dumps interesting state information about a request to
 * the associated servlet context log file, before allowing the servlet to
 * process the request in the usual way. This can be installed as needed to
 * assist in debugging problems.
 *
 * @author Craig McClanahan
 * @version $Revision: 1.2 $ $Date: 2004/03/18 16:40:28 $
 */

public final class RequestDumperFilter implements Filter {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The filter configuration object we are associated with. If this value is
	 * null, this filter instance is not currently configured.
	 */
	private FilterConfig filterConfig = null;

	// --------------------------------------------------------- Public Methods

	/**
	 * Take this filter out of service.
	 */
	@Override
	public void destroy() {

		this.filterConfig = null;

	}

	/**
	 * Time the processing that is performed by all subsequent com.bixuebihui.filters in the
	 * current filter stack, including the ultimately invoked servlet.
	 *
	 * @param request
	 *            The servlet request we are processing
	 * @param response
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 *
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {

		if (filterConfig == null) {
            return;
        }

		// Render the generic servlet request properties
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		writer.println("Request Received at "
				+ (new Timestamp(System.currentTimeMillis())));
		writer.println(" characterEncoding=" + request.getCharacterEncoding());
		writer.println("     contentLength=" + request.getContentLength());
		writer.println("       contentType=" + request.getContentType());
		writer.println("            locale=" + request.getLocale());
		writer.print("           locales=");
		Enumeration locales = request.getLocales();
		boolean first = true;
		while (locales.hasMoreElements()) {
			Locale locale = (Locale) locales.nextElement();
			if (first) {
                first = false;
            } else {
                writer.print(", ");
            }
			writer.print(locale.toString());
		}
		writer.println();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			writer.print("         parameter=" + name + "=");
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
                    writer.print(", ");
                }
				writer.print(values[i]);
			}
			writer.println();
		}
		writer.println("          protocol=" + request.getProtocol());
		writer.println("        remoteAddr=" + request.getRemoteAddr());
		writer.println("        remoteHost=" + request.getRemoteHost());
		writer.println("            scheme=" + request.getScheme());
		writer.println("        serverName=" + request.getServerName());
		writer.println("        serverPort=" + request.getServerPort());
		writer.println("          isSecure=" + request.isSecure());

		// Render the HTTP servlet request properties
		if (request instanceof HttpServletRequest) {
			writer.println("---------------------------------------------");
			HttpServletRequest req = (HttpServletRequest) request;
			writer.println("       contextPath=" + req.getContextPath());
			Cookie[] cookies = req.getCookies();
			if (cookies == null) {
                cookies = new Cookie[0];
            }
			for (int i = 0; i < cookies.length; i++) {
				writer.println("            cookie=" + cookies[i].getName()
						+ "=" + cookies[i].getValue());
			}
			names = req.getHeaderNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				String value = req.getHeader(name);
				writer.println("            header=" + name + "=" + value);
			}
			writer.println("            method=" + req.getMethod());
			writer.println("          pathInfo=" + req.getPathInfo());
			writer.println("       queryString=" + req.getQueryString());
			writer.println("        remoteUser=" + req.getRemoteUser());

			//comment out because http://cwe.mitre.org/data/definitions/807
			//https://www.sans.org/top25-software-errors/#cat3
			// writer.println("requestedSessionId=" + req.getRequestedSessionId());
			// sessionId 本身是cookie的一部分， 这个没有意义， 根据sonar提示， 注释掉了

			writer.println("        requestURI=" + req.getRequestURI());
			writer.println("       servletPath=" + req.getServletPath());
		}
		writer.println("=============================================");

		// Log the resulting string
		writer.flush();
		filterConfig.getServletContext().log(sw.getBuffer().toString());

		// Pass control on to the next filter
		chain.doFilter(request, response);

	}

	/**
	 * Place this filter into service.
	 *
	 * @param filterConfig
	 *            The filter configuration object
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		this.filterConfig = filterConfig;

	}

	/**
	 * Return a String representation of this object.
	 */
	@Override
	public String toString() {

		if (filterConfig == null) {
            return ("RequestDumperFilter()");
        }
		StringBuilder sb = new StringBuilder("RequestDumperFilter(");
		sb.append(filterConfig);
		sb.append(")");
		return (sb.toString());

	}

}

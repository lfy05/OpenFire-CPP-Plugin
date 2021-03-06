/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: JspC/ApacheTomcat8
 * Generated at: 2020-06-15 04:46:54 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.jivesoftware.openfire.plugin.OFCPPSDKPlugin;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.util.JiveGlobals;
import java.lang.String;
import java.lang.Integer;
import org.bouncycastle.cert.jcajce.JcaAttributeCertificateIssuer;
import org.jivesoftware.openfire.plugin.CPPSDKIntegPlugin;
import org.jivesoftware.util.Log;

public final class cpp_002dsdk_002dintegration_002dsettings_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("java.lang.String");
    _jspx_imports_classes.add("org.jivesoftware.util.Log");
    _jspx_imports_classes.add("java.lang.Integer");
    _jspx_imports_classes.add("org.bouncycastle.cert.jcajce.JcaAttributeCertificateIssuer");
    _jspx_imports_classes.add("org.jivesoftware.util.JiveGlobals");
    _jspx_imports_classes.add("org.jivesoftware.openfire.plugin.CPPSDKIntegPlugin");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n<html>\r\n<head>\r\n    <title>C++ SDK Integration Settings</title>\r\n    <!-- The string value of content must match the item id in plugin.xml\r\n    for UI to display properly -->\r\n    <meta name=\"pageID\" content=\"CPPSDKIntegration\"/>\r\n</head>\r\n\r\n");

    /* retrieve form response */
    // check if there is a previous form response
    try{
        // retrieve parameters
        String SDKAddr = request.getParameter("SDK Address");
        String SDKPort = request.getParameter("SDK Port");

        // validate parameters
        if (SDKAddr != null && SDKPort != null){

            if (SDKAddr.length() == 0 || SDKPort.length() == 0){
                response.getWriter().
                        write("<script>alert(\"Error: Property Update Failed - Please specify both C++ SDK Deployment Address and Port!\");</script>");
            } else {
                // check if valid numeric format
                // if not, NumberFormatException thrown
                Integer.parseInt(SDKPort);

                // set values
                JiveGlobals.setProperty("CPPSDKIntegration.SDKAddr", SDKAddr);
                JiveGlobals.setProperty("CPPSDKIntegration.SDKPort", SDKPort);
                response.getWriter().
                        write("<script>alert(\"Properties Updated Successfully\");</script>");
            }

        } else if (SDKAddr == null && SDKPort == null){
            // do nothing
        } else {
            response.getWriter().
                    write(" <script>alert(\"Error: Property Update FAILED - Invalid Server Address format\");</script>");
        }

        } catch (NumberFormatException numberFormatException){
            response.getWriter().
                    write("<script>alert(\"Error: Property Update FAILED - Invalid port format\");</script>");
    }

    String restart = request.getParameter("connection_restart");
    if(restart != null && restart.equals("true")){
        Log.info("C++SDKIntegrationPlugin: Restart requested through admin console");
        if(CPPSDKIntegPlugin.getPluginInstance().restart()){
            response.getWriter().
                    write("<script>alert(\"Restart Succeeded \");</script>");
        } else {
            response.getWriter().
                    write("<script>alert(\"Restart Failed, Check log for details. \");</script>");
        }

    }


      out.write("\r\n\r\n<body>\r\n<p></p>\r\n\r\n<form name=\"C++ SDK Configuration\" action=\"/plugins/ofcppsdkplugin-openfire-plugin-assembly/cpp-sdk-integration-settings.jsp\" method=\"post\" >\r\n    <p><b>Specify C++ SDK Deployment Address and Port:</b></p>\r\n    <label for=\"sdk_address\">SDK Address</label>\r\n    <dt></dt>\r\n    <input type=\"text\" id=\"sdk_address\" name=\"SDK Address\" >\r\n    <p></p>\r\n    <label for=\"sdk_port\">SDK Port</label>\r\n    <dt></dt>\r\n    <input type=\"text\" id=\"sdk_port\" name=\"SDK Port\">\r\n    <p></p>\r\n    <input type=\"submit\"  value=\"Update\">\r\n</form>\r\n\r\n<form name=\"C++ SDK Connection Restart\" action=\"/plugins/ofcppsdkplugin-openfire-plugin-assembly/cpp-sdk-integration-settings.jsp\" method=\"post\">\r\n    <input type=\"hidden\" id=\"connection_restart\" name=\"connection_restart\" value=\"true\">\r\n    <input type=\"submit\" value=\"Re/Start Connection\">\r\n</form>\r\n\r\n\r\n</body>\r\n</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

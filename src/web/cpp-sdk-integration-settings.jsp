<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.jivesoftware.util.JiveGlobals" %>
<%@ page import="java.lang.String" %>
<%@ page import="java.lang.Integer" %>
<%@ page import="org.bouncycastle.cert.jcajce.JcaAttributeCertificateIssuer" %>
<%@ page import="org.jivesoftware.openfire.plugin.CPPIntegrationPlugin" %>
<%@ page import="org.jivesoftware.util.Log" %>

<html>
<head>
    <title>C++ SDK Integration Settings</title>
    <!-- The string value of content must match the item id in plugin.xml
    for UI to display properly -->
    <meta name="pageID" content="CPPSDKIntegration"/>
</head>

<%
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
        if(CPPIntegrationPlugin.getPluginInstance().restart()){
            response.getWriter().
                    write("<script>alert(\"Restart Succeeded \");</script>");
        } else {
            response.getWriter().
                    write("<script>alert(\"Restart Failed, Check log for details. \");</script>");
        }

    }

%>

<body>
<p></p>

<form name="C++ SDK Configuration" action="/plugins/ofcppsdkplugin-openfire-plugin-assembly/cpp-sdk-integration-settings.jsp" method="post" >
    <p><b>Specify C++ SDK Deployment Address and Port:</b></p>
    <label for="sdk_address">SDK Address</label>
    <dt></dt>
    <input type="text" id="sdk_address" name="SDK Address" >
    <p></p>
    <label for="sdk_port">SDK Port</label>
    <dt></dt>
    <input type="text" id="sdk_port" name="SDK Port">
    <p></p>
    <input type="submit"  value="Update">
</form>

<form name="C++ SDK Connection Restart" action="/plugins/ofcppsdkplugin-openfire-plugin-assembly/cpp-sdk-integration-settings.jsp" method="post">
    <input type="hidden" id="connection_restart" name="connection_restart" value="true">
    <input type="submit" value="Re/Start Connection">
</form>


</body>
</html>
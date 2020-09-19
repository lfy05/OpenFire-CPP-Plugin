@ECHO OFF
ECHO Assume OpenFire and current project is in the same parent folder
ECHO Moving compiled plugin assembly to OpenFire plugin folder
xcopy /s /y "target\OFCPPSDKPlugin-openfire-plugin-assembly.jar" "..\..\Openfire-4.5.2\distribution\target\distribution-base\plugins\OFCPPSDKPlugin-openfire-plugin-assembly.jar*"

ECHO Delete previous version
RD /s /q "..\..\Openfire-4.5.2\distribution\target\distribution-base\plugins\ofcppsdkplugin-openfire-plugin-assembly"
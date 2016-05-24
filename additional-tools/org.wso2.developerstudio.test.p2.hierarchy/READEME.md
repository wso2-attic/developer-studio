=====================================================================
     READ ME
=====================================================================


This jar should be executed with the command 

java -jar plugins/org.eclipse.equinox.launcher_1.3.100.v20150511-1540.jar -consoleLog -console -nosplash -application org.wso2.developerstudio.eclipse.test.p2.hierarchy $repo_url $feature_id

after copying this jar to an eclipse MARS instance.

The script in the scripts folder should be copied to a known location and the path of the script should be modified in the code. 

The locatoin of a fresh eclipse tar ball and the location to which it would be extracted if it is extracted from the locatio it is at should be hardcoded in the script file.

It will test for the availability of one or more features in a given P2 repository and will install these features to a fresh eclipse instance and give the output in the console




----------------------------------------------------------------

known limitation(s)


1. The eclipse P2 director installer will not throw any exceptions if feature installation fails, instead it will only log the output which would be visible as console output.




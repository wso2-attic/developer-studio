#!/bin/bash
#The URL of the staging repo from which we should download the features, the eclipse tar ball location and the extracted
# eclipse location needs to be hard coded in this script
#
#axel http://ftp.jaist.ac.jp/pub/eclipse/technology/epp/downloads/release/mars/2/eclipse-jee-mars-2-linux-gtk-x86_64.tar.gz
tar -xvf #eclipse pack location, needs to be hard coded in the builder machine
workspace=/tmp/workspace-clean-34
mkdir -p foo
#read -p 'Feature Name : ' featureID
#read -p 'Feature Repository : ' repository
#read -p 'version: ' version
featureID=$1
version=$2
ecliseHome=
feature=$featureID.feature.group	
releasesRepository=http://builder1.us1.wso2.org/~developerstudio/developer-studio-kernel/$version/releases/

vm=$JAVA_HOME/bin/java
#vm=/opt/ibm-java2-5.0/bin/java

echo "================================================"
echo "Using:       vm=$vm and workspace=$workspace";
echo "Installing:  $feature";
echo "Destination: $ecliseHome";
echo "Repository URL:  $repository";

echo "[`date +%H:%M:%S`] Running p2.director to install feature ... ";
#  -console -noexit -debug 


./eclipse/eclipse -vm $vm -nosplash \
  -data $workspace -consolelog -clean \
  -application org.eclipse.equinox.p2.director \
  -repository $releasesRepository \
  -installIU $feature \
  -destination $ecliseHome

echo "finished installing feature from features Repository";

echo "Deleting the eclipse instance...";
#rm -fr $ecliseHome

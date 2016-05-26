#!/bin/bash
#The URL of the staging repo from which we should download the features, the eclipse tar ball location and the extracted
# eclipse location needs to be hard coded in this script
#
#axel http://ftp.jaist.ac.jp/pub/eclipse/technology/epp/downloads/release/mars/2/eclipse-jee-mars-2-linux-gtk-x86_64.tar.gz
tar -xvf /home/awanthika/eclipse-jee-mars-2-linux-gtk-x86_64.tar.gz
workspace=/tmp/workspace-clean-34

releasesRepository=$1
ecliseHome=/home/awanthika/eclipse

vm=$JAVA_HOME/bin/java

echo "================================================"
echo "Using:       vm = $vm and workspace = $workspace";
echo "Installing:  all features available at $releasesRepository ";
echo "Destination: $ecliseHome";
echo "Repository URL: $releasesRepository";

echo "[`date +%H:%M:%S`] Running p2.director to install features ... ";
#  -console -noexit -debug 


./eclipse/eclipse -vm $vm -nosplash \
  -data $workspace -consolelog -clean \
  -application org.eclipse.equinox.p2.director \
  -repository http://builder1.us1.wso2.org/~developerstudio/developer-studio-kernel/4.1.0/releases/ \
  -installIUs "Q:everything.select(x | x.properties ~= filter('(org.eclipse.equinox.p2.type.group=true)'))" \
  -destination $ecliseHome

echo "finished installing feature from features Repository";

echo "Deleting the eclipse instance...";
#rm -fr $ecliseHome

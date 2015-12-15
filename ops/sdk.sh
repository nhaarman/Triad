#!/bin/bash
file="$ANDROID_HOME/tools/source.properties"
version=$(cat $file | grep Pkg.Revision)
if [[ $version == *"24.4.1"* ]]
then
    	echo "Tools already installed."
else
	echo "Installing tools"
	echo y | android update sdk --no-ui --all --filter tools
	echo y | android update sdk --no-ui --all --filter platform-tools,build-tools-23.0.2,android-23,extra-android-support,extra-android-m2repository
fi

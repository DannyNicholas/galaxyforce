#
#           Circle CI & gradle.properties live in harmony
# 
# Android convention is to store your API keys in a local, non-versioned
# gradle.properties file. Circle CI doesn't allow users to upload pre-populated
# gradle.properties files to store this secret information, but instead allows
# users to store such information as environment variables.
#
# This script creates a local gradle.properties file on current the Circle CI
# instance. It then reads the environment variables PUBLIC_KEYx, which a user
# has defined in their Circle CI project settings environment variables, and 
# writes this value to the Circle CI instance's gradle.properties file.
# 
# You must execute this script via your circle.yml,
# so your gradle build process has access to all variables.
#
# - run:
#     name: Environment set-up for Gradle properties
#     command: source environmentSetup.sh && copyEnvVarsToGradleProperties

#!/usr/bin/env bash

function copyEnvVarsToGradleProperties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES
    echo "Gradle properties should exist at $GRADLE_PROPERTIES"

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
        echo "Gradle properties does not exist"

        echo "Creating Gradle properties file..."
        mkdir -p $HOME"/.gradle"
        touch $GRADLE_PROPERTIES

        echo "Writing Public Key to gradle.properties..."
        echo "PUBLIC_KEY1=$PUBLIC_KEY1" >> $GRADLE_PROPERTIES
        echo "PUBLIC_KEY2=$PUBLIC_KEY2" >> $GRADLE_PROPERTIES
        echo "PUBLIC_KEY3=$PUBLIC_KEY3" >> $GRADLE_PROPERTIES
        echo "PUBLIC_KEY4=$PUBLIC_KEY4" >> $GRADLE_PROPERTIES
    fi
}
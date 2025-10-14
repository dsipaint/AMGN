#!/bin/bash
# RUN THIS TO ADD  AMGN AS A DEPENDENCY
AMGN_VERSION="1.3"

mvn clean package
mvn install:install-file -Dfile=target/AMGN-$AMGN_VERSION-jar-with-dependencies.jar -DgroupId=com.github.dsipaint -DartifactId=AMGN -Dversion=$AMGN_VERSION -Dpackaging=jar -DgeneratePom=true
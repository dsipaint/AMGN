#!/bin/bash
# RUN THIS TO ADD  AMGN AS A DEPENDENCY
mvn clean package
mvn install:install-file -Dfile=target/AMGN-1.1-jar-with-dependencies.jar -DgroupId=com.github.dsipaint -DartifactId=AMGN -Dversion=1.1 -Dpackaging=jar -DgeneratePom=true
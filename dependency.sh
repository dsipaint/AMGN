#!/bin/bash
# RUN THIS TO ADD  AMGN AS A DEPENDENCY
mvn package
mvn install:install-file -Dfile=target/AMGN-beta-1.2.1-jar-with-dependencies.jar -DgroupId=com.github.dsipaint -DartifactId=AMGN -Dversion=beta-1.2.1 -Dpackaging=jar -DgeneratePom=true
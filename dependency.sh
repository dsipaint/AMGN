#!/bin/bash
# RUN THIS TO ADD  AMGN AS A DEPENDENCY
mvn package
mvn install:install-file -Dfile=target/AMGN-alpha-1.4-jar-with-dependencies.jar -DgroupId=com.github.dsipaint -DartifactId=AMGN -Dversion=alpha-1.4 -Dpackaging=jar -DgeneratePom=true
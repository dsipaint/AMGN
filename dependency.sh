# RUN THIS TO ADD  AMGN AS A DEPENDENCY
bash build.sh
mvn install:install-file -Dfile=target/AMGN-alpha-1.2.0-jar-with-dependencies.jar -DgroupId=com.github.dsipaint -DartifactId=AMGN -Dversion=alpha-1.2.0 -Dpackaging=jar -DgeneratePom=true
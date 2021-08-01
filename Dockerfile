FROM openjdk:8-jre-alpine
COPY target/groovyship-1.0-SNAPSHOT-jar-with-dependencies.jar .
CMD java -jar groovyship-1.0-SNAPSHOT-jar-with-dependencies.jar

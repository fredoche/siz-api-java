FROM quay.io/sizio/maven-spring-boot-base 

MAINTAINER SIZ IO

COPY pom.xml pom.xml
COPY src src

RUN mvn clean install spring-boot:repackage -Dmaven.test.skip

CMD java -jar target/api-0.0.1-SNAPSHOT.war

EXPOSE 8080
EXPOSE 9000
EXPOSE 5005

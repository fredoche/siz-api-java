FROM quay.io/sizio/maven-spring-boot-base 

MAINTAINER SIZ IO

COPY pom.xml pom.xml
COPY src src

RUN mvn clean install spring-boot:repackage -Dmaven.test.skip

CMD java -jar target/api-0.0.1-SNAPSHOT.war --spring.profiles.active=dev,fixtures 

EXPOSE 8080
EXPOSE 5005

FROM quay.io/sizio/maven-spring-boot-base 

MAINTAINER SIZ IO

RUN mkdir target

COPY target/api-0.0.1-SNAPSHOT.war target/api-0.0.1-SNAPSHOT.war

CMD java -jar target/api-0.0.1-SNAPSHOT.war --spring.profiles.active=dev,fixtures 

EXPOSE 8080
EXPOSE 5005

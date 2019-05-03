FROM openjdk:8u201-jdk-alpine3.9
ADD target/toolroom-1.0.0.jar .
EXPOSE 8080
CMD java -jar -Dspring.profiles.active=prod toolroom-1.0.0.jar
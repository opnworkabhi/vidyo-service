FROM openjdk:17
EXPOSE 8080
ADD target/vidyo-moderators-service-1.0.jar vidyo-moderators-service-1.0.jar
ENTRYPOINT ["java", "-jar", "vidyo-moderators-service-1.0.jar"]
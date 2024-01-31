FROM openjdk:17
EXPOSE 8080
ADD target/vidyo-service.jar vidyo-service.jar
#ENTRYPOINT ["java", "-jar", "vidyo-service-1.0-SNAPSHOT.jar"]
ENTRYPOINT ["java", "-jar", "/vidyo-service.jar"]
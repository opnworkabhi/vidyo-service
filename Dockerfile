From openjdk:17
copy ./target/vidyo-moderators-service-1.0.jar vidyo-moderators-service-1.0.jar
CMD ["java","-jar","vidyo-moderators-service-1.0.jar"]
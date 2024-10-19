FROM openjdk:17-ea-11-jdk-slim
VOLUME /logs
COPY build/libs/backend-0.0.1-SNAPSHOT.jar FriendShip.jar
ENTRYPOINT ["java", "-jar", "FriendShip.jar"]
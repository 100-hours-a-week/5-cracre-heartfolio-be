FROM openjdk:17
ENV SPRING_PROFILES_ACTIVE=release
COPY build/libs/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app.jar"]

FROM maven:3.8.3-openjdk-17 AS build
COPY src /kantakap/src
COPY pom.xml /kantakap
RUN mvn -f /kantakap/pom.xml clean package

FROM openjdk:17-alpine
COPY --from=build /kantakap/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]

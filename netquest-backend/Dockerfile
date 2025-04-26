# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk

# Copy the built .jar file into the image
COPY target/netquest-api-0.0.1-SNAPSHOT.jar /app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
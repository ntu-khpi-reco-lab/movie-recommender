# Step 1: Use an official Maven Amazon Corretto image to build the app
FROM maven:3-amazoncorretto-21 AS build

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the entire project into the Docker container
COPY . /app

# Step 4: Set the working directory to the api-gateway submodule
WORKDIR /app/api-gateway

# Step 5: Build only the api-gateway module using Maven
RUN mvn clean package -DskipTests

# Step 6: Use an official Amazon Corretto JDK image for the runtime
FROM amazoncorretto:21-alpine-jdk

# Step 7: Set the working directory inside the container
WORKDIR /app

# Step 8: Copy the built JAR from the child module's target directory in the build stage
COPY --from=build /app/api-gateway/target/*.jar app.jar

# Step 9: Expose the port that app uses
EXPOSE 8080

# Step 10: Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

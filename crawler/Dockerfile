# Step 1: Use an official Maven Amazon Corretto image to build the app
FROM maven:3-amazoncorretto-21 AS build

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the entire project into the Docker container
COPY . /app

# Step 4: Build only the crawler module using Maven
RUN mvn clean package -DskipTests

# Step 5: Use an official Amazon Corretto JDK image for the runtime
FROM amazoncorretto:21-alpine-jdk

# Step 6: Set the working directory inside the container
WORKDIR /app

# Step 7: Copy the built JAR from the child module's target directory in the build stage
COPY --from=build /app/crawler/target/*.jar app.jar

# Step 8: Expose the port that app uses
EXPOSE 9001

# Step 9: Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]

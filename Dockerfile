FROM openjdk:17-alpine
WORKDIR /app
COPY ./target/expenses-api-0.0.1-SNAPSHOT.jar /app/expenses-api-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","expenses-api-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
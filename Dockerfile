FROM amazoncorretto:22-alpine-jdk

COPY . /app

WORKDIR /app

RUN chmod +x ./mvnw

RUN ./mvnw clean package

COPY target/netlife-academic-api-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
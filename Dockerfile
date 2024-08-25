FROM amazoncorretto:22-alpine-jdk
VOLUME [ "/tmp" ]
COPY target/netlife-academic-api-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 CMD [ "curl -f http://localhost:8080/actuator/health || exit 1" ]
ENTRYPOINT ["java","-jar","/app.jar"]
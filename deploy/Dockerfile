FROM openjdk:15.0.1-slim

COPY build/financial-manager-api-*.jar /financial-manager-api.jar

ENTRYPOINT ["java", "-jar", "--enable-preview", "financial-manager-api.jar"]
# Etapa 1: compilar con Maven y Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# Etapa 2: ejecutar solo el .jar
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render Free tiene 512 MB de RAM y 0,1 CPU: limitamos la memoria de Java
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Xss512k -XX:TieredStopAtLevel=1"
# Render entrega el puerto en la variable PORT (application.yml la lee)
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
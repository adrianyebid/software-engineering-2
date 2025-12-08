# ===== Stage 1: Build & Test =====
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build

# Copiar solo pom.xml para aprovechar la caché de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora sí copiamos el código
COPY src ./src

# Compila + ejecuta los tests + genera el JAR
RUN mvn -B clean package

# ===== Stage 2: Run =====
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copiar solo el JAR final
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

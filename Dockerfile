# Multi-stage Docker build for RahimunishaMart
# Stage 1: Build .WAR package
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY checkstyle.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run inside Apache Tomcat 9
FROM tomcat:9.0-jdk17-temurin
LABEL maintainer="Rahimunisha Mart Team"
LABEL description="RahimunishaMart Capstone Application"

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file as ROOT.war for direct root context access
COPY --from=builder /app/target/rahimunishamart.war /usr/local/tomcat/webapps/ROOT.war

# Create persistent storage directory for H2 database
RUN mkdir -p /usr/local/tomcat/data
VOLUME /usr/local/tomcat/data

ENV PORT=8080
EXPOSE 8080

# Dynamically configure server.xml to listen on $PORT assigned by cloud providers (Render, Railway, Heroku, etc.)
CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT:-8080}\\\"/\" /usr/local/tomcat/conf/server.xml && catalina.sh run"]

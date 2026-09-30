# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application on Tomcat
FROM tomcat:9.0-jre11
# Xóa các ứng dụng mặc định của Tomcat để tránh đụng độ (tùy chọn)
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy file war từ bước build sang thư mục webapps của Tomcat
COPY --from=build /app/target/ch12_sqlgateway.war /usr/local/tomcat/webapps/ch12_sqlgateway.war

# Port mặc định của Tomcat
EXPOSE 8080

CMD ["catalina.sh", "run"]

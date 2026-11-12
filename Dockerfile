FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM tomcat:11.0-jdk21-temurin

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

ENV CATALINA_OPTS="-Duser.home=/data"
RUN mkdir -p /data
VOLUME /data

EXPOSE 8080
CMD ["catalina.sh", "run"]
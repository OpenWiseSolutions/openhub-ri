FROM openjdk:8-jdk-alpine
ADD openhub-war/target/openhub-ri.war openhub-ri.war
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=example-module,h2 -jar /openhub-ri.war" ]
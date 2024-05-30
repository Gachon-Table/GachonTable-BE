FROM openjdk:17-jdk

COPY build/libs/*.jar gachontable.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dspring.profiles.active=dev -jar /gachontable.jar"]
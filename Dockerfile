FROM openjdk:17-jdk

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

COPY build/libs/*.jar gachontable.jar

EXPOSE 8080
EXPOSE 63544

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dspring.profiles.active=dev -jar /gachontable.jar"]
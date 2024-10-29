FROM openjdk:17-jdk

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

COPY build/libs/*.jar gachontable.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Dspring.profiles.active=prod -javaagent:/pinpoint-agent/pinpoint-bootstrap-2.5.3.jar -Dpinpoint.agentId=LUPG -Dpinpoint.applicationName=LUPG-Prod -jar /gachontable.jar"]
FROM amazoncorretto:17

ENV TZ=Asia/Seoul

ARG JAR_FILE=build/libs/spring-batch-practice-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

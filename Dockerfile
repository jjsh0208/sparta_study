FROM openjdk:17-jdk-slim
# jdk17 이미지를 다운로드받아서
VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar
#jar 파일을 복사한 다음에 실행해라
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
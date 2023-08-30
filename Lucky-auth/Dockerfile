FROM openjdk:8-jre-slim
EXPOSE 8082
MAINTAINER caosen

ADD target/auth.jar Auth.jar


ENV TZ="Asia/Shanghai"
CMD ["java","-jar","/Auth.jar"]

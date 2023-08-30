FROM openjdk:8-jre-slim
EXPOSE 8080
MAINTAINER caosen

ADD Lucky-interfaces/target/Lucky.jar Lucky.jar


ENV TZ="Asia/Shanghai"
CMD ["java","-jar","/Lucky.jar"]

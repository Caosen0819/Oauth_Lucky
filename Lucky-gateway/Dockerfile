FROM openjdk:8-jre-slim
EXPOSE 8201
MAINTAINER caosen

ADD target/gateway.jar Gateway.jar


ENV TZ="Asia/Shanghai"
CMD ["java","-jar","/Gateway.jar"]

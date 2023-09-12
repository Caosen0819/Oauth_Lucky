FROM openjdk:8-jre-slim
EXPOSE 80
MAINTAINER caosen

ADD target/gateway.jar Gateway.jar


ENV TZ="Asia/Shanghai"
CMD ["java","-jar","/Gateway.jar"]

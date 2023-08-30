FROM openjdk:8-jre-slim
EXPOSE 9001
MAINTAINER caosen
ENV APP_HOME=/apps
COPY target/Lucky-API-1.0-SNAPSHOT.jar Lucky-API.jar
ENTRYPOINT ["java","-jar","-Xms100m","-Xmx100m"]
ENV TZ="Asia/Shanghai"
CMD ["java","-jar","/Lucky.jar"]

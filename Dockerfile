FROM alpine:latest
EXPOSE 8080
COPY ./target/msfuegoquasar-0.0.1-SNAPSHOT.jar msfuegoquasar-0.0.1-SNAPSHOT.jar
RUN apk add openjdk11
RUN apk update
RUN apk add busybox-extras
RUN apk add curl
ENTRYPOINT ["java","-jar","/msfuegoquasar-0.0.1-SNAPSHOT.jar"]
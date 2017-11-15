FROM java:8
MAINTAINER oxchains
ADD ./target/themis-zuul-1.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["/usr/bin/java","-jar","app.jar"]
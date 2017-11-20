FROM java:8
MAINTAINER oxchains
ADD ./target/themis-order-1.0.1-SNAPSHOT.jar app.jar
EXPOSE 8882
ENTRYPOINT ["/usr/bin/java","-jar","app.jar"]
# 这个打成images之后，默认使用的是OpenJdk，在测试服务器上有新的Dockerfile，指定使用了OracleJdk
FROM java:8
MAINTAINER oxchains
ADD ./target/themis-notice-1.0.1-SNAPSHOT.jar themis-notice-1.0.1-SNAPSHOT.jar
EXPOSE 8083
ENTRYPOINT ["/usr/bin/java","-jar","themis-notice-1.0.1-SNAPSHOT.jar"]
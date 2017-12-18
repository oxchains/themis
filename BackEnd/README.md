部署打包：
1.修改配置文件application.properties
spring.profiles.active=dev # 修改成需要的版本 dev/test/prod
2.修改相应版本的配置文件 application-xxx.properties 
注意几个ip地址和端口号，按需更改
3.修改logstash配置文件 logback-spring.xml
<root level="INFO">
        <appender-ref ref="console"/>
        <appender-ref ref="logstash"/>
        <!--<appender-ref ref="rollingFileINFO"/>-->
</root>


<destination>IP:PORT</destination>
<destination>IP:PORT</destination>

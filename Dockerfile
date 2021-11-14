FROM openjdk:8

WORKDIR /app

COPY ./target/grpc-demo-*.jar /app/grpc-demo.jar

ARG Xmx=1024m
ARG Xms=512m

ENV JDK_JAVA_OPTIONS="-Xmx${Xmx} -Xms${Xms}"


ENTRYPOINT ["java", "-jar", "grpc-demo.jar"]
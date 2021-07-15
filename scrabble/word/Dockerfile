FROM openjdk:11

WORKDIR /app

COPY  ./word/target/word-1-spring-boot.jar ./word.jar

# Run the jar file
ENTRYPOINT java -jar word.jar $SERVER_HOST $SERVER_PORT
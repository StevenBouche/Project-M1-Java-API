FROM openjdk:11

WORKDIR /app

COPY ./game/target/game-1-spring-boot.jar ./game.jar

# Run the jar file
ENTRYPOINT java -jar game.jar $SERVER_HOST $SERVER_PORT $WORD_HOST $DOCKER_USE



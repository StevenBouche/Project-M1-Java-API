FROM openjdk:11

WORKDIR /app

COPY ./player/target/player-1-spring-boot.jar ./player.jar

# Run the jar file
ENTRYPOINT java -jar player.jar $SERVER_HOST $SERVER_PORT $GAME_HOST $WORD_HOST $DOCKER_USE






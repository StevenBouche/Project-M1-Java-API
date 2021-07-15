package com.miage.share.game.viewmodel;

import com.miage.share.player.model.PlayerIdentity;

public class ResultAuth {

    private String message;
    private String gameId;
    private String gameUrl;
    private PlayerIdentity identity;

    public ResultAuth(String message, String gameId, String gameUrl, PlayerIdentity identity){
        this.setMessage(message);
        this.setGameId(gameId);
        this.setGameUrl(gameUrl);
        this.setIdentity(identity);
    }

    public ResultAuth(){

    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public PlayerIdentity getIdentity() {
        return identity;
    }

    public void setIdentity(PlayerIdentity identity) {
        this.identity = identity;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Result Auth { message : %s, gameId : %s, identity : %s }",
                this.message == null ? "null" : this.message,
                this.gameId == null ? "null" : this.gameId,
                this.identity == null ? "null" : this.identity.toString());
    }


}

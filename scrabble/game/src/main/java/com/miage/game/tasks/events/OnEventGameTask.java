package com.miage.game.tasks.events;

import com.miage.share.game.model.Game;
import com.miage.share.player.model.PlayerIdentity;

/**
 * Gather all game events
 */
public interface OnEventGameTask {
    void playerHaveJoin(PlayerIdentity identity);
    void playerHavePlay(PlayerIdentity identity, String word);
    void gameIsFinished(Game game);
}

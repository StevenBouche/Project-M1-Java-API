package com.miage.game.tasks.events;

import com.miage.game.tasks.GameTaskState;

/**
 * Gather the state of a game
 */
public interface OnStateGameTaskChange {
    void stateUpdated(GameTaskState state);
}

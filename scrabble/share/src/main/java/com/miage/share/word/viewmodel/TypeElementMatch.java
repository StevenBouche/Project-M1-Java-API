package com.miage.share.word.viewmodel;

import com.miage.share.share.Direction;

public enum TypeElementMatch {

    ROW(Direction.EAST),
    COLUMN(Direction.SOUTH);

    private Direction direction;

    TypeElementMatch(Direction direction){
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

}

package com.miage.share.share;

public enum Direction {

	NORTH(-1, 0),
	SOUTH(1, 0),
	EAST(0, 1),
	WEST(0, -1);
	
	public final int dirX;
	public final int dirY;
	
	private Direction(int dirX, int dirY) {
		this.dirX = dirX;
		this.dirY = dirY;
	}

	public Direction getInverseXYDirection(){
		switch(this){
			case EAST: return Direction.SOUTH;
			case WEST: return Direction.NORTH;
			case NORTH: return Direction.WEST;
			case SOUTH: return Direction.EAST;
			default: return null;
		}
	}
	
}

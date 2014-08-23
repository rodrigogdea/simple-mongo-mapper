package org.smp.model;


public class WhiteBlackMove {

	private final String whiteMove;
	private final String blackMove;

	public WhiteBlackMove(String whiteMove, String blackMove) {
		this.whiteMove = whiteMove;
		this.blackMove = blackMove;
	}

	public String getWhiteMove() {
		return whiteMove;
	}

	public String getBlackMove() {
		return blackMove;
	}
	
}

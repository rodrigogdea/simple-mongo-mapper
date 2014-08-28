package org.smm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoard {

	private final String blackName;
	private final String whiteName;
	private Map<String, String> simpleBoard = new HashMap<String, String>();
	private List<String> moves = new ArrayList<String>();
	

	public ChessBoard(String whiteName, String blackName) {
		this.whiteName = whiteName;
		this.blackName = blackName;
		
		this.simpleBoard.put("A1", "BR");
		this.simpleBoard.put("B1", "BN");
		this.simpleBoard.put("C1", "BB");
		this.simpleBoard.put("D1", "BQ");
		this.simpleBoard.put("E1", "BK");
		this.simpleBoard.put("F1", "BB");
		this.simpleBoard.put("G1", "BN");
		this.simpleBoard.put("H1", "BR");
		// etc...
		
		moves.add("E2-E4,E7-E5");
		moves.add("G1-F3,B8-C6");
		moves.add("F1-B5,A7-A6");
		// etc
	}

	public String getBlackName() {
		return blackName;
	}

	public String getWhiteName() {
		return whiteName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getClass().isInstance(obj)) {
			ChessBoard aChessBoard = (ChessBoard) obj;
			return this.blackName.equals(aChessBoard.blackName)
					&& this.whiteName.equals(aChessBoard.whiteName);
		}
		return false;
	}
	
	public String getPieceOnPosition(String position){
		return this.simpleBoard.get(position);
	}
	
	public List<String> getMoves(){
		return moves;
	}
}

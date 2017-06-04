package me.tr.roulette.other;

public enum RoundState {

	WAITING, INPROGRESS;
	
	static RoundState state;
	
	public static void setRoundState(RoundState state){
		RoundState.state = state;
	}
	
	public static RoundState getRoundState(){
		return state;
	}
	
}

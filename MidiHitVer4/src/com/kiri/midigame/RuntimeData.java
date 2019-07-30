package com.kiri.midigame;

public class RuntimeData {
	private boolean runFlag=false;
	private int combo=0;
	private long score=0;
	private int hitCheck=2;
	private int maxCombo=0;
	
	public boolean isRunFlag() {
		return runFlag;
	}
	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}
	public int getCombo() {
		return combo;
	}
	public void setCombo(int combo) {
		this.combo = combo;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public int getHitCheck() {
		return hitCheck;
	}
	public void setHitCheck(int hitCheck) {
		this.hitCheck = hitCheck;
	}
	public int getMaxCombo() {
		return maxCombo;
	}
	public void setMaxCombo(int maxCombo) {
		this.maxCombo = maxCombo;
	}
	
	public void reset(){
		this.combo=0;
		this.maxCombo=0;
		this.hitCheck=2;
		this.score=0;
	}
	
}

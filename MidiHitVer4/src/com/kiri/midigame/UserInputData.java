package com.kiri.midigame;

public class UserInputData {
	private int[] inputs;
	private boolean waitSomeTime = false;
	private boolean runSomeTime = false;
	public UserInputData(int width){
		inputs = new int[width];
	}
	public boolean isWaitSomeTime() {
		return waitSomeTime;
	}
	public void setWaitSomeTime(boolean waitSomeTime) {
		this.waitSomeTime = waitSomeTime;
	}
	public boolean isRunSomeTime() {
		return runSomeTime;
	}
	public void setRunSomeTime(boolean runSomeTime) {
		this.runSomeTime = runSomeTime;
	}
	public int[] getInputs() {
		return inputs;
	}
	public void setInputs(int[] inputs) {
		this.inputs = inputs;
	}
	
}

package com.kiri.midigame;

import java.io.Serializable;

public class SettingData implements Serializable{
	private static final long serialVersionUID = 3L;
	private String musicFile = "";
	private boolean bigTrackCheck = false;//mode
	private boolean smallTrackCheck = false;//mode
	private int speedX = 1;//speed multiplying power
	private int signature = 0;
	private int lineNumber = 7; // how much lines used?
	public String getMusicFile() {
		return musicFile;
	}
	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}
	public boolean isBigTrackCheck() {
		return bigTrackCheck;
	}
	public void setBigTrackCheck(boolean bigTrackCheck) {
		this.bigTrackCheck = bigTrackCheck;
	}
	public boolean isSmallTrackCheck() {
		return smallTrackCheck;
	}
	public void setSmallTrackCheck(boolean smallTrackCheck) {
		this.smallTrackCheck = smallTrackCheck;
	}
	public int getSpeedX() {
		return speedX;
	}
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getSignature() {
		return signature;
	}
	public void setSignature(int signature) {
		this.signature = signature;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	
}

package com.kiri.midigame;

import java.io.Serializable;

public class MusicFileData implements Serializable{
	private static final long serialVersionUID = 4L;
	private String musicFile = null;
	private long ticks = 10;
	private long totalTime = 10;
	private int defaultSignature=0;
	private short[] noteList;
	private short[] bigList;
	private short[] smallList;

	public String getMusicFile() {
		return musicFile;
	}
	public void setMusicFile(String musicFile) {
		this.musicFile = musicFile;
	}
	public long getTicks() {
		return ticks;
	}
	public void setTicks(long ticks) {
		this.ticks = ticks;
	}
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	
	public int getDefaultSignature() {
		return defaultSignature;
	}
	public void setDefaultSignature(int defaultSignature) {
		this.defaultSignature = defaultSignature;
	}
	public short[] getNoteList() {
		return noteList;
	}
	public void setNoteList(short[] noteList) {
		this.noteList = noteList;
	}
	public short[] getBigList() {
		return bigList;
	}
	public void setBigList(short[] bigList) {
		this.bigList = bigList;
	}
	public short[] getSmallList() {
		return smallList;
	}
	public void setSmallList(short[] smallList) {
		this.smallList = smallList;
	}
	
}

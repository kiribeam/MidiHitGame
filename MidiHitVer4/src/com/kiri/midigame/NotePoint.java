//This is a source file which shows a data structure to set notes
package com.kiri.midigame;

public class NotePoint{
	private int sound;
	private long tick;
	public NotePoint(int s,long t) {
		sound = s;
		tick = t;
	}
	public int getSound() {
		return sound;
	}
	public void setSound(int sound) {
		this.sound = sound;
	}
	public long getTick() {
		return tick;
	}
	public void setTick(long tick) {
		this.tick = tick;
	}
}


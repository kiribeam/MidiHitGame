//This source file defines a general Music player which coulde be used in serveral cases
package com.kiri.midigame;
import javax.sound.midi.*;
import java.io.*;
public class MusicPlayer{
	private static Sequencer sequencer;
	private static Sequence sequence;
	//To initialize the sequencer to avoid null pointer
	static {
		try {
			sequencer = MidiSystem.getSequencer();
		}catch (Exception e) {
			System.out.println("Can't get sequencer");
			throw new RuntimeException();
		}
	}
	
	public static long getTickPosition(){
		return sequencer.getTickPosition();
	}
	
	public static void setTickPosition(long tick){
		sequencer.setTickPosition(tick);
	}
	public static void setMute(boolean mute){
		if(sequencer == null || sequence == null) return;
		for(int i=0; i<sequence.getTracks().length; i++){
			sequencer.setTrackMute(i, mute);
		}
	}

	//play a midi file which named as filename
	public static void play(File filename) {
		MusicPlayer.setSequence(filename);
		if(sequence == null) return;
		try{
			if(sequencer.isRunning()) sequencer.stop();
			if(sequencer.isOpen()) sequencer.close();
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setSequence(sequence);
			System.out.println("Tempo:" + sequencer.getTempoInBPM());
			if(!sequencer.isRunning()) {
				sequencer.start();
				System.out.println("TickLength:" + sequencer.getTickLength());
				System.out.println("MicroSeconds:" + sequencer.getMicrosecondLength());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//set a sequence
	public static void setSequence(File filename) {
		if(filename == null) return;
		try{
			sequence = MidiSystem.getSequence(filename);
		}catch(Exception e) {
			System.out.println("Can't get Sequence from filename");
		}
	}
	//stop play
	public static void stopPlay(){
		if(sequencer == null) return;
		if(sequencer.isRunning()) sequencer.stop();
		if(sequencer.isOpen()) sequencer.close();
	}
	//get the length of sequence's time
	public static long getTime(){
		if(sequencer==null || sequence == null) return 0;
		long time = sequence.getMicrosecondLength();
    	return time;
	}
	//get the length of sequence's ticks
	public static long getTick(){
		if(sequencer==null || sequence==null) return 0;
		long tick = sequence.getTickLength();
		return tick;
	}
	//to return the sequence file
	public static Sequence outputSequence(File filename){
		try{
			sequence = MidiSystem.getSequence(filename);
			System.out.println("Sequence file:" + filename);
		}catch(Exception e) {
			System.out.println("can't get Sequence");
			throw new RuntimeException();
		}
		return sequence;
	}
}

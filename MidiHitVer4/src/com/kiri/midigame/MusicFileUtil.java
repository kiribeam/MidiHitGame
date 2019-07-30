/* This source file is to manage all the file logic in the game.
 * kiri 2017,06,10*/
package com.kiri.midigame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;



public class MusicFileUtil{
	private static final int NUM = 12;
  //set music file and use player to load it
	public static MusicFileData readFile(String fileName){
		if(fileName == null) return null;
		File musicFile = new File(fileName);
		if(!musicFile.exists()) return null;
		try{
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(musicFile.getAbsolutePath()));
			MusicFileData data = (MusicFileData) is.readObject();
			is.close();
			return data;
		} catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Read Music File failed!");
			throw new RuntimeException();
		}
	}
  
  //create a new note file
	public static void createNoteFile(String fileName) {
		File musicFile = new File(fileName);
		if(!musicFile.exists()) return;
		Sequence sequence;
		sequence = MusicPlayer.outputSequence(musicFile);
		Track[] track = sequence.getTracks();
		int bigTrack = 0;
		int smallTrack = -1;
		//divide tracks and find two biggest track
		if(track.length < 2){
			smallTrack = 0;
		}else{
			for(int i = 1; i < track.length; i++) {
				if(track[bigTrack].size() <= track[i].size()){
					smallTrack = bigTrack;
					bigTrack = i;
				}else{
					if(smallTrack == -1 || track[smallTrack].size() < track[i].size())
						smallTrack = i;
				}
			}
		}
		System.out.println("bigtrack.size(" + bigTrack +")" + " = " + track[bigTrack].size());
		System.out.println("smalltrack.size("+smallTrack+")" + " = " + track[smallTrack].size());
		//create 12 arrays to fill the notes
		ArrayList<LinkedList<NotePoint>> notelines = new ArrayList<>();
		ArrayList<LinkedList<NotePoint>> notelinesbig = new ArrayList<>();
		ArrayList<LinkedList<NotePoint>> notelinessmall = new ArrayList<>();
		for(int i=0; i<NUM; i++) {
			notelines.add(new LinkedList<NotePoint>());
			notelinesbig.add(new LinkedList<NotePoint>());
			notelinessmall.add(new LinkedList<NotePoint>());
		}
		//set notes 
		for(int k=0; k<track.length; k++){
			setNote(track[k], notelines);
		}
		//System.out.println(" longth is " + messageI.getLength());
        //System.out.println(" tick is " + tick);
        //for(byte b: messageB){
		//System.out.println(b);
        //
		setNote(track[bigTrack], notelinesbig);
		setNote(track[smallTrack], notelinessmall);
		for(int i=0; i<NUM; i++) {
			System.out.println(" noteline " + i + " " +  notelines.get(i).size());
			System.out.println("notelinebigver" + i +" "+ notelinesbig.get(i).size());
		}
		System.out.println("ticklength is "+ sequence.getTickLength());
		outputNoteFile(notelines,notelinesbig, notelinessmall, musicFile, ".notes");
	}
	//set note
	public static void setNote(Track trackI, ArrayList<LinkedList<NotePoint>> notelines) {
		for(int i=0;i<trackI.size();i++) {
			MidiEvent eventI = trackI.get(i);
			long tick = eventI.getTick();
			MidiMessage messageI = eventI.getMessage();
			byte[] messageB = messageI.getMessage();
			NotePoint point;
			if(messageI.getLength() == 3) {
				if(((messageB[0] & 0xF0)>>4) == 9){
					point = new NotePoint(messageB[1], tick);
					//Change signature at other place
					int j = 12 - 0;
					j += messageB[1];
					notelines.get(j%12).add(point);
				}
			}
		}
	}
  // output file
	public static void outputNoteFile(ArrayList<LinkedList<NotePoint>> notelines,
			ArrayList<LinkedList<NotePoint>> notelinesbig,
			ArrayList<LinkedList<NotePoint>> notelinessmall,
			File musicFile, String name) {
		long tickLength = MusicPlayer.outputSequence(musicFile).getTickLength();
		short[] noteList = generateNotes(notelines, tickLength);
		short[] noteListBig = generateNotes(notelinesbig, tickLength);
		short[] noteListSmall = generateNotes(notelinessmall, tickLength);
		MusicFileData data = new MusicFileData();
		data.setMusicFile(musicFile.getAbsolutePath());
		data.setNoteList(noteList);
		data.setBigList(noteListBig);
		data.setSmallList(noteListSmall);
		MusicPlayer.setSequence(musicFile);
		data.setTicks(MusicPlayer.getTick());
		data.setTotalTime(MusicPlayer.getTime());
		try{
			FileOutputStream fs = new FileOutputStream(musicFile.getAbsolutePath()+name);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(data);
			os.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public static short[] generateNotes(ArrayList<LinkedList<NotePoint>> notelines,
			long tickLength){
		short[] noteList = new short[(int) (tickLength/30) + 300];
		for(int i=0; i<NUM; i++) {
			LinkedList<NotePoint> list = notelines.get(i);
			for(NotePoint point : list) {
				long t = point.getTick()/30;
				noteList[(int)t] = (short)(noteList[(int)t] | ((short) (1<<i)));
			}
		}
		return noteList;
	}
}

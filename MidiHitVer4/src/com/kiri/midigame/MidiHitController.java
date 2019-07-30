/* This is a controller that controls the basic action of program performed
 * kiri 2017,06,10*/
package com.kiri.midigame;
import java.io.File;

public class MidiHitController implements ControlInterface{
	private GameModel model;
	private MidiHitView view;
	private SettingData settingData;
	private RuntimeData runtimeData;
	public MidiHitController(MidiHitView v, SettingData sd, 
			RuntimeData rd, GameModel m) {
		model = m;
		view = v;
		settingData = sd;
		runtimeData = rd;
	}

	@Override
	public void readAndStart(){
		String musicFile = getSettedFile();
		if(!loadFile(new File(musicFile))) return;
		model.musicFileData = MusicFileUtil.readFile(musicFile + ".notes");

  		view.repaint();
  		synchronized(runtimeData){
  			runtimeData.setRunFlag(true);
  			runtimeData.reset();
  		}
  		System.out.println("Read over now start!");

  		new Thread(new Runnable(){
  			public void run(){
  				String musicFile; 
  				synchronized(settingData){
  					musicFile = settingData.getMusicFile();
  				}
  				MusicPlayer.play(new File(musicFile));  
  				MusicPlayer.setMute(true);
  			}
  		}).start();
  		model.startGame();

  		System.out.println("Init view!");
  		view.openBoard();
	}
	//stop file
	@Override
	public void stopFile() {
		synchronized(runtimeData){
			runtimeData.setRunFlag(false);
		};
		view.repaint();
		MusicPlayer.stopPlay();
	}
	//select file
	@Override
	public void selectFile(){
		File musicFile;
		musicFile = view.selectFile();
		if(musicFile == null) return;
		synchronized(settingData){
			settingData.setMusicFile(musicFile.getAbsolutePath());
		}
	}
	//create note file
	@Override
	public void createFile(){
		String filename = getSettedFile();
		MusicFileUtil.createNoteFile(filename);
	}
	//play music
	@Override
	public void playFile() {
		String filename = getSettedFile();
		MusicPlayer.play(new File(filename));
		MusicPlayer.setMute(false);
	}
  	private boolean loadFile(File musicFile){
  		if( musicFile==null  ||!musicFile.exists()) return false;
  		String filename = musicFile.getAbsolutePath();
  		if(! new File(filename + ".nor").exists()
  				||! new File(filename + ".big").exists()
  				||! new File(filename + ".sma").exists()){
  			createFile();
  		}
  		synchronized(settingData){
  			settingData.setMusicFile(filename);
  		}
  		return true;
  	}
  	private String getSettedFile(){
		String filename;
		synchronized(settingData){
			filename =settingData.getMusicFile();
		}
		if(filename == null || filename.length() == 0){
			selectFile();
			synchronized(settingData){
				filename = settingData.getMusicFile();
			}
		}
		return filename;
  	}
}

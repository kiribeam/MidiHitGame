package com.kiri.midigame;

public class GameModel {
	  private FlagData flagData;
	  private RuntimeData runtimeData;
	  public SettingData settingData;
	  private UserInputData userInputData;
	  public MusicFileData musicFileData;
	  private MidiHitView view;
	  
	  public final int HEIGHT;
	  public final int WIDTH;
	  public final int STEP;

	  public GameModel(FlagData fd, RuntimeData rd, SettingData sd, 
			  UserInputData ud, MusicFileData md, MidiHitView v){
		  flagData = fd;
		  runtimeData = rd;
		  settingData = sd;
		  userInputData = ud;
		  musicFileData = md;
		  view = v;
		  HEIGHT = GameConfig.HEIGHT;
		  WIDTH = GameConfig.WIDTH;
		  STEP = GameConfig.STEP;
	  }

	  public void flow(int pos, short note, int speedX, int signature, int lineNumber){
		  //System.out.println("flowing");
		  synchronized(flagData){
			  boolean[][] flag = flagData.getFlag();
			  flagData.setY(flagData.getY()+1);
			  if(pos%speedX == 0){
				  GameModelUtil.checkFlag(flag[pos%HEIGHT], note, signature, lineNumber);
			  }
		  }
	  }
	
	public void judge(int badSize, int pos, int perfectSize, int goodSize,
			 boolean[][] tempFlag, int[] inputs){
		//System.out.println("judging");
		synchronized(flagData){
			boolean[][] flag = flagData.getFlag();
			for(int i=0; i<WIDTH; i++){
				for(int k=0; k<badSize+2; k++){
					tempFlag[k][i] = flag[(pos+k)%HEIGHT][i];
				}
			}
		}
		synchronized(userInputData){
			System.arraycopy(userInputData.getInputs(), 0, inputs, 0, inputs.length);
		}
		int hitCheck;
		synchronized(runtimeData){
			hitCheck = runtimeData.getHitCheck();
		}
		for(int j=0; j<WIDTH; j++){
			for(int k=0; k<badSize+2; k++){
				if(tempFlag[k][j] && inputs[j] > 9){
					if(k>=goodSize+2) hitCheck = 1;
					else if(k<=1 || k>= perfectSize+2) hitCheck = 2;
					else hitCheck = 3;
					synchronized(flagData){
						flagData.getFlag()[(pos+k)%HEIGHT][j] = false;
					}
					tempFlag[k][j] = false;
					synchronized(runtimeData){
						if(hitCheck>1) runtimeData.setCombo(runtimeData.getCombo()+1);
						runtimeData.setScore(runtimeData.getScore()+runtimeData.getCombo()*500 + hitCheck*2000);
						runtimeData.setHitCheck(hitCheck);
						if(runtimeData.getCombo() > runtimeData.getMaxCombo())
							runtimeData.setMaxCombo(runtimeData.getCombo());
					}
					break;
				}
			}

			if(inputs[j]>0){ 
				synchronized(userInputData){
					userInputData.getInputs()[j] = inputs[j]-1;
				}
			}
			if(tempFlag[0][j] || hitCheck == 1){
				if(tempFlag[0][j]) hitCheck = 0;
				//System.out.println("temp:" + tempFlag[0][j] + " hit:" + hitCheck);
				synchronized(runtimeData){
					runtimeData.setCombo(0);
					runtimeData.setHitCheck(hitCheck);
				}
			}
		}
	}

	public void startGame(){
		new Thread(new GameThread()).start();
		
	}

	private class GameThread implements Runnable{
		public void run(){
			runGame();
		}

		private void runGame(){
			view.repaint();
			synchronized(flagData){
				flagData.clear();
			}
			short[] finalNoteList;
			synchronized(settingData){
				if(settingData.isBigTrackCheck())
					finalNoteList = musicFileData.getBigList();
				else if(settingData.isSmallTrackCheck())
					finalNoteList = musicFileData.getSmallList();
				else finalNoteList= musicFileData.getNoteList();
			}
			long totalTime = musicFileData.getTotalTime();
			long ticks = musicFileData.getTicks();
			int speedX;
			int signature;
			int lineNumber;
			synchronized(settingData){
				speedX = settingData.getSpeedX();
				signature = settingData.getSignature();
				lineNumber = settingData.getLineNumber();
			}
			//long time = System.nanoTime() /1000 ;
			//System.out.println("current time: " + time);
			long temp = (totalTime * 30)/( ticks);
			long sleepUs = temp/speedX;
			int perfectSize =(int) (40000/sleepUs)+1;
			int badSize = (int) (145000/sleepUs)+1;
			int goodSize = (int) (120000/sleepUs)+1;
			System.out.println("p:" + perfectSize + " B" + badSize + "G: " + goodSize);
			boolean[][] tempFlag = new boolean[badSize+2][WIDTH];
			int[] inputs = new int[WIDTH];
			//System.out.println("runing");
			long curTick = 0;
			for(int i=0; i<HEIGHT/2; i++){
				flow(i, finalNoteList[i/speedX], speedX, signature, lineNumber);
			}
			for(int i=HEIGHT/2; i<finalNoteList.length*speedX; i++){
				if(i == HEIGHT) {
					MusicPlayer.setTickPosition(0);
					curTick = 0;
					MusicPlayer.setMute(false);
				}

				synchronized(runtimeData){
					if(!runtimeData.isRunFlag()) return;
				}
				judge(badSize, i, perfectSize, goodSize, tempFlag, inputs);
				flow(i, finalNoteList[i/speedX], speedX, signature, lineNumber);
				view.repaint();

				if(i%speedX == 0) curTick += 30;
				boolean runSomeTime;
				boolean waitSomeTime;
				synchronized(userInputData){
					runSomeTime = userInputData.isRunSomeTime();
					waitSomeTime = userInputData.isWaitSomeTime();
				}
				long musicTick;
				if(runSomeTime) {
					try{
						Thread.sleep(sleepUs/2000);
					}catch(Exception e){
						;
					}
					musicTick = MusicPlayer.getTickPosition();
					curTick= musicTick;
					continue;
				}
				if(waitSomeTime){
					try{
						Thread.sleep(sleepUs*2/100);
					}catch(Exception e){
						;
					}
					musicTick = MusicPlayer.getTickPosition();
					curTick = musicTick;
					continue;
				}
				
				musicTick = MusicPlayer.getTickPosition();
				//System.out.println("Tick" + curTick + ":" + musicTick);
				try{
					if(curTick - musicTick > 30){
						Thread.sleep(sleepUs/800);
					}else{
						Thread.sleep(sleepUs/1200);
					}
				}catch(Exception e){
					e.printStackTrace();;
				}
			}
		}
	}
}

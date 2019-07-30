package com.kiri.midigame;

public class FlagData {

	private boolean[][] flag;
	//Height
	private int y;
	
	public FlagData(int height, int width){
		flag = new boolean[height][width];
		y = 0;
	}
	
	public void clear(){
		synchronized(this){
		    for(int i = 0; i < flag.length; i++) {
		        for(int j = 0; j < flag[0].length; j++){
		        	flag[i][j] = false;
		        }
		    }
		    y=0;
		}
	}

	public boolean[][] getFlag() {
		return flag;
	}

	public void setFlag(boolean[][] flag) {
		this.flag = flag;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}

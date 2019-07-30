package com.kiri.midigame;

public class GameModelUtil {
	public static void checkFlag(boolean[] flags, short note, 
			int signature, int lineNumber){
		switch(lineNumber){
		case 4 :
			checkFour(flags, note, signature);
			break;
		case 5 :
			checkFive(flags, note, signature);
			break;
		case 6 :
			checkSix(flags, note, signature);
			break;
		case 7 :
			checkSeven(flags, note, signature);
			break;
		}
	}
	private static void checkFour(boolean[] flags, short note, int signature){
	}
	private static void checkFive(boolean[] flags, short note, int signature){
	}
	private static void checkSix(boolean[] flags, short note, int signature){
	}
	private static void checkSeven(boolean[] flags, short note, int signature){
		flags[0] = (((1<<(signature)) | (1<<((signature+1)%12))) & note) != 0;
		flags[1] = (((1<<((signature+2)%12)) | (1<<((signature+3)%12))) & note) != 0;
		flags[2] = ((1<<((signature+4)%12)) & note) != 0;
		flags[3] = ((1<<((signature+5)%12)) & note) != 0;
		flags[4] = (((1<<((signature+6)%12)) | (1<<((signature+7)%12))) & note) != 0;
		flags[5] = (((1<<((signature+8)%12)) | (1<<((signature+9)%12))) & note) != 0;
		flags[6] = (((1<<((signature+10)%12)) | (1<<((signature+11)%12))) & note) != 0;
	}
}

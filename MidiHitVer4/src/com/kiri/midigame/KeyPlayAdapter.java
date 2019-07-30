/*This is a keyadapter to listen key events,
 * it can be better to define a interface and use
 * serveral patterns to control the game*/
package com.kiri.midigame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class KeyPlayAdapter extends KeyAdapter{
	private UserInputData userInputData;

	public KeyPlayAdapter(UserInputData userInputData) {
		this.userInputData = userInputData;
	}

   //to avoid the pressed and not released event
    private boolean[] flowLengthFlag = new boolean[7];
    public void keyPressed(KeyEvent e) {
    	//System.out.println("typed");
    	synchronized(userInputData){
    		switch(e.getKeyCode()) {
    		case KeyEvent.VK_S :if(!flowLengthFlag[0]) userInputData.getInputs()[0]=10;
    		flowLengthFlag[0] = true;break;
    		case KeyEvent.VK_D :if(!flowLengthFlag[1]) userInputData.getInputs()[1]=10;
    		flowLengthFlag[1] = true;break;
    		case KeyEvent.VK_F :if(!flowLengthFlag[2]) userInputData.getInputs()[2]=10;
    		flowLengthFlag[2] = true;break;
    		case KeyEvent.VK_SPACE :if(!flowLengthFlag[3]) userInputData.getInputs()[3]=10;
    		flowLengthFlag[3] = true;break;
    		case KeyEvent.VK_J :if(!flowLengthFlag[4]) userInputData.getInputs()[4]=10;
    		flowLengthFlag[4] = true;break;
    		case KeyEvent.VK_K :if(!flowLengthFlag[5]) userInputData.getInputs()[5]=10;
    		flowLengthFlag[5] = true;break;
    		case KeyEvent.VK_L :if(!flowLengthFlag[6]) userInputData.getInputs()[6]=10;
    		flowLengthFlag[6] = true;break;
    		case KeyEvent.VK_A :userInputData.setWaitSomeTime(true);break;
    		case KeyEvent.VK_SEMICOLON: userInputData.setRunSomeTime(true);break;
    		}
    	}
    }

    public void keyReleased(KeyEvent e) {
     // System.out.println("released");
      switch(e.getKeyCode()) {
        case KeyEvent.VK_S :flowLengthFlag[0] =false; break;
        case KeyEvent.VK_D :flowLengthFlag[1] =false; break;
        case KeyEvent.VK_F :flowLengthFlag[2] =false; break;
        case KeyEvent.VK_SPACE :flowLengthFlag[3] =false; break;
        case KeyEvent.VK_J :flowLengthFlag[4] =false; break;
        case KeyEvent.VK_K :flowLengthFlag[5] =false; break;
        case KeyEvent.VK_L :flowLengthFlag[6] =false; break;
        case KeyEvent.VK_A :
        	synchronized(userInputData){
        		userInputData.setWaitSomeTime(false);
        	}
        break;
        case KeyEvent.VK_SEMICOLON: 
        	synchronized(userInputData){
        		userInputData.setRunSomeTime(false);
        	}
        break;
      	}
    }
}


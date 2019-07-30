/* This source file is the graphic frame to show the game's content
 * kiri 2017,06,10*/
package com.kiri.midigame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

public class MidiHitView implements GameInterface{
	//gui parts
	private JFrame frame;
	private MyMPanel mPanel;
	private MyBPanel bPanel;
	private JTextField speedShowText;
	private JCheckBox bigTrackBox;
	private JCheckBox smallTrackBox;
	private static JList<String> sigList;
	private String[] sigNames = {"C", "#C", "D","#D",
			  "E", "F", "#F","G", "#G", "A", "bB", "B"};

	//Game datas
	private FlagData flagData;
	private RuntimeData runtimeData;
	public SettingData settingData;
	private UserInputData userInputData;
  
	//M&C
	private GameModel model;
	private MidiHitController controller;

	public MidiHitView(){
		flagData = new FlagData(GameConfig.HEIGHT, GameConfig.WIDTH);
		runtimeData = new RuntimeData();
		userInputData = new UserInputData(GameConfig.WIDTH);
		settingData = new SettingData();
		model = new GameModel(flagData, runtimeData, settingData, 
				userInputData, null, this);
		controller = new MidiHitController(this, settingData, 
				runtimeData, model); 
	}
	//setGui

	public void setGui(){
		frame = new JFrame();
		bPanel = new MyBPanel();
		mPanel = new MyMPanel();
		JButton startBt = new JButton("Start  ");
		JButton stopBt = new JButton("Stop   ");
		JButton selectBt = new JButton("Select");
		JButton createBt = new JButton("Create");
		JButton playBt = new JButton("Play   ");
		bigTrackBox = new JCheckBox("Big Track Mode");
		smallTrackBox = new JCheckBox("Small Track Mode");
		JLabel hintLabel = new JLabel("Adjust timing by 'a' & ';'");
		hintLabel.setForeground(Color.gray);
		JLabel speedShowLabel = new JLabel("Change Speed By Buttons");
		speedShowLabel.setForeground(Color.gray);
		speedShowText = new JTextField(""+1,1);
		speedShowText.setMaximumSize(new Dimension(5,20));
		speedShowText.setEditable(false);
		JButton speedUp = new JButton("Up");
		JButton speedDown = new JButton("Down");
		
		JButton changeSigBt = new JButton("Change Signature");
	    sigList = new JList<String>(sigNames);
	    sigList.setVisibleRowCount(4);
	    sigList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    JScrollPane sigScroller = new JScrollPane(sigList);
	    sigScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    sigScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    sigScroller.setMaximumSize(new Dimension(200,700));
		
		startBt.addActionListener(new StartListener());
		stopBt.addActionListener(new StopListener());
		selectBt.addActionListener(new SelectListener());
		createBt.addActionListener(new CreateListener());
		playBt.addActionListener(new PlayListener());
		bigTrackBox.addActionListener(new BigTrackListener());
		smallTrackBox.addActionListener(new SmallTrackListener());
		speedUp.addActionListener(new SpeedUpListener());
		speedDown.addActionListener(new SpeedDownListener());
		changeSigBt.addActionListener(new SigChangeListener());
		
		
		bPanel.add(startBt);
		bPanel.add(stopBt);
		bPanel.add(selectBt);
		bPanel.add(createBt);
		bPanel.add(playBt);
		bPanel.add(bigTrackBox);
		bPanel.add(smallTrackBox);
		bPanel.add(hintLabel);
		bPanel.add(speedShowLabel);
		bPanel.add(speedUp);
		bPanel.add(speedShowText);
		bPanel.add(speedDown);
		bPanel.add(changeSigBt);
	    bPanel.add(sigScroller);	
		
		
		frame.getContentPane().add(BorderLayout.EAST, bPanel);
		mPanel.setBackground(Color.white);
		//frame.getContentPane().add(BorderLayout.SOUTH, pPanel);
		frame.getContentPane().add(BorderLayout.CENTER, mPanel);
		// frame.remove(pPanel);
		// pPanel.setFocusable(true);
		mPanel.setFocusable(false);
		mPanel.setMaximumSize(new Dimension(500,650));
		frame.setSize(960, 720);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("MidiHit_ver_4.00");
		//while(true) pPanel.requestFocus();
	}
	//panel for game
	class MyMPanel extends JPanel{
		private static final long serialVersionUID = 3L;

		public void paintComponent(Graphics g) {
			Color[] colors = new Color[7];
			colors[0] = Color.gray;
			colors[1] = Color.blue;
			colors[2] = Color.gray;
			colors[3] = Color.orange;
			colors[4] = Color.gray;
			colors[5] = Color.blue;
			colors[6] = Color.gray;
			g.setColor(Color.black);
			g.fillRect(0,0,1500,720);

			synchronized(flagData){
				for(int i = 0; i < 144; i++) {
					for(int j = 0; j < 7; j++) {
						if(flagData.getFlag()[i][j]){
							g.setColor(colors[j]); //flag[i][j]
							g.fillRect(100 + 50*j,30 + (flagData.getY()*4-i*4+576*10)%576, 50, 12);
						}
					}
				}
			}

			for(int i = 1; i < GameConfig.WIDTH; i++) {
				g.setColor(Color.darkGray);
				g.drawLine(100 + 50*i, 0,100 + 50*i, 606);
			}
			// g.setColor(new Color(170,0,0));
			// g.fillRect(100,606,350,20);
			g.setColor(new Color(204,102,0));
			g.fillRect(90,0,10,720);
			g.fillRect(450,0,10,720);

			synchronized(runtimeData){
				g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
				g.drawString("Score: "+runtimeData.getScore(),500,40);
				g.drawString("Combo: " +runtimeData.getCombo(),500,80);
				g.drawString("Max Combo: " +runtimeData.getMaxCombo(),500,120);
				g.drawString("Speed: " + settingData.getSpeedX() + "x", 500, 160);
				String[] performance = {"Lose", "Bad", "Hit", "Perfect"};
				Color[] fontColor = { Color.gray, Color.blue, Color.orange, Color.red };
				g.setColor(fontColor[runtimeData.getHitCheck()]);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
				g.drawString(performance[runtimeData.getHitCheck()], 500,500);
			}
			g.setColor(new Color(170,0,0));
			g.fillRect(100, 603, 350, 3);
			synchronized(userInputData){
				for(int i = 0; i < GameConfig.WIDTH; i++) {
					g.setColor(colors[i]);
					g.fillRect(100+50*i,638-4*userInputData.getInputs()[i],50,4*userInputData.getInputs()[i]+5);
					g.setColor(Color.black);
					g.drawLine(100 + 50*i, 608,100 + 50*i, 661);
				}
			}
		}
	}

	//panel for button
	class MyBPanel extends JPanel{
		private static final long serialVersionUID = 3L;
		public MyBPanel(){
			this.setPreferredSize(new Dimension(150,700));
		}
		public void paintComponent(Graphics g) {
			g.setColor(Color.darkGray);
			g.fillRect(0,0,300,720);
		}
	}
	//repaint the frame 
	public void repaint(){
		mPanel.repaint();
	}
	//connect controller to start game
	class StartListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a) {
			controller.stopFile();
			controller.readAndStart();
		}
	}
	//connect controller to create note file
	class CreateListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a) {
			//System.out.println("musicfile is " + musicFile.getAbsolutePath());
			controller.createFile();
		}
	}
	//connect controller to play music
	class PlayListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a) {
			controller.stopFile();
			controller.playFile();
		}
	}
	//connect controller to stop
	class StopListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a) {
			controller.stopFile();
		}
	}
	//connect controller to select file
	class SelectListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a) {
			controller.selectFile();
		}
	}
	//connect model to change speed multiplying power
	class SpeedUpListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			synchronized(runtimeData){
				if(runtimeData.isRunFlag())return;
			}
			synchronized(settingData){
				switch(settingData.getSpeedX()) {
				case 1: settingData.setSpeedX(2);break;
    			case 2: settingData.setSpeedX(3);break;
    			case 3: settingData.setSpeedX(4);break;
    			case 4: settingData.setSpeedX(6);break;
    			case 6: settingData.setSpeedX(8);break;
				}
				speedShowText.setText(settingData.getSpeedX() + "");
			}
			mPanel.repaint();
		}
	}

	class SpeedDownListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent a){
			synchronized(runtimeData){
				if(runtimeData.isRunFlag()) return;
			}
			synchronized(settingData){
				switch(settingData.getSpeedX()) {
				case 2: settingData.setSpeedX(1);break;
				case 3: settingData.setSpeedX(2);break;
				case 4: settingData.setSpeedX(3);break;
				case 6: settingData.setSpeedX(4);break;
				case 8: settingData.setSpeedX(6);break;
				}
				speedShowText.setText(settingData.getSpeedX() + "");
			}
			mPanel.repaint();
		}
    }


	class SigChangeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
		      if(sigList.isSelectionEmpty()) return;
		      int sig = sigList.getSelectedIndex();
		      synchronized(settingData){
		    	  settingData.setSignature(sig);
		      }
		}
		
	}

	//show the file choose dialog
	public File selectFile(){
		JFileChooser fileOpen = new JFileChooser();
		fileOpen.setFileFilter(new FileFilter(){
			public boolean accept(File f) {
				if(f.isDirectory()) return true;
				return f.getName().endsWith(".mid");
			}
			public String getDescription(){
				return ".mid";
			}
		});
		fileOpen.showOpenDialog(frame);
		try{
			File f = fileOpen.getSelectedFile();
			//System.out.println(f);
			return f;
		}catch(Exception e) {
			return null;
		}
	}

	//change mode
	class BigTrackListener implements ActionListener{
		public void actionPerformed(ActionEvent a) {
			controller.stopFile();
			synchronized(settingData){
				if(settingData.isBigTrackCheck()) settingData.setBigTrackCheck(false);
				else {
					settingData.setBigTrackCheck(true);
					settingData.setSmallTrackCheck(false);
					smallTrackBox.setSelected(false);
				}
			}
		}
	}
	class SmallTrackListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			controller.stopFile();
			synchronized(settingData){
				if(settingData.isSmallTrackCheck()) settingData.setSmallTrackCheck(false);
				else {
					settingData.setSmallTrackCheck(true);
					settingData.setBigTrackCheck(false);
					bigTrackBox.setSelected(false);
				}
			}
		}
	}
	//open the input
	public void openBoard(){
		PlayThread pRun= new PlayThread();
		Thread playit = new Thread(pRun);
		playit.start();
	}

	class PlayThread implements Runnable{
		public void run(){
			inputOn();
		}
	}
	public void inputOn(){
		//pPanel = new MyPPanel();
		//frame.getContentPane().add(BorderLayout.SOUTH, pPanel);
		//frame.repaint();
		mPanel.setFocusable(true);
		//add new game operator
		KeyPlayAdapter keyl = new KeyPlayAdapter(userInputData);
		mPanel.addKeyListener(keyl);
		while(true) {
			synchronized(runtimeData){
				if(!runtimeData.isRunFlag()) break;
			}
			mPanel.requestFocus();
			try{
				Thread.sleep(500);
			}catch(Exception e) {
				;
			}
		}
		//remove the operator
		mPanel.removeKeyListener(keyl);
		//frame.remove(pPanel);
		return;
	}

	//hear the message which model updates
	public static void main(String[] args){
		new MidiHitView().setGui();
	}
}


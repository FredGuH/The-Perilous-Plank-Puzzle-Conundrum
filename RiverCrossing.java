/**
 * Program description goes here.
 * @author Gu Han
 * @date 28/5/2018
 */
/** 
 * Plan to divide the interface into four parts: front, center, bottom and info. 
 * The front includes the score and a timer. 
 * The center includes the main game interface. 
 * The bottom includes three buttons to operate. 
 * The info includes some functional buttons. 
 */


import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;


public class RiverCrossing{
	
	
	//Import the images
	ImageIcon bank1 = new ImageIcon("Image\\bank1.jpg");
	ImageIcon bank2 = new ImageIcon("Image\\bank2.jpg");
	ImageIcon plank1 = new ImageIcon("Image\\plank1.jpg");
	ImageIcon plank1_man = new ImageIcon("Image\\plank1_man.jpg");
	ImageIcon plank2 = new ImageIcon("Image\\plank2.jpg");
	ImageIcon plank2_man = new ImageIcon("Image\\plank2_man.jpg");
	ImageIcon stump1 = new ImageIcon("Image\\stump1.jpg");
	ImageIcon stump1_man = new ImageIcon("Image\\stump1_man.jpg");
	ImageIcon stump2 = new ImageIcon("Image\\stump2.jpg");
	ImageIcon stump2_man = new ImageIcon("Image\\stump2_man.jpg");
	ImageIcon stump3 = new ImageIcon("Image\\stump3.jpg");
	ImageIcon stump3_man = new ImageIcon("Image\\stump3_man.jpg");
	ImageIcon water1 = new ImageIcon("Image\\water1.jpg");
	
	
	//Main attributes
	private int cr = 0;
	private int cc = 0;
	private int c = 0;
	private int r = 0;
	private int level = 4;
	private int[] highestScore = {0,0,0,0};
	private int currentScore = 0;
	private int score = 0;
	
	//Create the frame
	JFrame a = new JFrame();
	
	//Create a JPanel to store the score and timer
	JPanel frontPane = new JPanel();
	
	//Create a JPanel to show the score
	JPanel scorePane = new JPanel();
	JLabel scorelabel = new JLabel();
	
	//Create a JPanel to show the timer
	JPanel timerPane = new JPanel(new BorderLayout());
    private long timerStart = System.currentTimeMillis();
    private long timeUsed = 0;
    JLabel timerlabel = new JLabel("00:00");
    
    private int minute, second;
	
    /**
     * This is the class to control the timer which uses thread.
     */
    private class CountingThread extends Thread {
        public boolean stop = false; 
        private CountingThread() {    
            setDaemon(true);    
        }
        @Override    
        public void run() {    
            while (true) {    
                if (!stop) {    
                    timeUsed = System.currentTimeMillis() - timerStart;    
                    timerlabel.setText("TIME: "+format(timeUsed));
                    scorelabel.setText("SCORE:"+Integer.toString(score)+":"+highestScore[level-1]);
                }
                try {    
                    sleep(1);  //Update every one millisecond  
                } 
                catch (InterruptedException e) {    
                    e.printStackTrace();    
                    System.exit(1);    
                }    
            }    
        }    
     
        /**
         * This is a method to format the timer.    
         * @param timeUsed Time has been used. Counting in millisecond. 
         * @return Time has been formatted. 
         */
        private String format(long timeUsed) {
            timeUsed = timeUsed / 1000;    
            second = (int) (timeUsed % 60);
            timeUsed = timeUsed / 60;    
            minute = (int) (timeUsed % 60);
            score = 1000-second-60*minute;
            return String.format("%02d:%02d", minute, second);    
        }    
    }
	
	//Create a JPanel which contains restart, quit and other information
	JPanel info = new JPanel();
	
	//Operate on the restart button
	JPanel restartPane = new JPanel();
	JButton restart = new JButton("RESTART LEVEL");
	
	//Show the plank the user is taking
	JPanel taking = new JPanel();
	JLabel carrying = new JLabel("You are carrying: ");
	JButton[] planks = new JButton[5];
	
	//Operate on the quit button
	JPanel quitPane = new JPanel();
	JButton quit = new JButton("QUIT");
	
	//Operate on the centerPane
	JPanel centerPane = new JPanel();
	JButton[][] button = new JButton[13][9];
	
    /**
     * This is the start method to start playing the game. 
     */
	public void startGame(){
		
		
		//Modify the images
		bank1 = new ImageIcon(bank1.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		bank2 = new ImageIcon(bank2.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		plank1 = new ImageIcon(plank1.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		plank1_man = new ImageIcon(plank1_man.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		plank2 = new ImageIcon(plank2.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		plank2_man = new ImageIcon(plank2_man.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		stump1 = new ImageIcon(stump1.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		stump1_man = new ImageIcon(stump1_man.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		stump2 = new ImageIcon(stump2.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		stump2_man = new ImageIcon(stump2_man.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		stump3 = new ImageIcon(stump3.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		stump3_man = new ImageIcon(stump3_man.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		water1 = new ImageIcon(water1.getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		
		
		//Read in the highest score from file. 
		try {
			BufferedReader in = new BufferedReader(new FileReader("D:/a/highestScore.txt"));
			for(int i=0;i<4;i++){
				highestScore[i] = Integer.parseInt(in.readLine());
			}
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		//Set up the frontPane
		frontPane.setSize(600, 100);
		frontPane.setLayout(new GridLayout(1,2));
		frontPane.setBackground(Color.GREEN);
		a.add(frontPane, BorderLayout.NORTH);
		
		//Set up the scorePane
		scorePane.setSize(300, 100);
		scorePane.setBackground(Color.GREEN);
		scorePane.setLayout(new FlowLayout());
		scorelabel.setFont(new java.awt.Font("Dialog", 1, 20));
		scorePane.add(scorelabel);
		frontPane.add(scorePane);
		
		
		//Set up the timerPane
		timerPane.setSize(300, 100);
		timerPane.setBackground(Color.GREEN);
		CountingThread thread = new CountingThread();
		timerlabel.setFont(new java.awt.Font("Dialog", 1, 20));
        timerPane.add(timerlabel);
		frontPane.add(timerPane);
        thread.start();//Start to time. 
		
		//Set up the infoPane
		info.setBackground(Color.BLUE);
		info.setSize(150, 600);
		info.setLayout(new BorderLayout());
		a.add(info, BorderLayout.EAST);
		
		//Set up the restartPane
		restartPane.setBackground(Color.BLUE);
		info.add(restartPane, BorderLayout.NORTH);
		restart.setPreferredSize(new Dimension(150, 80));
		restart.setFont(new java.awt.Font("Dialog", 1, 14));
		restart.addMouseListener(new MouseAdapter(){//Make the restart button to restart the game. 
			public void mouseClicked(MouseEvent e){
				restart();
			}
		});
		restartPane.add(restart, BorderLayout.NORTH);
			
		//Set up the takingPane
		taking.setBackground(Color.BLUE);
		taking.setLayout(new GridLayout(10, 1));
		carrying.setFont(new java.awt.Font("Dialog", 1, 14));
		info.add(taking, BorderLayout.CENTER);
		taking.add(carrying, BorderLayout.NORTH);
		for(int sum=0;sum<5;sum++){//Record the planks taken
			planks[sum]=new JButton();
			planks[sum].setBackground(Color.BLUE);
			planks[sum].setBorderPainted(false);
			planks[sum].setName(null);
			taking.add(planks[sum]);
		}
				
		//Set up the quitPane
		info.add(quitPane, BorderLayout.SOUTH);
		quitPane.setBackground(Color.BLUE);
		quit.setPreferredSize(new Dimension(150, 80));
		quit.setFont(new java.awt.Font("Dialog", 1, 14));
		quit.addMouseListener(new MouseAdapter(){//Make the quit button to quit. 
			public void mouseClicked(MouseEvent e){
				a.dispose();
			}
		});
		quitPane.add(quit, BorderLayout.SOUTH);
		
		
		//Create a JPanel to store the 9x13 grid
		centerPane.setSize(500, 600);
		centerPane.setLayout(new GridLayout(13, 9));
		a.add(centerPane,BorderLayout.CENTER);
		
		
		//Fill the grid
		for(r=0;r<13;r++){
			for(c=0;c<9;c++){
				
				//Set the background
				if(r==0){
					button[r][c] = new JButton(bank2);
					button[r][c].setName("bank2");
				}
				else if(r==12){
					button[r][c] = new JButton(bank1);
					button[r][c].setName("bank1");
				}
				else{
					button[r][c] = new JButton(water1);
					button[r][c].setName("water1");
				}
				button[r][c].setBorderPainted(false);
				centerPane.add(button[r][c]);
			}
		}
		
		//Set up the detailed map according to the current level. 
		if(level==1){
			button[0][4].setIcon(stump3);
			button[6][4].setIcon(stump1);
			button[12][4].setIcon(stump2_man);
			for(int i=7;i<12;i++){
				button[i][4].setIcon(plank2);
			}
			button[0][4].setName("stump3");
			button[6][4].setName("stump1");
			button[12][4].setName("stump2_man");
			for(int i=7;i<12;i++){
				button[i][4].setName("plank2");
			}
		}
		if(level==2){
			button[0][4].setIcon(stump3);
			button[2][0].setIcon(stump1);
			button[4][4].setIcon(stump1);
			button[6][0].setIcon(stump1);
			button[6][2].setIcon(stump1);
			button[6][3].setIcon(plank1);
			button[6][4].setIcon(plank1);
			button[6][5].setIcon(plank1);
			button[6][6].setIcon(stump1);
			button[8][0].setIcon(stump1);
			button[8][4].setIcon(stump1);
			button[9][0].setIcon(plank2);
			button[9][4].setIcon(plank2);
			button[10][0].setIcon(stump1);
			button[10][4].setIcon(plank2);
			button[11][4].setIcon(plank2);
			button[12][4].setIcon(stump2_man);
			button[0][4].setName("stump3");
			button[2][0].setName("stump1");
			button[4][4].setName("stump1");
			button[6][0].setName("stump1");
			button[6][2].setName("stump1");
			button[6][3].setName("plank1");
			button[6][4].setName("plank1");
			button[6][5].setName("plank1");
			button[6][6].setName("stump1");
			button[8][0].setName("stump1");
			button[8][4].setName("stump1");
			button[9][0].setName("plank2");
			button[9][4].setName("plank2");
			button[10][0].setName("stump1");
			button[10][4].setName("plank2");
			button[11][4].setName("plank2");
			button[12][4].setName("stump2_man");
		}
		
		if(level==3){
			button[0][6].setIcon(stump3);
			button[2][2].setIcon(stump1);
			button[2][8].setIcon(stump1);
			button[3][2].setIcon(plank2);
			button[4][2].setIcon(plank2);
			button[4][4].setIcon(stump1);
			button[4][6].setIcon(stump1);
			button[5][2].setIcon(plank2);
			button[6][0].setIcon(stump1);
			button[6][2].setIcon(plank2);
			button[6][6].setIcon(stump1);
			button[7][2].setIcon(plank2);
			button[8][2].setIcon(stump1);
			button[8][6].setIcon(stump1);
			button[8][8].setIcon(stump1);
			button[9][2].setIcon(plank2);
			button[9][8].setIcon(plank2);
			button[10][0].setIcon(stump1);
			button[10][2].setIcon(plank2);
			button[10][4].setIcon(stump1);
			button[10][8].setIcon(stump1);
			button[11][2].setIcon(plank2);
			button[12][2].setIcon(stump2_man);
			button[0][6].setName("stump3");
			button[2][2].setName("stump1");
			button[2][8].setName("stump1");
			button[3][2].setName("plank2");
			button[4][2].setName("plank2");
			button[4][4].setName("stump1");
			button[4][6].setName("stump1");
			button[5][2].setName("plank2");
			button[6][0].setName("stump1");
			button[6][2].setName("plank2");
			button[6][6].setName("stump1");
			button[7][2].setName("plank2");
			button[8][2].setName("stump1");
			button[8][6].setName("stump1");
			button[8][8].setName("stump1");
			button[9][2].setName("plank2");
			button[9][8].setName("plank2");
			button[10][0].setName("stump1");
			button[10][2].setName("plank2");
			button[10][4].setName("stump1");
			button[10][8].setName("stump1");
			button[11][2].setName("plank2");
			button[12][2].setName("stump2_man");
		}
		
		if(level==4){
			button[0][2].setIcon(stump3);
			button[2][0].setIcon(stump1);
			button[2][6].setIcon(stump1);
			button[2][8].setIcon(stump1);
			button[3][6].setIcon(plank2);
			button[4][2].setIcon(stump1);
			button[4][6].setIcon(plank2);
			button[4][8].setIcon(stump1);
			button[5][6].setIcon(plank2);
			button[6][0].setIcon(stump1);
			button[6][4].setIcon(stump1);
			button[6][6].setIcon(plank2);
			button[6][8].setIcon(stump1);
			button[7][0].setIcon(plank2);
			button[7][6].setIcon(plank2);
			button[7][8].setIcon(plank2);
			button[8][0].setIcon(plank2);
			button[8][6].setIcon(stump1);
			button[8][8].setIcon(plank2);
			button[9][0].setIcon(plank2);
			button[9][8].setIcon(plank2);
			button[10][0].setIcon(stump1);
			button[10][2].setIcon(stump1);
			button[10][4].setIcon(stump1);
			button[10][8].setIcon(stump1);
			button[11][2].setIcon(plank2);
			button[12][2].setIcon(stump2_man);
			button[0][2].setName("stump3");
			button[2][0].setName("stump1");
			button[2][6].setName("stump1");
			button[2][8].setName("stump1");
			button[3][6].setName("plank2");
			button[4][2].setName("stump1");
			button[4][6].setName("plank2");
			button[4][8].setName("stump1");
			button[5][6].setName("plank2");
			button[6][0].setName("stump1");
			button[6][4].setName("stump1");
			button[6][6].setName("plank2");
			button[6][8].setName("stump1");
			button[7][0].setName("plank2");
			button[7][6].setName("plank2");
			button[7][8].setName("plank2");
			button[8][0].setName("plank2");
			button[8][6].setName("stump1");
			button[8][8].setName("plank2");
			button[9][0].setName("plank2");
			button[9][8].setName("plank2");
			button[10][0].setName("stump1");
			button[10][2].setName("stump1");
			button[10][4].setName("stump1");
			button[10][8].setName("stump1");
			button[11][2].setName("plank2");
			button[12][2].setName("stump2_man");
		}
		
		for(r=0;r<13;r++){
			for(c=0;c<9;c++){
				//Set the event
				button[r][c].addMouseListener(new MouseAdapter(){
					public void mouseClicked(MouseEvent e) {
						for(r=0;r<13;r++){
							for(c=0;c<9;c++){
								if(button[r][c]==(JButton)e.getSource()){
									cr=r;
									cc=c;
								}
							}
						}
						
						//Click left to control the movement
						if(e.getButton()==MouseEvent.BUTTON1){
							if(button[cr][cc].getName()=="plank1"){
								//One step
								if(cc>0){
									if(button[cr][cc-1].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-1].setIcon(plank1);
										button[cr][cc-1].setName("plank1");
									}
									if(button[cr][cc-1].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-1].setIcon(stump1);
										button[cr][cc-1].setName("stump1");
									}
								}
								if(cc<8){
									if(button[cr][cc+1].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+1].setIcon(plank1);
										button[cr][cc+1].setName("plank1");
									}
									if(button[cr][cc+1].getName()=="stump1_man" && cc<8){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+1].setIcon(stump1);
										button[cr][cc+1].setName("stump1");
									}
								}
								//Two steps
								if(cc>1){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-2].setIcon(plank1);
										button[cr][cc-2].setName("plank1");
									}
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-2].setIcon(stump1);
										button[cr][cc-2].setName("stump1");
									}
								}
								if(cc<7){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+2].setIcon(plank1);
										button[cr][cc+2].setName("plank1");
									}
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+2].setIcon(stump1);
										button[cr][cc+2].setName("stump1");
									}
								}
								//Three steps
								if(cc>2){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-3].setIcon(plank1);
										button[cr][cc-3].setName("plank1");
									}
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-3].setIcon(stump1);
										button[cr][cc-3].setName("stump1");
									}
								}
								if(cc<6){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+3].setIcon(plank1);
										button[cr][cc+3].setName("plank1");
									}
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+3].setIcon(stump1);
										button[cr][cc+3].setName("stump1");
									}
								}
								//Four steps
								if(cc>3){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-4].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-4].setIcon(plank1);
										button[cr][cc-4].setName("plank1");
									}
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-4].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-4].setIcon(stump1);
										button[cr][cc-4].setName("stump1");
									}
								}
								if(cc<5){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="plank1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+3].setIcon(plank1);
										button[cr][cc+3].setName("plank1");
									}
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+4].setIcon(stump1);
										button[cr][cc+4].setName("stump1");
									}
								}
								//Five steps
								if(cc>4){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-4].getName()=="plank1" && button[cr][cc-5].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc-5].setIcon(stump1);
										button[cr][cc-5].setName("stump1");
									}
								}
								if(cc<4){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="plank1" && button[cr][cc+5].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank1_man);
										button[cr][cc].setName("plank1_man");
										button[cr][cc+5].setIcon(stump1);
										button[cr][cc+5].setName("stump1");
									}
								}
							}
							
							if(button[cr][cc].getName()=="plank2"){
								//One step
								if(cr>0){
									if(button[cr-1][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-1][cc].setIcon(plank2);
										button[cr-1][cc].setName("plank2");
									}
									if(button[cr-1][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-1][cc].setIcon(stump1);
										button[cr-1][cc].setName("stump1");
									}
								}
								if(cr<12){
									if(button[cr+1][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+1][cc].setIcon(plank2);
										button[cr+1][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+1][cc].setIcon(stump1);
										button[cr+1][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+1][cc].setIcon(stump2);
										button[cr+1][cc].setName("stump2");
									}
								}
								//Two steps
								if(cr>1){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-2][cc].setIcon(plank2);
										button[cr-2][cc].setName("plank2");
									}
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-2][cc].setIcon(stump1);
										button[cr-2][cc].setName("stump1");
									}
								}
								if(cc<11){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+2][cc].setIcon(plank2);
										button[cr+2][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+2][cc].setIcon(stump1);
										button[cr+2][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+2][cc].setIcon(stump2);
										button[cr+2][cc].setName("stump2");
									}
								}
								//Three steps
								if(cr>2){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-3][cc].setIcon(plank2);
										button[cr-3][cc].setName("plank2");
									}
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-3][cc].setIcon(stump1);
										button[cr-3][cc].setName("stump1");
									}
								}
								if(cr<10){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+3][cc].setIcon(plank2);
										button[cr+3][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+3][cc].setIcon(stump1);
										button[cr+3][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+3][cc].setIcon(stump2);
										button[cr+3][cc].setName("stump2");
									}
								}
								//Four steps
								if(cr>3){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-4][cc].setIcon(plank2);
										button[cr-4][cc].setName("plank2");
									}
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-4][cc].setIcon(stump1);
										button[cr-4][cc].setName("stump1");
									}
								}
								if(cr<9){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+3][cc].setIcon(plank2);
										button[cr+3][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+4][cc].setIcon(stump1);
										button[cr+4][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+4][cc].setIcon(stump2);
										button[cr+4][cc].setName("stump2");
									}
								}
								//Five steps
								if(cr>4){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="plank2" && button[cr-5][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr-5][cc].setIcon(stump1);
										button[cr-5][cc].setName("stump1");
									}
								}
								if(cc<4){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+5][cc].setIcon(stump1);
										button[cr+5][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(plank2_man);
										button[cr][cc].setName("plank2_man");
										button[cr+5][cc].setIcon(stump2);
										button[cr+5][cc].setName("stump2");
									}
								}
							}
							
							if(button[cr][cc].getName()=="stump1"){
								//One step
								if(cc>0){
									if(button[cr][cc-1].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-1].setIcon(plank1);
										button[cr][cc-1].setName("plank1");
									}
								}
								if(cc<8){
									if(button[cr][cc+1].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+1].setIcon(plank1);
										button[cr][cc+1].setName("plank1");
									}
								}
								if(cr>0){
									if(button[cr-1][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-1][cc].setIcon(plank2);
										button[cr-1][cc].setName("plank2");
									}
								}
								if(cr<12){
									if(button[cr+1][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+1][cc].setIcon(plank2);
										button[cr+1][cc].setName("plank2");
									}
								}
								//Two steps
								if(cc>1){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-2].setIcon(plank1);
										button[cr][cc-2].setName("plank1");
									}
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-2].setIcon(stump1);
										button[cr][cc-2].setName("stump1");
									}
								}
								if(cc<7){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+2].setIcon(plank1);
										button[cr][cc+2].setName("plank1");
									}
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+2].setIcon(stump1);
										button[cr][cc+2].setName("stump1");
									}
								}
								if(cr>1){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-2][cc].setIcon(plank2);
										button[cr-2][cc].setName("plank2");
									}
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-2][cc].setIcon(stump1);
										button[cr-2][cc].setName("stump1");
									}
								}
								if(cr<11){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+2][cc].setIcon(plank2);
										button[cr+2][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+2][cc].setIcon(stump1);
										button[cr+2][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+2][cc].setIcon(stump2);
										button[cr+2][cc].setName("stump2");
									}
								}
								//Three steps
								if(cc>2){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-3].setIcon(plank1);
										button[cr][cc-3].setName("plank1");
									}
								}
								if(cc<6){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+3].setIcon(plank1);
										button[cr][cc+3].setName("plank1");
									}
								}
								if(cr>2){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-3][cc].setIcon(plank2);
										button[cr-3][cc].setName("plank2");
									}
								}
								if(cr<10){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+3][cc].setIcon(plank2);
										button[cr+3][cc].setName("plank2");
									}
								}
								//Four steps
								if(cc>3){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-4].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-4].setIcon(plank1);
										button[cr][cc-4].setName("plank1");
									}
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-4].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-4].setIcon(stump1);
										button[cr][cc-4].setName("stump1");
									}
								}
								if(cc<5){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+4].setIcon(plank1);
										button[cr][cc+4].setName("plank1");
									}
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+4].setIcon(stump1);
										button[cr][cc+4].setName("stump1");
									}
								}
								if(cr>3){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-4][cc].setIcon(plank2);
										button[cr-4][cc].setName("plank2");
									}
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-4][cc].setIcon(stump1);
										button[cr-4][cc].setName("stump1");
									}
								}
								if(cr<9){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+4][cc].setIcon(plank2);
										button[cr+4][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+4][cc].setIcon(stump1);
										button[cr+4][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+4][cc].setIcon(stump2);
										button[cr+4][cc].setName("stump2");
									}
								}
								//Five steps
								if(cc>4){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-4].getName()=="plank1" && button[cr][cc-5].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-5].setIcon(plank1);
										button[cr][cc-5].setName("plank1");
									}
								}
								if(cc<4){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="plank1" && button[cr][cc+5].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+5].setIcon(plank1);
										button[cr][cc+5].setName("plank1");
									}
								}
								if(cr>4){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="plank2" && button[cr-5][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-5][cc].setIcon(plank2);
										button[cr-5][cc].setName("plank2");
									}
								}
								if(cr<8){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+5][cc].setIcon(plank2);
										button[cr+5][cc].setName("plank2");
									}
								}
								//Six steps
								if(cc>5){
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-4].getName()=="plank1" && button[cr][cc-5].getName()=="plank1" && button[cr][cc-6].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-6].setIcon(plank1);
										button[cr][cc-6].setName("plank1");
									}
									if(button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-3].getName()=="plank" && button[cr][cc-4].getName()=="plank1" && button[cr][cc-5].getName()=="plank1" && button[cr][cc-6].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc-6].setIcon(stump1);
										button[cr][cc-6].setName("stump1");
									}
								}
								if(cc<3){
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="plank1" && button[cr][cc+5].getName()=="plank1" && button[cr][cc+6].getName()=="plank1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+6].setIcon(plank1);
										button[cr][cc+6].setName("plank1");
									}
									if(button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="plank1" && button[cr][cc+5].getName()=="plank1" && button[cr][cc+6].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr][cc+6].setIcon(stump1);
										button[cr][cc+6].setName("stump1");
									}
								}
								if(cr>5){
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="plank2" && button[cr-5][cc].getName()=="plank2" && button[cr-6][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-6][cc].setIcon(plank2);
										button[cr-6][cc].setName("plank2");
									}
									if(button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-4][cc].getName()=="plank2" && button[cr-5][cc].getName()=="plank2" && button[cr-6][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr-6][cc].setIcon(stump1);
										button[cr-6][cc].setName("stump1");
									}
								}
								if(cr<7){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="plank2" && button[cr+6][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+6][cc].setIcon(plank2);
										button[cr+6][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="plank2" && button[cr+6][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+6][cc].setIcon(stump1);
										button[cr+6][cc].setName("stump1");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="plank2" && button[cr+6][cc].getName()=="stump2_man"){
										button[cr][cc].setIcon(stump1_man);
										button[cr][cc].setName("stump1_man");
										button[cr+6][cc].setIcon(stump2);
										button[cr+6][cc].setName("stump2");
									}
								}
							}
							if(button[cr][cc].getName()=="stump3"){
								//One step
								if(cr<12){
									if(button[cr+1][cc].getName()=="plank2_man"){
									    button[cr][cc].setIcon(stump3_man);
									    button[cr][cc].setName("stump3_man");
									    button[cr+1][cc].setIcon(plank2);
									    button[cr+1][cc].setName("plank2");
									}
								}
								//Two steps
								if(cr<11){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2_man"){
									    button[cr][cc].setIcon(stump3_man);
									    button[cr][cc].setName("stump3_man");
									    button[cr+2][cc].setIcon(plank2);
									    button[cr+2][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump3_man);
										button[cr][cc].setName("stump3_man");
										button[cr+2][cc].setIcon(stump1);
										button[cr+2][cc].setName("stump1");
									}
								}
								//Three steps
								if(cr<10){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump3_man);
										button[cr][cc].setName("stump3_man");
										button[cr+3][cc].setIcon(plank2);
										button[cr+3][cc].setName("plank2");
									}
								}
								//Four steps
								if(cr<9){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2_man"){
									    button[cr][cc].setIcon(stump3_man);
									    button[cr][cc].setName("stump3_man");
									    button[cr+4][cc].setIcon(plank2);
									    button[cr+4][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump3_man);
									    button[cr][cc].setName("stump3_man");
										button[cr+4][cc].setIcon(stump1);
										button[cr+4][cc].setName("stump1");
									}
								}
								//Five steps
								if(cr<8){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="plank2_man"){
										button[cr][cc].setIcon(stump3_man);
										button[cr][cc].setName("stump3_man");
										button[cr+5][cc].setIcon(plank2);
										button[cr+5][cc].setName("plank2");
									}
								}
								//Six steps
								if(cr<7){
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="plank2" && button[cr+6][cc].getName()=="plank2_man"){
									    button[cr][cc].setIcon(stump3_man);
									    button[cr][cc].setName("stump3_man");
									    button[cr+6][cc].setIcon(plank2);
									    button[cr+6][cc].setName("plank2");
									}
									if(button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && button[cr+5][cc].getName()=="plank2" && button[cr+6][cc].getName()=="stump1_man"){
										button[cr][cc].setIcon(stump3_man);
										button[cr][cc].setName("stump3_man");
										button[cr+6][cc].setIcon(stump1);
										button[cr+6][cc].setName("stump1");
									}
								}
								
								if(button[cr][cc].getName()=="stump3_man"){
									currentScore = score;
									if(level==1 || level==2 || level==3){
										if(currentScore > highestScore[level-1]){
											highestScore[level-1] = currentScore;
										}
										try{
											BufferedWriter out = new BufferedWriter(new FileWriter("D:/a/highestScore.txt"));
											for(int i=0;i<4;i++){
												out.write(Integer.toString(highestScore[i]));
												out.write("\r\n");
											}
											out.close();
										}catch(FileNotFoundException f){
											f.printStackTrace();
										}catch(IOException f){
											f.printStackTrace();
										}
										level++;
										timerStart = System.currentTimeMillis();
										timeUsed = 0;
										restart();
									}
									if(level==4){
										a.dispose();
										JFrame fb = new JFrame("Congratulations!");
										JPanel pb = new JPanel();
										JLabel lb = new JLabel("YOU WIN!");
										new BorderLayout();
										pb.add(lb, BorderLayout.CENTER);
										fb.add(pb);
										fb.setBounds(700, 200, 300, 150);
										fb.setResizable(false);
										fb.setVisible(true);
										fb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
									}
								}
							}
						}
						
						//Click right to pick or place planks
						if(e.getButton()==MouseEvent.BUTTON3 && planks[0].getName()!="plank2"){
							//Pick
							//One plank
							//Horizontal
							if(cc>0 && cc<8){
								if(button[cr][cc].getName()=="plank1" && 
								   ((button[cr][cc-1].getName()=="stump1" && button[cr][cc+1].getName()=="stump1_man") || 
									(button[cr][cc-1].getName()=="stump1_man" && button[cr][cc+1].getName()=="stump1"))){
									planks[0].setIcon(plank2);
									planks[0].setName("plank2");
									button[cr][cc].setIcon(water1);
									button[cr][cc].setName("water1");
								}
							}
							
							//Vertical
							if(cr>0 && cr<12){
								if(button[cr][cc].getName()=="plank2" && 
								  ((button[cr-1][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump1_man") || 
								   (button[cr-1][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump1") || 
								   (button[cr-1][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump2_man") || 
								   (button[cr-1][cc].getName()=="stump3_man" && button[cr+1][cc].getName()=="stump1"))){
									planks[0].setIcon(plank2);
									planks[0].setName("plank2");
									button[cr][cc].setIcon(water1);
									button[cr][cc].setName("water1");
								}
							}
							
							//Three planks
							//Horizontal
							if(cc>0 && cc<6){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && 
								  ((button[cr][cc-1].getName()=="stump1" && button[cr][cc+3].getName()=="stump1_man") || 
								   (button[cr][cc-1].getName()=="stump1_man" && button[cr][cc+3].getName()=="stump1"))){
									for(int sum=0;sum<3;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc+sum].setIcon(water1);
										button[cr][cc+sum].setName("water1");
									}
								}
							}
							if(cc>1 && cc<7){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc-1].getName()=="plank1" && button[cr][cc+1].getName()=="plank1" && 
								  ((button[cr][cc-2].getName()=="stump1" && button[cr][cc+2].getName()=="stump1_man") || 
								   (button[cr][cc-2].getName()=="stump1_man" && button[cr][cc+2].getName()=="stump1"))){
									for(int sum=0;sum<3;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc-1+sum].setIcon(water1);
										button[cr][cc-1+sum].setName("water1");
									}
								}
							}
							if(cc>2 && cc<8){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc-1].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" &&
								  ((button[cr][cc-3].getName()=="stump1" && button[cr][cc+1].getName()=="stump1_man") || 
								   (button[cr][cc-3].getName()=="stump1_man" && button[cr][cc+1].getName()=="stump1"))){
									for(int sum=0;sum<3;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc-2+sum].setIcon(water1);
										button[cr][cc-2+sum].setName("water1");
									}
								}
							}
							
							//Vertical
							if(cr>0 && cr<10){
								if(button[cr][cc].getName()=="plank2" && button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && 
								  ((button[cr-1][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump1_man") || 
								   (button[cr-1][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump1") || 
								   (button[cr-1][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump2_man") || 
								   (button[cr-1][cc].getName()=="stump3_man" && button[cr+3][cc].getName()=="stump1") || 
								   (button[cr-1][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump2"))){
									for(int sum=0;sum<3;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr+sum][cc].setIcon(water1);
										button[cr+sum][cc].setName("water1");
									}
								}
							}
							if(cr>1 && cr<11){
								if(button[cr][cc].getName()=="plank2" && button[cr-1][cc].getName()=="plank2" && button[cr+1][cc].getName()=="plank2" && 
								  ((button[cr-2][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump1_man") || 
								   (button[cr-2][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump1") || 
								   (button[cr-2][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump2_man") || 
								   (button[cr-2][cc].getName()=="stump3_man" && button[cr+2][cc].getName()=="stump1") || 
								   (button[cr-2][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump2"))){
									for(int sum=0;sum<3;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr-1+sum][cc].setIcon(water1);
										button[cr-1+sum][cc].setName("water1");
									}
								}
							}
							if(cr>2 && cr<12){
								if(button[cr][cc].getName()=="plank2" && button[cr-1][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" &&
								  ((button[cr-3][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump1_man") || 
								   (button[cr-3][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump1") || 
								   (button[cr-3][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump2_man") || 
								   (button[cr-3][cc].getName()=="stump3_man" && button[cr+1][cc].getName()=="stump1") || 
								   (button[cr-3][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump2"))){
									for(int sum=0;sum<3;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr-2+sum][cc].setIcon(water1);
										button[cr-2+sum][cc].setName("water1");
									}
								}
							}
							
							
							//Five planks
							//Horizontal
							if(cc>0 && cc<4){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && button[cr][cc+4].getName()=="plank1" && 
								  ((button[cr][cc-1].getName()=="stump1" && button[cr][cc+5].getName()=="stump1_man") || 
								   (button[cr][cc-1].getName()=="stump1_man" && button[cr][cc+5].getName()=="stump1"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc+sum].setIcon(water1);
										button[cr][cc+sum].setName("water1");
									}
								}
							}
							if(cc>1 && cc<5){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc-1].getName()=="plank1" && button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && button[cr][cc+3].getName()=="plank1" && 
								  ((button[cr][cc-2].getName()=="stump1" && button[cr][cc+4].getName()=="stump1_man") || 
								   (button[cr][cc-2].getName()=="stump1_man" && button[cr][cc+4].getName()=="stump1"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc-1+sum].setIcon(water1);
										button[cr][cc-1+sum].setName("water1");
									}
								}
							}
							if(cc>2 && cc<6){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-1].getName()=="plank1" && button[cr][cc+1].getName()=="plank1" && button[cr][cc+2].getName()=="plank1" && 
								  ((button[cr][cc-3].getName()=="stump1" && button[cr][cc+3].getName()=="stump1_man") || 
								   (button[cr][cc-3].getName()=="stump1_man" && button[cr][cc+3].getName()=="stump1"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc-2+sum].setIcon(water1);
										button[cr][cc-2+sum].setName("water1");
									}
								}
							}
							if(cc>3 && cc<7){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-1].getName()=="plank1" && button[cr][cc+1].getName()=="plank1" && 
								  ((button[cr][cc-4].getName()=="stump1" && button[cr][cc+2].getName()=="stump1_man") || 
								   (button[cr][cc-4].getName()=="stump1_man" && button[cr][cc+2].getName()=="stump1"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc-3+sum].setIcon(water1);
										button[cr][cc-3+sum].setName("water1");
									}
								}
							}
							if(cc>4 && cc<8){
								if(button[cr][cc].getName()=="plank1" && button[cr][cc-4].getName()=="plank1" && button[cr][cc-3].getName()=="plank1" && button[cr][cc-2].getName()=="plank1" && button[cr][cc-1].getName()=="plank1" && 
								  ((button[cr][cc-5].getName()=="stump1" && button[cr][cc+1].getName()=="stump1_man") || 
								   (button[cr][cc-5].getName()=="stump1_man" && button[cr][cc+1].getName()=="stump1"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr][cc-4+sum].setIcon(water1);
										button[cr][cc-4+sum].setName("water1");
									}
								}
							}
							
							//Vertical
							if(cr>0 && cr<8){
								if(button[cr][cc].getName()=="plank2" && button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && button[cr+4][cc].getName()=="plank2" && 
								  ((button[cr-1][cc].getName()=="stump1" && button[cr+5][cc].getName()=="stump1_man") || 
								   (button[cr-1][cc].getName()=="stump1_man" && button[cr+5][cc].getName()=="stump1") || 
								   (button[cr-1][cc].getName()=="stump1" && button[cr+5][cc].getName()=="stump2_man") || 
								   (button[cr-1][cc].getName()=="stump3_man" && button[cr+5][cc].getName()=="stump1") || 
								   (button[cr-1][cc].getName()=="stump1_man" && button[cr+5][cc].getName()=="stump2"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr+sum][cc].setIcon(water1);
										button[cr+sum][cc].setName("water1");
									}
								}
							}
							if(cr>1 && cr<9){
								if(button[cr][cc].getName()=="plank2" && button[cr-1][cc].getName()=="plank2" && button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && button[cr+3][cc].getName()=="plank2" && 
								  ((button[cr-2][cc].getName()=="stump1" && button[cr+4][cc].getName()=="stump1_man") || 
								   (button[cr-2][cc].getName()=="stump1_man" && button[cr+4][cc].getName()=="stump1") || 
								   (button[cr-2][cc].getName()=="stump1" && button[cr+4][cc].getName()=="stump2_man") || 
								   (button[cr-2][cc].getName()=="stump3_man" && button[cr+4][cc].getName()=="stump1") || 
								   (button[cr-2][cc].getName()=="stump1_man" && button[cr+4][cc].getName()=="stump2"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr-1+sum][cc].setIcon(water1);
										button[cr-1+sum][cc].setName("water1");
									}
								}
							}
							if(cr>2 && cr<10){
								if(button[cr][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-1][cc].getName()=="plank2" && button[cr+1][cc].getName()=="plank2" && button[cr+2][cc].getName()=="plank2" && 
								  ((button[cr-3][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump1_man") || 
								   (button[cr-3][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump1") || 
								   (button[cr-3][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump2_man") || 
								   (button[cr-3][cc].getName()=="stump3_man" && button[cr+3][cc].getName()=="stump1") || 
								   (button[cr-3][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump2"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr-2+sum][cc].setIcon(water1);
										button[cr-2+sum][cc].setName("water1");
									}
								}
							}
							if(cr>3 && cr<11){
								if(button[cr][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-1][cc].getName()=="plank2" && button[cr+1][cc].getName()=="plank2" && 
								  ((button[cr-4][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump1_man") || 
								   (button[cr-4][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump1") || 
								   (button[cr-4][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump2_man") || 
								   (button[cr-4][cc].getName()=="stump3_man" && button[cr+2][cc].getName()=="stump1") || 
								   (button[cr-4][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump2"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr-3+sum][cc].setIcon(water1);
										button[cr-3+sum][cc].setName("water1");
									}
								}
							}
							if(cr>4 && cr<12){
								if(button[cr][cc].getName()=="plank2" && button[cr-4][cc].getName()=="plank2" && button[cr-3][cc].getName()=="plank2" && button[cr-2][cc].getName()=="plank2" && button[cr-1][cc].getName()=="plank2" && 
								  ((button[cr-5][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump1_man") || 
								   (button[cr-5][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump1") || 
								   (button[cr-5][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump2_man") || 
								   (button[cr-5][cc].getName()=="stump3_man" && button[cr+1][cc].getName()=="stump1") || 
								   (button[cr-5][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump2"))){
									for(int sum=0;sum<5;sum++){
										planks[sum].setIcon(plank2);
										planks[sum].setName("plank2");
										button[cr-4+sum][cc].setIcon(water1);
										button[cr-4+sum][cc].setName("water1");
									}
								}
							}
						}
						
						
						else if(e.getButton()==MouseEvent.BUTTON3 && planks[0].getName()=="plank2"){
							//Place
							//One plank
							if(planks[0].getName()=="plank2" && planks[1].getName()!="plank2"){
								if(button[cr][cc].getName()=="water1"){
									//Horizontal
									if(cc>0 && cc<8){
										if((button[cr][cc-1].getName()=="stump1_man" && button[cr][cc+1].getName()=="stump1") || 
										   (button[cr][cc-1].getName()=="stump1" && button[cr][cc+1].getName()=="stump1_man")){
											button[cr][cc].setIcon(plank1);
											button[cr][cc].setName("plank1");
											planks[0].setIcon(null);
											planks[0].setName(null);
										}
									}
									
									//Vertical
									if(cr>0 && cr<12){
										if((button[cr-1][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump1") || 
										   (button[cr-1][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump1_man") || 
									       (button[cr-1][cc].getName()=="stump3_man" && button[cr+1][cc].getName()=="stump1") || 
										   (button[cr-1][cc].getName()=="stump3" && button[cr+1][cc].getName()=="stump1_man") || 
										   (button[cr-1][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump2") || 
										   (button[cr-1][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump2_man")){
											button[cr][cc].setIcon(plank2);
											button[cr][cc].setName("plank2");
											planks[0].setIcon(null);
											planks[0].setName(null);
										}
									}
								}
							}
							
							//Three planks
							if(planks[2].getName()=="plank2" && planks[3].getName()!="plank2"){
								//Horizontal
								if(cc>0 && cc<6){
									if(button[cr][cc].getName()=="water1" && button[cr][cc+1].getName()=="water1" && button[cr][cc+2].getName()=="water1"){
										if((button[cr][cc-1].getName()=="stump1_man" && button[cr][cc+3].getName()=="stump1") || 
										   (button[cr][cc-1].getName()=="stump1" && button[cr][cc+3].getName()=="stump1_man")){
											for(int i=0;i<3;i++){
												button[cr][cc+i].setIcon(plank1);
												button[cr][cc+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cc>1 && cc<7){
									if(button[cr][cc].getName()=="water1" && button[cr][cc-1].getName()=="water1" && button[cr][cc+1].getName()=="water1"){
										if((button[cr][cc-2].getName()=="stump1_man" && button[cr][cc+2].getName()=="stump1") || 
										   (button[cr][cc-2].getName()=="stump1" && button[cr][cc+2].getName()=="stump1_man")){
											for(int i=0;i<3;i++){
												button[cr][cc-1+i].setIcon(plank1);
												button[cr][cc-1+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cc>2 && cc<8){
									if(button[cr][cc].getName()=="water1" && button[cr][cc-2].getName()=="water1" && button[cr][cc-1].getName()=="water1"){
										if((button[cr][cc-3].getName()=="stump1_man" && button[cr][cc+1].getName()=="stump1") || 
										    (button[cr][cc-3].getName()=="stump1" && button[cr][cc+1].getName()=="stump1_man")){
											for(int i=0;i<3;i++){
												button[cr][cc-2+i].setIcon(plank1);
												button[cr][cc-2+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								
								//Vertical
								if(cr>0 && cr<10){
									if(button[cr][cc].getName()=="water1" && button[cr+1][cc].getName()=="water1" && button[cr+2][cc].getName()=="water1"){
										if((button[cr-1][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump1") || 
										    (button[cr-1][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump1_man") || 
										    (button[cr-1][cc].getName()=="stump3_man" && button[cr+3][cc].getName()=="stump1") || 
										    (button[cr-1][cc].getName()=="stump3" && button[cr+3][cc].getName()=="stump1_man") || 
										    (button[cr-1][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump2") || 
										    (button[cr-1][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump2_man")){
											for(int i=0;i<3;i++){
												button[cr+i][cc].setIcon(plank2);
												button[cr+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cr>1 && cr<11){
									if(button[cr][cc].getName()=="water1" && button[cr-1][cc].getName()=="water1" && button[cr+1][cc].getName()=="water1"){
										if((button[cr-2][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump1") || 
										    (button[cr-2][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump1_man") || 
										    (button[cr-2][cc].getName()=="stump3_man" && button[cr+2][cc].getName()=="stump1") || 
										    (button[cr-2][cc].getName()=="stump3" && button[cr+2][cc].getName()=="stump1_man") || 
										    (button[cr-2][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump2") || 
										    (button[cr-2][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump2_man")){
											for(int i=0;i<3;i++){
												button[cr-1+i][cc].setIcon(plank2);
												button[cr-1+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cr>2 && cr<12){
									if(button[cr][cc].getName()=="water1" && button[cr-2][cc].getName()=="water1" && button[cr-1][cc].getName()=="water1"){
										if((button[cr-3][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump1") || 
										    (button[cr-3][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump1_man") || 
										    (button[cr-3][cc].getName()=="stump3_man" && button[cr+1][cc].getName()=="stump1") || 
										    (button[cr-3][cc].getName()=="stump3" && button[cr+1][cc].getName()=="stump1_man") || 
										    (button[cr-3][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump2") || 
										    (button[cr-3][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump2_man")){
											for(int i=0;i<3;i++){
												button[cr-2+i][cc].setIcon(plank2);
												button[cr-2+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								
							}
							
							//Five planks
							if(planks[4].getName()=="plank2"){
								//Horizontal
								if(cc>0 && cc<4){
									if(button[cr][cc].getName()=="water1" && button[cr][cc+1].getName()=="water1" && button[cr][cc+2].getName()=="water1" && button[cr][cc+3].getName()=="water1" && button[cr][cc+4].getName()=="water1"){
										if((button[cr][cc-1].getName()=="stump1_man" && button[cr][cc+5].getName()=="stump1") || 
										    (button[cr][cc-1].getName()=="stump1" && button[cr][cc+5].getName()=="stump1_man")){
											for(int i=0;i<5;i++){
												button[cr][cc+i].setIcon(plank1);
												button[cr][cc+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cc>1 && cc<5){
									if(button[cr][cc].getName()=="water1" && button[cr][cc+1].getName()=="water1" && button[cr][cc+2].getName()=="water1" && button[cr][cc+3].getName()=="water1" && button[cr][cc-1].getName()=="water1"){
										if((button[cr][cc-2].getName()=="stump1_man" && button[cr][cc+4].getName()=="stump1") || 
										    (button[cr][cc-2].getName()=="stump1" && button[cr][cc+4].getName()=="stump1_man")){
											for(int i=0;i<5;i++){
												button[cr][cc-1+i].setIcon(plank1);
												button[cr][cc-1+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cc>2 && cc<6){
									if(button[cr][cc].getName()=="water1" && button[cr][cc+1].getName()=="water1" && button[cr][cc+2].getName()=="water1" && button[cr][cc-2].getName()=="water1" && button[cr][cc-1].getName()=="water1"){
										if((button[cr][cc-3].getName()=="stump1_man" && button[cr][cc+3].getName()=="stump1") || 
										    (button[cr][cc-3].getName()=="stump1" && button[cr][cc+3].getName()=="stump1_man")){
											for(int i=0;i<5;i++){
												button[cr][cc-2+i].setIcon(plank1);
												button[cr][cc-2+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cc>3 && cc<7){
									if(button[cr][cc].getName()=="water1" && button[cr][cc+1].getName()=="water1" && button[cr][cc-3].getName()=="water1" && button[cr][cc-2].getName()=="water1" && button[cr][cc-1].getName()=="water1"){
										if((button[cr][cc-4].getName()=="stump1_man" && button[cr][cc+2].getName()=="stump1") || 
										    (button[cr][cc-4].getName()=="stump1" && button[cr][cc+2].getName()=="stump1_man")){
											for(int i=0;i<5;i++){
												button[cr][cc-3+i].setIcon(plank1);
												button[cr][cc-3+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cc>4 && cc<8){
									if(button[cr][cc].getName()=="water1" && button[cr][cc-1].getName()=="water1" && button[cr][cc-2].getName()=="water1" && button[cr][cc-3].getName()=="water1" && button[cr][cc-4].getName()=="water1"){
										if((button[cr][cc-5].getName()=="stump1_man" && button[cr][cc+1].getName()=="stump1") || 
										    (button[cr][cc-5].getName()=="stump1" && button[cr][cc+1].getName()=="stump1_man")){
											for(int i=0;i<5;i++){
												button[cr][cc-4+i].setIcon(plank1);
												button[cr][cc-4+i].setName("plank1");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								
								//Vertical
								if(cr>0 && cr<8){
									if(button[cr][cc].getName()=="water1" && button[cr+1][cc].getName()=="water1" && button[cr+2][cc].getName()=="water1" && button[cr+3][cc].getName()=="water1" && button[cr+4][cc].getName()=="water1"){
										if((button[cr-1][cc].getName()=="stump1_man" && button[cr+5][cc].getName()=="stump1") || 
										    (button[cr-1][cc].getName()=="stump1" && button[cr+5][cc].getName()=="stump1_man") || 
										    (button[cr-1][cc].getName()=="stump3_man" && button[cr+5][cc].getName()=="stump1") || 
										    (button[cr-1][cc].getName()=="stump3" && button[cr+5][cc].getName()=="stump1_man") || 
										    (button[cr-1][cc].getName()=="stump1_man" && button[cr+5][cc].getName()=="stump2") || 
										    (button[cr-1][cc].getName()=="stump1" && button[cr+5][cc].getName()=="stump2_man")){
											for(int i=0;i<5;i++){
												button[cr+i][cc].setIcon(plank2);
												button[cr+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cr>1 && cr<9){
									if(button[cr][cc].getName()=="water1" && button[cr+1][cc].getName()=="water1" && button[cr+2][cc].getName()=="water1" && button[cr+3][cc].getName()=="water1" && button[cr-1][cc].getName()=="water1"){
										if((button[cr-2][cc].getName()=="stump1_man" && button[cr+4][cc].getName()=="stump1") || 
										    (button[cr-2][cc].getName()=="stump1" && button[cr+4][cc].getName()=="stump1_man") || 
										    (button[cr-2][cc].getName()=="stump3_man" && button[cr+4][cc].getName()=="stump1") || 
										    (button[cr-2][cc].getName()=="stump3" && button[cr+4][cc].getName()=="stump1_man") || 
										    (button[cr-2][cc].getName()=="stump1_man" && button[cr+4][cc].getName()=="stump2") || 
										    (button[cr-2][cc].getName()=="stump1" && button[cr+4][cc].getName()=="stump2_man")){
											for(int i=0;i<5;i++){
												button[cr-1+i][cc].setIcon(plank2);
												button[cr-1+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cr>2 && cr<10){
									if(button[cr][cc].getName()=="water1" && button[cr+1][cc].getName()=="water1" && button[cr+2][cc].getName()=="water1" && button[cr-2][cc].getName()=="water1" && button[cr-1][cc].getName()=="water1"){
										if((button[cr-3][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump1") || 
										    (button[cr-3][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump1_man") || 
										    (button[cr-3][cc].getName()=="stump3_man" && button[cr+3][cc].getName()=="stump1") || 
										    (button[cr-3][cc].getName()=="stump3" && button[cr+3][cc].getName()=="stump1_man") || 
										    (button[cr-3][cc].getName()=="stump1_man" && button[cr+3][cc].getName()=="stump2") || 
										    (button[cr-3][cc].getName()=="stump1" && button[cr+3][cc].getName()=="stump2_man")){
											for(int i=0;i<5;i++){
												button[cr-2+i][cc].setIcon(plank2);
												button[cr-2+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cr>3 && cr<11){
									if(button[cr][cc].getName()=="water1" && button[cr+1][cc].getName()=="water1" && button[cr-3][cc].getName()=="water1" && button[cr-2][cc].getName()=="water1" && button[cr-1][cc].getName()=="water1"){
										if((button[cr-4][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump1") || 
										    (button[cr-4][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump1_man") || 
										    (button[cr-4][cc].getName()=="stump3_man" && button[cr+2][cc].getName()=="stump1") || 
										    (button[cr-4][cc].getName()=="stump3" && button[cr+2][cc].getName()=="stump1_man") || 
										    (button[cr-4][cc].getName()=="stump1_man" && button[cr+2][cc].getName()=="stump2") || 
										    (button[cr-4][cc].getName()=="stump1" && button[cr+2][cc].getName()=="stump2_man")){
											for(int i=0;i<5;i++){
												button[cr-3+i][cc].setIcon(plank2);
												button[cr-3+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
								if(cr>4 && cr<12){
									if(button[cr][cc].getName()=="water1" && button[cr-4][cc].getName()=="water1" && button[cr-3][cc].getName()=="water1" && button[cr-2][cc].getName()=="water1" && button[cr-1][cc].getName()=="water1"){
										if((button[cr-5][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump1") || 
										    (button[cr-5][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump1_man") || 
										    (button[cr-5][cc].getName()=="stump3_man" && button[cr+1][cc].getName()=="stump1") || 
										    (button[cr-5][cc].getName()=="stump3" && button[cr+1][cc].getName()=="stump1_man") || 
										    (button[cr-5][cc].getName()=="stump1_man" && button[cr+1][cc].getName()=="stump2") || 
										    (button[cr-5][cc].getName()=="stump1" && button[cr+1][cc].getName()=="stump2_man")){
											for(int i=0;i<5;i++){
												button[cr-4+i][cc].setIcon(plank2);
												button[cr-4+i][cc].setName("plank2");
												planks[i].setIcon(null);
												planks[i].setName(null);
											}
										}
									}
								}
							}
						}
					}
				}
				);
			}
		}
		
		
		//Create a JPanel to show hints
		JPanel bottomPane = new JPanel();
		bottomPane.setSize(600, 100);
		bottomPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottomPane.setBackground(Color.GREEN);
		a.add(bottomPane, BorderLayout.SOUTH);
		JLabel hint = new JLabel("Click left to move, right to pick or place plank. ");
		hint.setFont(new java.awt.Font("Dialog", 1, 14));
		bottomPane.add(hint);
		
		
	    //Set up attributions of the frame
	    a.setTitle("RIVER CROSSING: The Perilous Plank Puzzle Conundrum");
		a.setBounds(650, 150, 570, 690);
		a.setResizable(false);
		a.setVisible(true);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * This is the method to restart the game.
	 * This method will reset the map according to current level. 
	 * This method will reset the timer and score. 
	 */
	public void restart(){
		
		//Reread the highest score in the file. 
		try {
			BufferedReader in = new BufferedReader(new FileReader("D:/a/highestScore.txt"));
			for(int i=0;i<4;i++){
				highestScore[i] = Integer.parseInt(in.readLine());
			}
			
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//Reset the timer. 
		timerStart = System.currentTimeMillis();
	    timeUsed = 0;
		
	    //Reset the map. 
		for(int i=0;i<5;i++){
			planks[i].setIcon(null);
			planks[i].setName(null);
		}
				
		for(r=0;r<13;r++){
			for(c=0;c<9;c++){
				
				//Set the background
				if(r==0){
					button[r][c].setIcon(bank2);
					button[r][c].setName("bank2");
				}
				else if(r==12){
					button[r][c].setIcon(bank1);
					button[r][c].setName("bank1");
				}
				else{
					button[r][c].setIcon(water1);
					button[r][c].setName("water1");
				}
			}
		}
				
		if(level==1){
			button[0][4].setIcon(stump3);
			button[6][4].setIcon(stump1);
			button[12][4].setIcon(stump2_man);
			for(int i=7;i<12;i++){
				button[i][4].setIcon(plank2);
			}
			button[0][4].setName("stump3");
			button[6][4].setName("stump1");
			button[12][4].setName("stump2_man");
			for(int i=7;i<12;i++){
				button[i][4].setName("plank2");
			}
		}
		if(level==2){
			button[0][4].setIcon(stump3);
			button[2][0].setIcon(stump1);
			button[4][4].setIcon(stump1);
			button[6][0].setIcon(stump1);
			button[6][2].setIcon(stump1);
			button[6][3].setIcon(plank1);
			button[6][4].setIcon(plank1);
			button[6][5].setIcon(plank1);
			button[6][6].setIcon(stump1);
			button[8][0].setIcon(stump1);
			button[8][4].setIcon(stump1);
			button[9][0].setIcon(plank2);
			button[9][4].setIcon(plank2);
			button[10][0].setIcon(stump1);
			button[10][4].setIcon(plank2);
			button[11][4].setIcon(plank2);
			button[12][4].setIcon(stump2_man);
			button[0][4].setName("stump3");
			button[2][0].setName("stump1");
			button[4][4].setName("stump1");
			button[6][0].setName("stump1");
			button[6][2].setName("stump1");
			button[6][3].setName("plank1");
			button[6][4].setName("plank1");
			button[6][5].setName("plank1");
			button[6][6].setName("stump1");
			button[8][0].setName("stump1");
			button[8][4].setName("stump1");
			button[9][0].setName("plank2");
			button[9][4].setName("plank2");
			button[10][0].setName("stump1");
			button[10][4].setName("plank2");
			button[11][4].setName("plank2");
			button[12][4].setName("stump2_man");
		}
		
		if(level==3){
			button[0][6].setIcon(stump3);
			button[2][2].setIcon(stump1);
			button[2][8].setIcon(stump1);
			button[3][2].setIcon(plank2);
			button[4][2].setIcon(plank2);
			button[4][4].setIcon(stump1);
			button[4][6].setIcon(stump1);
			button[5][2].setIcon(plank2);
			button[6][0].setIcon(stump1);
			button[6][2].setIcon(plank2);
			button[6][6].setIcon(stump1);
			button[7][2].setIcon(plank2);
			button[8][2].setIcon(stump1);
			button[8][6].setIcon(stump1);
			button[8][8].setIcon(stump1);
			button[9][2].setIcon(plank2);
			button[9][8].setIcon(plank2);
			button[10][0].setIcon(stump1);
			button[10][2].setIcon(plank2);
			button[10][4].setIcon(stump1);
			button[10][8].setIcon(stump1);
			button[11][2].setIcon(plank2);
			button[12][2].setIcon(stump2_man);
			button[0][6].setName("stump3");
			button[2][2].setName("stump1");
			button[2][8].setName("stump1");
			button[3][2].setName("plank2");
			button[4][2].setName("plank2");
			button[4][4].setName("stump1");
			button[4][6].setName("stump1");
			button[5][2].setName("plank2");
			button[6][0].setName("stump1");
			button[6][2].setName("plank2");
			button[6][6].setName("stump1");
			button[7][2].setName("plank2");
			button[8][2].setName("stump1");
			button[8][6].setName("stump1");
			button[8][8].setName("stump1");
			button[9][2].setName("plank2");
			button[9][8].setName("plank2");
			button[10][0].setName("stump1");
			button[10][2].setName("plank2");
			button[10][4].setName("stump1");
			button[10][8].setName("stump1");
			button[11][2].setName("plank2");
			button[12][2].setName("stump2_man");
		}
		
		if(level==4){
			button[0][2].setIcon(stump3);
			button[2][0].setIcon(stump1);
			button[2][6].setIcon(stump1);
			button[2][8].setIcon(stump1);
			button[3][6].setIcon(plank2);
			button[4][2].setIcon(stump1);
			button[4][6].setIcon(plank2);
			button[4][8].setIcon(stump1);
			button[5][6].setIcon(plank2);
			button[6][0].setIcon(stump1);
			button[6][4].setIcon(stump1);
			button[6][6].setIcon(plank2);
			button[6][8].setIcon(stump1);
			button[7][0].setIcon(plank2);
			button[7][6].setIcon(plank2);
			button[7][8].setIcon(plank2);
			button[8][0].setIcon(plank2);
			button[8][6].setIcon(stump1);
			button[8][8].setIcon(plank2);
			button[9][0].setIcon(plank2);
			button[9][8].setIcon(plank2);
			button[10][0].setIcon(stump1);
			button[10][2].setIcon(stump1);
			button[10][4].setIcon(stump1);
			button[10][8].setIcon(stump1);
			button[11][2].setIcon(plank2);
			button[12][2].setIcon(stump2_man);
			button[0][2].setName("stump3");
			button[2][0].setName("stump1");
			button[2][6].setName("stump1");
			button[2][8].setName("stump1");
			button[3][6].setName("plank2");
			button[4][2].setName("stump1");
			button[4][6].setName("plank2");
			button[4][8].setName("stump1");
			button[5][6].setName("plank2");
			button[6][0].setName("stump1");
			button[6][4].setName("stump1");
			button[6][6].setName("plank2");
			button[6][8].setName("stump1");
			button[7][0].setName("plank2");
			button[7][6].setName("plank2");
			button[7][8].setName("plank2");
			button[8][0].setName("plank2");
			button[8][6].setName("stump1");
			button[8][8].setName("plank2");
			button[9][0].setName("plank2");
			button[9][8].setName("plank2");
			button[10][0].setName("stump1");
			button[10][2].setName("stump1");
			button[10][4].setName("stump1");
			button[10][8].setName("stump1");
			button[11][2].setName("plank2");
			button[12][2].setName("stump2_man");
		}
	}
	
	/**
	 * This is the main method to run the game. 
	 * @param args
	 */
	public static void main(String[] args){
		RiverCrossing r = new RiverCrossing();
		r.startGame();
	}
}
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;


public class InvadersApplication extends JFrame implements Runnable, KeyListener {
	
	//Member Data
	private static final Dimension WindowSize= new Dimension(800,600);
	private static boolean isInitialised = false;
	private BufferStrategy strategy;
	private Graphics offscreenGraphics;
	
	private static final int NUMALIENS = 30;
	private Alien[] AlienArray = new Alien[NUMALIENS];
	private int fleetSpeed = 2;
	private Spaceship PlayerShip;
	
	private static String workingDirectory;
	private ArrayList<PlayerBullet> bulletsList = new ArrayList();
	
	private boolean isGameInProgress = false;
	private boolean openingMenu = true;
	private long score = 0;
	private long bestScore = 0;
	
	ImageIcon icon1 = new ImageIcon(workingDirectory+ "\\alien_ship_1.png");
	Image alienImage1 = icon1.getImage();
	ImageIcon icon2 = new ImageIcon(workingDirectory+ "\\alien_ship_2.png");
	Image alienImage2 = icon2.getImage();
	ImageIcon icon3= new ImageIcon(workingDirectory+ "\\player_ship.png");
    Image playerShipImage = icon3.getImage();
    ImageIcon icon4= new ImageIcon(workingDirectory+ "\\bullet.png");
    Image bulletImage = icon4.getImage();
	
	
	public static void main(String[] args) {
		workingDirectory= System.getProperty("user.dir");
		InvadersApplication IA = new InvadersApplication();

	}
	
	//Constructor
	public InvadersApplication() {
		this.setTitle("SPACE INVADERS (Finally) !!!");
		
		Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = screensize.width/2 - WindowSize.width/2;
		int y = screensize.height/2 - WindowSize.height/2;
		setBounds(x, y, WindowSize.width, WindowSize.height);
		setVisible(true);
		
		for (int i=0; i < NUMALIENS;i++) {
			AlienArray[i] = new Alien(alienImage1,alienImage2);
			}
		
		PlayerShip = new Spaceship(playerShipImage,bulletImage);
		Sprite2D.setWinWidth(WindowSize.width);
		
		Thread t = new Thread(this);
		t.start();
		
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		offscreenGraphics = strategy.getDrawGraphics();
		
		addKeyListener(this);
		
		isInitialised = true;
	}
	
	public void RunGame() {
		for (int i=0; i < NUMALIENS;i++) {
			AlienArray[i].isAlive = true;
			AlienArray[i].framesDrawn = 0;
			double xx = (i%5)*80 +70;
			double yy = (i/5)*40 +50;
			AlienArray[i].setPosition(xx, yy);
			}
		Alien.setFLeetXSpeed(fleetSpeed);
		fleetSpeed += 2;
		
		PlayerShip.setPosition(300, 530);
	}
	
	public void run() {
		while(true) {
			try{  
				Thread.sleep(20);
                
            }catch(InterruptedException e){
            	System.out.println(e);
            }
			
			boolean alienDirectionReversalNeeded = false;
			for (int i = 0; i < NUMALIENS;i++) {
					if(AlienArray[i].move() && AlienArray[i].isAlive == true) {
					for(int j=0;j < NUMALIENS;j++) {
						alienDirectionReversalNeeded = true;
						}
					}	
			}
			
			if(alienDirectionReversalNeeded) {
					Alien.reverseDirection();
					for(int i=0;i<NUMALIENS;i++) {
						AlienArray[i].jumpDownwards();
					}
				}
					
			for(PlayerBullet i: bulletsList) {
					i.move();
					for (int j=0;j< NUMALIENS;j++) {
						if(((AlienArray[j].x<i.x && AlienArray[j].x+AlienArray[j].myImage.getWidth(null)>i.x)|| 
							(i.x<AlienArray[j].x && i.x+i.myImage.getWidth(null)>AlienArray[j].x))&& 
							((AlienArray[j].y<i.y && AlienArray[j].y+AlienArray[j].myImage.getHeight(null)>i.y)|| 
							(i.y<AlienArray[j].y && i.y+i.myImage.getHeight(null)>AlienArray[j].y))&& 
							(AlienArray[j].isAlive == true && i.isAlive == true)) {
							AlienArray[j].isAlive = false;
							i.isAlive = false;
							score += 10;
						}
						
						if(((AlienArray[j].x<PlayerShip.x && AlienArray[j].x+AlienArray[j].myImage.getWidth(null)>PlayerShip.x)|| 
								(PlayerShip.x<AlienArray[j].x && PlayerShip.x+PlayerShip.myImage.getWidth(null)>AlienArray[j].x))&& 
								((AlienArray[j].y<PlayerShip.y && AlienArray[j].y+AlienArray[j].myImage.getHeight(null)>PlayerShip.y)|| 
								(PlayerShip.y<AlienArray[j].y && PlayerShip.y+PlayerShip.myImage.getHeight(null)>AlienArray[j].y))&& 
								(AlienArray[j].isAlive == true)) {
							isGameInProgress = false;
							score = 0;
							bestScore = 0;
							fleetSpeed = 2;
						}
					}
					
				}
			int counter = 0;
			for (int j=0;j< NUMALIENS;j++) {
				if(AlienArray[j].isAlive == false) {
					counter++;
				}
				
				if(counter >= 30) {
					bestScore += score;
					score = 0;
					RunGame();
				}
			}
					
			PlayerShip.move();
			this.repaint();
			
		}
    }
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(isGameInProgress == false) {
			if((((keyCode>=65)&&(keyCode<=90))||((keyCode>=97)&&(keyCode<=122))||((keyCode>=48)&&(keyCode<=57)))){
					isGameInProgress = true;
					openingMenu = false;
					RunGame();
			}
		}else {
		if(keyCode == KeyEvent.VK_RIGHT) {
			PlayerShip.setXSpeed(4);
		}
		
		if(keyCode == KeyEvent.VK_LEFT) {
			PlayerShip.setXSpeed(-4);
		}
		
		if(keyCode == KeyEvent.VK_SPACE) {
			bulletsList.add(PlayerShip.shootBullet());
		}
		
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
			PlayerShip.setXSpeed(0);
		}
		
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	
	public void paint (Graphics g) {  
		if (!isInitialised)
				return;
		
		g = offscreenGraphics;
		
		g.setColor(Color.black);
		g.fillRect (0, 0, 1000,1000);
		
		if(isGameInProgress) {
			g.setFont(new Font("Gotham", Font.BOLD, 25));
		    g.setColor(Color.white);
			g.drawString("Score: "+score,((WindowSize.width/3)-40),50);
			g.drawString("Best: "+bestScore,((WindowSize.width/3)+100),50);
		
		for (int i=0; i< NUMALIENS;i++) {
			if(AlienArray[i].isAlive == true)
				AlienArray[i].paint(g);
        	}
		
		PlayerShip.paint(g);
		
		Iterator iterator = bulletsList.iterator();
		while(iterator.hasNext()) {
			PlayerBullet b = (PlayerBullet) iterator.next();
			if(b.isAlive)
				b.paint(g);
		}
		
		}else {
			if(openingMenu) {
				g.setFont(new Font("Gotham", Font.BOLD, 50));
			    g.setColor(Color.white);
				g.drawString("Start the Game",((WindowSize.width/3)-44),200);
				g.setFont(new Font("Gotham", Font.BOLD, 25));
				g.drawString("Press any key to play",((WindowSize.width/3)),300);
				g.setFont(new Font("Gotham", Font.PLAIN, 25));
				g.drawString("[Arrow keys to move, space to fire]",((WindowSize.width/3)-60),350);
			}else {
				g.setFont(new Font("Gotham", Font.BOLD, 50));
			    g.setColor(Color.white);
				g.drawString("Game Over",((WindowSize.width/3)-24),200);
				g.setFont(new Font("Gotham", Font.BOLD, 25));
				g.drawString("Press any key to play",((WindowSize.width/3)),300);
				g.setFont(new Font("Gotham", Font.PLAIN, 25));
				g.drawString("[Arrow keys to move, space to fire]",((WindowSize.width/3)-60),350);
			}
		}
		
		strategy.show();
	}
}

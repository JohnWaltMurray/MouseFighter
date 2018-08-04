import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.util.*;

public class GameCanvas extends Canvas implements MouseListener, MouseMotionListener {
	boolean Updating = false;
	private Dimension size;
	private ArrayList<Bullet> playerB = new ArrayList<Bullet>();
	private ArrayList<Bullet> bossB = new ArrayList<Bullet>();
	private ArrayList<Bullet> bigBullet = new ArrayList<Bullet>();
	private final double GAME_SPEED = 5;
	private Bullet Boss = new Bullet(75, 75, 250, 25, 1*GAME_SPEED, 0, 0, false);
	private int mouseX;
	private int mouseY;
	private ListIterator<Bullet> i;
	private int playerLife = 10;
	private int bossLife = 80;
	private int fireRate = 0;
	private boolean isDamaged = false;
	private double bossMiddleX;
	private double bossMiddleY;
	private int invulnerbilityCounter = 0;
	private boolean isInvulnerble = false;
	private boolean playerDamaged = false;
	private Timer gameTime;
	private boolean gameStarted;
	private int counter = 0;
	private Random rand = new Random();
	private int score = 1000;
	private int count;
	private boolean victory = false;
	private String name = "";
	private boolean showScoreboard;
	
	public GameCanvas() {
		setIgnoreRepaint(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		FrameRate time = new FrameRate(this);
		
		gameTime = new Timer(16, time);
		gameTime.start();
	}
	public void UpdateCanvas() {
		
		if(Updating) {
			return;
		}
		Updating = true;
		size = getSize();
		BufferStrategy strategy = getBufferStrategy();
		Graphics render = strategy.getDrawGraphics();
		bossMiddleX = Boss.getPosX()+(Boss.getWidth()/2);
		bossMiddleY = Boss.getPosY()+(Boss.getHeight()/2);
		if(playerDamaged) {
			render.setColor(Color.red);
			playerDamaged = false;
		} else {
			render.setColor(Color.black);
		}
		render.fillRect(0, 0, (int)getSize().getHeight()+100, (int)getSize().getWidth()+100);
		render.setColor(Color.white);
		if(showScoreboard) {
			Scoreboard board = new Scoreboard("scores.data");
			count = 1;
			render.setColor(Color.black);
			render.fillRect(0, 0, (int)getSize().getHeight()+100, (int)getSize().getWidth()+100);
			render.setColor(Color.white);
			render.drawString("Scoreboard", (getX()+getWidth())/2-40, 10);
			for(Score s : board.getScoreboard() ) {
				count++;
				String scoreString = s.getName().toUpperCase();
				render.drawString(scoreString, 0, count*10);
				render.drawString(s.getScoreAmount()+"", getWidth()-((""+s.getScoreAmount()).length()*10), count*10);
			}
			
		}
		if(!gameStarted && !showScoreboard) {
			render.drawString("Start Game", (getX()+getWidth()/2)-40, (getY()+getHeight()/2)-20);
			render.drawString("Scoreboard", (getX()+getWidth()/2)-41, (getY()+getHeight()/2));
			render.drawString("Quit", (getX()+getWidth()/2)-20, (getY()+getHeight()/2)+20);
		} else if (gameStarted) {
		i = playerB.listIterator();
		while(i.hasNext()) {
			Bullet b = i.next();
			b.setX(b.getPosX()+b.getDirectionX());
			b.setY(b.getPosY()+b.getDirectionY());
			if(isIntersecting(b.getBounds(), Boss.getBossBounds())
					&& isInvulnerble == false) {
				isDamaged = true;
				bossLife--;
				i.remove();
			}
			if(isUnbounded(b)) {
				i.remove();
			}
			else {
				render.fillOval((int)(b.getPosX()+(b.getWidth()/2)), (int)(b.getPosY()+(b.getHeight()/2)), (int)b.getWidth(), (int)b.getHeight());
			}
		}
		
		if(bossLife > 40 && fireRate%15 == 0) {
			double direction;
			double dX = mouseX-7 - bossMiddleX;
			double dY = mouseY-7 - bossMiddleY;
			direction = Math.atan2(dY, dX);
			bossB.add(new Bullet(9, 9, bossMiddleX, bossMiddleY, Math.cos(direction)*(GAME_SPEED*2), Math.sin(direction)*(GAME_SPEED*2), 1, false));
			fireRate = 1;
		}
		if(bossLife <= 40 && fireRate%200 == 0 ) {
			double direction;
			double dX = mouseX-7 - bossMiddleX;
			double dY = mouseY-7 - bossMiddleY;
			direction = Math.atan2(dY, dX);
			bigBullet.add(new Bullet(90, 90, bossMiddleX-75, bossMiddleY-75, Math.cos(direction)*(GAME_SPEED/4), Math.sin(direction)*(GAME_SPEED/4), 2, true));
			fireRate = 1;
		}
		
		render.setColor(Color.red);
		i = bossB.listIterator();
		while(i.hasNext()) {
			Bullet b = i.next();
			b.setX(b.getPosX()+b.getDirectionX());
			b.setY(b.getPosY()+b.getDirectionY());
			if (b.isAccelerating()) {
				if(b.getDirectionX()==0 || b.getDirectionY()==0) {
					b.setDirectionX(Math.random());
					b.setDirectionY(Math.random());
				}
				b.setDirectionX((b.getDirectionX())*1.02);
				b.setDirectionY((b.getDirectionY())*1.02);
			}
			if(isIntersecting(b.getBounds(), mouseBounds())
					&& isInvulnerble == false) {
				isInvulnerble = true;
				playerDamaged = true;
				playerLife--;
			}
			if(isUnbounded(b)) {
				if (b.getBounce() == 0) {
					i.remove();
				} else {
					b.setBounce(b.getBounce()-1);
					if(b.getPosX() < 0 || b.getPosX()+b.getWidth() > getWidth()) {
						b.setDirectionX(-b.getDirectionX());
					} else {
						b.setDirectionY(-b.getDirectionY());
					}
				}
			} else {
				render.fillOval((int)(b.getPosX()+(b.getWidth()/2)), (int)(b.getPosY()+(b.getHeight()/2)), (int)b.getWidth(), (int)b.getHeight());
			}
		}
		
		render.setColor(Color.red);
		i = bigBullet.listIterator();
		
		while(i.hasNext()) {
			counter++;
			Bullet b = i.next();
			b.setX(b.getPosX()+b.getDirectionX());
			b.setY(b.getPosY()+b.getDirectionY());
			b.setWidth(b.getWidth()-.3);
			b.setHeight(b.getHeight()-.3);
			if (counter > 7) {
				bossB.add(new Bullet(9, 9, (Math.random()*70)+b.getPosX()+(b.getWidth()/2), (Math.random()*70)+b.getPosY()+(b.getHeight()/2), (rand.nextInt(3)-1)*.5, (rand.nextInt(3)-1)*.5,  1, true));
				counter = 0;
			}
			if (b.isAccelerating()) {
				b.setDirectionX((b.getDirectionX())*1.01);
				b.setDirectionY((b.getDirectionY())*1.01);
			}
			
			if(isIntersecting(b.getBounds(), mouseBounds())
					&& isInvulnerble == false) {
				render.fillRect(b.getBounds().x, b.getBounds().y, (int)b.getWidth(), (int)b.getHeight());
				isInvulnerble = true;
				playerDamaged = true;
				playerLife--;
			}
			if(isUnbounded(b)) {
				if (b.getBounce() == 0||b.getWidth()<=0) {
					i.remove();
				} else {
					b.setBounce(b.getBounce()-1);
					if(b.getPosX() < 0 || b.getPosX()+b.getWidth() > getWidth()) {
						b.setDirectionX(-b.getDirectionX());
					} else {
						b.setDirectionY(-b.getDirectionY());
					}
				}
			} else {
				render.fillOval((int)(b.getPosX()+(b.getWidth()/2)), (int)(b.getPosY()+(b.getHeight()/2)), (int)b.getWidth(), (int)b.getHeight());
			}
		}
		count++;
		if(count >= 10) {
			score--;
			count = 0;
		}
		Boss.setX(Boss.getPosX()+Boss.getDirectionX());
		Boss.setY(Boss.getPosY()+Boss.getDirectionY());
		if(isUnboundedDimensions(Boss)) {
			Boss.setDirectionX(Boss.getDirectionX()*-1);
		}
		if(!isDamaged) {
			render.setColor(Color.green);
		} else {
			render.setColor(Color.red);
		}
		
		render.drawString("PosX: "+mouseX, 5, 10);
		render.drawString("PosY: "+mouseY, 5, 25);
		render.drawString("Score: "+score, 400, 10);
		render.fillRect((int)Boss.getPosX(), (int)Boss.getPosY(), (int)Boss.getWidth(), (int)Boss.getHeight());
		render.fillRect(getX()+10, getY()+getHeight()-50, bossLife, 20);
		render.drawString("Boss Life", getX()+10, getY()+getHeight()-60);
		
		render.setColor(Color.white);
		render.fillRect(getX()+getWidth()-110, getY()+getHeight()-50, playerLife*10, 20);
		render.drawString("Player Life", getX()+getWidth()-110, getY()+getHeight()-60);
		if (playerLife <= 0) {
			render.setColor(Color.red);
			render.drawString("Game Over", (getX()+getWidth()/2)-30, (getY()+getHeight()/2)-20);
			gameTime.stop();
		}
		if (bossLife <= 0) {
			render.setColor(Color.green); 
			count = 0;
			render.drawString("You Win!", (getX()+getWidth()/2)-30, (getY()+getHeight()/2)-20);
			while(count < 100) {
				count++;
				System.out.println("You win");
			}
			
			name = JOptionPane.showInputDialog(this, "Enter your name: ", "");
			if(name == null) {
				System.exit(0);
			}
				name = name.replace(" ", "");
			FileWriter writer;
			try {
				writer = new FileWriter(new File("scores.data"), true);
				writer.write(name+" "+score+"\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gameTime.stop();
		}
		if(render != null){
			render.dispose();
		}
		isDamaged = false;
		if(isInvulnerble) {
			invulnerbilityCounter++;
			if(invulnerbilityCounter > 30) {
				invulnerbilityCounter = 0;
				isInvulnerble = false;
			}
		}
		
		fireRate++;
		}
		strategy.show();
		Toolkit.getDefaultToolkit().sync();
		Updating = false;
		
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(gameStarted) {
			playerB.add(new Bullet(6, 12, getMouseX()-6, getMouseY()-6, 0, -2*GAME_SPEED, 0, false));
		} else {
			if(isIntersecting(new Rectangle(e.getX(), e.getY(), 1, 1), new Rectangle((getX()+getWidth()/2)-41, (getY()+getHeight()/2)-14, 63, 15))) {
				showScoreboard = true;
				Updating = false;
				UpdateCanvas();
			} else if(isIntersecting(new Rectangle(e.getX(), e.getY(), 1, 1), new Rectangle((getX()+getWidth()/2)-20, (getY()+getHeight()/2)+6, 25, 15))) {
				System.exit(0);
			}
			else if(isIntersecting(new Rectangle(e.getX(), e.getY(), 1, 1), new Rectangle((getX()+getWidth()/2)-40, (getY()+getHeight()/2)-35, 60, 18))) {
				gameStarted = true;
			}
			
		}
	}
	public double getMouseX() {
		return mouseX;
	}
	
	public double getMouseY() {
		return mouseY;
	}
	public boolean isUnbounded(Bullet b) {
		if(b.getPosX()+(b.getWidth()/2) < 0|| b.getPosX()+(b.getWidth()/2) > getSize().getWidth() || b.getPosY()+(b.getHeight()/2) < 0 || b.getPosY()+(b.getHeight()/2) > getSize().getHeight()) {
			return true;
		} else { 
			return false;
		}
	}
	public boolean isUnboundedDimensions(Bullet b) {
		if(b.getPosX() < 0|| b.getPosX()+(b.getWidth()) > getSize().getWidth() || b.getPosY() < 0 || b.getPosY()+(b.getHeight()) > getSize().getHeight()) {
			return true;
		} else { 
			return false;
		}
	}
	public Rectangle mouseBounds() {
		return new Rectangle(mouseX, mouseY, 3, 3);
	}
	
	public boolean isIntersecting(Rectangle r1, Rectangle r2) {
		return r1.intersects(r2);
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

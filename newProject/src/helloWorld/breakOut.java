package helloWorld;
import acm.program.*;
import acm.util.RandomGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import acm.graphics.*;
public class breakOut extends GraphicsProgram {
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;
	//canvassýn pixel deðerleri;
	private RandomGenerator rgen = RandomGenerator.getInstance();//random methodlarý;
	
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10; 
	private static final int PADDLE_Y_OFFSET = 30;
	//raketi ve yerden yüksekliði;
	private static final int BRICKS = 10;//tuðla sayýsý;
	private static final int BRICK_SEP = 4;//tuðlalar arasý boþluk;
	private static final double BRICK_WIDTH = (APPLICATION_WIDTH - (BRICKS - 1) * BRICK_SEP) / BRICKS;
	private static final int BRICK_HEIGHT = 8;
	//tuðlalarýn boyutlarý;
	private static final int BRICK_Y_OFFSET = 70;//tuðlalarýn yerden yuksekliði;
	
	private static final int BALL_RADIUS = 10;//top boyutu;
	private static final int NTURNS = 3;//tur sayýsý;
	
	private static final int DELAY = 15;//animasyon süresi ve gecikme;
	
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private GLabel point;
	private Color scoreColor;
	private double dx,dy;
	int nTurns = NTURNS;
	int howManyBreak = 0;
	int score = 0;
	
	public void run() {
		addMouseListeners();
		setupGame();
		playGame();
	}
	private void buildBricks() {
		for(int i=0;i<BRICKS;i++) {
			for(int j=0;j<BRICKS;j++) {
				brick = new GRect((BRICK_WIDTH + BRICK_SEP) * j,BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * i,BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				switch(i) {
					case 0:
						brick.setColor(Color.RED);break;
					case 1:
						brick.setColor(Color.RED);break;
					case 2:
						brick.setColor(Color.ORANGE);break;
					case 3:
						brick.setColor(Color.ORANGE);break;
					case 4:
						brick.setColor(Color.YELLOW);break;
					case 5:
						brick.setColor(Color.YELLOW);break;
					case 6:
						brick.setColor(Color.GREEN);break;
					case 7:
						brick.setColor(Color.GREEN);break;
					case 8:
						brick.setColor(Color.CYAN);break;
					case 9:
						brick.setColor(Color.CYAN);break;
					default:break;
				}
				add(brick);
			}
		}
	}
	
	private void buildPaddle() {
		paddle = new GRect((APPLICATION_WIDTH - PADDLE_WIDTH)/2,APPLICATION_HEIGHT - PADDLE_Y_OFFSET*2,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}
	
	private void makeBall() {
		ball = new GOval(BALL_RADIUS*2,BALL_RADIUS*2);
		ball.setFilled(true);
		add(ball,(APPLICATION_WIDTH - BALL_RADIUS)/2,(APPLICATION_HEIGHT - BALL_RADIUS)/2);
	}
	
	private void setupGame() {
		addStartScreen();
		buildBricks();
		buildPaddle();
		makeBall();
		addPoint();
	}
	
	private void playGame() {
		moveBall();
		while(true) {
			ball.move(dx, dy);
			pause(DELAY);
			checkWall();
			GObject collider = checkCollision();
			if(collider == paddle) 
				dy = -dy;
			else if(collider != null && collider != paddle && collider != point) {
				remove(collider);
				dy = -dy;
				scoreColor = collider.getColor();
				gameScore();
			}
			if(nTurns == 0)
				break;
		}
	}
	
	private void addPoint() {
		point = new GLabel("Score : " + score,APPLICATION_WIDTH/2.5,30);
		point.setFont("SERIF-17");
		add(point);
	}
	
	private void gameScore() {//scorebricks tuðlalarýn hepsinin kýrýlýp kýrýlmadýðýný kontrol ediyor;
		howManyBreak++;
		if(howManyBreak == BRICKS * BRICKS) {//score ise renklere göre puan veriyor;
			GLabel youWin = new GLabel("YOU WIN!",APPLICATION_WIDTH/2.5,APPLICATION_HEIGHT/2);
			youWin.setFont("SERIF-16");
			removeAll();
			pause(DELAY);
			add(youWin);
		}
		if(Color.RED == scoreColor)
			score += 1000;	
		else if(Color.ORANGE == scoreColor)
			score += 750;
		else if(Color.YELLOW == scoreColor)
			score += 500;
		else if(Color.GREEN == scoreColor)
			score += 250;
		else if(Color.CYAN == scoreColor)
			score += 100;
		remove(point);
		addPoint();
	}
	
	private void moveBall() {
		dx = rgen.nextDouble(1.0,3.0);
		if(rgen.nextBoolean(0.5))
			dx = -dx;
		dy = 3.0;
	}
	
	private void checkWall() {
		if(ball.getX() <= 0)
			dx = -dx;
		else if(ball.getX() + 2 * BALL_RADIUS >= APPLICATION_WIDTH) 
			dx = - dx;
		else if(ball.getY() <= 0)
			dy = -dy;
		else if(ball.getY() + 2 * BALL_RADIUS >= APPLICATION_HEIGHT - PADDLE_Y_OFFSET) {
			remove(ball);
			nTurns--;
			score -= 500;
			remove(point);addPoint();
			if(nTurns == 0) {
				GLabel gOver = new GLabel("GAME OVER!",APPLICATION_WIDTH/2.5,APPLICATION_HEIGHT/2);
				gOver.setFont("SERIF-16");
				pause(DELAY);
				removeAll();
				add(gOver);
				addPoint();
			}
			waitForClick();
			if(nTurns > 0) {
				remove(point);
				addPoint();
				makeBall();
			}
		}
	}
	
	private GObject checkCollision() {
		if(getElementAt(ball.getX(),ball.getY()) != null)
			return getElementAt(ball.getX(),ball.getY());
		
		else if(getElementAt((ball.getX() + 2 * BALL_RADIUS),ball.getY()) != null)
				return getElementAt((ball.getX() + 2 * BALL_RADIUS),ball.getY());
		
		else if(getElementAt(ball.getX(),(ball.getY() + 2 * BALL_RADIUS)) != null)
			return getElementAt(ball.getX(),(ball.getY() + 2 * BALL_RADIUS));
		
		else if(getElementAt((ball.getX() + 2 * BALL_RADIUS),(ball.getY() + 2 * BALL_RADIUS)) != null)
			return getElementAt((ball.getX() + 2 * BALL_RADIUS),(ball.getY() + 2 * BALL_RADIUS));
		
		else return null;
	}
	
	private void addStartScreen() {
		GLabel welcomeScreen = new GLabel("CLICK TO START",APPLICATION_WIDTH/2.5,APPLICATION_HEIGHT/2);
		welcomeScreen.setFont("SERIF-14");
		add(welcomeScreen);
		waitForClick();
		removeAll();
	}
	
	public void mouseMoved(MouseEvent m) {//raket kontrolünün(orta noktasý ile) saðlanmasý;
		double newPaddleXPos = m.getX() - PADDLE_WIDTH/2;
		double newPaddleYPos = APPLICATION_HEIGHT - PADDLE_Y_OFFSET*2 - PADDLE_HEIGHT;
		paddle.setLocation(newPaddleXPos, newPaddleYPos);
		if(newPaddleXPos + PADDLE_WIDTH>= APPLICATION_WIDTH)
			paddle.setLocation(APPLICATION_WIDTH - PADDLE_WIDTH, newPaddleYPos);
		else if(newPaddleXPos <= 0)
			paddle.setLocation(0, newPaddleYPos);
	}
}
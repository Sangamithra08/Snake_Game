import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 20;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 115;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel()
	{
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
	    startGame();
	}
	public void startGame()
	{
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
	    if (running) {
	        // Draw a colorful background
	        g.setColor(new Color(0, 102, 0)); // Dark green
	        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

	        // Draw the apple with a different color
	        g.setColor(new Color(255, 0, 0)); // Red
	        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

	        // Draw the snake with a gradient color
	        for (int i = 0; i < bodyParts; i++) {
	            if (i == 0) {
	                // Head of the snake
	                g.setColor(new Color(255, 255, 0)); // Yellow
	            } else {
	                // Body parts
	                int red = 255 - i * 10;
	                int green = 255 - i * 5;
	                int blue = 0;
	                g.setColor(new Color(red, green, blue));
	            }

	            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
	        }

	        // Draw the score in a stylish way
	        g.setColor(new Color(0, 255, 255)); // Cyan
	        g.setFont(new Font("Arial", Font.BOLD, 20));
	        g.drawString("Score: " + applesEaten, 10, 20);
	    } else {
	        gameOver(g);
	    }
	}

	public void newApple()
	{
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move()
	{
		for(int i=bodyParts; i>0 ; i--)
		{
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction)
		{
		    case 'U':
		    	y[0] = y[0] - UNIT_SIZE;
		    	break;
		    case 'D':
		    	y[0] = y[0] + UNIT_SIZE;
		    	break;
		    case 'L':
		    	x[0] = x[0] - UNIT_SIZE;
		    	break;
		    case 'R':
		    	x[0] = x[0] + UNIT_SIZE;
		    	break;
		}
	}
	public void checkApple()
	{
		if((x[0] == appleX) && (y[0] == appleY))
		{
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions()
	{
	    for(int i=bodyParts; i>0 ; i--)
	    {
	    	if((x[0] == x[i]) && (y[0] == y[i]))
	    	{
	    		running=false;
	    	}
	    }
	    if((x[0] < 0) || (x[0] > SCREEN_WIDTH) || (y[0] < 0) || (y[0] > SCREEN_HEIGHT))
	    {
	    	running=false;
	    }
	    if(!running)
	    {
	    	timer.stop();
	    }
	}
//	public void gameOver(Graphics g)
//	{
		
		
		public void gameOver(Graphics g) {
		    // Draw a semi-transparent overlay to darken the background
		    g.setColor(new Color(0, 0, 0, 150)); // Dark overlay with alpha (transparency)
		    g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		    // Draw the "Game Over" message in a gradient color
		    Font titleFont = new Font("Arial", Font.BOLD, 75);
		    g.setFont(titleFont);
		    FontMetrics titleMetrics = getFontMetrics(titleFont);

		    int titleX = (SCREEN_WIDTH - titleMetrics.stringWidth("Game Over")) / 2;
		    int titleY = SCREEN_HEIGHT / 2 - 50;

		    GradientPaint titleGradient = new GradientPaint(titleX, titleY, Color.MAGENTA, titleX + titleMetrics.stringWidth("Game Over"), titleY, Color.YELLOW);
		    ((Graphics2D) g).setPaint(titleGradient);
		    g.drawString("Game Over", titleX, titleY);

		    // Draw the final score in a different color
		    Font scoreFont = new Font("Arial", Font.ITALIC, 40);
		    g.setFont(scoreFont);
		    FontMetrics scoreMetrics = getFontMetrics(scoreFont);

		    int scoreX = (SCREEN_WIDTH - scoreMetrics.stringWidth("Score: " + applesEaten)) / 2;
		    int scoreY = titleY + titleMetrics.getHeight() + 20;

		    g.setColor(new Color(255, 255, 255)); // White
		    g.drawString("Score: " + applesEaten, scoreX, scoreY);
		}

	public void actionPerformed(ActionEvent e)
	{
	    if(running)
	    {
	    	move();
	    	checkApple();
	    	checkCollisions();
	    }
	    repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyCode())
			{
			   case KeyEvent.VK_LEFT :
				   if(direction != 'R')
				   {
					   direction = 'L';
				   }
				   break;
			   case KeyEvent.VK_RIGHT :
				   if(direction != 'L')
				   {
					   direction = 'R';
				   }
				   break;
			   case KeyEvent.VK_DOWN :
				   if(direction != 'U')
				   {
					   direction = 'D';
				   }
				   break;
			   case KeyEvent.VK_UP :
				   if(direction != 'D')
				   {
					   direction = 'U';
				   }
				   break;
			}
		}
	}

}

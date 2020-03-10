import java.awt.Graphics;
import java.awt.Image;

public class Sprite2D {
	protected double x,y;
	protected Image myImage;
	protected Image myImage2;
	protected int framesDrawn;
	public boolean isAlive = true;
	
	
	protected static int winWidth;
	
	public Sprite2D(Image i, Image i2) {
		myImage = i;
		myImage2 = i2;
		framesDrawn = 0;
	}
	
	public void setPosition(double xx, double yy) {
		x = xx;
		y = yy;
	}
	
	public void paint(Graphics g) {
		framesDrawn++;
		if( framesDrawn%100<50)
			g.drawImage(myImage, (int)x, (int)y,null);
		else
			g.drawImage(myImage2, (int)x, (int)y,null);
	}
	
	public static void setWinWidth(int w) {
		winWidth = w;
	}
}

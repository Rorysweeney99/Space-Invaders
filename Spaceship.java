import java.awt.Image;

public class Spaceship extends Sprite2D {
	private double xSpeed =0;
	private Image bulletImage;

	public Spaceship(Image i,Image bullet) {
		super(i,i);
		bulletImage = bullet;
	}
	
	public void setXSpeed(double dx) {
		xSpeed =dx;
	}
	
	public void move() {
		x += xSpeed;
		
		if(x<=0) {
			x=0;
			xSpeed = 0;
		}else if (x>=winWidth-myImage.getWidth(null)) {
			x=winWidth-myImage.getWidth(null);
			xSpeed = 0;
		}
	}
	
	public PlayerBullet shootBullet() {
		PlayerBullet b = new PlayerBullet(bulletImage);
		b.setPosition(this.x+54/2, this.y);
		return b;
	}
}

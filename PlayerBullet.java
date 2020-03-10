import java.awt.Image;

public class PlayerBullet extends Sprite2D{

	
	public PlayerBullet(Image i) {
		super(i,i);
	}

	public boolean move() {	
		y = y-10;
		if(y<=0) {
			return false;
		}else
			return true;
	}
	
}

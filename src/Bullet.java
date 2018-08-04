import java.awt.Rectangle;

public class Bullet {

	private double posX;
	private double posY;
	private double width;
	private double height;
	private double directionX;
	private double directionY;
	private int bounce;
	private boolean isAccelerating;
	
	public Bullet(int w, int h, double x, double y, double dX, double dY, int b, boolean accel) {
		posX = x;
		posY = y;
		width = w;
		height = h;
		isAccelerating = accel;
		directionX = dX;
		directionY = dY;
		bounce = b;
		
	}
	
	public double getPosX() {
		return posX;
	}
	
	public void setX(double x) {
			posX = x;
	}
	
	public double getPosY() {
		return posY;
	}
	
	public void setY(double y) {
			posY = y;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double d) {
		width = d;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double d) {
		height = d;
	}
	
	public double getDirectionX() {
		return directionX;
	}
	public void setDirectionX(double x) {
		directionX = x;
	}
	
	public double getDirectionY() {
		return directionY;
	}
	public void setDirectionY(double y) {
		directionY = y;
	}
	
	public int getBounce() {
		return bounce;
	}
	public void setBounce(int b) {
		bounce = b;
	}
	public boolean isAccelerating() {
		return isAccelerating;
	}
	public Rectangle getBounds() {
		return new Rectangle((int)(posX+(width/2)), (int)(posY+(height/2)), (int)(width), (int)(height));
	}
	public Rectangle getBossBounds() {
		return new Rectangle((int)(posX), (int)(posY), (int)(width), (int)(height));
	}
	
}

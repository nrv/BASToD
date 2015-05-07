package name.herve.bastod.tools.math;

public class Point {
	private int x;
	private int y;

	public Point() {
		super();
	}

	public Point(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Pt [" + x + ", " + y + "]";
	}
}

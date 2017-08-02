package xsh.raindrops.struct.dom.org.w3c.dom;


public abstract class AbstractPdf2XmlNode {
	
	protected int width;
	
	protected int height;

	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "AbstractPdf2XmlNode [width=" + width + ", height=" + height + "]";
	}
	
	
	
}

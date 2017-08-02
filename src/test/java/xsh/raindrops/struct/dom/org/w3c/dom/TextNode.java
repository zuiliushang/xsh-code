package xsh.raindrops.struct.dom.org.w3c.dom;

public class TextNode extends AbstractPdf2XmlNode {

	private String value;

	protected int pageIndex;
	
	protected int top;

	protected int left;

	public String getValue() {
		return value;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	@Override
	public String toString() {
		return "TextNode [pageIndex="+pageIndex+", value=" + value + ", top=" + top + ", left=" + left + ", width=" + width + ",height=" + height +"]";
	}

}

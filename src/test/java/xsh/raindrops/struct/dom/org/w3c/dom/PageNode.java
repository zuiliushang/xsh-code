package xsh.raindrops.struct.dom.org.w3c.dom;

import java.util.List;

public class PageNode extends AbstractPdf2XmlNode{
	
	private Integer number;

	List<TextNode> textNodes;
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<TextNode> getTextNodes() {
		return textNodes;
	}

	public void setTextNodes(List<TextNode> textNodes) {
		this.textNodes = textNodes;
	}

	@Override
	public String toString() {
		return "PageNode [number=" + number + ", textNodes=" + textNodes + "]";
	}
	
}

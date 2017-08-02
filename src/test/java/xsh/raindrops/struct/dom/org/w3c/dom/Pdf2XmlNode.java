package xsh.raindrops.struct.dom.org.w3c.dom;

import java.util.List;

public class Pdf2XmlNode{
	
	private List<PageNode> pageNodes;

	public List<PageNode> getPageNodes() {
		return pageNodes;
	}

	public void setPageNodes(List<PageNode> pageNodes) {
		this.pageNodes = pageNodes;
	}

	@Override
	public String toString() {
		return "Pdf2XmlNode [pageNodes=" + pageNodes + "]";
	}
	
	
	
}

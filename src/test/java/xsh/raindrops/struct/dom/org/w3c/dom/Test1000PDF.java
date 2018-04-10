package xsh.raindrops.struct.dom.org.w3c.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.druid.util.StringUtils;

public class Test1000PDF {

	@Test
	public void test01() throws Exception {
		//Paths.get("E:\\feellike21\\体检报告1000\\temp");
		String foldPath = "E:\\feellike21\\体检报告1000\\temp\\";
		String pythonPath = "C:\\Python27\\Scripts\\pdf2xml.py";
		File file = new File(foldPath);
		Process proc;
		System.out.println(file.listFiles().length);
		File[] files = file.listFiles();
		for (int i =0 ;i<files.length;i++) {
			String filename = files[i].getName();
			filename = FilenameUtils.getBaseName(filename);
			if (System.getProperty("os.name").startsWith("Window")) {
				System.out.println("cmd /c python "+pythonPath+" "+foldPath+filename+".pdf > "+foldPath+"xml\\"+filename+".xml");
				proc = Runtime.getRuntime().exec("cmd /c python "+pythonPath+" "+foldPath+filename+".pdf > "+foldPath+"\\xml\\"+filename+".xml");
				proc.waitFor();
			}
		}
	}
	
	@Test
	public void test03() throws Exception {
		String foldPath = "E:\\feellike21\\体检报告1000\\嘉仁体检";
		String pythonPath = "C:\\Python27\\Scripts\\pdf2xml.py";
		File file = new File(foldPath);
		Process proc;
		System.out.println(file.listFiles().length);
		File[] files = file.listFiles();
		for (int i =0 ;i<files.length;i++) {
			String filename = files[i].getName();
			filename = FilenameUtils.getBaseName(filename);
			if (System.getProperty("os.name").startsWith("Window")) {
				System.out.println("cmd /c python "+pythonPath+" "+foldPath+filename+".pdf > "+foldPath+"xml\\"+filename+".xml");
				proc = Runtime.getRuntime().exec("cmd /c python "+pythonPath+" "+foldPath+filename+".pdf > "+foldPath+"\\xml\\"+filename+".xml");
				proc.waitFor();
			}
		}
	}
	
	@Test
	public void testsave() throws Exception {
		String foldPath = "E:\\feellike21\\体检报告1000\\temp\\";
		String pythonPath = "C:\\Python27\\Scripts\\pdf2xml.py";
		File file = new File(foldPath);
		Process proc;
		System.out.println(file.listFiles().length);
		File[] files = file.listFiles();
			String filename = "59AA14E1FA3B00A6E053C0A8005C52B8";//files[i].getName();
			//filename = FilenameUtils.getBaseName(filename);
			if (System.getProperty("os.name").startsWith("Window")) {
				System.out.println("cmd /c python "+pythonPath+" "+foldPath+filename+".pdf > "+foldPath+"xml\\"+filename+".xml");
				proc = Runtime.getRuntime().exec("cmd /c python "+pythonPath+" "+foldPath+filename+".pdf > "+foldPath+"xml\\"+filename+".xml");
				proc.waitFor();
			}
	}
	
	@Test
	public void test02() {
		String foldPath = "E:\\feellike21\\体检报告1000\\temp\\xml\\";
		File folder = new File(foldPath);
		File[] xmls = folder.listFiles();
		String hansi = "E:\\feellike21\\体检报告1000\\瀚思维康\\";
		Stream.of(xmls).parallel().forEach((xml)->{
			try {
				InputStream inputStream =new FileInputStream(xml);
				Document document = XmlUtil.getDocument(inputStream);
				NodeList nodeList = document.getElementsByTagName("page");
				// 定义双向链表存储
				LinkedList<TextNode> textNodes = new LinkedList<>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element pNode = (Element) nodeList.item(i);
					NodeList pageNodeList = pNode.getElementsByTagName("text");
					for (int j = 0; j < pageNodeList.getLength(); j++) {// 遍历每个page下的所有text节点
						Element node = (Element) pageNodeList.item(j);
						TextNode textNode = new TextNode();
						textNode.setHeight(new Integer(node.getAttribute("height")));
						textNode.setLeft(new Integer(node.getAttribute("left")));
						textNode.setTop(new Integer(node.getAttribute("top")));
						textNode.setWidth(new Integer(node.getAttribute("width")));
						textNode.setValue(node.getFirstChild().getNodeValue().trim());
						textNode.setPageIndex(new Integer(pNode.getAttribute("number")));
						textNodes.add(textNode);
						if(textNode.getPageIndex()!=1) {
							break;
						}
					}
					Iterator<TextNode> listIterator = textNodes.iterator();
					while (listIterator.hasNext()) {
						String saveFold = null;
						TextNode textNode = listIterator.next();
						if (textNode.getValue().contains("瀚思维康")) {
							saveFold = hansi;
						}
						if (textNode.getValue().replace(" ", "").contains("光华健康")) {
							saveFold = "E:\\feellike21\\体检报告1000\\光华健康\\";
						}
						if (textNode.getValue().contains("宽裕情怀")) {
							saveFold = "E:\\feellike21\\体检报告1000\\北京裕和中西医综合康复医院\\";
						}//E:\\feellike21\\体检报告1000\\慈铭\\
						if(textNode.getValue().contains("北京松乔门诊部")) {
							saveFold = "E:\\feellike21\\体检报告1000\\北京松乔门诊部\\";
						}
						if(textNode.getValue().contains("北京维特奥医院")) {
							saveFold = "E:\\feellike21\\体检报告1000\\北京维特奥医院\\";
						}
						if(textNode.getValue().contains("美年")) {
							saveFold = "E:\\feellike21\\体检报告1000\\美年\\";
						}
						if(textNode.getValue().contains("青岛市南门诊部")) {
							saveFold = "E:\\feellike21\\体检报告1000\\慈铭\\";
						}
						if(textNode.getValue().contains("天津二分")) {
							saveFold = "E:\\feellike21\\体检报告1000\\慈铭天津二分院\\";
						}
						if (!StringUtils.isEmpty(saveFold)) {
							String filename = FilenameUtils.getBaseName(xml.getName());
							String source = "E:\\feellike21\\体检报告1000\\temp\\"+filename+".pdf";
							OutputStream out =  new FileOutputStream(new File(saveFold+filename+".pdf"));
							Files.copy(new File(source).toPath(), out);
							File file = new File("E:\\feellike21\\体检报告1000\\okxml\\"+xml.getName());
							Files.move(xml.toPath(), file.toPath() , StandardCopyOption.REPLACE_EXISTING);
						}
						
					}
				}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
}

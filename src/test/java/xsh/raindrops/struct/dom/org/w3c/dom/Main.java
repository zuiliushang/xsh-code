package xsh.raindrops.struct.dom.org.w3c.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.ImmutableRangeMap.Builder;
import com.google.common.collect.Range;

/**
 * 
 * 有可能过页 那么与page无关 页头页尾可以剪去
 * 
 * 一格内容 left + width top + height
 * 
 * 
 * 
 * top left---------------------------- height| | | | |
 * ---------------------------- width
 * 
 * ----------------------------- | | | | | --------------------- | | ---------
 * 
 * ----------------------------- | | | | | ---------| |------------ | | --------
 * 
 * 
 */
public class Main {

	@Test
	public void test01() {
		try {
			// 把要解析的xml文档读入DOM解析器
			InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("school.xml");
			Document doc = XmlUtil.getDocument(inputStream);
			System.out.println("处理该文档的DomImplementation对象  = " + doc.getImplementation());
			// 得到文档名称为Student的元素的节点列表
			NodeList nList = doc.getElementsByTagName("Student");
			// 遍历该集合，显示结合中的元素及其子元素的名字
			for (int i = 0; i < nList.getLength(); i++) {
				Element node = (Element) nList.item(i);
				System.out.println("Name: " + node.getElementsByTagName("Name").item(0).getFirstChild().getNodeValue());
				System.out.println("Num: " + node.getElementsByTagName("Num").item(0).getFirstChild().getNodeValue());
				System.out.println(
						"Classes: " + node.getElementsByTagName("Classes").item(0).getFirstChild().getNodeValue());
				System.out.println(
						"Address: " + node.getElementsByTagName("Address").item(0).getFirstChild().getNodeValue());
				System.out.println("Tel: " + node.getElementsByTagName("Tel").item(0).getFirstChild().getNodeValue());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 双向链表尝试
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	@Test
	public void test02() throws ParserConfigurationException, SAXException, IOException {
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("test.xml");
		Document document = XmlUtil.getDocument(inputStream);
		NodeList nodeList = document.getElementsByTagName("page");
		// 定义双向链表存储
		LinkedList<TextNode> textNodes = new LinkedList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {// 遍历所有page节点
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
			}
		}
		ListIterator<TextNode> listIterator = textNodes.listIterator();
		int index = 0;// 记录迭代器位置
		List<Dictionary> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			TextNode nextNode = listIterator.next();// 获取下个节点
			if (nextNode.getValue().equals("项目名称")) {// 从项目名称开始
				// 往前获取科室项 1):记录当前迭代器位置. 2):往前找寻找科室项 3):恢复当前位置
				String departName;
				// TODO: pdf的科室必须单独一行
				if (listIterator.hasPrevious()) {// 必须是前面有科室才获取
					index = listIterator.nextIndex();// 记录状态
					TextNode firstPreNode = listIterator.previous();
					departName = "";
					if (listIterator.hasPrevious()) {// 如果前面还有
						// 继续往上获取node节点
						while (listIterator.hasPrevious()) {
							TextNode secondPreNode = listIterator.previous();
							if (firstPreNode.getLeft() == secondPreNode.getLeft()) {
								// 如果 两个节点在同一 left 那么是名称太长导致换行
								// 所以要把它拼接在前面
								// 虽然会多创建String对象 但是一般不会很多 所以用 + 是最好选择
								departName = secondPreNode.getValue() + departName;
								continue;
							}
							break;// 其他情况退出
						}
					}
					// 重置迭代器位置
					listIterator = textNodes.listIterator(index-1);//项目名称都包含进来
					/////////////////////////////////////////////////////////////////////////////////
					// 寻找科室结束
					/////////////////////////////////////////////////////////////////////////////////
					// 往后寻找体检项
					// 不是小结的内容 存在List里面使用 迭代器 进行筛选
					List<TextNode> tmpResultNodes = new ArrayList<>();
					// 小结同一行的筛选掉
					while (listIterator.hasNext()) {
						TextNode maybeEndPointNode = listIterator.next();
						// 往后寻找找到项目名称
						if (maybeEndPointNode.getValue().startsWith("项目名称")) {
							// 如果和项目名称同行的 那么直接next掉
							boolean key = true;
							if (listIterator.hasNext()) {
								TextNode tmpValidateNode = listIterator.next();
								while (key) {
									key = false;
									if (tmpValidateNode.getPageIndex() == maybeEndPointNode.getPageIndex()) {// 同一页
										if (tmpValidateNode.getTop() >= maybeEndPointNode.getTop()) {//同一行
											if (tmpValidateNode.getTop() <= (maybeEndPointNode.getTop()
													+ maybeEndPointNode.getHeight())) {
												key = true;
												tmpValidateNode = listIterator.next();//同一行过滤(next掉)
											}
										}
									}
								}
							}
							if (key==false) {
								listIterator.previous();//把多next掉的pre回来
							}
							continue;//已经找到了项目名称 并不需要往下走
						}
						// 往后寻找直到找到 "小结"
						if (maybeEndPointNode.getValue().startsWith("小结")) {
							// TODO: 小结在这里往下遍历可以取到完整小结 暂时不做小结业务

							break;
						}
						// 页头页尾不捣乱
						if (maybeEndPointNode.getTop() > 50 && maybeEndPointNode.getTop() < 820) {
							tmpResultNodes.add(maybeEndPointNode);
						}

					}
					// 进行分组筛选
					// 都有
					// 分页
					// 局部有
					List<List<TextNode>> rangeLists = new ArrayList<>();
					List<TextNode> rangeList = new ArrayList<>();
					TextNode headNode = null;//从第一个开始
					int top = 0;
					int bottom = 0;
					Iterator<TextNode> iterator = tmpResultNodes.iterator();
					while (iterator.hasNext()) {
						TextNode n = iterator.next();//下一个
						// 分行
						if (rangeList.size() == 0) {// 为空 开始一行
							headNode = n;//每行初始化
							top = headNode.getTop();//每行初始化
							bottom = headNode.getTop() + headNode.getHeight();//每行初始化
							rangeList.add(headNode);//加入队列头部中
						} else {//不是第一个
							if (bottom >= n.getTop() && n.getTop() >= top
									&& headNode.getPageIndex() == n.getPageIndex()) {// 与头部headNode同一行(上下在同一行
								rangeList.add(n);
								//最后一行
								if (rangeList.size()>1&&!iterator.hasNext()) {
									rangeLists.add(rangeList);//结束 
								}
							} else {//不同行
								//如果没有下一个 那么这个属性单独作为一行
								headNode = n;
								top = headNode.getTop();
								bottom = headNode.getTop() + headNode.getHeight();
								if (!iterator.hasNext()) {// 最后一行
									rangeLists.add(rangeList);//消化前一行
									rangeLists.add(Arrays.asList(headNode));
								}else {//如果有下一个 
									rangeLists.add(rangeList);
									rangeList = new ArrayList<>();// 空
									rangeList.add(headNode);
								}
							}
						}
					}
					//开始遍历每一行
					Dictionary dictionary = null;
					Iterator<List<TextNode>> nodeListIterator = rangeLists.iterator();
					while (nodeListIterator.hasNext()) {
						List<TextNode> list = nodeListIterator.next();
						//每一行根据left进行排序 保证 项目名称 检查结果 参考结果 提示 单位 顺序排序 
						list.sort(Comparator.comparing(TextNode::getLeft));
						if (list.size()>1) {
							if (dictionary!=null) {//不为空就存起来
								dictionaries.add(dictionary);
							}
							dictionary = new Dictionary();
							dictionary.setDepartName(departName);
							dictionary.setDicName(list.get(0).getValue());
							
							Optional<TextNode> optional = list.stream().filter(t->{return (t.getLeft()>=175&&t.getLeft()<=195);}).findFirst();
							if (optional.isPresent()) {//英文名
								dictionary.setEnName(optional.get().getValue());
							}
							optional = list.stream().filter(t->{return (t.getLeft()>=234&&t.getLeft()<=259);}).findFirst();
							if (optional.isPresent()) {//检查结果
								dictionary.setDicResult(optional.get().getValue());
							}
							optional = list.stream().filter(t->{return (t.getLeft()>=380&&t.getLeft()<=400);}).findFirst();
							if (optional.isPresent()) {//提示
								dictionary.setTip(optional.get().getValue());
							}
							optional = list.stream().filter(t->{return (t.getLeft()>=420&&t.getLeft()<=490);}).findFirst();
							if (optional.isPresent()) {//参考结果
								dictionary.setDicExplain(optional.get().getValue());
							}
							optional = list.stream().filter(t->{return (t.getLeft()>=521);}).findFirst();
							if (optional.isPresent()) {//单位
								dictionary.setDicUnit(optional.get().getValue());
							}
							//最后一行 有可能没有下一个
							if (!nodeListIterator.hasNext()) {
								dictionaries.add(dictionary);
							}
						}else if (list.size()==1) {//只有一个大小 那么拼接在对应的字段后面
							TextNode finalNode = list.get(0);
							int left = list.get(0).getLeft();
							if (left>=175&&left<=195) {//英文
								dictionary.setEnName(dictionary.getEnName()+finalNode.getValue());
							}else if (left>=234 && left <=259) {
								dictionary.setDicResult(dictionary.getDicResult()+finalNode.getValue());
							}else if (left>=380 && left <= 400) {
								dictionary.setTip(dictionary.getTip()+finalNode.getValue());
							}else if (left>=420 && left <= 490){
								dictionary.setDicExplain(dictionary.getDicExplain()+finalNode.getValue());
							}else if (left >= 521) {
								dictionary.setDicUnit(dictionary.getDicUnit()+finalNode.getValue());
							}
							if (!nodeListIterator.hasNext()) {//如果没有下一行了 那么存起来
								dictionaries.add(dictionary);
							}
						}
					}
				}
			}
		}
		dictionaries.forEach(System.out::println);//得到结果
		System.out.println(dictionaries.size());
	}

	@Test
	public void test03() throws ParserConfigurationException, SAXException, IOException {
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("meinian2.xml");
		Document document = XmlUtil.getDocument(inputStream);
		NodeList nodeList = document.getElementsByTagName("page");
		// 定义双向链表存储
		LinkedList<TextNode> textNodes = new LinkedList<>();
		for (int i = 0; i < nodeList.getLength(); i++) {// 遍历所有page节点
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
			}
		}
		ListIterator<TextNode> listIterator = textNodes.listIterator();
		int index = 0;// 记录迭代器位置
		String departName = null;//科室
		int top = 0;
		int page = 0;
		List<Dictionary> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			TextNode textNode = listIterator.next();
			if (textNode.getValue().startsWith("检查项目名称")) {//读取直到读取到检查项目名称
				//记录指针位置
				index = listIterator.nextIndex();
				//往上查找直到找到科室
				//科室在 左上角 left = 66 左右的位置
				while (listIterator.hasPrevious()) {
					TextNode preNode = listIterator.previous();
					if (preNode.getLeft()<=67&&preNode.getLeft()>=65) {//找到了
						departName = preNode.getValue();//获取科室
						//恢复位置
						listIterator = textNodes.listIterator(index);
						top = textNode.getTop();
						page = textNode.getPageIndex();
						break;//终止循环
					}
				}
				while (textNode.getTop()==top&&listIterator.hasNext()) {//和检查项目名称同行
					textNode = listIterator.next();//next掉
				}
				//最后一个pre回来
				listIterator.previous();
				// 开始筛选要的数据
				List<TextNode> dataNodes = new ArrayList<>();
				while (listIterator.hasNext()) {
					TextNode tmpNode = listIterator.next();
					if (tmpNode.getValue().startsWith("小结")) {//如果有小结 那么跳出
						break;
					}
					if (tmpNode.getLeft()==66) {//没有小结 判断下一项
						break;
					}
					dataNodes.add(tmpNode);
				}
				// 筛选了要的数据 开始归类
				// System.out.println(dataNodes);
				// 进行分组筛选
				// 都有
				// 分页
				// 局部有
				List<List<TextNode>> rangeLists = new ArrayList<>();
				List<TextNode> rangeList = new ArrayList<>();
				TextNode headNode = null;//从第一个开始
				top = 0;
				int bottom = 0;
				Iterator<TextNode> iterator = dataNodes.iterator();
				while (iterator.hasNext()) {
					TextNode n = iterator.next();//下一个
					// 分行
					if (rangeList.size() == 0) {// 为空 开始一行
						headNode = n;//每行初始化
						top = headNode.getTop();//每行初始化
						bottom = headNode.getTop() + headNode.getHeight();//每行初始化
						rangeList.add(headNode);//加入队列头部中
					} else {//不是第一个
						if (bottom >= n.getTop() && n.getTop() >= top
								&& headNode.getPageIndex() == n.getPageIndex()) {// 与头部headNode同一行(上下在同一行
							rangeList.add(n);
							//最后一行
							if (rangeList.size()>1&&!iterator.hasNext()) {
								rangeLists.add(rangeList);//结束 
							}
						} else {//不同行
							//如果没有下一个 那么这个属性单独作为一行
							headNode = n;
							top = headNode.getTop();
							bottom = headNode.getTop() + headNode.getHeight();
							if (!iterator.hasNext()) {// 最后一行
								rangeLists.add(rangeList);//消化前一行
								rangeLists.add(Arrays.asList(headNode));
							}else {//如果有下一个 
								rangeLists.add(rangeList);
								rangeList = new ArrayList<>();// 空
								rangeList.add(headNode);
							}
						}
					}
				}
				// rangeLists.forEach(System.out::println);
				//开始遍历每一行
				Dictionary dictionary = null;
				Iterator<List<TextNode>> nodeListIterator = rangeLists.iterator();
				while (nodeListIterator.hasNext()) {
					List<TextNode> list = nodeListIterator.next();
					//每一行根据left进行排序 保证 项目名称 检查结果 参考结果 提示 单位 顺序排序 
					list.sort(Comparator.comparing(TextNode::getLeft));
					if (list.size()>0) {
						if (list.size()==1 && list.get(0).getLeft()>=100) {
							continue;
						}
						if (dictionary!=null) {//不为空就存起来
							dictionaries.add(dictionary);
						}
						dictionary = new Dictionary();
						dictionary.setDepartName(departName);
						dictionary.setDicName(list.get(0).getValue());
						Optional<TextNode> optional = list.stream().filter(t->{return (t.getLeft()>=200&&t.getLeft()<=220);}).findFirst();
						if (optional.isPresent()) {//检查结果
							dictionary.setDicResult(optional.get().getValue());
						}
						/*optional = list.stream().filter(t->{return (t.getLeft()>=380&&t.getLeft()<=400);}).findFirst();
						if (optional.isPresent()) {//提示
							dictionary.setTip(optional.get().getValue());
						}*/
						optional = list.stream().filter(t->{return (t.getLeft()>=485);}).findFirst();
						if (optional.isPresent()) {//参考结果
							dictionary.setDicExplain(optional.get().getValue());
						}
						optional = list.stream().filter(t->{return (t.getLeft()>=375&&t.getLeft()<=385);}).findFirst();
						if (optional.isPresent()) {//单位
							dictionary.setDicUnit(optional.get().getValue());
						}
						//最后一行 有可能没有下一个
						if (!nodeListIterator.hasNext()) {
							dictionaries.add(dictionary);
						}
					}/*else if (list.size()==1) {//只有一个大小 那么拼接在对应的字段后面
						if (dictionary!=null) {//不为空就存起来
							dictionaries.add(dictionary);
						}
						dictionary = new Dictionary();
						dictionary.setDepartName(departName);
						dictionary.setDicName(list.get(0).getValue());
						Optional<TextNode> optional = list.stream().filter(t->{return (t.getLeft()>=200&&t.getLeft()<=220);}).findFirst();
						if (optional.isPresent()) {//检查结果
							dictionary.setDicResult(optional.get().getValue());
						}
						optional = list.stream().filter(t->{return (t.getLeft()>=380&&t.getLeft()<=400);}).findFirst();
						if (optional.isPresent()) {//提示
							dictionary.setTip(optional.get().getValue());
						}
						optional = list.stream().filter(t->{return (t.getLeft()>=485);}).findFirst();
						if (optional.isPresent()) {//参考结果
							dictionary.setDicExplain(optional.get().getValue());
						}
						optional = list.stream().filter(t->{return (t.getLeft()>=375&&t.getLeft()<=385);}).findFirst();
						if (optional.isPresent()) {//单位
							dictionary.setDicUnit(optional.get().getValue());
						}
						//最后一行 有可能没有下一个
						if (!nodeListIterator.hasNext()) {
							dictionaries.add(dictionary);
						}
					}*/
				}
			}
		}
		dictionaries.forEach(System.out::println);
		System.out.println(dictionaries.size());
	}
	
	@Test
	public void test05() {
		
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("test.xml");
		Document document = XmlUtil.getDocument(inputStream);
		// System.out.println(document);
		NodeList nodeList = document.getElementsByTagName("page");

		// 定义整个pdf2Xml节点对象
		Pdf2XmlNode pdf2XmlNode = new Pdf2XmlNode();
		// 所有page节点
		List<PageNode> pageNodes = new ArrayList<>();

		for (int i = 0; i < nodeList.getLength(); i++) {// 遍历所有page节点
			Element pNode = (Element) nodeList.item(i);
			PageNode pageNode = new PageNode();
			// 获取属性
			pageNode.setNumber(new Integer(pNode.getAttribute("number")));
			pageNode.setHeight(new Integer(pNode.getAttribute("height")));
			pageNode.setWidth(new Integer(pNode.getAttribute("width")));
			NodeList pageNodeList = pNode.getElementsByTagName("text");
			List<TextNode> textNodes = new ArrayList<>();
			for (int j = 0; j < pageNodeList.getLength(); j++) {// 遍历每个page下的所有text节点
				// Element node = (Element)pageNodeList.item(j);
				Element node = (Element) pageNodeList.item(j);
				TextNode textNode = new TextNode();
				textNode.setHeight(new Integer(node.getAttribute("height")));
				textNode.setLeft(new Integer(node.getAttribute("left")));
				textNode.setTop(new Integer(node.getAttribute("top")));
				textNode.setWidth(new Integer(node.getAttribute("width")));
				textNode.setValue(node.getFirstChild().getNodeValue());
				/*
				 * System.out.print(node.getFirstChild().getNodeValue());
				 * System.out.print("  top: "+
				 * node.getAttributeNode("top").getValue());
				 * System.out.print("  left: "+
				 * node.getAttributeNode("left").getValue());
				 * System.out.print("  width: "+
				 * node.getAttributeNode("width").getValue());
				 * System.out.println("  height: "+
				 * node.getAttributeNode("height").getValue());
				 */
				// System.out.print(XmlUtil.loadAttribute(node));
				// System.out.println(node.getAttribute("top"));
				// System.out.println(",value="+node.getFirstChild().getNodeValue());
				textNodes.add(textNode);
			}
			pageNode.setTextNodes(textNodes);
			pageNodes.add(pageNode);
		}
		pdf2XmlNode.setPageNodes(pageNodes);

		System.out.println(pdf2XmlNode);

		/*
		 * 
		 * 解析完成整个 xml 文本对象 Pdf2XmlNode --- PageNode (List) ---number int页面id
		 * ---width ---height ---TextNode (List) ---top ---left ---width
		 * ---height ---value
		 */

		// 循环遍历
		// LinkedList<TextNode> paNodes = new LinkedList<>();
		// paNodes.
		// paNodes.listIterator();

		pdf2XmlNode.getPageNodes().forEach(p -> {
			if (p.getNumber() > 2) {// 前两页不读
				p.getTextNodes().forEach(t -> {
					if (t.getTop() > 40 && t.getTop() < 820) {// 排除页头页尾

					}
				});
			}
		});
	}
	

}

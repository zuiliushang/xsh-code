package xsh.raindrops.struct.dom.org.w3c.dom;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Text;

import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;

import xsh.raindrops.project.util.DateStyle;
import xsh.raindrops.project.util.DateUtil;

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
		ReportResultData reportResultData = new ReportResultData();
		SourceUser sourceUser = new SourceUser();
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
		List<SourceDictionaryDTO> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			TextNode nextNode = listIterator.next();// 获取下个节点
			// 从第一页获取个人信息
			// 从第一页找用户数据
			if (nextNode.getValue().equals("健康体检报告")) {
				TextNode tmpNode = null;
				List<TextNode> tmpArray = null;
				while (listIterator.hasNext() && nextNode.getPageIndex() == 1) {
					nextNode = listIterator.next();
					if (tmpNode != null && (tmpNode.getTop() + tmpNode.getHeight()) >= nextNode.getTop()
							&& tmpNode.getTop() <= nextNode.getTop()) {
						tmpArray.add(nextNode);
					} else {
						if (!CollectionUtils.isEmpty(tmpArray)) {
							tmpArray.sort(Comparator.comparing(TextNode::getLeft));
							if (tmpArray.size() >= 2) {
								TextNode itemTitle = tmpArray.get(0);
								String itemStr = itemTitle.getValue().replaceAll(" ", "");
								if (itemStr.startsWith("体检编号")) {
									reportResultData.setReportCode(tmpArray.get(1).getValue());
								} else if (itemStr.startsWith("姓名")) {
									sourceUser.setName(tmpArray.get(1).getValue());
								} else if (itemStr.startsWith("性别")) {
									sourceUser.setGender(tmpArray.get(1).getValue());
								} else if (itemStr.startsWith("单位编号")) {
									reportResultData.setInstitutionCode(tmpArray.get(1).getValue());
								} else if (itemStr.startsWith("体检日期")) {
									reportResultData.setExamTime(DateUtil.StringToDate(tmpArray.get(1).getValue(),
											DateStyle.YYYYMMdd_Cn.getValue()));
								}
								/*
								 * if (tmpArray.size() >= 4) { if (tmpArray.get(2).getValue().replaceAll(" ",
								 * "").startsWith("年龄")) { sourceUser.set } }
								 */
							}
						}
						tmpArray = new ArrayList<>();
						tmpNode = nextNode;
						tmpArray.add(tmpNode);
					}
				}
			}
			// 获取小结
			StringBuffer sgb = new StringBuffer();
			while (nextNode.getValue().startsWith("体检小结") && listIterator.hasNext()) {
				TextNode sugNode = listIterator.next();
				// 页头页尾不捣乱
				if (sugNode.getTop() > 50 && sugNode.getTop() < 820) {
					if (sugNode.getLeft() != 86) {
						reportResultData.setSummary(sgb.toString());
						listIterator.previous();
						break;
					}
					sgb.append(sugNode.getValue());
				}
			}

			// 获取体检建议
			sgb = new StringBuffer();
			while (nextNode.getValue().startsWith("总检建议") && listIterator.hasNext()) {
				TextNode sugNode = listIterator.next();
				// 页头页尾不捣乱
				if (sugNode.getTop() > 50 && sugNode.getTop() < 820) {
					if (sugNode.getLeft() != 86) {
						reportResultData.setSuggest(sgb.toString());
						listIterator.previous();
						break;
					}
					sgb.append(sugNode.getValue());
				}
			}

			if (nextNode.getValue().equals("项目名称")) {// 从项目名称开始
				// 往前获取科室项 1):记录当前迭代器位置. 2):往前找寻找科室项 3):恢复当前位置
				String departName = null;
				String doctor = null;
				// TODO: pdf的科室必须单独一行
				if (listIterator.hasPrevious()) {// 必须是前面有科室才获取
					index = listIterator.nextIndex();// 记录状态
					TextNode firstPreNode = listIterator.previous();
					if (listIterator.hasPrevious()) {// 如果前面还有
						// 继续往上获取node节点 TODO: 如果前面没有科室 那么需要修改逻辑(现在默认是都有科室)
						while (listIterator.hasPrevious()) {
							TextNode secondPreNode = listIterator.previous();
							if (firstPreNode.getLeft() == secondPreNode.getLeft()) {
								// 如果 两个节点在同一 left 那么是名称太长导致换行
								// 所以要把它拼接在前面
								// 虽然会多创建String对象 但是一般不会很多 所以用 + 是最好选择
								if (StringUtils.isEmpty(departName)) {
									departName = secondPreNode.getValue();
								}else {
									departName = secondPreNode.getValue() + departName;
								}
								 
								// 将firstNode重置
								firstPreNode = secondPreNode;
								continue;
							}
							break;// 其他情况退出
						}

					}
					// 往前找3项
					int preIndex = 0;
					while (listIterator.hasPrevious() && preIndex < 3) {
						preIndex++;
						TextNode tmpNode = listIterator.previous();
						if (tmpNode.getLeft() == 128) {
							doctor = tmpNode.getValue();
						}

					}
					// 重置迭代器位置
					listIterator = textNodes.listIterator(index - 1);// 项目名称都包含进来
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
							// 如果和项目名称同行的 那么直接next掉(剔除掉垃圾多余信息)
							boolean key = true;
							if (listIterator.hasNext()) {
								TextNode tmpValidateNode = listIterator.next();
								while (key) {
									key = false;
									if (tmpValidateNode.getPageIndex() == maybeEndPointNode.getPageIndex()) {// 同一页
										if (tmpValidateNode.getTop() >= maybeEndPointNode.getTop()) {// 同一行
											if (tmpValidateNode.getTop() <= (maybeEndPointNode.getTop()
													+ maybeEndPointNode.getHeight())) {
												key = true;
												tmpValidateNode = listIterator.next();// 同一行过滤(next掉)
											}
										}
									}
								}
							}
							if (key == false) {
								listIterator.previous();// 把多next掉的pre回来
							}
							continue;// 已经找到了项目名称 并不需要往下走
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
					TextNode headNode = null;// 从第一个开始
					int top = 0;
					int bottom = 0;
					Iterator<TextNode> iterator = tmpResultNodes.iterator();
					while (iterator.hasNext()) {
						TextNode n = iterator.next();// 下一个
						// 分行
						if (rangeList.size() == 0) {// 为空 开始一行
							headNode = n;// 每行初始化
							top = headNode.getTop();// 每行初始化
							bottom = headNode.getTop() + headNode.getHeight();// 每行初始化
							rangeList.add(headNode);// 加入队列头部中
						} else {// 不是第一个
							if (bottom >= n.getTop() && n.getTop() >= top
									&& headNode.getPageIndex() == n.getPageIndex()) {// 与头部headNode同一行(上下在同一行
								rangeList.add(n);
								// 最后一行
								if (rangeList.size() > 1 && !iterator.hasNext()) {
									rangeLists.add(rangeList);// 结束
								}
							} else {// 不同行
								// 如果没有下一个 那么这个属性单独作为一行
								headNode = n;
								top = headNode.getTop();
								bottom = headNode.getTop() + headNode.getHeight();
								if (!iterator.hasNext()) {// 最后一行
									rangeLists.add(rangeList);// 消化前一行
									rangeLists.add(Arrays.asList(headNode));
								} else {// 如果有下一个
									rangeLists.add(rangeList);
									rangeList = new ArrayList<>();// 空
									rangeList.add(headNode);
								}
							}
						}
					}
					// 开始遍历每一行
					SourceDictionaryDTO dictionary = null;
					Iterator<List<TextNode>> nodeListIterator = rangeLists.iterator();
					while (nodeListIterator.hasNext()) {
						List<TextNode> list = nodeListIterator.next();
						// 每一行根据left进行排序 保证 项目名称 检查结果 参考结果 提示 单位 顺序排序
						list.sort(Comparator.comparing(TextNode::getLeft));
						if (list.size() > 1) {
							if (dictionary != null) {// 不为空就存起来
								dictionaries.add(dictionary);
							}
							dictionary = new SourceDictionaryDTO();
							dictionary.setDepartName(departName);
							dictionary.setDoctor(doctor);
							dictionary.setDicName(list.get(0).getValue());

							Optional<TextNode> optional = list.stream().filter(t -> {
								return (t.getLeft() >= 175 && t.getLeft() <= 195);
							}).findFirst();
							if (optional.isPresent()) {// 英文名
								dictionary.setEnName(optional.get().getValue());
							}
							optional = list.stream().filter(t -> {
								return (t.getLeft() >= 234 && t.getLeft() <= 259);
							}).findFirst();
							if (optional.isPresent()) {// 检查结果
								dictionary.setDicResult(optional.get().getValue());
							}
							optional = list.stream().filter(t -> {
								return (t.getLeft() >= 380 && t.getLeft() <= 400);
							}).findFirst();
							if (optional.isPresent()) {// 提示
								dictionary.setTip(optional.get().getValue());
							}
							optional = list.stream().filter(t -> {
								return (t.getLeft() >= 420 && t.getLeft() <= 490);
							}).findFirst();
							if (optional.isPresent()) {// 参考结果
								dictionary.setDicExplain(optional.get().getValue());
							}
							optional = list.stream().filter(t -> {
								return (t.getLeft() >= 517);
							}).findFirst();
							if (optional.isPresent()) {// 单位
								dictionary.setDicUnit(optional.get().getValue());
							}
							// 最后一行 有可能没有下一个
							if (!nodeListIterator.hasNext()) {
								dictionaries.add(dictionary);
							}
						} else if (list.size() == 1) {// 只有一个大小 那么拼接在对应的字段后面
							TextNode finalNode = list.get(0);
							int left = list.get(0).getLeft();
							if (left >= 175 && left <= 195) {// 英文
								dictionary.setEnName(dictionary.getEnName() + finalNode.getValue());
							} else if (left >= 234 && left <= 259) {
								dictionary.setDicResult(dictionary.getDicResult() + finalNode.getValue());
							} else if (left >= 380 && left <= 400) {
								dictionary.setTip(dictionary.getTip() + finalNode.getValue());
							} else if (left >= 420 && left <= 490) {
								dictionary.setDicExplain(dictionary.getDicExplain() + finalNode.getValue());
							} else if (left >= 517) {
								dictionary.setDicUnit(dictionary.getDicUnit() + finalNode.getValue());
							}
							if (!nodeListIterator.hasNext()) {// 如果没有下一行了 那么存起来
								dictionaries.add(dictionary);
							}
						}
					}
				}
			}
		}
		dictionaries.forEach(System.out::println);// 得到结果
		System.out.println(dictionaries.size());
		reportResultData.setDictionaries(dictionaries);
		reportResultData.setSourceUser(sourceUser);
		
	}

	@Test
	public void test03() throws ParserConfigurationException, SAXException, IOException {
		ReportResultData reportResultData = new ReportResultData();
		SourceUser sourceUser = new SourceUser();
		
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("12222.xml");
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
		String doctor = null;
		int top = 0;
		int page = 0;
		SourceDictionaryDTO tmpDictionary = null;
		List<SourceDictionaryDTO> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			TextNode textNode = listIterator.next();
			//从第一页 获取用户信息
			while (listIterator.hasNext()&&textNode.pageIndex==1) {
				textNode = listIterator.next();
				if (textNode.getValue().contains("女")) {
					String tmpValue = textNode.getValue();
					String[] stringArr = tmpValue.split("女");
					String name = stringArr[0].trim();
					String gender = stringArr[1].trim();
					sourceUser.setName(name);//名字
					sourceUser.setGender("女"+gender);//性别
				}else if (textNode.getValue().contains("男")||(textNode.getValue().contains("先生"))) {
					String tmpValue = textNode.getValue();
					String[] stringArr = tmpValue.split("男");
					if (stringArr.length<2) {
						stringArr = tmpValue.split("先生");
					}
					String name = stringArr[0].trim();
					sourceUser.setName(name);//名字
					sourceUser.setGender("男");//性别
				}
				if (textNode.getValue().startsWith("身份证") && listIterator.hasNext()) {
					TextNode tmpNode = listIterator.next();
					if (tmpNode.getTop()==textNode.getTop()) {
						sourceUser.setIdentityNumber(tmpNode.getValue());//获取身份证
					}
				}
				if (textNode.getValue().startsWith("体检号") && listIterator.hasNext()) {
					TextNode tmpNode = listIterator.next();
					if (tmpNode.getTop()==textNode.getTop()) {
						reportResultData.setReportCode(tmpNode.getValue());
					}
				}
				if (textNode.getValue().startsWith("体检时间") && listIterator.hasNext()) {
					TextNode tmpNode = listIterator.next();
					if (tmpNode.getTop()==textNode.getTop()) {
						reportResultData.setExamTime(DateUtil.StringToDate(tmpNode.getValue()));
					}
				}
			}
			
			//寻找体检结果数据
			if (textNode.getValue().startsWith("★") && textNode.getLeft()==66) {
				StringBuffer stringBuffer = new StringBuffer(textNode.getValue());
				while (listIterator.hasNext()) {
					TextNode tmpNode = listIterator.next();
					if (tmpNode.getValue().startsWith("体检结果说明")||tmpNode.getValue().contains("异常指标解读")) {//找完了
						break;
					}
					if (tmpNode.getLeft()==66) {
						stringBuffer.append(tmpNode.getValue());
					}
				}
				reportResultData.setSummary(stringBuffer.toString());//获取结果
			}
			
			if (textNode.getValue().startsWith("检查项目名称")) {//读取直到读取到检查项目名称
				tmpDictionary = null;
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
					if (textNode.getValue().startsWith("医生")) {
						doctor = textNode.getValue().split(":")[1];
					}
				}
				//最后一个pre回来
				listIterator.previous();
				// 开始筛选要的数据
				List<TextNode> dataNodes = new ArrayList<>();
				while (listIterator.hasNext()) {
					TextNode tmpNode = listIterator.next();
					if (tmpNode.getValue().startsWith("小结")) {//如果有小结 那么跳出
						tmpDictionary = new SourceDictionaryDTO();
						tmpDictionary.setDepartName(departName);
						tmpDictionary.setDicName("小结");
						
						String result = "";
						LinkedList<TextNode> tmpLinkNodes = new LinkedList<>();
						int tmpIndex = listIterator.nextIndex();
						listIterator.previous();
						while (listIterator.hasPrevious()) {
							TextNode preTmpNode = listIterator.previous();
							if (preTmpNode.getLeft()<=213&&preTmpNode.getLeft()>=209) {
								tmpLinkNodes.add(preTmpNode);
							}else {
								if (!tmpLinkNodes.isEmpty()) {
									TextNode tmpNode1 = tmpLinkNodes.getLast();
									if (tmpNode1.getTop()==preTmpNode.getTop()) {
										tmpLinkNodes.removeLast();
									}
									for (int k=0 ; k<tmpLinkNodes.size();k++) {
										result = tmpLinkNodes.get(k).getValue() + result;
									}
								}
								break;
							}
						}
						listIterator = textNodes.listIterator(tmpIndex);
						TextNode nTextNode = listIterator.next();
						if (nTextNode.getTop() <= tmpNode.getTop()+6 && nTextNode.getTop() >= tmpNode.getTop()-6) {
							tmpDictionary.setDicResult(result + nTextNode.getValue());
						}else {
							listIterator.previous();
						}
						
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
				SourceDictionaryDTO dictionary = null;
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
						dictionary = new SourceDictionaryDTO();
						dictionary.setDepartName(departName);
						dictionary.setDoctor(doctor);
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
				
				if (tmpDictionary!=null) {
					dictionaries.add(tmpDictionary);
				}
				
			}
		}
		dictionaries.forEach(System.out::println);
		reportResultData.setDictionaries(dictionaries);
		reportResultData.setSourceUser(sourceUser);
		//System.out.println(dictionaries.size());
		//System.out.println(sourceUser);
		
		System.out.println(reportResultData);
		String json = JSON.toJSONString(reportResultData);
		System.out.println(json);
	}
	
	@Test
	public void test05() throws ParseException {
		String time = "2017-08-15 4:15:55";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = dateFormat.parse(time);
		System.out.println(date);
	}
	
	@Test
	public void test06() throws ParserConfigurationException, SAXException, IOException {
		ReportResultData reportResultData = new ReportResultData();
		SourceUser sourceUser = new SourceUser();
		
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("2.xml");
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
		List<SourceDictionaryDTO> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			TextNode textNode = listIterator.next();
			while (listIterator.hasNext()&&textNode.getPageIndex()==1) {//从第一页获取User信息
				if (313<textNode.getTop()&&textNode.getTop()<313+textNode.getHeight()) {
					if (305<textNode.getLeft()&&textNode.getLeft()<305+textNode.getWidth()) {//找到姓名
						sourceUser.setName(textNode.getValue());
					}else if (430<textNode.getLeft()&&textNode.getLeft()<430+textNode.getWidth()) {//找到性别
						sourceUser.setGender(textNode.getValue());
					}
				}else if (340<textNode.getTop()&&textNode.getTop()<340+textNode.getHeight()) {
					if (295<textNode.getLeft()&&textNode.getLeft()<295+textNode.getWidth()) {//找到体检编号报告编号
						reportResultData.setReportCode(textNode.getValue());
					}
				}else if (431<textNode.getTop()&&textNode.getTop()<431+textNode.getHeight()) {
					if (320<textNode.getLeft()&&textNode.getLeft()<320+textNode.getWidth()) {//体检日期
						reportResultData.setExamTime(DateUtil.StringToDate(textNode.getValue()));
					}
				}
				textNode = listIterator.next();
			}
			if (textNode.getValue().startsWith("主检报告")) {//找体检结果和体检建议
				StringBuffer suggestBuffer = new StringBuffer();
				StringBuffer resultBuffer = new StringBuffer();
				while (listIterator.hasNext()) {
					textNode = listIterator.next();
					if (textNode.getLeft()==60) {//结果总结
						resultBuffer.append(textNode.getValue());
					}else if (textNode.getLeft()==126) {//体检建议
						suggestBuffer.append(textNode.getValue());
					}
					if (textNode.getValue().startsWith("温馨提示")) {//结束
						reportResultData.setSuggest(suggestBuffer.toString());
						reportResultData.setSummary(resultBuffer.toString());
						break;
					}
				}
			}
			
			//////////
			///开始寻找体检项 
			if (textNode.getLeft()==56&&(textNode.getHeight()==15||textNode.getHeight()==16)) {//找到科室 第一种情况 left="56"  height="15"
				departName = textNode.getValue();//获取科室
				List<SourceDictionaryDTO> tmpDictionaries = new ArrayList<>();
				while (listIterator.hasNext()) {//从下面开始检索 能够获取检查项 和医生
					textNode = listIterator.next();
					if ((departName.equals("甲状腺功能五项")||departName.equals("前列腺肿瘤检测组合"))&&textNode.getLeft()==33&&!textNode.getValue().startsWith("项目")&&textNode.getHeight()==11) {
						top = textNode.getTop();//获取TOP
						List<TextNode> tmpNodes = new ArrayList<>();
						// 往前找同一行的检查项
						tmpNodes.add(textNode);//把检查项给记录下来
						index = listIterator.nextIndex();
						listIterator.previous();
						while (listIterator.hasPrevious()) {//有些体检结果跑前面了
							TextNode prenode = listIterator.previous();
							if (prenode.getTop()>top-7&&prenode.getTop()<top+7) {//同一行
								tmpNodes.add(prenode);
							}else {
								listIterator = textNodes.listIterator(index);
								break;
							}
						}
						// 往后找同一行的检查项
						while (listIterator.hasNext()) {
							textNode = listIterator.next();
							if (textNode.getTop()>top-7&&textNode.getTop()<top+13) {//同一行
								tmpNodes.add(textNode);
							}else {
								listIterator.previous();
								break;
							}
						}
						// 排序配置dictionary
						tmpNodes.sort(Comparator.comparing(TextNode::getLeft));
						Iterator<TextNode> tmpIterator = tmpNodes.iterator();
						SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
						while (tmpIterator.hasNext()&&tmpNodes.size()>1) {// 有两个检查项
							TextNode tmpNode = tmpIterator.next();
							if (tmpNode.getLeft() == 33) {
								if (!StringUtils.isEmpty(dictionary.getDicName())) {
									dictionary.setDicName(dictionary.getDicName()+tmpNode.getValue());// 检查项
								}else {
									dictionary.setDicName(tmpNode.getValue());// 检查项
								}
							} else if (tmpNode.getLeft() >156
									&& tmpNode.getLeft() < 180) {
								dictionary.setDicResult(tmpNode.getValue());// 测量结果
							} else if (tmpNode.getLeft() > 210
									&& tmpNode.getLeft() < 238) {
								dictionary.setDicExplain(tmpNode.getValue());// 参考结果
							} else if (tmpNode.getLeft() > 538) {
								dictionary.setDicUnit(tmpNode.getValue());// 单位
							} 
						}
						dictionary.setDepartName(departName);// 科室名称
						tmpDictionaries.add(dictionary);// 把老的项添加了
					}else if (textNode.getLeft()==30&&!textNode.getValue().startsWith("项目")&&textNode.getHeight()==11) {//一种是项目在前面30的位置
						SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
						dictionary.setDepartName(departName);//科室名
						dictionary.setDicName(textNode.getValue());
						while (listIterator.hasNext()) {
							TextNode tmpNode = listIterator.next();
							if (tmpNode.getTop()==textNode.getTop()) {//同一行
								if (tmpNode.getLeft()==110) {//结果
									dictionary.setDicResult(tmpNode.getValue());
								}else if (tmpNode.getLeft()==305) {//项目
									tmpDictionaries.add(dictionary);
									dictionary = new SourceDictionaryDTO();
									dictionary.setDepartName(departName);
									dictionary.setDicName(tmpNode.getValue());
								}else if (tmpNode.getLeft()==385) {//结果
									dictionary.setDicResult(tmpNode.getValue());
								}
							}
							if (tmpNode.getTop()!=textNode.getTop()) {//换行了
								listIterator.previous();
								tmpDictionaries.add(dictionary);
								break;
							}
						}
					}else if (textNode.getLeft()==33&&!textNode.getValue().startsWith("项目")&&textNode.getHeight()==11) {//另外一种是项目在前面33的位置
						top = textNode.getTop();//获取TOP
						List<TextNode> tmpNodes = new ArrayList<>();
						// 往前找同一行的检查项
						tmpNodes.add(textNode);//把检查项给记录下来
						index = listIterator.nextIndex();
						while (listIterator.hasPrevious()) {//有些体检结果跑前面了
							TextNode prenode = listIterator.previous();
							if (prenode.getTop()>top-7&&prenode.getTop()<top+7) {//同一行
								tmpNodes.add(prenode);
							}else {
								listIterator = textNodes.listIterator(index);
								break;
							}
						}
						// 往后找同一行的检查项
						while (listIterator.hasNext()) {
							textNode = listIterator.next();
							if (textNode.getTop()>top-7&&textNode.getTop()<top+7) {//同一行
								tmpNodes.add(textNode);
							}else {
								break;
							}
						}
						// 排序配置dictionary
						tmpNodes.sort(Comparator.comparing(TextNode::getLeft));
						Iterator<TextNode> tmpIterator = tmpNodes.iterator();
						SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
						while (tmpIterator.hasNext()) {// 有两个检查项
							TextNode tmpNode = tmpIterator.next();
							if (tmpNode.getLeft() == 33) {
								dictionary.setDicName(tmpNode.getValue());// 检查项
							} else if (tmpNode.getLeft() >156
									&& tmpNode.getLeft() < 180) {
								dictionary.setDicResult(tmpNode.getValue());// 测量结果
							} else if (tmpNode.getLeft() > 215
									&& tmpNode.getLeft() < 238) {
								dictionary.setDicExplain(tmpNode.getValue());// 参考结果
							} else if (tmpNode.getLeft() > 265
									&& tmpNode.getLeft() < 305) {
								dictionary.setDicUnit(tmpNode.getValue());// 单位
							} else if (tmpNode.getLeft() == 308) {// 新的体检项
								dictionary.setDepartName(departName);
								tmpDictionaries.add(dictionary);// 把老的项添加了
								dictionary = new SourceDictionaryDTO();// 创建新的项
								dictionary.setDicName(tmpNode.getValue());// 检查项
							} else if (tmpNode.getLeft() > 446 - tmpNode.getWidth()
									&& tmpNode.getLeft() < 446 + tmpNode.getWidth()) {
								dictionary.setDicResult(tmpNode.getValue());// 测量结果
							} else if (tmpNode.getLeft() > 507 - tmpNode.getWidth()
									&& tmpNode.getLeft() < 507 + tmpNode.getWidth()) {
								dictionary.setDicExplain(tmpNode.getValue());// 参考结果
							} else if (tmpNode.getLeft() > 551 - tmpNode.getWidth()
									&& tmpNode.getLeft() < 551 + tmpNode.getWidth()) {
								dictionary.setDicUnit(tmpNode.getValue());// 单位
							}
						}
						if (tmpNodes.size()>1) {
							dictionary.setDepartName(departName);// 科室名称
							tmpDictionaries.add(dictionary);// 把老的项添加了
						}
					}
					if (textNode.getValue().contains("操作者：")||textNode.getValue().contains("医生：")) {//如果是找到了医生
						String tmpStr = textNode.getValue().split("：")[1];
						tmpStr = tmpStr.replaceAll(" ", "");
						tmpStr = tmpStr.replaceAll("审核者", "");
						String doctor = tmpStr;
						tmpDictionaries.forEach(d->{
							d.setDoctor(doctor);
						});
						dictionaries.addAll(tmpDictionaries);//第一种情况的体检项添加进去
						break;
					}
					if (textNode.getLeft()==56&&(textNode.getHeight()==15||textNode.getHeight()==16)) {
						departName = textNode.getValue();
					}
				}
			}else if (textNode.getLeft()==85&&textNode.getHeight()==27) {//第二种情况是在体检报告里
				departName = textNode.getValue();//得到科室并且当做检查项
				SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
				dictionary.setDepartName(departName);
				dictionary.setDicName(departName);
				boolean key = false;
				StringBuffer resultSB = new StringBuffer();
				if (departName.equals("肺功能检查")) {
					while (listIterator.hasNext()) {
						textNode = listIterator.next();
						if (textNode.getLeft()==45) {
							resultSB.append(textNode.getValue());
						}
						if (textNode.getLeft()==419&&textNode.getHeight()==13) {
							if (textNode.getValue().contains("操作者：")||textNode.getValue().contains("医生：")) {//如果是找到了医生
								String tmpStr = textNode.getValue().split("：")[1];
								tmpStr = tmpStr.replaceAll(" ", "");
								tmpStr = tmpStr.replaceAll("审核者", "");
								String doctor = tmpStr;
								dictionary.setDicResult(resultSB.toString());
								dictionary.setDoctor(doctor);
								dictionaries.add(dictionary);//第一种情况的体检项添加进去
								break;
							}
						}
					}
				}else {
					while (listIterator.hasNext()) {
						textNode = listIterator.next();
						if (key&&textNode.getLeft()==45) {
							resultSB.append(textNode.getValue());
						}
						if (textNode.getValue().equals("【印 象】")) {
							key=true;
						}
						if (textNode.getLeft()==419&&textNode.getHeight()==13) {
							if (textNode.getValue().contains("操作者：")||textNode.getValue().contains("医生：")) {//如果是找到了医生
								String tmpStr = textNode.getValue().split("：")[1];
								tmpStr = tmpStr.replaceAll(" ", "");
								tmpStr = tmpStr.replaceAll("审核者", "");
								String doctor = tmpStr;
								dictionary.setDicResult(resultSB.toString());
								dictionary.setDoctor(doctor);
								dictionaries.add(dictionary);//第一种情况的体检项添加进去
								break;
							}
						}
					}
				}
			}
		}
		reportResultData.setDictionaries(dictionaries);
		reportResultData.setSourceUser(sourceUser);
		System.out.println(reportResultData);
	}
	
	
	@Test
	public void test07() throws Exception {
		ReportResultData reportResultData = new ReportResultData();
		SourceUser sourceUser = new SourceUser();
		
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("3.xml");
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
		List<SourceDictionaryDTO> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			//从第一页获取User信息
			TextNode textNode = listIterator.next();
			if (textNode.getPageIndex()==1) {
				if (textNode.getTop()==40 && textNode.getLeft()==39) {//体检编号
					reportResultData.setReportCode(textNode.getValue().split("：")[1]);
				}else if (textNode.getTop()==82&&textNode.getLeft()==127) {//姓名
					sourceUser.setName(textNode.getValue());
				}else if (textNode.getTop()==102&&textNode.getLeft()==127) {//性别
					sourceUser.setGender(textNode.getValue());
				}else if (textNode.getTop()==122&&textNode.getLeft()==49) {//检查日期
					reportResultData.setExamTime(DateUtil.StringToDate(textNode.getValue().split("：")[1], DateStyle.YYYY_MM_DD));
				}else if (textNode.getTop()==162&&textNode.getLeft()==52) {//获取身份证
					sourceUser.setIdentityNumber(textNode.getValue().split("：")[1]);
				}
			}
			
			if (textNode.getLeft()==39&&textNode.getHeight()==16) {//找到科室开始
				departName = textNode.getValue();//得到科室
				while (listIterator.hasNext()) {
					textNode = listIterator.next();
					List<TextNode> tmpNodes = new ArrayList<>();//暂存
					List<SourceDictionaryDTO> tmpDictionaries = new ArrayList<>();//得到临时的结果
					if (textNode.getLeft()==39&&textNode.getHeight()==13) {//第一种检查项情况得到每一行检查项
						tmpNodes.add(textNode);
						top = textNode.getTop();
						while (listIterator.hasNext()) {//同一行存起来
							textNode = listIterator.next();
							if (textNode.getTop()==top) {//同一行
								
							}else {//换行了
								listIterator.previous();
								break;
							}
						}
					}
					if (textNode.getLeft()==459) {//找到医生
						String doctor = textNode.getValue().split("：")[1];
					}
				}
			}
			
		}
		
		
	}
	
	
	/*
	 
	 if (textNode.getLeft()==75&&textNode.getHeight()==27) {//找到科室 第一种情况 left=75并且字体高度27
				while (listIterator.hasNext()) {//从下面开始检索 能够获取检查项 和医生
					departName = textNode.getValue();//获取科室
					textNode = listIterator.next();
					List<Dictionary> tmpDictionaries = new ArrayList<>();
					if (textNode.getLeft()==30&&!textNode.getValue().startsWith("项目")) {//一种是项目在前面30的位置
						while (listIterator.hasNext()) {
							Dictionary dictionary = new Dictionary();
							dictionary.setDepartName(departName);//科室名
							TextNode tmpNode = listIterator.next();
							if (tmpNode.getTop()==textNode.getTop()) {
								if (tmpNode.getLeft()==110) {//结果
									dictionary.setDicResult(tmpNode.getValue());
									tmpDictionaries.add(dictionary);
								}else if (tmpNode.getLeft()==305) {//项目
									dictionary.setDicName(tmpNode.getValue());
								}else if (tmpNode.getLeft()==385) {//结果
									dictionary.setDicResult(tmpNode.getValue());
									tmpDictionaries.add(dictionary);
								}
							}
							if (tmpNode.getTop()!=textNode.getTop()) {//换行了
								listIterator.previous();
								break;
							}
						}
					}
					if (textNode.getLeft()==33&&!textNode.getValue().startsWith("项目")) {//另外一种是项目在前面33的位置
						top = textNode.getTop();//获取TOP
						List<TextNode> tmpNodes = new ArrayList<>();
						// 往前找同一行的检查项
						tmpNodes.add(textNode);//把检查项给记录下来
						index = listIterator.nextIndex();
						while (listIterator.hasPrevious()) {//有些体检结果跑前面了
							TextNode prenode = listIterator.previous();
							if (prenode.getTop()>top-5&&prenode.getTop()<top+5) {//同一行
								tmpNodes.add(prenode);
							}else {
								listIterator = textNodes.listIterator(index);
								break;
							}
						}
						// 往后找同一行的检查项
						while (listIterator.hasNext()) {
							textNode = listIterator.next();
							if (textNode.getTop()>top-5&&textNode.getTop()<top+5) {//同一行
								tmpNodes.add(textNode);
							}else {
								break;
							}
						}
						// 排序配置dictionary
						tmpNodes.sort(Comparator.comparing(TextNode::getLeft));
						Iterator<TextNode> tmpIterator = tmpNodes.iterator();
						Dictionary dictionary = new Dictionary();
						if (textNode.getValue()=="") {
							
						}else {
							while (tmpIterator.hasNext()) {//有两个检查项
								TextNode tmpNode = tmpIterator.next();
								if (tmpNode.getLeft()==33) {
									dictionary.setDicName(tmpNode.getValue());//检查项
								}else if (tmpNode.getLeft()>174-tmpNode.getWidth()&&tmpNode.getLeft()<174+tmpNode.getWidth()) {
									dictionary.setDicResult(tmpNode.getValue());//测量结果
								}else if (tmpNode.getLeft()>238-tmpNode.getWidth()&&tmpNode.getLeft()<238+tmpNode.getWidth()) {
									dictionary.setDicExplain(tmpNode.getValue());//参考结果
								}else if (tmpNode.getLeft()>276-tmpNode.getWidth()&&tmpNode.getLeft()<276+tmpNode.getWidth()) {
									dictionary.setDicUnit(tmpNode.getValue());//单位
								}else if (tmpNode.getLeft()==308) {//新的体检项
									dictionary.setDepartName(departName);
									dictionaries.add(dictionary);//把老的项添加了
									dictionary = new Dictionary();//创建新的项
									dictionary.setDicName(tmpNode.getValue());//检查项
								}else if (tmpNode.getLeft()>446-tmpNode.getWidth()&&tmpNode.getLeft()<446+tmpNode.getWidth()) {
									dictionary.setDicResult(tmpNode.getValue());//测量结果
								}else if (tmpNode.getLeft()>507-tmpNode.getWidth()&&tmpNode.getLeft()<507+tmpNode.getWidth()) {
									dictionary.setDicExplain(tmpNode.getValue());//参考结果
								}else if (tmpNode.getLeft()>551-tmpNode.getWidth()&&tmpNode.getLeft()<551+tmpNode.getWidth()) {
									dictionary.setDicUnit(tmpNode.getValue());//单位
								}
							}
							dictionary.setDepartName(departName);//科室名称
							dictionaries.add(dictionary);//把老的项添加了
						}
					}
					
					if (textNode.getLeft()==415) {//如果是找到了医生
						String doctor = textNode.getValue().split("：")[1];
						tmpDictionaries.forEach(d->{
							d.setDoctor(doctor);
						});
						dictionaries.addAll(tmpDictionaries);//第一种情况的体检项添加进去
						break;
					} 
				}
			}else if (textNode.getLeft()==85&&textNode.getHeight()==27) {//找到科室 第二种情况 left=85并且字体高度27
				if (textNode.getValue().equals("耳鼻喉科检查（无音叉）")) {//耳鼻喉科检查（无音叉）的结果结构不一样 耳：  耳廓： 未见异常
					List<Dictionary> tmpDictionaries = new ArrayList<>();
					while (listIterator.hasNext()) {
						departName = textNode.getValue();//获取科室
						textNode = listIterator.next();
						if (textNode.getValue().contains("检查所见")) {//从检查所见开始
							while (listIterator.hasNext()) {
								textNode = listIterator.next();
								if (textNode.getLeft()==45&&!textNode.getValue().contains("印 象")) {
									String tmpValue = textNode.getValue();
									tmpValue.replaceAll(" ", "");//去掉空格
									String[] tmpArr = tmpValue.split("：");//分解
									Dictionary dictionary = new Dictionary();
									if (tmpArr.length>=2) {
										dictionary.setDicResult(tmpArr[tmpArr.length-1]);//获取
										dictionary.setDicName(tmpArr[tmpArr.length-2]);//检查项
										tmpDictionaries.add(dictionary);
									}
								}
								if (textNode.getValue().contains("印 象")) {
									break;
								}
							}
						}
						if (textNode.getLeft()==419) {//找到医生
							String doctor = textNode.getValue().split("：")[1];
							tmpDictionaries.forEach(d->{
								d.setDoctor(doctor);
							});
							dictionaries.addAll(tmpDictionaries);
							break;
						}
					}
				}else if (textNode.getValue().equals("骨密度检查")) {//骨密度检查他喵也是单独一种结果结构要排除出来
					while (listIterator.hasNext()) {
						departName = textNode.getValue();//获取科室
						textNode = listIterator.next();
						Dictionary dictionary = new Dictionary();
						if (textNode.getValue().contains("检查所见")) {//从检查所见开始
							while (listIterator.hasNext()) {
								textNode = listIterator.next();
								if (textNode.getValue().contains("印 象")) {
									break;
								}
								
								dictionary.setDepartName(departName);
								dictionary.setDicName(departName);//把科室当成检查项
								dictionary.setDicResult(textNode.getValue());
								
							}
						}
						if (textNode.getLeft()==419) {//如果是找到了医生
							String doctor = textNode.getValue().split("：")[1];
							dictionary.setDoctor(doctor);
							dictionaries.add(dictionary);
							break;
						}
					}
				}else if (textNode.getValue().equals("肺功能检查")) {//肺功能检查特特瞄也是单独一种结果结构
					while (listIterator.hasNext()) {
						departName = textNode.getValue();//获取科室
						textNode = listIterator.next();
						Dictionary dictionary = new Dictionary();
						if (textNode.getLeft()==45) {
							dictionary.setDepartName(departName);
							dictionary.setDicName(departName);//把科室当成检查项
							dictionary.setDicResult(textNode.getValue());
						}
						if (textNode.getLeft()==419) {//如果是找到了医生
							String doctor = textNode.getValue().split("：")[1];
							dictionary.setDoctor(doctor);
							dictionaries.add(dictionary);
							break;
						}
					}
				}
				
			}
	 
	 
	 * */
	
	@Test
	public void test08() throws ParserConfigurationException, SAXException, IOException {
		
		ReportResultData reportResultData = new ReportResultData();
		SourceUser sourceUser = new SourceUser();
		
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("e12.xml");
		Document document = XmlUtil.getDocument(inputStream);
		// System.out.println(document);
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
				if (textNode.getTop() > 38 && textNode.getTop() < 793) {
					textNodes.add(textNode);
				}
			}
		}
		System.out.println(textNodes);
		
		ListIterator<TextNode> listIterator = textNodes.listIterator();
		int index = 0;// 记录迭代器位置
		String departName = null;//科室
		int top = 0;
		int page = 0;
		String doctor = null;
		List<SourceDictionaryDTO> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			TextNode textNode = listIterator.next();
			while (listIterator.hasNext() && textNode.getPageIndex()==1) {//第一页查询用户信息
				if (textNode.getLeft()==164 && (textNode.getTop() >= 498-3&&textNode.getTop() <= 498+3)) {//姓名
					sourceUser.setName(textNode.getValue());
				}
				if (textNode.getLeft()==440 && (textNode.getTop() >= 498-3&&textNode.getTop() <= 498+3)) {//性别
					sourceUser.setGender(textNode.getValue());
				}
				if (textNode.getLeft()==213 && (textNode.getTop() >= 613-3&&textNode.getTop() <= 613+3)) {//报告编号
					reportResultData.setReportCode(textNode.getValue());
				}
				if (textNode.getLeft() == 405 && (textNode.getTop() >= 663-3&&textNode.getTop() <= 663+3)) {
					reportResultData.setExamTime(DateUtil.StringToDate(textNode.getValue()));
				}
				textNode = listIterator.next();
			}
			if (textNode.getValue().equals("阳性结果和异常情况")) {//找到体检结果
				String explainResult = "";
				String resultSuggest = "";
				while (listIterator.hasNext()) {
					textNode = listIterator.next();
					if (textNode.getValue().equals("专家建议与指导")) {
						while (listIterator.hasNext()) {
							textNode =  listIterator.next();
							if (textNode.getValue().equals("健康体检结果")) {
								break;
							}
							resultSuggest = resultSuggest + textNode.getValue();
						}
						listIterator.previous();
						break;
					}
					explainResult = explainResult + textNode.getValue();
				}
				reportResultData.setSummary(explainResult);
				reportResultData.setSuggest(resultSuggest);
			}
			//找到科室
			while (listIterator.hasNext() && (textNode.getLeft()==72 || textNode.getLeft()==71) && (textNode.getHeight() == 12 || textNode.getHeight() == 11) && !textNode.getValue().equals("报告阅读说明")) {
				//科室
				departName = textNode.getValue().startsWith("·")? textNode.getValue().replaceAll("·", "") : textNode.getValue();
				TextNode tmpNode = textNode;
				//如果出现两行的都是科室的伪情况 直接忽略
				TextNode validateTrueNode = listIterator.next();
				if (validateTrueNode.getValue().contains("放弃检查")) {
					break;
				}
				if (validateTrueNode.getLeft() == tmpNode.getLeft() && validateTrueNode.getHeight() == tmpNode.getHeight()) {
					while (listIterator.hasNext()) {
						if (validateTrueNode.getLeft() == tmpNode.getLeft() && validateTrueNode.getHeight() == tmpNode.getHeight()) {
							validateTrueNode = listIterator.next();
						}else {
							listIterator.previous();
							break;
						}
					}
					break;
				}
				//pre
				listIterator.previous();
				//寻找检查者 可能在上面 或者 在下面
				index = listIterator.nextIndex();//得到迭代器状态
				while(listIterator.hasPrevious()) {
					textNode = listIterator.previous();
					if (textNode.getTop() < tmpNode.getTop() + 6 && textNode.getTop() > tmpNode.getTop() - 6) {
						doctor = textNode.getValue();
					}else {
						while (listIterator.hasNext()) {
							if (textNode.getTop() < tmpNode.getTop() + 6 && textNode.getTop() > tmpNode.getTop() - 6) {
								doctor = textNode.getValue();
							}else {
								break;
							}
						}
						//处理doctor结构 操作者：XXX  审核者：BBB 只要XXX
						doctor = 
								doctor
								.replaceAll("检查者：", "")
								.replaceAll(" ", "")
								.replaceAll("操作者：", "")
								.split("：")[0]
										.replaceAll("审核者", "");
						break;
					}
				}
				//重置迭代器位置
				listIterator = textNodes.listIterator(index);
				//查找体检项
				int count = 0;
				while (listIterator.hasNext()) {
					//找到小结
					if (textNode.getValue().trim().equals("初步意见") || textNode.getValue().trim().equals("小结")) {
						listIterator.next();
						StringBuffer sb = new StringBuffer();
						while (listIterator.hasNext()) {
							TextNode tmpTextNode = listIterator.next();
							if (tmpTextNode.getLeft()==199) {
								sb.append(tmpTextNode.getValue());
							}
							else {
								SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
								dictionary.setDepartName(departName);
								dictionary.setDoctor(doctor);
								dictionary.setDicName("小结");
								dictionary.setDicResult(sb.toString());
								dictionaries.add(dictionary);
								listIterator.previous();
								break;
							}
						}
						break;
					}
					if (count==0) {
						TextNode tmpTextNodeva = listIterator.next();
						while (!tmpTextNodeva.getValue().contains("检查项目")&&listIterator.hasNext()) {
							tmpTextNodeva = listIterator.next();
						}
						if (tmpTextNodeva.getValue().contains("者")) {
							tmpTextNodeva = listIterator.next();
						}
						if (tmpTextNodeva.getLeft()==77 && (tmpTextNodeva.getHeight()==10 || tmpTextNodeva.getHeight()==9)) {
							listIterator.previous();
							break;
						}
						count++;
						while (listIterator.hasNext()) {
							TextNode tmpTextNode = listIterator.next();
							if (tmpTextNode.getTop()>tmpTextNodeva.getTop()+5 || tmpTextNode.getTop()<tmpTextNodeva.getTop()-5) {
								listIterator.previous();
								break;
							}
							if (tmpTextNode.getLeft()==77 && (tmpTextNode.getHeight()==10||tmpTextNode.getHeight()==9)) {
								listIterator.previous();
								break;
							}
							count++;
						}
					}
					if (listIterator.hasNext()) {
						textNode = listIterator.next();
						//找到体检项
						if (textNode.getLeft()==77 && (textNode.getHeight()==10 || textNode.getHeight()==9))  {
							/*List<TextNode> tmpTextNodes = new ArrayList<>();
							TextNode selectListNode = textNode;
							tmpTextNodes.add(textNode);//合并
							while (listIterator.hasNext()) {
								textNode = listIterator.next();
								if (textNode.getTop() > selectListNode.getTop() - 15 && textNode.getTop() < selectListNode.getTop() + 15) {
									tmpTextNodes.add(textNode);
								}else {
									listIterator.previous();//pre回去 端点
									break;
								}
							}*/
							TwoTuple<List<TextNode>, ListIterator<TextNode>> result = modelOne(listIterator, textNode, textNodes,2);
							listIterator = result.getSecond();
							List<TextNode> tmpTextNodes = result.getFirst();
							//获取完成 处理
							//项名
							StringBuffer dicName = new StringBuffer();
							//结论
							StringBuffer dicResult = new StringBuffer();
							//单位
							StringBuffer dicUnit = new StringBuffer();
							//提示
							StringBuffer tip = new StringBuffer();
							//参考区间
							StringBuffer explain = new StringBuffer();
							//英文名
							StringBuffer enName = new StringBuffer();
							SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
							dictionary.setDepartName(departName);
							dictionary.setDoctor(doctor);
							
							//用count的行数来定义字段意义
							System.out.println(count);
							if (count == 5) {
								tmpTextNodes.forEach(t->{
									if (t.getLeft()==77) {
										dicName.append(t.getValue());
									}
									if (t.getLeft()==320) {
										dicUnit.append(t.getValue());
									}
									if (t.getLeft() == 199) {
										dicResult.append(t.getValue());
									}
									if (t.getLeft() == 447) {
										explain.append(t.getValue());
									}
								});
							}else if (count == 3) {
								tmpTextNodes.forEach(t->{
									if (t.getLeft()==77) {
										dicName.append(t.getValue());
									}
									if (t.getLeft()==199) {
										dicResult.append(t.getValue());
									}
									if (t.getLeft()==497) {
										dicUnit.append(t.getValue());
									}
								});
							}else if (count == 6){
								tmpTextNodes.forEach(t->{
									if (t.getLeft()==77) {
										dicName.append(t.getValue());
									}
									if (t.getLeft()==497) {
										dicUnit.append(t.getValue());
									}
									if (t.getLeft()==256) {
										dicResult.append(t.getValue());
									}
									if (t.getLeft()==376) {
										tip.append(t.getValue());
									}
									if (t.getLeft()==405) {
										explain.append(t.getValue());
									}
									if (t.getLeft() == 199) {
										enName.append(t.getValue());
									}
								});
							}
							if(!StringUtils.isEmpty(dicName.toString())) {
								dictionary.setDicExplain(explain.toString());
								dictionary.setDicName(dicName.toString());
								dictionary.setDicResult(dicResult.toString());
								dictionary.setDicUnit(dicUnit.toString());
								dictionary.setEnName(enName.toString());
								dictionary.setTip(tip.toString());
								dictionaries.add(dictionary);
							}
						}
					}
				}
			}
			
			//找到科室
			while (trueDepartment(textNode) && listIterator.hasNext()) {
				departName = textNode.getValue();
				TextNode tmpNode = textNode;
				//如果出现两行的都是科室的伪情况 直接忽略
				TextNode validateTrueNode = listIterator.next();
				if (validateTrueNode.getValue().contains("放弃检查")) {
					break;
				}
				if (validateTrueNode.getLeft() == tmpNode.getLeft() && validateTrueNode.getHeight() == tmpNode.getHeight()) {
					while (listIterator.hasNext()) {
						if (validateTrueNode.getLeft() == tmpNode.getLeft() && validateTrueNode.getHeight() == tmpNode.getHeight()) {
							validateTrueNode = listIterator.next();
						}else {
							listIterator.previous();
							break;
						}
					}
					break;
				}
				//pre
				listIterator.previous();
				//寻找检查者 可能在上面 或者 在下面
				index = listIterator.nextIndex();//得到迭代器状态
				while(listIterator.hasNext()) {
					textNode = listIterator.previous();
					if (textNode.getValue().contains("操作者") || textNode.getValue().contains("审核者")) {
						doctor = 
								textNode.getValue()
								.replaceAll("检查者：", "")
								.replaceAll(" ", "")
								.replaceAll("操作者：", "")
								.split("：")[0]
										.replaceAll("审核者", "");
						break;
					}
					if (textNode.getLeft()==77) {
						listIterator.previous();
						break;
					}
				}
				//重置迭代器位置
				listIterator = textNodes.listIterator(index);
				//查找体检项
				int count = 0;
				while (listIterator.hasNext()) {
					//找到小结
					if (textNode.getValue().trim().contains("总检专家建议和健康指导") || textNode.getValue().trim().equals("小结")) {
						listIterator.next();
						StringBuffer sb = new StringBuffer();
						while (listIterator.hasNext()) {
							TextNode tmpTextNode = listIterator.next();
							if (tmpTextNode.getLeft()==72) {
								sb.append(tmpTextNode.getValue());
							}
							else {
								SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
								dictionary.setDepartName(departName);
								dictionary.setDoctor(doctor);
								dictionary.setDicName("小结");
								dictionary.setDicResult(sb.toString());
								dictionaries.add(dictionary);
								listIterator.previous();
								break;
							}
						}
						break;
					}
					if (count==0) {
						TextNode tmpTextNodeva = listIterator.next();
						while (!tmpTextNodeva.getValue().contains("检查项目")&&listIterator.hasNext()) {
							tmpTextNodeva = listIterator.next();
						}
						if (tmpTextNodeva.getValue().contains("者")) {
							tmpTextNodeva = listIterator.next();
						}
						if (tmpTextNodeva.getLeft()==77 && (tmpTextNodeva.getHeight()==10 || tmpTextNodeva.getHeight()==9)) {
							listIterator.previous();
							break;
						}
						count++;
						while (listIterator.hasNext()) {
							TextNode tmpTextNode = listIterator.next();
							if (tmpTextNode.getTop()>tmpTextNodeva.getTop()+5 || tmpTextNode.getTop()<tmpTextNodeva.getTop()-5) {
								listIterator.previous();
								break;
							}
							if (tmpTextNode.getLeft()==77 && (tmpTextNode.getHeight()==10||tmpTextNode.getHeight()==9)) {
								listIterator.previous();
								break;
							}
							count++;
						}
					}
					if (listIterator.hasNext()) {
						textNode = listIterator.next();
						//找到体检项
						if (textNode.getLeft()==77 && (textNode.getHeight()==10 || textNode.getHeight()==9))  {
							/*List<TextNode> tmpTextNodes = new ArrayList<>();
							TextNode selectListNode = textNode;
							tmpTextNodes.add(textNode);//合并
							while (listIterator.hasNext()) {
								textNode = listIterator.next();
								if (textNode.getTop() > selectListNode.getTop() - 15 && textNode.getTop() < selectListNode.getTop() + 15) {
									tmpTextNodes.add(textNode);
								}else {
									listIterator.previous();//pre回去 端点
									break;
								}
							}*/
							TwoTuple<List<TextNode>, ListIterator<TextNode>> result = modelOne(listIterator, textNode, textNodes,2);
							listIterator = result.getSecond();
							List<TextNode> tmpTextNodes = result.getFirst();
							//获取完成 处理
							//项名
							StringBuffer dicName = new StringBuffer();
							//结论
							StringBuffer dicResult = new StringBuffer();
							//单位
							StringBuffer dicUnit = new StringBuffer();
							//提示
							StringBuffer tip = new StringBuffer();
							//参考区间
							StringBuffer explain = new StringBuffer();
							//英文名
							StringBuffer enName = new StringBuffer();
							SourceDictionaryDTO dictionary = new SourceDictionaryDTO();
							dictionary.setDepartName(departName);
							dictionary.setDoctor(doctor);
							
							//用count的行数来定义字段意义
							System.out.println(count);
							if (count == 5) {
								tmpTextNodes.forEach(t->{
									if (t.getLeft()==77) {
										dicName.append(t.getValue());
									}
									if (t.getLeft()==320) {
										dicUnit.append(t.getValue());
									}
									if (t.getLeft() == 199) {
										dicResult.append(t.getValue());
									}
									if (t.getLeft() == 447) {
										explain.append(t.getValue());
									}
								});
							}else if (count == 3) {
								tmpTextNodes.forEach(t->{
									if (t.getLeft()==77) {
										dicName.append(t.getValue());
									}
									if (t.getLeft()==199) {
										dicResult.append(t.getValue());
									}
									if (t.getLeft()==497) {
										dicUnit.append(t.getValue());
									}
								});
							}else if (count == 6){
								tmpTextNodes.forEach(t->{
									if (t.getLeft()==77) {
										dicName.append(t.getValue());
									}
									if (t.getLeft()==497) {
										dicUnit.append(t.getValue());
									}
									if (t.getLeft()==256) {
										dicResult.append(t.getValue());
									}
									if (t.getLeft()==376) {
										tip.append(t.getValue());
									}
									if (t.getLeft()==405) {
										explain.append(t.getValue());
									}
									if (t.getLeft() == 199) {
										enName.append(t.getValue());
									}
								});
							}
							if(!StringUtils.isEmpty(dicName.toString())) {
								dictionary.setDicExplain(explain.toString());
								dictionary.setDicName(dicName.toString());
								dictionary.setDicResult(dicResult.toString());
								dictionary.setDicUnit(dicUnit.toString());
								dictionary.setEnName(enName.toString());
								dictionary.setTip(tip.toString());
								dictionaries.add(dictionary);
							}
						}
					}
				}
			}
			
			
		}
		reportResultData.setSourceUser(sourceUser);
		reportResultData.setDictionaries(dictionaries);
		System.out.println(JSON.toJSONString(reportResultData));
	}
	
	private boolean trueDepartment(TextNode textNode) {
		int width = (textNode.getLeft()*2+textNode.getWidth())/2 ;
		System.out.println(width);
		if (width < 319 || width > 325) {
			return false;
		}
		if (textNode.getHeight()!=26) {
			return false;
		}
		return true;
	}
	
	@Test
	public void test09() throws Exception{
		ReportResultData reportResultData = new ReportResultData();
		SourceUser sourceUser = new SourceUser();
		
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("6543.xml");
		Document document = XmlUtil.getDocument(inputStream);
		// System.out.println(document);
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
				if (textNode.getTop() > 38 && textNode.getTop() < 793) {
					textNodes.add(textNode);
				}
			}
		}
		System.out.println(textNodes);
		
		textNodes = new LinkedList<>(textNodes.stream().filter(n->{return n.getTop()<800 && n.getTop()>54;}).collect(Collectors.toList()));
		
		ListIterator<TextNode> listIterator = textNodes.listIterator();
		int index = 0;// 记录迭代器位置
		String departName = null;//科室
		int top = 0;
		int page = 0;
		String doctor = null;
		List<SourceDictionaryDTO> dictionaries = new ArrayList<>();//结果集
		while (listIterator.hasNext()) {
			TextNode textNode = listIterator.next();
			//找到用户信息
			while (textNode.getPageIndex()==1&&listIterator.hasNext()) {
				//体检编号
				if (textNode.getLeft()>=258-4&&textNode.getLeft()<=258+4&&textNode.getTop()>=288-4&&textNode.getTop()<=288+4) {
					reportResultData.setReportCode(textNode.getValue());
				}
				//姓名
				if (textNode.getLeft()>=258-4&&textNode.getLeft()<=258+4&&textNode.getTop()>=314-4&&textNode.getTop()<=314+4) {
					sourceUser.setName(textNode.getValue());
				}
				//性别
				if (textNode.getLeft()>=257-4&&textNode.getLeft()<=257+4&&textNode.getTop()>=338-4&&textNode.getTop()<=338+4) {
					//reportResultData.setReportCode(textNode.getValue());
					String sex = textNode.getValue();
					if (sex.contains("男")||sex.contains("先生")) {
						sourceUser.setGender("男");
					}else {
						sourceUser.setGender("女");
					}
				}
				//体检日期
				if (textNode.getLeft()>=255-4&&textNode.getLeft()<=255+4&&textNode.getTop()>=387-4&&textNode.getTop()<=387+4) {
					reportResultData.setExamTime(DateUtil.StringToDate(textNode.getValue().trim()));
				}
				textNode = listIterator.next();
			}
			
			StringBuffer rssb = new StringBuffer();
			//开始找总检和建议
			if (textNode.getValue().contains("总检结论")&&listIterator.hasNext()) {
				textNode = listIterator.next();
				while (textNode.getLeft()<=78+3&&textNode.getLeft()>=78-3&&listIterator.hasNext()) {
					rssb.append(textNode.getValue());
					textNode = listIterator.next();
				}
				listIterator.previous();
				System.out.println(rssb.toString());
				reportResultData.setSummary(rssb.toString());
			}
			StringBuffer sgsb = new StringBuffer();
			//找建议
			if (textNode.getValue().contains("总检建议")&&listIterator.hasNext()) {
				textNode = listIterator.next();
				while (textNode.getLeft()<=78+3&&textNode.getLeft()>=78-3&&listIterator.hasNext()) {
					sgsb.append(textNode.getValue());
					textNode = listIterator.next();
				}
				listIterator.previous();
				System.out.println(sgsb.toString());
				reportResultData.setSuggest(sgsb.toString());
			}
			//寻找体检项
			//有三种情况的体检项 一种是中间对其 269 一种是左对齐 一种是照片
			if ((textNode.getLeft()*2+textNode.getWidth())/2>=268&&(textNode.getLeft()*2+textNode.getWidth())/2<=270) {
				//获取科室
				departName = textNode.getValue();
				index = listIterator.nextIndex();
				while (listIterator.hasNext()) {
					TextNode tmp = listIterator.next();
					if (tmp.getValue().contains("检验师")||tmp.getValue().contains("执行者")||(tmp.getValue().contains("审核")&&!tmp.getValue().contains("审核日期"))) {
						doctor = tmp.getValue().replaceAll(" ", "").replaceAll("审核", "").split(":")[1];
						break;
					}
				}
				listIterator = textNodes.listIterator(index);
				//向下查找体检项
				while (listIterator.hasNext()) {
					textNode = listIterator.next();
					if (textNode.getLeft()==60 && textNode.getHeight()==11) {//找到体检项
						//获取一行体检项
						TwoTuple<List<TextNode>, ListIterator<TextNode>> result = modelOne(listIterator, textNode, textNodes,3);
						listIterator = result.getSecond();
						List<TextNode> tmpNodeList = result.getFirst();
						StringBuffer dicNameSB  = tmpNodeList.stream().filter(n->{return n.getLeft()<=60+5 && n.getLeft() >= 60-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						StringBuffer dicResultSB = tmpNodeList.stream().filter(n->{return n.getLeft()<=153+5 && n.getLeft() >= 153-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						if (!StringUtils.isEmpty(dicNameSB.toString())) {
							SourceDictionaryDTO dictionaryDTO = new SourceDictionaryDTO();
							dictionaryDTO.setDepartName(departName);
							dictionaryDTO.setDicName(dicNameSB.toString());
							dictionaryDTO.setDicResult(dicResultSB.toString());
							dictionaryDTO.setDoctor(doctor);
							dictionaries.add(dictionaryDTO);
						}
						dicNameSB = tmpNodeList.stream().filter(n->{return n.getLeft()<=329+5 && n.getLeft() >= 329-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						dicResultSB = tmpNodeList.stream().filter(n->{return n.getLeft()<=422+5 && n.getLeft() >= 422-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						if (!StringUtils.isEmpty(dicNameSB.toString())) {
							SourceDictionaryDTO dictionaryDTO = new SourceDictionaryDTO();
							dictionaryDTO.setDepartName(departName);
							dictionaryDTO.setDicName(dicNameSB.toString());
							dictionaryDTO.setDicResult(dicResultSB.toString());
							dictionaryDTO.setDoctor(doctor);
							dictionaries.add(dictionaryDTO);
						}
					}
					if (textNode.getValue().contains("小结")) {
						textNode = listIterator.next();
						String result = new String();
						while (listIterator.hasNext() ) {
							if (textNode.getLeft()>59+5||textNode.getLeft()<59-5) {
								SourceDictionaryDTO dictionaryDTO = new SourceDictionaryDTO();
								dictionaryDTO.setDepartName(departName);
								dictionaryDTO.setDicName("小结");
								dictionaryDTO.setDicResult(result);
								dictionaryDTO.setDoctor(doctor);
								dictionaries.add(dictionaryDTO);
								listIterator.previous();
								break;
							}
							result = result + textNode.getValue();
							textNode = listIterator.next();
						}
						break;
					}
				}
			}//第一种情况结束
			//第二种情况 在中间263的位置
			if (textNode.getLeft()>=263-2&&textNode.getLeft()<=263+2&&textNode.getHeight()==16) {
				//获取科室
				departName = textNode.getValue();
				index = listIterator.nextIndex();
				while (listIterator.hasNext()) {
					TextNode tmp = listIterator.next();
					if (tmp.getValue().contains("检验师")||tmp.getValue().contains("执行者")||(tmp.getValue().contains("审核")&&!tmp.getValue().contains("审核日期"))) {
						doctor = tmp.getValue().replaceAll(" ", "").replaceAll("审核", "").split(":")[1];
						break;
					}
				}
				listIterator = textNodes.listIterator(index);
				//向下查找体检项
				while (listIterator.hasNext()) {
					textNode = listIterator.next();
					if (midleDicName(textNode)&& textNode.getHeight()==11&& !textNode.getValue().contains("检验项目") && !textNode.getValue().contains("检查项目")) {//找到体检项
						//获取一行体检项
						TwoTuple<List<TextNode>, ListIterator<TextNode>> result = modelOne(listIterator, textNode, textNodes,3);
						listIterator = result.getSecond();
						List<TextNode> tmpNodeList = result.getFirst();
						StringBuffer dicNameSB  = tmpNodeList.stream().filter(n->{return midleDicName(n);}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						StringBuffer dicResultSB = tmpNodeList.stream().filter(n->{return n.getLeft()<=177+5 && n.getLeft() >= 177-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						if (!StringUtils.isEmpty(dicNameSB.toString())) {
							SourceDictionaryDTO dictionaryDTO = new SourceDictionaryDTO();
							if(!StringUtils.isEmpty(dicNameSB.toString())) {
								dictionaryDTO.setDepartName(departName);
								dictionaryDTO.setDicName(dicNameSB.toString());
								dictionaryDTO.setDicResult(dicResultSB.toString());
								dictionaryDTO.setDoctor(doctor);
								dictionaries.add(dictionaryDTO);
							}
						}
					}
					if (textNode.getValue().contains("小结")) {
						textNode = listIterator.next();
						String result = new String();
						while (listIterator.hasNext() ) {
							if (textNode.getLeft()>76+5||textNode.getLeft()<76-5) {
								SourceDictionaryDTO dictionaryDTO = new SourceDictionaryDTO();
								dictionaryDTO.setDepartName(departName);
								dictionaryDTO.setDicName("小结");
								dictionaryDTO.setDicResult(result);
								dictionaryDTO.setDoctor(doctor);
								dictionaries.add(dictionaryDTO);
								listIterator.previous();
								break;
							}
							result = result + textNode.getValue();
							textNode = listIterator.next();
						}
						break;
					}
				}
			}//第二种情况结束
			//第三种情况 在左边61位置
			if (textNode.getLeft()>=61-2&&textNode.getLeft()<=61+2&&textNode.getHeight()==13) {
				//获取科室
				departName = textNode.getValue();
				//向下查找体检项
				index = listIterator.nextIndex();
				while (listIterator.hasNext()) {
					TextNode tmp = listIterator.next();
					if (tmp.getValue().contains("检验师")||tmp.getValue().contains("执行者")||(tmp.getValue().contains("审核")&&!tmp.getValue().contains("审核日期"))) {
						doctor = tmp.getValue().replaceAll(" ", "").replaceAll("审核", "").split(":")[1];
						break;
					}
				}
				listIterator = textNodes.listIterator(index);
				while (listIterator.hasNext()) {
					textNode = listIterator.next();
					if (textNode.getLeft()>=75-3
							&&textNode.getLeft()<=75+3 
							&& (textNode.getHeight()==11||textNode.getHeight()==10)
							&& !textNode.getValue().contains("检验项目") 
							&& !textNode.getValue().contains("检查项目")) {//找到体检项
						//获取一行体检项
						TwoTuple<List<TextNode>, ListIterator<TextNode>> result = modelOne(listIterator, textNode, textNodes,3);
						listIterator = result.getSecond();
						List<TextNode> tmpNodeList = result.getFirst();
						StringBuffer dicNameSB  = tmpNodeList.stream().filter(n->{return n.getLeft()>=75-3&&n.getLeft()<=75+3;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						StringBuffer dicResultSB = tmpNodeList.stream().filter(n->{return n.getLeft()<=264+5 && n.getLeft() >= 264-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						StringBuffer exSB = tmpNodeList.stream().filter(n->{return n.getLeft()<=370+5 && n.getLeft() >= 370-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						StringBuffer unitSB = tmpNodeList.stream().filter(n->{return n.getLeft()+n.getWidth()<=513+5 && n.getLeft()+n.getWidth() >= 513-5;}).reduce(new StringBuffer(),((sb,n)->sb.append(n.getValue())),((total1,total2)->total1=total2));
						if (!StringUtils.isEmpty(dicNameSB.toString())) {
							SourceDictionaryDTO dictionaryDTO = new SourceDictionaryDTO();
							dictionaryDTO.setDepartName(departName);
							dictionaryDTO.setDicName(dicNameSB.toString());
							dictionaryDTO.setDicResult(dicResultSB.toString());
							dictionaryDTO.setDoctor(doctor);
							dictionaryDTO.setDicExplain(exSB.toString());
							dictionaryDTO.setDicUnit(unitSB.toString());
							dictionaries.add(dictionaryDTO);
						}
					}
					if (textNode.getValue().contains("小结")) {
						textNode = listIterator.next();
						String result = new String();
						while (listIterator.hasNext() ) {
							if (textNode.getLeft()>76+5||textNode.getLeft()<76-5) {
								SourceDictionaryDTO dictionaryDTO = new SourceDictionaryDTO();
								dictionaryDTO.setDepartName(departName);
								dictionaryDTO.setDicName("小结");
								dictionaryDTO.setDicResult(result);
								dictionaryDTO.setDoctor(doctor);
								dictionaries.add(dictionaryDTO);
								listIterator.previous();
								break;
							}
							result = result + textNode.getValue();
							textNode = listIterator.next();
						}
						break;
					}
					if (textNode.getLeft()>=61-2&&textNode.getLeft()<=61+2&&textNode.getHeight()==13) {
						listIterator.previous();
						break;
					}
				}
			}
		}
		System.out.println(1);
		reportResultData.setDictionaries(dictionaries);
		reportResultData.setSourceUser(sourceUser);
		String json = JSON.toJSONString(reportResultData);
		System.out.println(json);
	}

	/**
	 * 第一种情况 
	 * =========================
	 * 			XXXXXX
	 * 体检项
	 * 			XXXXXX
	 * =========================
	 * @param listIterator
	 * @param textNode
	 * @return
	 */
	private TwoTuple<List<TextNode>, ListIterator<TextNode>> modelOne(ListIterator<TextNode> listIterator,TextNode textNode,LinkedList<TextNode> textNodes,int round){
		int index = listIterator.nextIndex();
		List<TextNode> result = new ArrayList<>();
		listIterator.previous();
		result.add(textNode);
		while (listIterator.hasPrevious()) {
			TextNode tmpNode = listIterator.previous();
			if (!modelOnePreNode(textNode,tmpNode)) {
				break;
			}
			result.add(tmpNode);
		}
		listIterator = textNodes.listIterator(index);
		while (listIterator.hasNext()) {
			TextNode nextNode = listIterator.next();
			if (!modelOneNextNode(textNode,nextNode,round)) {
				listIterator.previous();
				break;
			}
			textNode = nextNode;
			result.add(nextNode);
		}
		return new TwoTuple<List<TextNode>, ListIterator<TextNode>>(result, listIterator);
	}
	
	
	private boolean modelOnePreNode(TextNode sou,TextNode val) {
		int souT = sou.getTop();//161 
		int souB = sou.getTop()+sou.getHeight();//171
		int valB = val.getTop()+val.getHeight();//157 167
		if (souT > valB) {
			return false;
		}
		if (souB < valB) {
			return false;
		}
		if (sou.getLeft()+sou.getWidth()>=val.getLeft()) {
			return false;
		}
		return true;
	}
	
	private boolean modelOneNextNode(TextNode sou,TextNode val,int round) {
		int souB = sou.getTop() + sou.getHeight();
		int valT = val.getTop();
		if (valT>souB+round) {
			return false;
		}
		if (valT < sou.getTop()) {
			return false;
		}
		/*if (souR > valL) {
			return false;
		}*/
		return true;
	}
	
	private boolean midleDicName(TextNode textNode) {
		return (textNode.getLeft()*2+textNode.getWidth())/2>=115-2&&(textNode.getLeft()*2+textNode.getWidth())/2<=115+2;
	}
}

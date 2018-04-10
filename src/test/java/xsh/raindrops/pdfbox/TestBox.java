package xsh.raindrops.pdfbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.PDGraphicsState;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Test;

public class TestBox {

	@Test
	public void test01() throws Exception {
		File file = new File("G:\\ciming.pdf");  
        FileInputStream in = null;  
        try {  
            in = new FileInputStream(file);  
            // 新建一个PDF解析器对象  
            PDFParser parser = new PDFParser(in);  
            // 对PDF文件进行解析  
            parser.parse();  
            // 获取解析后得到的PDF文档对象  
            PDDocument pdfdocument = parser.getPDDocument();  
            // 新建一个PDF文本剥离器  
            PDFTextStripper stripper = new PDFTextStripper();  
            // 从PDF文档对象中剥离文本  
            String result = stripper.getText(pdfdocument);
            Stack<PDGraphicsState> pdfs = stripper.getGraphicsStack();
            System.out.println("PDF文件的文本内容如下：");  
            System.out.println(result);  

        } catch (Exception e) {  
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);  
            e.printStackTrace();  
        } finally {  
            if (in != null) {  
                try {  
                    in.close();  
                } catch (IOException e1) {  
                }  
            }  
        }  
	}
	
}

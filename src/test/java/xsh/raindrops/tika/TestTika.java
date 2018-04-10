package xsh.raindrops.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestTika {

	@Test
	public void test01() {
		Parser parser = new PDFParser();
		//parser.
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		InputStream stream = null;
		try {
		     
		    stream = new FileInputStream(new File("G:\\ciming.pdf"));
		    parser.parse(stream, handler, metadata, new ParseContext());
		     
		     for (String name : metadata.names()) {
		         System.out.println(name + ":\t" + metadata.get(name));
		     }
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (SAXException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (TikaException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} finally {
		    try {
		        stream.close();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		}
	}
	
}

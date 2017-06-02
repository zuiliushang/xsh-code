package xsh.raindrops.base.jpinyin;

import java.io.File;

import org.junit.Test;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 将中文转换成拼音
 * @author Raindrops on 2017年5月24日
 */
public class JpinyinTest {
	
	@Test
	public void testConvert() {
		/*String words = "丹参";
		String pinyin = null;
		final String separator = " ";

		pinyin = PinyinHelper.convertToPinyinString(words, separator, PinyinFormat.WITHOUT_TONE);
*/
		//System.out.println(pinyin);
		/*File file = new File("C:\\Users\\Raindrops\\Documents\\Feellike\\gaoxueya\\gaoxueya");
		if(file.exists() && file.isDirectory()){
			File[] childFiles = file.listFiles();
			String path = file.getAbsolutePath();
    		for(File childFile : childFiles){
    			//如果是文件
    			if(childFile.isFile()){
    				String oldName = childFile.getName();
    				String pinyin = null;
    				final String separator = "";
    				pinyin = PinyinHelper.convertToPinyinString(oldName, separator, PinyinFormat.WITHOUT_TONE);
    				String newName = oldName.substring(oldName.indexOf(""));
    				childFile.renameTo(new File(path + "\\" +  pinyin));
    			}
    		}
		}*/
	}
	
}

package xsh.raindrops.base.regex.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Main01 {

	@Test
	public void test01() {
		String result = "cm1231";
		/*Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(result);
		if (matcher.find()) {
			System.out.println(matcher.replaceFirst(""));
		}*/
		//System.out.println(Pattern.compile("\\w+").matcher(result).group(0));
		//System.out.println(result.matches("\\d+"));
		Pattern pattern = Pattern.compile("^\\d+");
		Matcher matcher = pattern.matcher(result);
		if (matcher.find()) {
			System.out.println(matcher.group(0));
		}else
			System.out.println(result);
	}
	
	@Test
	public void test02() {
		String[] mails = 
            {
                    "383688501@qq.com",
                    "374960005@qq.com",
                    "zuiliushang@haha.cc"
            };
        String mailRegEx = "\\w{3,20}@\\w+\\.(com|cc)";
        Pattern mailPattern = Pattern.compile(mailRegEx);
        Matcher matcher = null;
        for (int i = 0; i < mails.length; i++) {
            if (matcher == null) {
                matcher = mailPattern.matcher(mails[i]);
            }
            else {
                matcher.reset(mails[i]);
            }
            String result = mails[i] + (matcher.matches()?"是":"不是")+
                    "一个有效的邮件地址！";
            System.out.println(result);
        }
	}
}

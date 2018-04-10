package xsh.raindrops.struct.dom.org.w3c.dom;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class JsonTest {

	@Test
	public void test01() {
		List<SourceUser> users = new ArrayList<>();
		SourceUser sourceUser = new SourceUser();
		sourceUser.setGender("123");
		sourceUser.setIdentityNumber("sfsa");
		sourceUser.setName("raindrops");
		users.add(sourceUser);
		sourceUser = new SourceUser();
		sourceUser.setGender("123sdf");
		sourceUser.setIdentityNumber("sfsdfsa");
		sourceUser.setName("raindrops");
		users.add(sourceUser);
		sourceUser = new SourceUser();
		sourceUser.setGender("123dsf");
		sourceUser.setIdentityNumber("sfsa");
		sourceUser.setName("raindrfsdops");
		users.add(sourceUser);sourceUser = new SourceUser();
		sourceUser.setGender("12sdf3");
		sourceUser.setIdentityNumber("sfsasa");
		sourceUser.setName("raindrofdsps");
		users.add(sourceUser);sourceUser = new SourceUser();
		sourceUser.setGender("12fsd3");
		sourceUser.setIdentityNumber("sfsafsa");
		sourceUser.setName("raidfndrops");
		users.add(sourceUser);
		System.out.println(JSON.toJSONString(users));
	}
	
}

package xsh.raindrops.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import xsh.raindrops.project.entity.User;

@Component
public class TestService {
	
	@Autowired
	UserService userService;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void test() {
		User user = new User("raindrops", "hahah");
		userService.addUser(user);
		
		try {
			userService.addUserException(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

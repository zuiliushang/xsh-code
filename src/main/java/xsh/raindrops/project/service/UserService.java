package xsh.raindrops.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import xsh.raindrops.project.entity.User;
import xsh.raindrops.project.mapper.UserMapper;

@Component
public class UserService {
	
	@Autowired
	UserMapper userMapper;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void addUser(User user) {
		userMapper.insert(user);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void addUserException(User user) {
		userMapper.insert(user);
		System.out.println(1/0);
	}
	
}

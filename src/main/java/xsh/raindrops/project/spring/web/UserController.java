package xsh.raindrops.project.spring.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v/user")
public class UserController {
	
	@PostMapping("message")
	public String test(String message){
		System.out.println("/v/user/message执行 " + message);
		return message;
	}
	
}

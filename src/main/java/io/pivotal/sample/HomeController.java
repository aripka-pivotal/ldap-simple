package io.pivotal.sample;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	
	
	@RequestMapping("/")
	public String index(Model model) throws Exception {

		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		for(GrantedAuthority auth : userDetails.getAuthorities()){
			System.out.println(auth);
		}
		

		return "index";
	}

	
}

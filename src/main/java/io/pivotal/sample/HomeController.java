package io.pivotal.sample;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@GetMapping("/")
    public String index() {
		
		UserDetails userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		for(GrantedAuthority auth : userDetails.getAuthorities()){
			System.out.println(auth);
		}

        return "Welcome to the Test App";
    }
	
}

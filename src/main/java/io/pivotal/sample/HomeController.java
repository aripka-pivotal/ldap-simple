package io.pivotal.sample;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String index(Model model) throws Exception {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		for (GrantedAuthority auth : userDetails.getAuthorities()) {
			System.out.println(auth);
		}
		
		model.addAttribute("authorities", userDetails.getAuthorities());

		return "index";
	}

	// Login form
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	// Login form with error
	@RequestMapping("/login-error")
	public String loginError(Model model, HttpSession session) {
		
		if (session != null) {
			Exception ex = (Exception)session
					.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			String message = ex != null ? ex.getMessage() : "none"; 
			model.addAttribute("message", message);
		}
		
		model.addAttribute("loginError", true);
		return "login";
	}

}

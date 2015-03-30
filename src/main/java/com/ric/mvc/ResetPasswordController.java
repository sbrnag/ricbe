package com.ric.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/resetPassword")
public class ResetPasswordController {

	@RequestMapping(method = RequestMethod.GET)
	public String resetPassword(ModelMap model, @PathVariable("accessToken") String accessToken) {
		model.addAttribute("accessToken", accessToken);
		return "resetpassword";
	}

}

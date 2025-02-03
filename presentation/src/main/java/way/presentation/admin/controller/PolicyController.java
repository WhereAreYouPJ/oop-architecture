package way.presentation.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PolicyController {

	@GetMapping("/private-policy")
	public String getPolicyPage() {
		return "policy";
	}

}

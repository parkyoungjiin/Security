package com.cos.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //View 리턴.
public class IndexController {
	@GetMapping({"/", ""})
	public String index() {
		return "index";
	}
}

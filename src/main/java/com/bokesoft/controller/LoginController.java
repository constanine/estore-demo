package com.bokesoft.controller;

import com.bokesoft.service.LoginService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * @description:登陆时校验用户是否存在
 * */
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;

	@RequestMapping("/dologin")
	public String login(@RequestParam("userCode") String userCode, @RequestParam("password") String password,
			HttpServletRequest request) {

		boolean boo = loginService.check(userCode, password, request);

		// 判断用户是否存在
		if (boo) {
			// 用户存在，重定向到权限校验的controller
			return "redirect:/authority";
		} else {
			// 输入错误则返回至login.jsp并报对应错误
			return "login";
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userCode = (String) session.getAttribute(SerachFileController.USERCODE);
		// 判断用户是否为空
		if (StringUtils.isNoneBlank(userCode)) { // 用户不为空，说明此时退出登陆
			session.removeAttribute(SerachFileController.USERCODE);
			session.removeAttribute(SerachFileController.SUMFILESIZE);
			session.removeAttribute(SerachFileController.MISSIONID);
			session.removeAttribute(SerachFileController.MISSIONTYPE);
			session.removeAttribute(SerachFileController.FILESTATELIST);
			session.removeAttribute(SerachFileController.ISAUTHORIZER);
			session.removeAttribute("filenames");
			session.removeAttribute("uris");
		}
		return "login";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

}

package com.lhs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

	
	private final MemberService mService;

	@Value("#{config['site.context.path']}")
	String ctx;

	@RequestMapping("/member/goLoginPage.do")
	public String goLogin() {
		return "member/login";
	}

	@RequestMapping("/member/goRegisterPage.do")
	public String goRegisterPage() {
		return "member/register";
	}


	@RequestMapping("/member/logout.do")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		ModelAndView mv = new ModelAndView("redirect:/index.do");
		return mv;
	}

//	@RequestMapping("/member/logout.do")
//	public ModelAndView logout(HttpSession session) {
//		session.invalidate();
//		ModelAndView mv = new ModelAndView();
//		RedirectView rv = new RedirectView(ctx + "/index.do");
//		mv.setView(rv);
//		return mv;
//	}

	/*
	 * 로그인 : login.jsp : memberId ---> member_id , memberPw ---> member_pw
	 */

//	@RequestMapping("/member/login.do")
//	@ResponseBody
//	public HashMap<String, Object> login(@RequestParam HashMap<String, String> params, HttpSession session){
//		HashMap<String, Object> map = new HashMap<String, Object>();//객체 생성
//	boolean result = mService.loginCheck(params, session); // true면 로그인 성공, false면 로그인 실패
//	System.out.println("로그인을 성공하면 true : " + result);
//	
//	if(result) { // 로그인 성공이면 true
//		try {			
//			map.put("nextPage", "/index.do"); // 로그인 성공시 홈페이지로 
//			System.out.println("로그인에 성공하셨습니다!!!");
//		} catch (Exception e) {
//			e.printStackTrace();
//			// log.error("", e);
//			map.put("nextPage", "/member/goLoginPage.do"); // 로그인 실패시 로그인 페이지로
//			map.put("msg", e.getMessage()); // 
//		}
//	} else { // 로그인 실패 시
//	    map.put("nextPage", "/member/goLoginPage.do"); // 로그인 실패시 로그인 페이지로
//	    map.put("msg", "로그인 실패!");
//	}
//	return map;
//}


}

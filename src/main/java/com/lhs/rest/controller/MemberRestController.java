package com.lhs.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lhs.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RestController
public class MemberRestController {

	@Autowired
	MemberService mService;

	@Value("#{config['site.context.path']}")
	String ctx;

	@GetMapping("/member/checkId.do")
	@ResponseBody
	public HashMap<String, Object> checkId(@RequestParam HashMap<String, String> params) {// HashMap은 키와 값으로 이루어졌고 여기서
																							// 키는 문자열(String)이고 값은 임의의
																							// 객체(Object)이다.
		System.out.println("회원가입시 중복 ID확인 : " + params.get("memberId"));

		// memberId가 중복이면 1이상, memberId가 중복이 아니면 0를 반환

		int cnt = mService.checkId(params);// MemberService객체의 주소에 있는 checkId를 호출하여 중복된 아이디의 개수를 반환한다.

		HashMap<String, Object> map = new HashMap<String, Object>();// HashMap객체를 생성한다. 정보 전달하기 위한 용도로 객체 생성함.
		map.put("cnt", cnt);// "cnt"는 키(key) : map에서 값을 참조할 때 사용, cnt는 값(value) : 이 값은 중복된 아이디의 개수를 나타내는 변수
		map.put("msg", cnt == 1 ? "중복된 ID 입니다." : "");

		return map;
	}

	@PostMapping("/member/join.do")
	@ResponseBody
	public HashMap<String, Object> join(@RequestParam HashMap<String, String> params, HttpServletRequest request) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		System.out.println("memberId : " + params.get("memberId"));
		System.out.println("memberPw : " + params.get("memberPw"));
		System.out.println("email : " + params.get("email"));

		int cnt = mService.join(params);
		System.out.println("cnt : " + cnt);

		map.put("cnt", cnt);
		map.put("msg", cnt == 1 ? "회원 가입 완료!" : "회원 가입 실패!");
		String email = params.get("email"); // 수정: memberDto.params.get("email") 대신 params.get("email") 사용

		boolean emailOk = mService.Welcomeemail(email, request); // 수정: memberDto 대신 params 사용
		if (emailOk) {
			System.out.println("가입 환영 이메일 발송 완료! : " + emailOk);
		} else {
			System.out.println("가입은 완료했지만 이메일은 미발송 : " + emailOk);
			// 해결 방안
		}

		map.put("nextPage", cnt == 1 ? "/member/goLoginPage.do" : "/member/goRegisterPage.do");
		return map;
	}

	@PostMapping("/member/login.do")
	@ResponseBody
	public HashMap<String, Object> login(@RequestParam HashMap<String, String> params, HttpSession session) {
		// HttpSession session: 세션 객체를 전달
		HashMap<String, Object> map = new HashMap<String, Object>();// 객체 생성
		try {
			boolean result = mService.login(params, session);// mService
			System.out.println("로그인을 성공하면 true : " + result);
			if (!result) {
				map.put("msg", "로그인실패");
				System.out.println("로그인을 실패하면 false : " + map);
			}
			map.put("nextPage", "/index.do");// 로그인 실패이므로 index.do로 이동한다.
		} catch (Exception e) {// 예외처리
			e.printStackTrace();
			log.error("", e);
			map.put("nextPage", "/index.do");
			map.put("msg", e.getMessage());
		}
		return map;
	}

	@RequestMapping("/admin/memberList.do")
	@ResponseBody // 비동기식 호출
	public HashMap<String, Object> memberList(@RequestParam HashMap<String, Object> params) {
		// 페이징
		// 모든 회원 가져오기
		List<HashMap<String, Object>> memberList = new ArrayList<HashMap<String, Object>>();

		// go to JSP

		HashMap<String, Object> result = new HashMap<String, Object>();
		// 정해진 키 이름으로 넘겨주기..
		result.put("page", params.get("page")); // 현재 페이지
		result.put("rows", memberList); // 불러온 회원목록
		result.put("total", 1);// 전체 페이지
		result.put("records", 10); // 전체 회원수

		return result;

	}

	@RequestMapping("/member/delMember.do")
	@ResponseBody
	public HashMap<String, Object> delMember(@RequestParam HashMap<String, Object> params) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int result = 0;
		map.put("msg", (result == 1) ? "삭제되었습니다." : "삭제 실패!");
		map.put("result", result);
		return map;

	}
}

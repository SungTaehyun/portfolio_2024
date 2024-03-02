package com.lhs.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.lhs.dto.Member;
import com.lhs.exception.PasswordMissMatchException;
import com.lhs.exception.UserNotFoundException;

public interface MemberService {

	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params);
	
	//총 회원수 for paging
	public int totalMemberCnt(HashMap<String, Object> params);

	public int join(HashMap<String, String> params);
	
	public int checkId(HashMap<String, String> params);//0또는 1인지 유무에 따라 상태를 나타내기 떄문에 int를 사용
	
	public int delMember(HashMap<String,Object> params);

	public boolean login(HashMap<String, String> params, HttpSession session)throws UserNotFoundException, PasswordMissMatchException;//result값이 ture인지 false인지 상태에 따른 결과로 boolean을 사

	// 로그인시 ID와 비밀번호가 일치하는지 확인
}
package com.lhs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhs.dao.MemberDao;
import com.lhs.exception.PasswordMissMatchException;
import com.lhs.exception.UserNotFoundException;
import com.lhs.service.MemberService;
import com.lhs.util.Sha512Encoder;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired MemberDao mDao; 

	@Override
	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params) {
		return mDao.memberList(params);
	}
	
	//총회원수
	@Override
	public int totalMemberCnt(HashMap<String, Object> params) {
		return mDao.totalMemberCnt(params);
	}

	@Override
	public int join(HashMap<String, String> params) {
		System.out.println("params111111111111 : " +  params );
//		int result = mDao.join(params);
//		System.out.println("result111111111111 : " + result);
//		return result;
		String passwd = params.get("memberPw");
		
		String encodeTxt = Sha512Encoder.getInstance().getSecurePassword(passwd);
		params.put("memberPw", encodeTxt);
		return mDao.join(params); //return mDao.join(params);는 DAO 객체의 join 메서드를 호출하여 데이터베이스 작업을 실행하고 결과를 반환합니다. 반환 값은 일반적으로 작업의 성공 또는 실패 여부를 나타냅니
	}

	@Override
	public int checkId(HashMap<String, String> params) {
		System.out.println("params22222222222222222 : " +  params );
		
		return mDao.checkId(params);
	}

	@Override
	public HashMap<String, Object> login(HashMap<String, String> params) throws UserNotFoundException, PasswordMissMatchException {
		HashMap<String, Object> member = mDao.getMemberById(params);
		return member;
	}

	@Override
	public int delMember(HashMap<String, Object> params) {	
		return mDao.delMember(params);
	}



	

}

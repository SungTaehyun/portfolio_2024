package com.lhs.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.lhs.dto.Member;

@Repository
public interface MemberDao {
	
	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params);

	public int totalMemberCnt(HashMap<String, Object> params);

	public int join(HashMap<String, String> params);
	
	public int checkId(HashMap<String, String> params);
	
	public Member getMemberById(HashMap<String, String> params);
	
	public String makeCipherText(HashMap<String, String> params);
	
	public int delMember(HashMap<String,Object> params);
	
}
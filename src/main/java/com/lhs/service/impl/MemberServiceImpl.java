package com.lhs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.lhs.dao.MemberDao;
import com.lhs.dto.EmailDto;
import com.lhs.dto.Member;
import com.lhs.exception.PasswordMissMatchException;
import com.lhs.exception.UserNotFoundException;
import com.lhs.service.MemberService;
import com.lhs.util.EmailUtil;
import com.lhs.util.Sha512Encoder;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDao mDao;

	// 이메일을 발송하기 위해서 주입
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public ArrayList<HashMap<String, Object>> memberList(HashMap<String, Object> params) {
		return mDao.memberList(params);
	}

	// 총회원수
	@Override
	public int totalMemberCnt(HashMap<String, Object> params) {
		return mDao.totalMemberCnt(params);
	}

	/*
	 * 회원가입 : register.jsp memberPw ---> member_pw
	 */
	@Override
	public int join(HashMap<String, String> params) { // HashMap<String, String>은 맵 자료구조를 선언하고, params는 이 맵을 담아서 메서드에
														// 전달하는 역할
		// *비밀번호 암호화
		Sha512Encoder encoder = Sha512Encoder.getInstance(); // 1. SHA-512 암호화를 위한 인스턴스를 얻는다
		System.out.println("encoder : " + encoder); // com.feb.test.util.Sha512Encoder@7c3a98fc

		String passwd = params.get("memberPw"); // 2. 브라우저에서 입력한 비밀번호
		System.out.println("memberPw : " + params.get("memberPw")); // dig404

		String encodeTxt = encoder.getSecurePassword(passwd); // 3. 비밀번호 암호화
		System.out.println("memberPw : " + encodeTxt); // 3c9909afec25354d551dae21590...

		params.put("memberPw", encodeTxt); // 4. 암호화한 패쓰워드로 저장

		return mDao.join(params);
	}

	@Override
	public int checkId(HashMap<String, String> params) {
		return mDao.checkId(params);
	}

	@Override
	public int delMember(HashMap<String, Object> params) {
		return mDao.delMember(params);
	}

	// 로그인시 ID와 비밀번호가 일치하는지 확인
	@Override
	public boolean login(HashMap<String, String> params, HttpSession session)
			throws UserNotFoundException, PasswordMissMatchException {
		// 사용자가 입력한 정보와 일치하는 유저 찾기

		// 1. id비교
		String memberId = params.get("memberId"); // 브라우저에서 입력한 ID
		// tip : select시에 값이 없으면 null인데 JDBC는 tye-catch문으로 했었는데 mybatis에서는 if문으로 처리 가능하다
		Member member = mDao.getMemberById(params); // 동일한 id가 없을 경우 null, 동일한 id가 있을경우 MemberDto 반환
		System.out.println("동일한 id가 없을 경우 null : " + member);

		if (ObjectUtils.isEmpty(member)) { // null이면 true 반환
			throw new UserNotFoundException();
		}

		// 2. 비밀번호 비교
		String passwd = params.get("memberPw"); // 사용자가 입력한 비밀번호
		System.out.println("사용자가 입력한 비밀번호 : " + passwd); // 123

		String memberPw = member.getMemberPw(); // DB에 저장되어 있는 암호화 된 비밀번호
		System.out.println("DB에 저장되어 있는 암호화 된 비밀번호  : " + memberPw); // 3c9909afec25354...

		Sha512Encoder encoder = Sha512Encoder.getInstance();
		System.out.println("encoder  : " + encoder); // com.lhs.util.Sha...

		String encodeTxt = encoder.getSecurePassword(passwd); // 사용자가 입력한 값을 암호화한 거다
		System.out.println("사용자가 입력한 값을 암호화한 거다  : " + encodeTxt); // 3c9909afec25354...

		if (member.getMemberPw().equals(encodeTxt)) {
			session.setAttribute("memberId", member.getMemberId());
			session.setAttribute("memberNick", member.getMemberNick());
			session.setAttribute("memberType", member.getTypeSeq());
			return true;
		} else {
			throw new PasswordMissMatchException();
		}
	}

	@Override
	public boolean Welcomeemail(String email, HttpServletRequest request) {
		Member emailchick = mDao.Welcomeemail(email); // null이면 발송x

		if (emailchick != null) { // null이 아니면 true
			EmailDto emailDto = new EmailDto();
			emailDto.setFrom("jbw02003@naver.com"); // 이메일을 발신하는 사람의 이메일 주소
			emailDto.setReceiver(email); // 이메일을 수신하는 사람의 이메일 주소, 회원가입한 이메일로 메일 발송

			String link = "https://www.notion.so/1-9c3f8a44ffb24dcba09699d45783d16c"; // 이메일 본문에 추가할

			String text = "스프링 프로젝트 :  " + link; // 이메일 본문 내용에 링크 추가
			emailDto.setText(text); // 이메일의 본문 내용 설정

			emailDto.setSubject("가입을 축하합니다!!!"); // 이메일의 제목
			System.out.println("emailDto 내용 확인 : " + emailDto.toString()); // com.lhs.dto.EmailDto@22ee9ac6

			try {
				// 이메일 발송 실패 시 예외처리
//				emailUtil.sendMail(emailDto); // @Autowired 타입으로 주입받고 사용
				 emailUtil.sendHtmlMail(emailDto);

				// 어떤 값으로 이루어진 이메일을 발송했는지 확인해보기
				System.out.println("이메일을 발신하는 사람의 이메일 주소 : " + emailDto.getFrom());
				System.out.println("이메일을 수신하는 사람의 이메일 주소 : " + emailDto.getReceiver()); 
				System.out.println("이메일의 본문 내용 설정 : " + emailDto.getText()); 
				System.out.println("이메일의 제목 : " + emailDto.getSubject()); // 가입을 축하합니다!!!
			} catch (Exception e) {
				request.setAttribute("errorMessage", "이메일 발송에 실패했습니다");
				System.out.println("이메일 발송 실패 : " + emailUtil.toString());
				return false;
			}
		}
		return true;
	}
}
package com.lhs.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.service.AttFileService;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

@Controller
public class BoardController {

	@Autowired
	BoardService bService;
	@Autowired
	AttFileService attFileService;
	@Autowired
	FileUtil fileUtil;

	private String typeSeq = "2";

	@RequestMapping("/board/list.do") // 이 메서드는 "/board/list.do" 경로로 들어오는 HTTP 요청을 처리
	public ModelAndView goLogin(@RequestParam HashMap<String, String> params) { // HTTP 요청의 쿼리 파라미터들을 HashMap으로 받음
		System.out.println("tgregtrgetrgec:" + params); // 전달된 파라미터들을 출력한다. // {}출력됨.

		ModelAndView mv = new ModelAndView(); // ModelAndView 객체를 생성

		if (!params.containsKey("typeSeq")) { // 만약 파라미터 중에 "typeSeq"라는 키가 없다면,
			params.put("typeSeq", this.typeSeq); // 기본값으로 설정된 키인 typeSeq에 this.typeSeq넣고 파라미터에 수정(추가)한다.
			System.out.println("11111111111111:" + params); // 변경된 파라미터들을 출력 //{typeSeq=2}출력됨
		} // 위 코드는 boardDto.setTypeSeq(typeSeq);와 같은 역할을 한다??

		ArrayList<HashMap<String, Object>> key = bService.list(params); // bService의 list 메서드를 호출하여 params를 전달하고, 그 결과를
																		// key 변수에 할당합니다. 이 메서드는
																		// ArrayList<HashMap<String, Object>> 타입의 객체를
																		// 반환합니다.
		mv.setViewName("board/list"); // ModelAndView 객체인 mv의 뷰 이름을 "board/list"로 설정합니다.

		mv.addObject("key", key); // ModelAndView 객체인 mv에 모델 데이터를 추가합니다. "key"는 모델 데이터의 이름이 되고, key 변수에 할당된
	//key는 jsp에 전달되어 item으로 이용된다.								// ArrayList<HashMap<String, Object>> 객체가 모델 데이터가 됩니다.
		// ArrayList<HashMap<String, Object>> 객체인 key에는 게시판 목록 페이지에 표시될 게시글 데이터가 포함된다.
		for (HashMap<String, Object> map : key) { // 게시글 목록(key)을 순회하면서,
			Object boardSeq = map.get("boardSeq"); // 각 게시글의 일련 번호(boardSeq)를 가져옴
//			System.out.println("boardSeq: " + boardSeq); // 일련 번호를 출력
//			boardSeq = map.get("board_seq"); // "board_seq"라는 키로도 일련 번호를 가져오려고 하지만, 이 코드는 의미가 없다.
//			System.out.println("boardSeq: " + boardSeq); // 따라서 동일한 값이 출력
		}

		return mv; // ModelAndView 객체를 반환하여 처리 결과를 클라이언트에게 전달합니다.
	}

	@RequestMapping("/test.do")
	public ModelAndView test() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("test");
		return mv;
	}

	// 글쓰기 페이지로
	@RequestMapping("/board/goToWrite.do")
	public ModelAndView goToWrite(@RequestParam HashMap<String, Object> params) {
		System.out.println("roundgetss:" + params);
		
		if (!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/board/write");
		return mv;
	}

	@RequestMapping("/board/write.do")
	@ResponseBody // 게시물 번호 = boardseq
	public HashMap<String, Object> write(@RequestParam HashMap<String, Object> params,
			MultipartHttpServletRequest mReq) { // MultipartHttpServletRequest mReq는 파일 업로드와 관련된 데이터를 처리할 수 있다.
		// @RequestParam HashMap<String, Object> params, MultipartHttpServletRequest
		// mReq)을 Dto로 변경하면 코드를 더 깔끔하게 만들 수 있습니다.
		System.out.println("params파람 : " + params);// params통해 출력된 정보를 토대로 boardDto를 만들면 된다.

		if (!params.containsKey("typeSeq")) {// params맵에 typeSeq(게시물번호)가 존재하지 않으면
			params.put("typeSeq", this.typeSeq);// , this.typeSeq값을 기본으로 한다. 여기서 this는 본래의 typeSeq값(즉, 위에서 선언한 private
												// String typeSeq = "2";를 의미한다.)
			System.out.println(mReq);// mReq 출력해봄
		}

		bService.write(params, mReq.getFiles("attFiles")); // 서비스 객체(bService)의 write 메서드를 호출하고,
															// mReq.getFiles("attFiles")는 파일업로드(mReq)와 관련된 파일을 추출하여
															// params에 담는다???
		// 여기서 params는 게시글과 관련된 정보를 담고 있는 맵, "attFiles"는 파일 업로드 폼에서 정의한 파일 필드의 이름
		return null;// 반환타입은 아무런 값을 반환하지 않음
	}

	@RequestMapping("/board/downdload.do")
	@ResponseBody
	public byte[] downdloadFile(@RequestParam int fileIdx, HttpServletResponse rep) {
		// 1.받아온 파람의 파일 pk로 파일 전체 정보 불러온다. -attFilesService필요!
		HashMap<String, Object> fileInfo = null;

		// 2. 받아온 정보를 토대로 물리적으로 저장된 실제 파일을 읽어온다.
		byte[] fileByte = null;

		if (fileInfo != null) { // 지워진 경우
			// 파일 읽기 메서드 호출
			fileByte = fileUtil.readFile(fileInfo);
		}

		// 돌려보내기 위해 응답(httpServletResponse rep)에 정보 입력. **** 응답사용시 @ResponseBody 필요 !!
		// Response 정보전달: 파일 다운로드 할수있는 정보들을 브라우저에 알려주는 역할
		rep.setHeader("Content-Disposition", "attachment; filename=\"" + fileInfo.get("file_name") + "\""); // 파일명
		rep.setContentType(String.valueOf(fileInfo.get("file_type"))); // content-type
		rep.setContentLength(Integer.parseInt(String.valueOf(fileInfo.get("file_size")))); // 파일사이즈
		rep.setHeader("pragma", "no-cache");
		rep.setHeader("Cache-Control", "no-cache");

		return fileByte;

// /board/download.do?fileIdx=1
	}

//// 읽기
//	@RequestMapping("/board/read.do")
//	public ModelAndView read(BoardDto boardDto) {
//	    // HTTP 요청 파라미터들이 자동으로 BoardDto 객체에 바인딩됨
//	    System.out.println("핑핑퐁 : " + boardDto); // boardDto 객체에 어떤 값이 담겨 있는지를 콘솔에 출력
//
//	    // typeSeq 속성이 null인지 확인하고, null이면 기본값(this.typeSeq)으로 설정
//		// boardDto.setTypeSeq(typeSeq);
//	    if(boardDto.getTypeSeq() == null) {
//	        boardDto.setTypeSeq(this.typeSeq); // 클래스 내에 선언된 다른 변수인 this.typeSeq를 사용하여 기본값 설정
//	    }
//
//	    // 새로운 ModelAndView 객체 생성(요청을 처리하고 그 결과를 보여주기 위한 뷰를 결정하기 위해서 객체생성함)
//	    ModelAndView mv = new ModelAndView();
//	    // 뷰 이름을 "/board/read"로 설정
//	    mv.setViewName("/board/read");
//
//	    // 설정된 ModelAndView 객체 반환
//	    return mv;
//	}

//	@RequestMapping("/board/read.do")
//	public ModelAndView read(BoardDto boardDto) {
//		boardDto.setTypeSeq(typeSeq);
//
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("/board/read");
//		return mv;
//	}

	@RequestMapping("/board/read.do")
	public ModelAndView read(@RequestParam HashMap<String, Object> params) {
		System.out.println("bobobobo:" + params);// 찍어보기

		if (!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
			System.out.println("uuuuuuuuuuuu:" + params);
		}

		HashMap<String, Object> boardread = bService.read(params); // bService의 list 메서드를 호출하여 params를 전달하고, 그 결과를 member 변수에 할당
		System.out.println("boardread11111:"+boardread);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("boardread",boardread);//"boardread"라는 키에 boardread값을 담는다.
		mv.addObject("boardSeq",params.get("boardSeq"));
		mv.setViewName("/board/read");
		return mv;
	}
	
//	System.out.println("READ PARAMS~~~~~~~~~~~~~~~~~~~~~~       " + params);
//	ModelAndView mv = new ModelAndView();
//	HashMap<String, Object> memberList = bService.read(params);
//	System.out.println("memberLISt                        " + memberList);
//	mv.addObject("memberL", memberList);
//	mv.addObject("boardS", params.get("boardSeq"));
//	mv.setViewName("/board/read");
//	return mv;
//}	

	// 수정 페이지로
	@RequestMapping("/board/goToUpdate.do")
	public ModelAndView goToUpdate(@RequestParam HashMap<String, Object> params, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		if (!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}

		return mv;

	}

	@RequestMapping("/board/update.do")
	@ResponseBody // !!!!!!!!!!!! 비동기 응답
	public HashMap<String, Object> update(@RequestParam HashMap<String, Object> params,
			MultipartHttpServletRequest mReq) {

		if (!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}

		return null;
	}

	@RequestMapping("/board/delete.do")
	@ResponseBody
	public HashMap<String, Object> delete(@RequestParam HashMap<String, Object> params, HttpSession session) {
		System.out.println("deleteasdsdasd: " + params);
		if (!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}
		System.out.println("delete222222222222222:" + params);
		
		int boardInfo = bService.delete(params);//boardInfo에 delete메서드에서의 params값을 들고와서 담는다.
		System.out.println("boardinfo1111111111111:"+boardInfo);
		
		
		return null; // 비동기: map return
	}
	
	
	@RequestMapping("/board/deleteAttFile.do")
	@ResponseBody
	public HashMap<String, Object> deleteAttFile(@RequestParam HashMap<String, Object> params) {

		if (!params.containsKey("typeSeq")) {
			params.put("typeSeq", this.typeSeq);
		}
		return null;
	}

}

package com.lhs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;
import com.lhs.dto.pageDto;
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

	@RequestMapping("/board/list.do")
	public ModelAndView golist(BoardDto boardDto, pageDto pagedto) {
		// 페이지dto 생성

		// 1. 현재 페이지 값 확인 및 설정(if-else사용)
		/// 현재 페이지 값이 없거나 빈 문자열인 경우
		if (pagedto.getCurrentPage() == 0 || pagedto.getCurrentPage() == null) {
//	    	pagedto.setStartPage();   // 전달된 현재 페이지 값이 없으면, 시작 게시글 위치를 설정
			pagedto.setStartPage((pagedto.getCurrentPage() - 1) * 10); // 전달된 현재 페이지 값이 없으면, 시작 게시글 위치를 설정
			System.out.println("페이지출력값확인 : " + (pagedto.getCurrentPage() - 1) * 10);
		} else {// 전달된 현재 페이지 값이 있는 경우, 시작 게시글 위치를 설정하고 현재 페이지를 설정
			pagedto.setStartPage((pagedto.getCurrentPage() - 1) * 10); // 시작 게시글 위치 설정
			System.out.println("페이지출력값확인2222222222222222222 : " + (pagedto.getCurrentPage() - 1) * 10);
//	    	 pagedto.setStartPage(1); // 시작 게시글 위치 설정
		}

		// 2. 페이지 크기 설정
		pagedto.getPageSize();

		// 3. typeseq값 확인 및 설정
		if (boardDto.getTypeSeq() == null) {
			boardDto.setTypeSeq(Integer.parseInt(this.typeSeq));// Integer.parseInt을 이용해서 typeSeq을 형변환한다.
		}

		// 4. 게시물 목록 조회하기

		HashMap<String, Object> params1 = new HashMap<String, Object>();
		params1.put("typeSeq", this.typeSeq);
		params1.put("startPage", pagedto.getStartPage());
		params1.put("pageSize", pagedto.getPageSize());
		ArrayList<BoardDto> key = bService.list(params1);
		System.out.println("asldkjlaksjd:" + key); // {member_id=jbw02003, hits=37, board_seq=7,...

		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/list");
		mv.addObject("key", key);

		// 5. 페이지 정보 설정(페이지 네비게이션을 구성하기 위해 시작 네비게이션과 최대 네비게이션 값을 설정)
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("typeSeq", typeSeq);

		pagedto.setTotalPage(bService.getTotalArticleCnt(params)); // pageDto객체의 getTotalArticleCnt메서드를 사용해서 전체 게시물 수를
																	// 설정
		pagedto.setStartNavi(pagedto.getCurrentPage() - 1 / pagedto.getPageNaviSize() * pagedto.getPageNaviSize() + 1);// 시작
																														
		pagedto.setEndNavi(pagedto.getStartNavi() + 9); // 최대 네비게이션 설정하기(최대 네비는 전체 페이지 수를 기준으로 표시할 페이지 네비의 최대 값이다.예를 들어,
														// 전체 페이지 수가 23이고 페이지 네비게이션의 크기가 5이면 최대 네비게이션은 5가 됨.)
		// 디버깅을 위해 list view로 전달되는 내용 출력

		// 6. 페이지 정보를 모델에 추가하여 뷰로 전달
		mv.addObject("pagedto", pagedto);

		// 6. 게시물 목록과 페이지 정보를 뷰에 전달하기
		return mv;
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

	@RequestMapping("/board/write.do") // list.jsp에서
	@ResponseBody // 게시물 번호 = boardseq
	public int write(@RequestParam HashMap<String, Object> params, MultipartHttpServletRequest mReq) { // MultipartHttpServletRequest
																										// mReq는 파일 업로드와
																										// 관련된 데이터를 처리할
																										// 수 있다.
		// @RequestParam HashMap<String, Object> params, MultipartHttpServletRequest
		// mReq)을 Dto로 변경하면 코드를 더 깔끔하게 만들 수 있습니다.
		System.out.println("params파람 : " + params);// params통해 출력된 정보를 토대로 boardDto를 만들면 된다.

		if (!params.containsKey("typeSeq")) {// params맵에 typeSeq(게시물번호)가 존재하지 않으면
			params.put("typeSeq", this.typeSeq);// , this.typeSeq값을 기본으로 한다. 여기서 this는 본래의 typeSeq값(즉, 위에서 선언한 private
												// String typeSeq = "2";를 의미한다.)
			System.out.println("파일업로드와 관련된 데이터출력               :" + mReq);// mReq 출력해봄
			System.out.println("typeSeq 출력하기                :" + typeSeq);
		}

		int result = bService.write(params, mReq.getFiles("attFiles"));

		return result; // 게시글 작성 결과를 반환합니다. 반환되는 값은 게시글의 번호일 것으로 예상
	}

	@RequestMapping("/board/download.do")
	@ResponseBody
	public byte[] downloadFile(@RequestParam int fileIdx, HttpServletResponse rep) {
	    System.out.println("파일 번호 값 출력1111111111111111111111 :" + fileIdx);
	    // 파일 전체 정보 받아오기 
	    AttFileDto fileInfo = bService.getFileInfo(fileIdx);

	    byte[] fileByte = null;

	    if (fileInfo != null) { // 지워진 경우
	        // 파일 읽기 메서드 호출
	        fileByte = fileUtil.readFile(fileInfo);
	    }

	    // 파일명이 한글이면 오류가 발생하기에 꼭 utf-8로 해줘야 한다.
	    String fileName = UriUtils.encode(fileInfo.getFileName(), "utf-8");

	    rep.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\""); 
	    rep.setContentType(fileInfo.getFileType()); // content-type
	    rep.setContentLength(fileInfo.getFileSize()); // 파일사이즈
	    rep.setHeader("pragma", "no-cache");
	    rep.setHeader("Cache-Control", "no-cache");

	    return fileByte;
	}


	@RequestMapping("/board/read.do")
	public ModelAndView read(@ModelAttribute("BoardDto") BoardDto boardDto) {
		System.out.println("/board/list에서 가져온 값 확인 " + boardDto);
		ModelAndView mv = new ModelAndView();

		if (boardDto.getTypeSeq() == null) {
			boardDto.setTypeSeq(Integer.parseInt(this.typeSeq));
		}

		BoardDto boardread = bService.read(boardDto);
		System.out.println("dalsfkjaslkdjf:" + boardread);// BoardDto [boardSeq=12, typeSeq=2, memberId=jbw02003,
															// memberNick=123, title=totam tempore at libero neq
		
		// 1. 첨부파일 읽기1 : 해당게시글(typeSeq, board_seq)의 모든첨부파일(다중이니까)
				List<AttFileDto> attFiles = attFileService.readAttFiles(boardread); // boardSeq=10776, typeSeq=2
				System.out.println("해당 게시물의 모든 첨부파일 : " + attFiles);
		
				// 조회수 +1
				bService.updateHits(boardread);
		
		// 댓글 리스트 가져오기(댓글 구현부분)
		////////////////////
		////////////////////
		
		// 1. 첨부파일 읽기1
		mv.addObject("attFiles", attFiles);
		mv.addObject("boardread", boardread);
		System.out.println("boardread값 출력 하기 :" + boardread);
		mv.setViewName("/board/read");
		return mv;
	}

	@RequestMapping("/board/goToUpdate.do")
	public ModelAndView goToUpdate(BoardDto boardDto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		System.out.println("asdasdassczdv " + boardDto); // boardSeq=13
		// typeSeq가 boardDto에 없을 경우, 기존의 typeseq 기본값을 넣는다.
		if (boardDto.getTypeSeq() == null) {
			boardDto.setTypeSeq(Integer.parseInt(this.typeSeq));
		}

		// 게시글 정보 읽어오기(bService의 read메서드에서 params에 담아서 객체에 저장
		BoardDto listupdate = bService.read(boardDto);

		System.out.println("aslkjadawisnk:" + listupdate);// BoardDto [boardSeq=13, typeSeq=2, memberId=jbw02003,
															// memberNick=123, title=harum hits=12, createDtm=null,
															// updateDtm=null, hasFile=nul

		// ModelAndView 객체에 게시물 정보 추가
		mv.addObject("listUpdate", listupdate);

		// 첨부 파일 정보
		ArrayList<AttFileDto> attFiles = bService.readFile(boardDto);
		mv.addObject("attFiles", attFiles);
		mv.addObject("curentPage", boardDto.getPage());

		// 뷰 이름 설정(update.jsp로 이동하게 하기 위해)
		mv.setViewName("/board/update");

		// 값이 들어오는지 객체 찍어보기
		System.out.println("리스트업데이트                 :" + listupdate); // BoardDto [boardSeq=13, typeSeq=2,
																		// memberId=jbw02003, memberNick=123,
																		// title=harum ............

		return mv;
	}

	@RequestMapping("/board/update.do")
	@ResponseBody // !!!!!!!!!!!! 비동기 응답
	public HashMap<String, Object> update(@ModelAttribute("BoardDto") BoardDto boardDto) {
		// 게시물 정보 출력해보기
		System.out.println("게시물출력해보기 : " + boardDto);

		// typeSeq가 없을 경우, 기본값 typeSeq를 사용한다.
		if (boardDto.getTypeSeq() == null) {
			boardDto.setTypeSeq(Integer.parseInt(this.typeSeq));
		}

		// 게시물을 업데이트하는 메서드를 호출하기
		int cnt = bService.update(boardDto, null);

		// 업데이트 결과를 확인하기 위해 콘솔에 출력하기(만약 업데이트가 성공했다면 result 값은 1이 될 것)
		System.out.println("게시물업데이트결과값확인 : " + cnt);

		// 결과를 담은 HashMap 생성
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("ASDFKJALSKDF:" + map);

		// 1. 업데이트 결과 카운트. 성공했을 경우 1, 실패했을 경우 0
		map.put("cnt", cnt);
		// 2. 업데이트 결과 메시지) 성공했을 경우 "게시물 업데이트 완료", 실패했을 경우 "게시물 업데이트 실패"가 출력
		map.put("msg", cnt == 1 ? "게시물 업데이트 완료" : "게시물 업데이트 실패");
		// 3. 업데이트 성공 시, 다음 페이지 경로
		map.put("nextPage", cnt == 1 ? "/board/list.do" : "/board/list.do");
		return map;
	}

	// 게시글 삭제 요청을 처리하는 핸들러 메서드
	@RequestMapping("/board/delete.do")
	@ResponseBody
	public HashMap<String, Object> delete(BoardDto boarddto, HttpSession session) {
	    // 받아온 DTO 값 출력
	    System.out.println("read.jsp에서 받아오는 dto값 : " + boarddto);
	    
	    // 게시글 유형 시퀀스가 없는 경우 기본 값 설정
	    if (boarddto.getTypeSeq() == null) {
	        boarddto.setTypeSeq(Integer.parseInt(this.typeSeq));
	    }
	    
	    // 삭제할 게시글 정보 출력
	    System.out.println("delete222222222222222:" + boarddto);

	    // 게시글 삭제 서비스 호출 및 결과 수신
	    int boardInfo = bService.delete(boarddto);
	    // 삭제된 게시글 정보 출력
	    System.out.println("boardinfo1111111111111:" + boardInfo);

	    return null; // 비동기 요청에 대한 응답으로 일단은 null 반환
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

package com.lhs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;
import com.lhs.dto.pageDto;
import com.lhs.service.AttFileService;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

import lombok.RequiredArgsConstructor;

	@Controller
	@RequiredArgsConstructor
	public class BoardController {

		private final BoardService bService;
		private final AttFileService attFileService;
		private final FileUtil fileUtil;

		private String typeSeq = "2";

	@RequestMapping("/board/list.do")
	public ModelAndView list(BoardDto boardDto, pageDto pagedto, @RequestParam(required = false) String keyword) {
		System.out.println("keyword값 들어오는지 확인 : " + keyword);
		System.out.println("pagedto값 들어오는지 확인 : " + pagedto);//pagedto값 들어오는지 확인 : pageDto [pageSize=10, pageNaviSize=10, offset=0, keyword=, option=, startNavi=null, totalPage=null, currentPage=1, endNavi=null, startPage=null]
		
	    // 페이지dto 생성
	    if (pagedto.getCurrentPage() == null || pagedto.getCurrentPage() == 0) {
	        pagedto.setCurrentPage(1); // 현재 페이지가 없는 경우 1페이지로 설정
	    }
	    pagedto.setStartPage((pagedto.getCurrentPage() - 1) * 10); // 시작 게시글 위치 설정

	    // 2. 페이지 크기 설정
	    pagedto.setPageSize(10); // 페이지 크기 설정 (한 페이지에 표시되는 게시물 수)

	    // 3. typeseq값 확인 및 설정
	    if (boardDto.getTypeSeq() == null) {
	        boardDto.setTypeSeq(Integer.parseInt(this.typeSeq)); // Integer.parseInt을 이용해서 typeSeq을 형변환한다.
	    }

	    List<BoardDto> key;
	    if (keyword != null && !keyword.isEmpty()) {
	    	 System.out.println("키워드가 존재합니다.");
	        // 키워드가 존재하는 경우 검색 기능 수행
	        HashMap<String, Object> params = new HashMap<>();
	        params.put("typeSeq", this.typeSeq); // 게시글 유형 시퀀스 설정
	        params.put("keyword", keyword); // 검색 키워드 설정
	        params.put("option", pagedto.getOption()); // dhqtus 키워드 설정
	        params.put("startPage", pagedto.getStartPage()); // 페이지 시작 위치 설정
	        params.put("pageSize", pagedto.getPageSize()); // 페이지 크기 설정
	        key = bService.searchSelectPage(params); // 검색된 결과를 key에 할당
	        System.out.println("검색 결과(key): " + key); // 검색 결과를 로그에 출력
	    } else {
	    	System.out.println("키워드가 존재하지 않습니다.");
	        // 키워드가 존재하지 않는 경우 기존 목록 조회
	        HashMap<String, Object> params = new HashMap<>();
	        params.put("typeSeq", this.typeSeq); // 게시글 유형 시퀀스 설정
	        params.put("startPage", pagedto.getStartPage()); // 페이지 시작 위치 설정
	        params.put("pageSize", pagedto.getPageSize()); // 페이지 크기 설정
	        key = bService.list(params); // 목록 조회 결과를 key에 할당
	        System.out.println("기존 목록 조회 결과(key): " + key); // 조회 결과를 로그에 출력
	    }

	    // ModelAndView 객체 생성
	    ModelAndView mv = new ModelAndView();
	    mv.setViewName("board/list"); // 뷰 이름 설정
	    mv.addObject("key", key); // "key"라는 이름으로 key 객체를 전달

	    // 페이지 정보 설정(페이지 네비게이션을 구성하기 위해 시작 네비게이션과 최대 네비게이션 값을 설정)
	    HashMap<String, String> paramsForTotalArticleCnt = new HashMap<>();
	    paramsForTotalArticleCnt.put("typeSeq", typeSeq); // 게시글 유형 시퀀스 설정

	    // 게시글의 총 수를 구해서 페이지 정보에 설정
	    int totalArticleCnt = bService.getTotalArticleCnt(paramsForTotalArticleCnt); // 전체 게시글 수를 가져옴
	    pagedto.setTotalPage((totalArticleCnt + pagedto.getPageSize() - 1) / pagedto.getPageSize()); // 전체 페이지 수 설정

	    int pageNaviSize = pagedto.getPageNaviSize(); // 페이지 네비게이션 크기 설정
	    pagedto.setStartNavi((pagedto.getCurrentPage() - 1) / pageNaviSize * pageNaviSize + 1); // 시작 네비게이션 설정
	    pagedto.setEndNavi(Math.min(pagedto.getStartNavi() + pageNaviSize - 1, pagedto.getTotalPage())); // 최대 네비게이션 설정

	    mv.addObject("pagedto", pagedto); // "pagedto"라는 이름으로 페이지 정보 객체를 전달

	    return mv; // ModelAndView 객체 반환
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

	

	@RequestMapping("/board/read.do")
	public ModelAndView read(BoardDto boardDto, @ModelAttribute("pagedto") pageDto pagedto) {
	    System.out.println("/board/list에서 가져온 값 확인 " + boardDto);
	    ModelAndView mv = new ModelAndView();
	    System.out.println("pagedto의 값 확인하자 : " + pagedto);//pageDto [pageSize=10, pageNaviSize=10, offset=0, keyword=, option=, startNavi=null, totalPage=null, currentPage=1, endNavi=null, startPage=null]
//	    currentPage의 값을 확인: + currentPage
	    
	    if (boardDto.getTypeSeq() == null) {
	        boardDto.setTypeSeq(Integer.parseInt(this.typeSeq));
	    }
	    
	 // currentPage 필드를 직접 설정합니다.
	    if (pagedto.getCurrentPage() != null) {
	    	pagedto.setCurrentPage(pagedto.getCurrentPage());
	    }
	    System.out.println("currentPage의 값을 확인 : + currentPage");//currentPage의 값을 확인: + currentPage
	    
	    // 자유게시판 글 조회
	    BoardDto read = bService.read(boardDto);
	    System.out.println("dalsfkjaslkdjf:" + read);
	    
	    // 첨부파일 읽기
	    List<AttFileDto> attFiles = attFileService.readAttFiles(read);
	    System.out.println("해당 게시물의 모든 첨부파일 : " + attFiles);
	    
	    // 조회수 +1
	    bService.updateHits(read);
	    
	    // 뷰에 전달할 데이터 설정
	    mv.addObject("attFiles", attFiles);
	    mv.addObject("read", read);
	    System.out.println("read값 출력 하기 :" + read);
	    
	    // 뷰 이름 설정
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

}
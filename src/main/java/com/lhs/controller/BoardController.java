package com.lhs.controller;

import java.util.ArrayList;
import java.util.HashMap;

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

import com.lhs.dto.BoardDto;
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
	public ModelAndView goLogin(BoardDto boardDto) {
	    if (boardDto.getTypeSeq() == null) {
	        boardDto.setTypeSeq(this.typeSeq);
	    }

	    BoardDto key = bService.list(boardDto);

	    ModelAndView mv = new ModelAndView();
	    mv.setViewName("board/list");
	    mv.addObject("key", key);

	    // BoardDto 객체의 필드를 직접 접근하여 작업
	    Object boardSeq = key.getBoardSeq();
	    // 필요에 따라 다른 필드에 대해서도 접근할 수 있습니다.

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


	@RequestMapping("/board/read.do")
	public ModelAndView read(@ModelAttribute("boardDto") BoardDto boardDto) {
	    if (boardDto.getTypeSeq() == null) {
	        boardDto.setTypeSeq(this.typeSeq);
	    }

	    BoardDto boardread = bService.read(boardDto);
	    
	    ModelAndView mv = new ModelAndView();
	    mv.addObject("boardread", boardread);
	    mv.addObject("boardSeq", boardDto.getBoardSeq());
	    mv.setViewName("/board/read");
	    return mv;
	}

	

	@RequestMapping("/board/goToUpdate.do")
	public ModelAndView goToUpdate(BoardDto boardDto, HttpSession session) {
	    ModelAndView mv = new ModelAndView();

	    // typeSeq가 boardDto에 없을 경우, 기존의 typeseq 기본값을 넣는다.
	    if (boardDto.getTypeSeq() == null) {
	        boardDto.setTypeSeq(this.typeSeq);
	    }
	    
	    // 게시글 정보 읽어오기(bService의 read메서드에서 params에 담아서 객체에 저장
	    BoardDto listupdate = bService.read(boardDto);
	    
	    //ModelAndView 객체에 게시물 정보 추가
	    mv.addObject("listupdate", listupdate);
	    
	    // 뷰 이름 설정(update.jsp로 이동하게 하기 위해)
	    mv.setViewName("/board/update"); 
	    
	    //값이 들어오는지 객체 찍어보기
	    System.out.println("리스트업데이트                 :" + listupdate);

	    return mv; 
	}

	@RequestMapping("/board/update.do")
	@ResponseBody // !!!!!!!!!!!! 비동기 응답
	public HashMap<String, Object> update(BoardDto boardDto, MultipartHttpServletRequest mReq) {
	    // 게시물 정보 출력해보기
	    System.out.println("게시물출력해보기 : " + boardDto.toString());
	    
	    // typeSeq가 없을 경우, 기본값 typeSeq를 사용한다.
	    if (boardDto.getTypeSeq() == null) {
	        boardDto.setTypeSeq(this.typeSeq);
	    }
	    
	    // 게시물을 업데이트하는 메서드를 호출하기
	    int cnt = bService.update(boardDto, mReq.getFiles(typeSeq));
	    
	    //업데이트 결과를 확인하기 위해 콘솔에 출력하기(만약 업데이트가 성공했다면 result 값은 1이 될 것)
	    System.out.println("게시물 업데이트 결과값확인 : " + cnt);
	    
	    // 결과를 담은 HashMap 생성
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    //1. 업데이트 결과 카운트. 성공했을 경우 1, 실패했을 경우 0
	    map.put("cnt", cnt);
	    //2. 업데이트 결과 메시지) 성공했을 경우 "게시물 업데이트 완료", 실패했을 경우 "게시물 업데이트 실패"가 출력
	    map.put("msg", cnt==1?"게시물 업데이트 완료!!!":"게시물 업데이트 실패!!!");
	    //3. 업데이트 성공 시, 다음 페이지 경로 
	    map.put("nextPage", cnt==1?"/board/list.do":"/board/list.do");
	    return map;
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

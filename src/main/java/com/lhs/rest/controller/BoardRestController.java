package com.lhs.rest.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
=======
>>>>>>> 1-remove-this-field-injection-and-use-constructor-injection-instead스프링-필드-주입에서-생성자-주입으로-변경하기
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriUtils;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;
import com.lhs.service.AttFileService;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;
<<<<<<< HEAD
//excetionHandler 사용
@RestController
public class BoardRestController {
	@Autowired
	BoardService bService;
	@Autowired
	AttFileService attFileService;
	@Autowired
	FileUtil fileUtil;
=======

import lombok.RequiredArgsConstructor;

//excetionHandler 사용
@RequiredArgsConstructor
@RestController
public class BoardRestController {

	private final BoardService bService;
	private final AttFileService attFileService;
	private final FileUtil fileUtil;
>>>>>>> 1-remove-this-field-injection-and-use-constructor-injection-instead스프링-필드-주입에서-생성자-주입으로-변경하기

	private String typeSeq = "2";
	
	@PostMapping("/board/write.do")
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

	@GetMapping("/board/download.do")
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
	
	@PostMapping("/board/update.do")
	@ResponseBody
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

	// 게시글 삭제 요청을 처리하는 핸들러 메서드//<c:url value="/board/{boardSeq}/delete.do"/>를 <c:url value="/board/${read.boardSeq}/delete.do"/>로 변경
	@DeleteMapping("/board/{boardSeq}/delete.do")//**********type : 'POST'를 -->type : 'DELETE'변경
	public HashMap<String, Object> delete(
			@PathVariable("boardSeq") Integer boardSeq,BoardDto boarddto, HttpSession session) {
	    // 받아온 DTO 값 출력
	    System.out.println("read.jsp에서 받아오는 dto값 : " + boarddto);
	    
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    
	    // 게시글 유형 시퀀스가 없는 경우 기본 값 설정
	    if (boarddto.getTypeSeq() == null) {
	        boarddto.setTypeSeq(Integer.parseInt(this.typeSeq));
	    }
//	    boardDto.setTypeSeq(typeSeq);
	    
	    // 삭제할 게시글 정보 출력
	    System.out.println("delete222222222222222:" + boarddto);

	    // 게시글 삭제 서비스 호출 및 결과 수신
	    int boardInfo = bService.delete(boarddto);
	    // 삭제된 게시글 정보 출력
	    System.out.println("게시판 삭제하기!! :" + boardInfo);
	    
	    if(boardInfo==1) {
	    	map.put("nextpage", "/board/list");
	    	map.put("msg", "게시글 삭제 성공했습니다");
	    }else {
	    	map.put("nextpage", "/board/list");
	    	map.put("msg", "게시글 삭제 실패했습니다");
	    }

	    return map; // 비동기 요청에 대한 응답으로 일단은 null 반환
	}

	

	// 게시물 수정에서 파일 삭제할 때
		@PostMapping("/board/deleteAttFile.do")
		@ResponseBody
	public HashMap<String, Object> deleteAttFile(BoardDto boarddto, AttFileDto attFileDto) {
			System.out.println("첨부파일 삭제 클릭 시 attFileDto: "+attFileDto);
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		 if (boarddto.getTypeSeq() == null) {
		        boarddto.setTypeSeq(Integer.parseInt(this.typeSeq));
		}
		 System.out.println(boarddto);
		return map;
	}
<<<<<<< HEAD
}
=======
}
>>>>>>> 1-remove-this-field-injection-and-use-constructor-injection-instead스프링-필드-주입에서-생성자-주입으로-변경하기

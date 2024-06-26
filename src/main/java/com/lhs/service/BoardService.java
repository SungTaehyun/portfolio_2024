package com.lhs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lhs.dto.AttFileDto;
import com.lhs.dto.BoardDto;
import com.lhs.dto.pageDto;

public interface BoardService {

	public ArrayList<BoardDto> list(HashMap<String, Object> params);

	public int getTotalArticleCnt(HashMap<String, String> params);

	/**
	 * 글 쓰기 write
	 * 
	 * @param params
	 * @return
	 */
	public int write(HashMap<String, Object> params, List<MultipartFile> mFiles);
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	public AttFileDto getFileInfo(int fileIdx);
	

	/**
	 * 글 조회
	 */
	public BoardDto read(BoardDto boardDto);

	/**
	 * 글 수정 update
	 * 
	 * @param params
	 * @return
	 */
	public int update(BoardDto boardDto, List<MultipartFile> mFiles);

	/**
	 * 첨부파일 삭제(수정 페이지에서 삭제버튼 눌러 삭제하는 경우임)
	 * 
	 * @param params
	 * @return
	 */
	public boolean deleteAttFile(HashMap<String, Object> params);

	/**
	 * 글 삭제 delete
	 * 
	 * @param boarddto
	 * @return
	 */
	// boarddto: 삭제할 게시글의 정보를 담고 있는 BoardDto 객체
	public int delete(BoardDto boarddto);

	public ArrayList<AttFileDto> readFile(BoardDto boardDto);

	public void updateHits(BoardDto read);
	
	//검색 후 페이징 
	public int searchResultCnt(pageDto pageDto);

	// 검색기능
	public List<BoardDto> searchSelectPage(HashMap<String, Object> params);
	

}
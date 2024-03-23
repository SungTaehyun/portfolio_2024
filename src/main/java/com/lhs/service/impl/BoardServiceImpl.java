package com.lhs.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lhs.dao.AttFileDao;
import com.lhs.dao.BoardDao;
import com.lhs.dto.BoardDto;
import com.lhs.dto.AttFileDto;
import com.lhs.service.BoardService;
import com.lhs.util.FileUtil;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDao bDao;
	@Autowired
	AttFileDao attFileDao;
	@Autowired
	FileUtil fileUtil;

	@Value("#{config['project.file.upload.location']}")
	private String saveLocation;

	@Override
	public ArrayList<BoardDto> list(HashMap<String, Object> params) {
		// startSi
		System.out.println("BoardServiceImpl의 list메서드 파라미터");
		System.out.println(params);
		return bDao.list(params);
	}

	@Override
	public int getTotalArticleCnt(HashMap<String, String> params) {
		return bDao.getTotalArticleCnt(params);
	}

	
	// 게시글을 작성하고 파일을 첨부하는 메서드입니다.
	@Override
	public int write(HashMap<String, Object> params, List<MultipartFile> mFiles) {
	    // 게시글 정보를 데이터베이스에 등록하고, 성공적으로 등록되었을 경우 결과를 write 변수에 저장합니다.
	    int write = bDao.write(params);
	    
	    // 각 파일에 대한 정보를 순회하며 처리합니다.
	    for (MultipartFile mFile : mFiles) {
	        // 업로드된 파일이 존재하지 않거나 파일 이름이 비어 있는 경우, 다음 파일로 넘어갑니다.
	        if (mFile.getSize() == 0 || mFile.getOriginalFilename().isEmpty())
	            continue;
	        
	        // 게시글과 연관된 파일 정보를 저장할 HashMap 객체를 생성합니다.
	        HashMap<String, Object> boardAttach = new HashMap<>();
	        
	        // 게시글 번호와 유형 번호를 HashMap에 저장합니다.
	        boardAttach.put("typeSeq", params.get("typeSeq"));
	        boardAttach.put("boardSeq", params.get("boardSeq"));
	        
	        // 파일의 원본 이름, 가짜 파일 이름, 크기, 유형을 HashMap에 저장합니다.
	        boardAttach.put("fileName", mFile.getOriginalFilename());
	        boardAttach.put("fakeFileName", UUID.randomUUID().toString().replaceAll("-", ""));
	        boardAttach.put("fileSize", mFile.getSize());
	        boardAttach.put("fileType", mFile.getContentType());
	        
	        // 파일을 저장할 위치를 HashMap에 저장합니다.
	        boardAttach.put("saveLoc", saveLocation);
	        
	        // 파일을 복사하여 저장하고, 해당 파일 정보를 출력합니다.
	        try {
	            // 파일을 지정된 위치에 가짜 파일 이름으로 복사하여 저장합니다.
	            fileUtil.copyFile(mFile, (String) boardAttach.get("fakeFileName"));
	            // 파일 첨부 정보를 데이터베이스에 등록합니다.
	            attFileDao.addAttFile(boardAttach);
	        } catch (IOException e) {
	            // 파일 복사 과정에서 예외가 발생한 경우, 해당 예외를 콘솔에 출력합니다.
	            e.printStackTrace();
	        }
	        
	        // 파일이 첨부된 게시글의 경우 해당 정보를 업데이트합니다.
	        HashMap<String, Object> updateParams = new HashMap<>();
	        updateParams.put("boardSeq", params.get("boardSeq"));
	        updateParams.put("hasFile", "Y");
	        bDao.updateHasFile(updateParams);
	    }
	    
	    // 게시글에 첨부된 파일이 없는 경우 해당 게시글의 파일 첨부 여부를 업데이트합니다.
	    if (attFileDao.countAttFile(params.get("boardSeq") + "") < 1) {
	        HashMap<String, Object> updateParams = new HashMap<>();
	        updateParams.put("boardSeq", params.get("boardSeq"));
	        updateParams.put("hasFile", "N");
	        bDao.updateHasFile(updateParams);
	    }
	    
	    // 콘솔에 최종 게시글 정보 출력
	    System.out.println("result of params : " + params);
	    
	    return write;
	}


	// 글 조회
	@Override
	public BoardDto read(BoardDto boardDto) {
		bDao.updateHits(boardDto);
		return bDao.read(boardDto);
	}

	@Override
	public int update(BoardDto boardDto, List<MultipartFile> mFiles) {
		System.out.println("업데이트 확인            :" + boardDto);
		if (boardDto.getHasFile().equals("Y")) { // 첨부파일 존재시
			// 파일 처리

		}
		// 글 수정 dao
		return bDao.update(boardDto);
	}

	@Override
	public int delete(BoardDto boarddto) {
		System.out.println("deletezzzzzzzzzzzzzzz:" + boarddto); // {boardSeq=12878, typeSeq=2}
//		Object hasFileValue = params.get("hasFile"); // "hasFile" 키에 해당하는 값을 가져옴
		 String hasFileValue = boarddto.getHasFile(); // "hasFile" 키에 해당하는 값을 가져옴
		
		// "hasFile" 키에 해당하는 값이 null이 아니고 "Y"인 경우에만 실행
		if (hasFileValue != null && hasFileValue.equals("Y")) { // hasFile 키가 매개변수에 존재하는 경우
			int result = bDao.deleteFile(boarddto); // 파일 처리
			System.out.println("result11111111111111 : " + result);
		}
		return bDao.delete(boarddto);
	}

	@Override
	public boolean deleteAttFile(HashMap<String, Object> params) {
		boolean result = false;
		return result;
	}

	// 파일의 모든 정보를 가져올때 사용..
	@Override
	// 파일 정보를 조회하는 메서드
	public AttFileDto getFileInfo(int fileIdx) {// 여기서 int형으로 fileIdx를 받는 이유는??
		// 파일 정보를 담을 FileDto 객체 생성
		AttFileDto filedto = bDao.getFileInfo(fileIdx); // boarddao에 getFileInfo메서드에서 fileIdx를 얻어와서 filedto에 저장한다.
		return filedto;
	}

	@Override
	public ArrayList<AttFileDto> readFile(BoardDto boardDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateHits(BoardDto read) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("해당 게시물 조회수 올리기 : " + read);
		// BoaedDto [boardSeq=8186, typeSeq=2, memberId=sinbumjun, memberNick=범그로, title=수정하기2, 
		//           content=test2, hasFile=, hits=1, createDtm=null, updateDtm=20240308163520]	
		map.put("boardSeq", read.getBoardSeq());
		map.put("typeSeq", read.getTypeSeq());
		
		bDao.updateHits(map);
	}
}
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
	public ArrayList<HashMap<String, Object>> list(HashMap<String, String> params) {
		System.out.println("params@@@:" +params);
		ArrayList<HashMap<String, Object>> result = bDao.list(params);
		System.out.println("sdfasdfasdf : "+result);
		
		return result;
	}

	@Override
	public int getTotalArticleCnt(HashMap<String, String> params) {
		return bDao.getTotalArticleCnt(params);
	}

	@Override
	public int write(HashMap<String, Object> params, List<MultipartFile> mFiles) {// params : 이 매개변수는 게시글의 정보를 담고 있다
																					// mFiles  : 이 매개변수는 게시글에 첨부된 파일(MultipartFile)들의 리스트를 담고 있다.

		// 1. board DB에 글 정보등록 + hasFile
		int write = bDao.write(params);// params안에는 게시글정보가 들어가 있고 그중에 게시물 번호인 boardseq도 들어 있고, 그 params의 정보를 write에 담는다.
		//데이터베이스에 새로운 게시글을 추가할 때, 성공적으로 추가되었다면 write 변수에는 추가된 행의 수가 저장될 것
		
		// 각 파일의 정보 출력
		for (MultipartFile mFile : mFiles) {// 이 코드는 mFiles 배열의 각 요소인 MultipartFile 객체를 순회하면서 현재 요소를 mFile 변수에 할당한다.
			System.out.println(mFile.getContentType()); // 각 요소인 mFile에 대해 getContentType() 메서드 호출하여 해당 파일의 콘텐츠 유형 출력
			System.out.println(mFile.getOriginalFilename()); // 각 요소인 mFile에 대해 getOriginalFilename() 메서드를 호출하여 원본 파일 이름을 출력.
			System.out.println(mFile.getName()); // 파일의 필드 이름 출력
			System.out.println(mFile.getSize()); // 파일의 크기 출력
			System.out.println("--"); // 구분선 출력
			// UUID 클래스를 활용하여 생성된 무작위 UUID를 문자열로 변환한 후, '-' 문자를 제거하여 고유한 무작위 문자열을 생성
			String fakeName = UUID.randomUUID().toString();// 이 문자열은 fakeName 변수에 저장,,주로 임시 파일이나 고유한
																				// 식별자가 필요한 경우에 사용
			
			// HashMap을 사용하여 파일과 관련된 정보들을 저장(p에 저장)하는 코드이다.(위에서 출력한 요소들)
			HashMap<String, Object> p = new HashMap<>();

			// "typeSeq" 키의 값으로 params에서 가져온 "typeSeq" 값을 저장
			p.put("typeSeq", params.get("typeSeq"));

			// "boardSeq" 키의 값으로 params에서 가져온 "boardSeq" 값을 저장
			p.put("boardSeq", params.get("boardSeq"));

			// "fileName" 키의 값으로 MultipartFile 객체에서 가져온 원본 파일 이름을 저장
			p.put("fileName", mFile.getOriginalFilename());

			// "fakeFilename" 키의 값으로 UUID를 사용하여 생성된 무작위 문자열로 대체된 가짜 파일 이름을 저장
			p.put("fakeFilename", UUID.randomUUID().toString().replaceAll("-", ""));

			// "fileSize" 키의 값으로 MultipartFile 객체에서 가져온 파일 크기를 저장
			p.put("fileSize", mFile.getSize());

			// "fileType" 키의 값으로 MultipartFile 객체에서 가져온 파일 유형을 저장
			p.put("fileType", mFile.getContentType());

			// "saveLoc" 키의 값으로 파일을 저장할 위치를 지정하는 문자열(saveLocation)을 저장한다. / 예: "config.ini" --> 위에서 선언했기때문에 간단하게 saveLocation만 쓰면 된다.
			p.put("saveLoc", saveLocation);
			
			System.out.println("p이이이이니니니 :" +p);

			// to-do리스트 (해야할 것) : smart_123.pdf를 --> (UUID).pdf가 되게
			// to-do리스트 (해야할 것) : smart_123.456.pdf를 --> (UUID).pdf가 되게 해라.
			try {
				fileUtil.copyFile(mFile, fakeName);// fileUtil 객체를 사용하여 업로드된 파일(mFile)을 지정된 위치에 복사하는 작업 / fakeName은 복사될 파일의 이름이며, 실제 파일의 이름이 아니다.
				attFileDao.addAttFile(p);//왜 들어가는지?
			} catch (IOException e) { // (복사 과정에서=try 블록에서) 예외가 발생하면 해당 예외를 처리하기 위해 catch 블록이 실행(catch 블록에서 발생한 예외를 처리하는 코드가 실행)
				e.printStackTrace();//e.printStackTrace()는 예외 발생 시 어디서 문제가 발생했는지를 추적할 수 있도록 도와준다.
			}
		}// 파일 복사가 성공적으로 완료되면 아래의 코드가 실행
		// "params"에 관련 정보가 담겨있는 것으로 보이며, "boardSeq"를 출력합니다.
		
		
		System.out.println("result of params : " + params); // 출력해서 boardSeq가 찍혀야 하며 반드시 확인해야 한다.
		return 0;
	}
	
	// 글 조회
	@Override
	public HashMap<String, Object> read(HashMap<String, Object> params) {
//	public BoardDto read(BoardDto boardDto) {

		return bDao.read(params);
	}

	@Override
	public int update(HashMap<String, Object> params, List<MultipartFile> mFiles) {
		if (params.get("hasFile").equals("Y")) { // 첨부파일 존재시
			// 파일 처리
		}
		// 글 수정 dao
		return bDao.update(params);
	}

	@Override
	public int delete(HashMap<String, Object> params) {
		if (params.get("hasFile").equals("Y")) { // 첨부파일 있으면
			// 파일 처리
		}
		return bDao.delete(params);
	}

	@Override
	public boolean deleteAttFile(HashMap<String, Object> params) {
		boolean result = false;
		return result;
	}
}
package com.lhs.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Alias("board")
//@Alias(value = "board")
public class BoardDto {

	Integer boardSeq; // 번호
	String typeSeq; // 카테고리 : 1공지사항, 2자유게시판
	String memberId;
	String memberNick;
	String title;
	String content;
	String hits;
	String createDtm;
	String updateDtm;
	String currentPage;
}
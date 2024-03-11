package com.lhs.dto;

public class BoardDto {

    Integer boardSeq; // 번호
    Integer typeSeq; // 카테고리 : 1공지사항, 2자유게시판
    String memberId;
    String memberNick;
    String title;
    String content;
    String hits;
    String createDtm;
    String updateDtm;
    String hasFile;
    int page;
    
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public Integer getBoardSeq() {
		return boardSeq;
	}
	public void setBoardSeq(Integer boardSeq) {
		this.boardSeq = boardSeq;
	}
	public Integer getTypeSeq() {
		return typeSeq;
	}
	public void setTypeSeq(Integer typeSeq) {
		this.typeSeq = typeSeq;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberNick() {
		return memberNick;
	}
	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHits() {
		return hits;
	}
	public void setHits(String hits) {
		this.hits = hits;
	}
	public String getCreateDtm() {
		return createDtm;
	}
	public void setCreateDtm(String createDtm) {
		this.createDtm = createDtm;
	}
	public String getUpdateDtm() {
		return updateDtm;
	}
	public void setUpdateDtm(String updateDtm) {
		this.updateDtm = updateDtm;
	}
	public String getHasFile() {
		return hasFile;
	}
	public void setHasFile(String hasFile) {
		this.hasFile = hasFile;
	}
	@Override
	public String toString() {
		return "BoardDto [boardSeq=" + boardSeq + ", typeSeq=" + typeSeq + ", memberId=" + memberId + ", memberNick="
				+ memberNick + ", title=" + title + ", content=" + content + ", hits=" + hits + ", createDtm="
				+ createDtm + ", updateDtm=" + updateDtm + ", hasFile=" + hasFile + ", page=" + page + "]";
	}
}
package com.lhs.dto;

public class pageDto {
    private Integer pageSize = 10; // 한 페이지의 크기
    private Integer pageNaviSize = 10; // 페이지 네비게이션 크기
    private Integer currentPage = 1; // 현재 페이지

    private Integer offset = 0; // 데이터베이스 쿼리에서 사용될 오프셋 값
    private String keyword = ""; // 검색에 사용될 키워드
    private String option = ""; // 옵션 (검색 옵션을 의미하는 것)

    private Integer startNavi; // 네비게이션 시작 페이지
    private Integer totalPage; // 전체 페이지 수
    private Integer endNavi; // 네비게이션의 마지막 페이지
    private Integer startPage; // 시작 페이지

	public pageDto() {} 
	
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public Integer getStartPage() {
		return startPage;
	}
	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNaviSize() {
		return pageNaviSize;
	}
	public void setPageNaviSize(Integer pageNaviSize) {
		this.pageNaviSize = pageNaviSize;
	}
	public Integer getStartNavi() {
		return startNavi;
	}
	public void setStartNavi(Integer startNavi) {
		this.startNavi = startNavi;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}


	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getEndNavi() {
		return endNavi;
	}
	public void setEndNavi(Integer endNavi) {
		this.endNavi = endNavi;
	}
	@Override
	public String toString() {
		return "pageDto [pageSize=" + pageSize + ", pageNaviSize=" + pageNaviSize + ", offset=" + offset + ", keyword="
				+ keyword + ", option=" + option + ", startNavi=" + startNavi + ", totalPage=" + totalPage
				+ ", currentPage=" + currentPage + ", endNavi=" + endNavi + ", startPage=" + startPage + "]";
	}
	
}

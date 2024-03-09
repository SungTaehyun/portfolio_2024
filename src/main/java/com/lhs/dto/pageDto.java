package com.lhs.dto;

public class pageDto {
	private Integer pageSize = 10; // 한 페이지의 크기
	private Integer pageNaviSize = 10;//페이지 네비게이션
	
	private Integer startNavi ;// 네비게이션 시작 페이지
	private Integer totalPage;// 전체 페이지의 갯수
	private Integer currentPage = 1; // 현재페이지
	private Integer endNavi; // 네비게이션의 마지막 페이지 
	private Integer startPage;
	
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
		return "pageDto [ pageSize=" + pageSize + ", pageNaviSize=" + pageNaviSize
				+ ", startNavi=" + startNavi + ", totalPage=" + totalPage + ", currentPage=" + currentPage
				+ ", endNavi=" + endNavi + ", startPage=" + startPage + "]";
	}
	
}

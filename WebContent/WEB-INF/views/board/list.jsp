<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<section>
	<div class="container">
		<h4>자유게시판</h4>
		<div class="table-responsive">
			<table class="table table-sm">
				<colgroup>
					<col width="10%" />
					<col width="35%" />
					<col width="10%" />
					<col width="8%" />
					<col width="8%" />
					<col width="15%" />
				</colgroup>

				<thead>
					<tr>
						<th class="fw-30" align="center">&emsp;&emsp;&emsp;#</th>
						<th align="center">제목</th>
						<th align="center">글쓴이</th>
						<th align="center">조회수</th>
						<th align="center">첨부파일</th>
						<th align="center">작성일</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach var="board" items="${key}" varStatus="rowStatus">
						<tr>
							<td align="center">${board.boardSeq}</td>
							<td><span class="bold"> <a
									href="javascript:movePage('/board/read.do?boardSeq=${board.boardSeq}&currentPage=currentPage')">
										${board.title} </a>
							</span></td>

							<td>${board.memberNick}</td>
							<td>${board.hits}</td>
							<td>${board.hasFile}</td>
							<td>${board.createDtm}</td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
		</div>
		<div class="row text-center">
			<div class="col-md-12">
				<ul class="pagination pagination-simple pagination-sm">





					<!-- 현재 페이지가 1보다 클 때(즉, 현재페이지가 1이 아닐 때) -->
					<c:if test="${pageDto.currentPage !=1 }">
						<li class="page-item"><a class="page-link"
							href="javascript:movePage('/board/list.do?currentPage=${pageDto.currentPage - 1}')">&laquo;</a></li>
					</c:if>

					<!--  반복문 사용 시작값 = 현재페이지 / 네비 사이즈(=10) * 네비 사이즈(=10)  +1  (정수계산이라 가정)       종료값 시작값 + 9  -->
					<c:forEach var="i" begin="${pageDto.startNavi}" end="${pageDto.endNavi}">
						<!-- 페이지 링크 표시 -->
						<li class="page-item"><a class="page-link"
							href="javascript:movePage('/board/list.do?currentPage=${i}')">${i}</a>
						</li>
					</c:forEach>

					<!-- <!--  반복문을 빠져 나오는 조건 i값이 MAX페이지값보다 클 때!  -->
					<!-- <li class="page-item"><a class="page-link">1</a></li>
					<li class="page-item active"><a class="page-link"
						href="javascript:movePage('/board/list.do?page=2')">2</a></li>
					<li class="page-item"><a class="page-link"
						href="javascript:movePage('/board/list.do?page=3')">3</a></li>
					<li class="page-item"><a class="page-link"
						href="javascript:movePage('/board/list.do?page=4')">4</a></li>
					<li class="page-item"><a class="page-link"
						href="javascript:movePage('/board/list.do?page=5')">5</a></li>
					<li class="page-item"><a class="page-link"
						href="javascript:movePage('/board/list.do?page=6')">6</a></li> -->
					<!-- 현재 페이지가 MAX페이지보다 작을 때 -->
					<c:if test="${pageDto.currentPage < pageDto.totalPage}">
						<li class="page-item"><a class="page-link"
							href="javascript:movePage('/board/list.do?currentPage=${pageDto.currentPage + 1}')">&raquo;</a>
						</li>
					</c:if>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 text-right">
				<a href="javascript:movePage('/board/goToWrite.do')">
					<button type="button" class="btn btn-primary">
						<i class="fa fa-pencil"></i> 글쓰기
					</button>
				</a>
			</div>
		</div>
	</div>
	</section>
	<!-- / -->
</body>
</html>
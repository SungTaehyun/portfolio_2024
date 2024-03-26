<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
$(document).ready(function(){	
	
	// 검색 버튼 클릭 이벤트 처리
	$('#searchButton').on('click', function(event){      
	    
	    // 검색 옵션과 검색어 가져오기
	    var option = $('.search-option').val();
	    var keyword = $('.search-input').val();
	    
	    // AJAX 호출을 통해 데이터를 검색
	    customAjax('<c:url value="/board/list.do"/>', 
	    		"/board/list.do?currentPage=${pagedto.currentPage}&pageSize=${pagedto.pageSize}&option=" + option + "&keyword=" + encodeURIComponent(keyword));
	    //
	});
	   
	function customAjax(url, responseUrl) {
	    var formData = $('#searchForm').serialize(); // 폼 데이터를 직렬화하여 가져옴
	    $.ajax({
	        url : url + '?' + formData, // URL에 직렬화한 데이터를 추가하여 GET 요청을 보냄
	        // data : formData,
	        type : 'GET', // GET 방식으로 변경
	        dataType : "text", // 반환되는 데이터 타입이 텍스트임을 명시
	        processData : false,
            contentType : false,
	        success : function (result, textStatus, XMLHttpRequest) {
	            // var data = $.parseJSON(result);
	            // alert(data.msg);
	            // var boardSeq = data.boardSeq;
	            console.log("result내용 : " + result);
	            console.log("textStatus내용 : " + textStatus);
	            console.log("XMLHttpRequest내용 : " + XMLHttpRequest);
	            
	            // alert("성공하면 다음으로 이동할 URL: " + responseUrl);
	            // 성공하면 다음으로 이동할 URL: /board/list.do?page=1&pageSize=10&option=A&keyword=%EC%8B%A0%EB%B2%94%EC%A4%80
	            
	            // movePage('/member/changePassword.do?memberId=' + memberId + '&email=' + email); // 수정된 부분
	            movePage(responseUrl); // 성공하면 게시판으로
	        },
	        error : function (XMLHttpRequest, textStatus, errorThrown) {
	            alert("에러 발생\n관리자에게 문의바랍니다.");
	            console.log("에러\n" + XMLHttpRequest.responseText);
	        }
	    });
	}  
});
</script>

</head>
<body>
	<section>
	<div class="container">
		<h4>자유게시판</h4>
		
		<!-- 검색 기능 -->
<div class="search-container">
   <!-- 검색 폼 -->
<!-- 검색 폼 -->
<form id="searchForm" class="search-form" method="post"> <!-- 수정: method를 POST로 변경 -->
    <!-- 검색 옵션 선택을 위한 셀렉트 박스 -->
    <select class="search-option" name="option">
        <!-- 제목+글쓴이 옵션 -->
        <option value="A" ${pagedto.option == 'A' || empty pagedto.option ? "selected" : ""} style="background-color: #f0f0f0;">제목+글쓴이</option> <!-- 수정: ph.sc.option을 pagedto.option으로 변경 -->
        <!-- 조회수 이상 옵션 -->
        <option value="T" ${pagedto.option == 'T' ? "selected" : ""} style="background-color: #f5f5f5;">조회수</option> <!-- 수정: ph.sc.option을 pagedto.option으로 변경 -->
    </select>

    <!-- 검색어 입력을 위한 텍스트 입력 상자 -->
    <input type="text" name="keyword" class="search-input" value="${pagedto.keyword}" placeholder="검색어를 입력해주세요" style="background-color: #f5f5f5;"> <!-- 수정: ph.sc.keyword를 pagedto.keyword으로 변경 -->
    
    <!-- 검색 버튼 -->
    <input type="submit" id="searchButton" class="search-button" value="검색" style="background-color: #007bff; color: #fff;">
</form>


</div>
		
		
		
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
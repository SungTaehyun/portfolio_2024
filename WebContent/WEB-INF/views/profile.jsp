<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
</head>

	<body class="smoothscroll enable-animation">

			
			<%-- 내용 나올 div 시작!!!! --%>

				<!-- -->
			<section>
				<div class="container">
					
					<div class="row">
					
						<div class="col-lg-6 col-md-6 col-sm-6">
							<div class="col-lg-3 col-md-3 col-sm-3" >
								<img class="img-responsive" src="resources/images/_smarty/lazy-loadimg.jpg" width="500" height="300" alt="">
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-6">
							<div class="heading-title heading-border-bottom">
								<h3>묵묵히 끈기있게 나아가는 개발자 성태현입니다</h3>
							</div>
							<p>저는 함께 성장하고, 공동의 목표를 이루기 위해 노력하는 개발자가 되기를 추구합니다.</p>
							<blockquote>
							<p>노력은 수단이 아니라 그 자체가 목적이다.<br/> 노력하는 것 자체에 보람을 느낀다면<br/>너는 인생의 마지막 시점에서 미소를 지을 수 있을 것이다.</p>
								<cite>톨스토이</cite>
							</blockquote>
						</div>

					</div>
					
				</div>
			</section>
			<!-- / -->




			<!-- -->
			<section>
				<div class="container">
					
					<div class="row">
					
						<div class="col-lg-6 col-md-6 col-sm-6">

							<div class="heading-title heading-border-bottom">
								<h3>개발 경력사항</h3>
							</div>

							<ul class="nav nav-tabs nav-clean">
								<li class="active"><a href="#tab1" data-toggle="tab">Web Development</a></li>
								<li><a href="#tab2" data-toggle="tab">Mysql</a></li>
								<li><a href="#tab3" data-toggle="tab">교육 이력</a></li>
							</ul>

							<div class="tab-content">
								<div id="tab1" class="tab-pane fade in active">
									<img class="pull-left" src="demo_files/images/mockups/600x399/20-min.jpg" width="200" alt="" />
									<p>AWS를 활용한 배포 및 RDS 경험과 OPENJDK 적용, REST API/AJAX 개발 경험이 있습니다. </p>
									<p>Spring 프레임워크를 활용한 웹 애플리케이션 개발 : 로그인/회원가입, 파일 업로드 및 다운로드, 메일 발송, 이메일 인증, 게시판 CRUD, 댓글 기능, 검색 기능 구현 <p>
								</div>
								<div id="tab2" class="tab-pane fade">
									<img class="pull-right" src="demo_files/images/mockups/600x399/20-min.jpg" width="200" alt="" />
									<p>이메일 인증 및 댓글 기능을 포함한 테이블 설계와 기능에 맞는 쿼리 작성<p>
									<a href='https://www.notion.so/2d0f8130a49946f8a3d2003078d72f4d' target='_blank'>통계 쿼리 학습 링크</a><br/>
									<p>통계 쿼리 학습으로 다양한 데이터 분석 기법을 배우고 있습니다. 더 깊이 있는 데이터 이해와 효과적인 활용에 대해 끊임없이 공부하고 있습니다</p>
								</div>
								<div id="tab3" class="tab-pane fade">
									<p>2023.10.16 - 2024.01.29    [남궁성 정석코딩 9기]<br/> 팀 프로젝트 시작 전 폐강</p>
									<p>2024.02.16 - 2024.04.01<br/>개인 프로젝트 진행중</p>
								</div>
							</div>
						</div>

						<div class="col-lg-6 col-md-6 col-sm-6">

							<div class="heading-title heading-border-bottom">
								<h3>Skills</h3>
							</div>

							<div class="progress progress-lg"><!-- progress bar -->
								<div class="progress-bar progress-bar-warning progress-bar-striped active text-left" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style="width: 70%; min-width: 2em;">
									<span>JAVA 70%</span>
								</div>
							</div><!-- /progress bar -->

							<div class="progress progress-lg"><!-- progress bar -->
								<div class="progress-bar progress-bar-danger progress-bar-striped active text-left" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%; min-width: 2em;">
									<span>HTML/JSP 40%</span>
								</div>
							</div><!-- /progress bar -->

							<div class="progress progress-lg"><!-- progress bar -->
								<div class="progress-bar progress-bar-success progress-bar-striped active text-left" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: 50%; min-width: 2em;">
									<span>JAVASCRIPT 50%</span>
								</div>
							</div><!-- /progress bar -->

							<div class="progress progress-lg"><!-- progress bar -->
								<div class="progress-bar progress-bar-info progress-bar-striped active text-left" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%; min-width: 2em;">
									<span>MYSQL 40%</span>
								</div>
							</div><!-- /progress bar -->
						</div>
					</div>
					
				</div>
			</section>
			<!-- / -->

		
	</body>
</html>
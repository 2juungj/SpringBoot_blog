<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form action="/action_page.php">
		<div class="form-group">
		<input type="text" class="form-control" placeholder="아이디 입력" id="username" required>
		</div>

		<div class="form-group">
			<input type="password" class="form-control" placeholder="비밀번호 입력" id="password" required>
		</div>

		<div class="form-group">
			<input type="email" class="form-control" placeholder="이메일 입력" id="email" required>
		</div>
	</form>

	<button id="btn-save" class="btn btn-primary" style="margin-bottom: 30px">회원가입</button>
	
	<div>
			<a href="/oauth2/authorization/kakao" ><img height="38px" src="/image/kakao_login_button.png"/></a>
			<a href="/oauth2/authorization/naver"><img height="38px" src="/image/naver_login_button.png"/></a>
		</div>
		
		<div style="height: 10px;"></div>
		
		<div>
			<a href="/oauth2/authorization/google"><img height="38px" src="/image/google_login_button.png"/></a>
		</div>
		
		<div style="height: 2px;"></div>
		
		<div>	
			<a href="/oauth2/authorization/facebook"><img height="40px" src="/image/facebook_login_button.png"/></a>
		</div>
	
	<div></div>

</div>

<script src="/js/user.js"></script>
<%@ include file="../layout/footer.jsp"%>




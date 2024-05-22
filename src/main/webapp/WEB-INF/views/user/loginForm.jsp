<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form action="/auth/loginProc" method="post">
		<div class="form-group">
			<!-- <label for="username">Username:</label>  -->
			<input type="text" name="username"class="form-control" placeholder="아이디 입력" id="username"required>
		</div>

		<div class="form-group">
			<!--<label for="password">Password:</label>  -->
			<input type="password" name="password"class="form-control" placeholder="비밀번호 입력" id="password" required>
		</div>
		
		<button id="btn-login" class="btn btn-primary" style="margin-bottom: 30px">로그인</button>
		
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
		
	</form>

</div>

<%@ include file="../layout/footer.jsp"%>




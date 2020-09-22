<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="sectionContainerCenter">

	<div>
		<div class="msg">${data.msg}</div>
		<form class="frm" action="/user/login" method="post">
			<div>
				<input type="text" name="user_id" placeholder="ID" value="${user_id}">
			</div>
			<div>
				<input type="password" name="user_pw" placeholder="PASSWORD">
			</div>
			<div>
				<input type="submit" value="Sign-In">
			</div>
		</form>
			<a href="/user/join"><button id="joinBtn">Join-In</button></a>
	</div>
	
</div>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="/res/css/common.css">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<title>${title}</title>
<c:forEach items="${css}" var="itemCss">
	<link rel="stylesheet" type="text/css" href="/res/css/${itemCss}.css">
</c:forEach>
</head>
<body>
	<div id="container">
	<header>
		<div class="headerLeft">
			<c:if test="${loginUser != null}">
			<div class="containerPImg">
				<c:choose>
				<c:when test="${loginUser.profile_img != null}"> <!-- /res/를 적은 이유는 containerSer에서 "/"를 걸러 낼 수 있기 때문 -->
					<img class ="pImg" src="/res/img/user/${loginUser.i_user}/${loginUser.profile_img}" >
				</c:when>
				<c:otherwise>
					<img class ="pImg" src="/res/img/default_profile.jpg">
				</c:otherwise>
	 			</c:choose>
		</div>
			<div class="headerNm"><span>${loginUser.nm}</span>님 환영합니다.</div> 
			<div class="headerLogOut"><a href="/user/logout">로그아웃</a></div>
		</c:if>
		<c:if test="${loginUser == null}">
			<div class="headerLogIn"><a href="/user/login">로그인</a></div>
		</c:if>
		</div>
		<div class="headerRight">
			<a class="area" href="/rest/map">지도</a>
			<c:if test="${loginUser != null}">
				<a class="reg" href="/rest/restReg">등록</a>
			</c:if>
			<c:if test="${loginUser == null}">
				<a class="reg" href="#" onclick="alert('로그인이 필요합니다😍')">등록</a>
			</c:if>
			<a class="zzim" href="/user/restFavolate">좋아요</a>
		</div>
	</header>
	<section>
		<jsp:include page="/WEB-INF/views/${view}.jsp"></jsp:include>
	</section>
	<footer>
		<span class="companyInfo">회사정보</span>
	</footer>
	</div>
</body>
</html>
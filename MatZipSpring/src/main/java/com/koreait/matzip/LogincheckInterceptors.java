package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.koreait.matzip.user.model.UserPARAM;

public class LogincheckInterceptors extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse reseponse, Object handler) throws Exception {
		
		String uri = request.getRequestURI();
		String[] uriArr = uri.split("/");
		//System.out.println("uri : " + uri);
		//System.out.println("uriArr.length : " + uriArr.length);
		
		if(uri.equals("/")) {
			return true;
		} else if(uriArr[1].equals("res")) { //js, css, img 걸러냄
			return true;
		} 
		
		System.out.println("인터셉터");
		boolean isLogOut = SecurityUtils.isLogOut(request);
		
		
		switch(uriArr[1]) {
		case ViewRef.URI_USER : 
			switch(uriArr[2]) {
			case "login" : case "join" : 
				if(!isLogOut) { //로그인 되어 있는 상태
					reseponse.sendRedirect("/rest/map");
					return false; //로그인이 되어 있을 때 login이나 join 페이지로 가지 않게 함
				}
			}
		case ViewRef.URI_REST :
			switch(uriArr[2]) {
			case "reg" : 
				if(isLogOut) { //로그아웃한 상태
					reseponse.sendRedirect("/user/login");
					return false;
				}
			
			}
			
		}
		
		return true;
	}

}

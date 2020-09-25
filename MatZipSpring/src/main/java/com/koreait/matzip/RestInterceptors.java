package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.koreait.matzip.rest.RestMapper;

public class RestInterceptors extends HandlerInterceptorAdapter  {
	
	@Autowired
	RestMapper mapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse reseponse, Object handler) throws Exception {
	//controller 넘어 가기전의 과정
		
		System.out.println("rest 인터셉터");
		
		String uri = request.getRequestURI();
		String[] uriArr = uri.split("/");
		
		String[] confirmKewords = {"del", "Del", "Upd", "upd"}; //옆의 주소가 포함되어 있다면 아래를 체크
		for(String keword: confirmKewords) {
			if(uriArr[2].contains(keword)) {
				int i_rest = CommonUtils.getIntParameter("i_rest", request); //t_reataurant 테이블의 레코드 PK값
				if(i_rest == 0) {
					return false;
				}
				int i_user = SecurityUtils.getLoginUserPk(request); //로그인한 유저의 i_user값
				
				boolean result = _authSuccess(i_rest, i_user);
				System.out.println("== auth result : " + result);
				return result;
			}
		}
		System.out.println("== auth result : true");
		return true;
	}
	
	private boolean _authSuccess(int i_rest, int i_user) {
	
		return i_user == mapper.selRestChkUser(i_rest);
	}

}

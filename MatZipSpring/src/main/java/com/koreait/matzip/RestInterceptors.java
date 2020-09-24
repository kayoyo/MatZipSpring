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
		
		System.out.println("rest 인터셉터");
		
		String uri = request.getRequestURI();
		String[] uriArr = uri.split("/");
		
		String[] confirmKewords = {"del", "Del", "Upd", "upd"};
		for(String keword: confirmKewords) {
			if(uriArr[2].contains(keword)) {
				int i_rest = CommonUtils.getIntParameter("i_rest", request);
				if(i_rest == 0) {
					return false;
				}
				int i_user = SecurityUtils.getLoginUserPk(request);
				
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

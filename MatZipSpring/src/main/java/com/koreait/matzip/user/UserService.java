package com.koreait.matzip.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.rest.RestMapper;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;
import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Service
public class UserService {
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private RestMapper restMapper;
	
	//1번 로그인 성공, 2번 아이디 없음, 3번 비번 틀림
	public int login(UserPARAM param) { //ID와 PW가 담겨있음
		
		if(param.getUser_id().equals("")) { //아이디 없음
			return Const.NO_ID; 	
		} 
			UserDMI dbUser = mapper.selUser(param);
			//System.out.println("pw : " + dbUser.getUser_pw());
			
			if(dbUser == null) {
				return Const.NO_ID;
			}
			
			String salt = dbUser.getSalt(); //Salt의 값은 dbUser에 담겨져 있는 이유는 select 했기 때문
			String encryptPw = SecurityUtils.getEncrypt(param.getUser_pw(), salt); //PW 암호화 처리
			
			if (encryptPw.equals(dbUser.getUser_pw())) { //로그인 성공
				param.setUser_pw(null);
				param.setNm(dbUser.getNm());
				param.setProfile_img(dbUser.getProfile_img());
				param.setI_user(dbUser.getI_user());
				return Const.SUCCESS;		
			} else {
				return Const.NO_PW;
			}
		}
		
	public int join(UserVO param) {
		String pw = param.getUser_pw();
		String salt = SecurityUtils.generateSalt();
		String cryptPw = SecurityUtils.getEncrypt(pw, salt);
		
		param.setSalt(salt);
		param.setUser_pw(cryptPw);
		
		return mapper.insUser(param);
	}
	
	public int ajaxToggleFavorite(UserPARAM param) { //i_user, i_rest, proc_type 담겨 있음
		switch(param.getProc_type()) {
		case "ins": 
			return mapper.insFavorite(param);
		case "del":
			return mapper.delFavorite(param);
		}
		return 0;
	}
	
	public List<UserDMI> selFavoriteList(UserPARAM param) {
		List<UserDMI> list = mapper.selFavoriteList(param);
		
		for(UserDMI vo : list) {
			RestPARAM param2 = new RestPARAM();
			param2.setI_rest(vo.getI_rest());
			
			List<RestRecMenuVO> eachRecMenuList = restMapper.selRecMenuList(param2);
			vo.setMenuList(eachRecMenuList);
		}
		
		return list;
	}

}

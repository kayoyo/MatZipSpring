package com.koreait.matzip.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Mapper
public interface UserMapper { 
	
	int insUser(UserVO p);
	int insFavorite(UserPARAM param);
	
	UserDMI selUser(UserPARAM p);
	List<UserDMI> selFavoriteList(UserPARAM param);
	
	int delFavorite(UserPARAM param);
	
}

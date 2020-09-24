package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Mapper
public interface RestMapper {
	
	List<RestDMI> selRestList(RestPARAM param);
	List<RestRecMenuVO> selRecMenuList(RestPARAM param);
	RestDMI selDetailRest(RestPARAM param);
	List<RestRecMenuVO> selRestMenus(RestPARAM param);
	int selRestChkUser(int i_rest);
	
	int insRest(RestPARAM param);
	int insRestRecMenu(RestRecMenuVO param);
	int insMenus(RestRecMenuVO param);
	
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
	
	
}

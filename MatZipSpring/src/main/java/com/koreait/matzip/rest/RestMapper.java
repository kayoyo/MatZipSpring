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

	int insRest(RestPARAM param);
	
	int insRestRecMenu(RestRecMenuVO vo);
	
	RestDMI selDetailRest(RestPARAM param);

	int delRestRecMenu(RestPARAM param);
	
	int delRestMenu(RestPARAM param);
	
	int delRest(RestPARAM param);
	
	int delRecMenu(RestPARAM param);
	
}

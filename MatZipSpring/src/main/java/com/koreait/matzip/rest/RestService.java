package com.koreait.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Service
public class RestService {
	
	@Autowired
	private RestMapper mapper;
	
	@Autowired
	private CommonMapper cMapper;
	
	public List<RestDMI>selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}
	
	public List<CodeVO> selCodeList() {
		CodeVO vo = new CodeVO();
		vo.setI_m(1); //음식점 카테고리 코드 
		return cMapper.selCodeList(vo);
	}
	
	public int insRest(RestPARAM param) {
		
		 return mapper.insRest(param);
	}
	
}

package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Service
public class RestService {
	
	@Autowired
	private RestMapper mapper;
	
	@Autowired
	private CommonMapper cMapper;
	
	public List<RestDMI>selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}
	
	public List<RestRecMenuVO> selRecMenuList(RestPARAM param){
		return mapper.selRecMenuList(param);
	}
	
	public List<CodeVO> selCodeList() {
		CodeVO vo = new CodeVO();
		vo.setI_m(1); //음식점 카테고리 코드 
		return cMapper.selCodeList(vo);
	}
	
	public int insRest(RestPARAM param) {
		
		 return mapper.insRest(param);
	}
	
	public RestDMI selDetailRest(RestPARAM param){
		return mapper.selDetailRest(param);
	}
	
	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
	}
	
	public int delRestRecMenu(RestPARAM param) {
		return mapper.delRestRecMenu(param);
	}
	
	public int delRestMenu(RestPARAM param) {
		
		return mapper.delRestMenu(param);
	}
	
	public int delRest(RestPARAM param) {
		
		return mapper.delRest(param);
	}
	
	public int insRecMenus(MultipartHttpServletRequest mReq) {
		
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");
		
		String path = mReq.getServletContext().getRealPath("/resources/img/rest/" + i_rest + "/rec_menu/");
		
		List<RestRecMenuVO> list = new ArrayList();
			
		for(int i=0; i<menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			
			String menu_nm = menuNmArr[i];
			//System.out.println(menu_nm);
			int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]);
			//System.out.println(menu_price);
			vo.setI_rest(i_rest);
			vo.setMenu_nm(menu_nm);
			vo.setMenu_price(menu_price);
			
			//파일 값 저장
			MultipartFile mf = fileList.get(i);
			//System.out.println(mf);
			
			//파일이 없으면 스킵
			if(mf.isEmpty()) {continue;}
			
			//파일 저장 위치
			String originFileNm = mf.getOriginalFilename();
			String ext = FileUtils.getExt(originFileNm);
			String saveFileNm = UUID.randomUUID() + ext;
			
			try {
				mf.transferTo(new File(path + saveFileNm));
				vo.setMenu_pic(saveFileNm);
			} catch(Exception e) {
				e.printStackTrace();
			}	
		}
		
		for(RestRecMenuVO vo : list) {
			mapper.insRestRecMenu(vo);
		}
		
		return i_rest;
	}
	
	public int delRecMenu(RestPARAM param, String realpath) {
		List<RestRecMenuVO> list = mapper.selRecMenuList(param);
		if(list.size() == 1) { //로그인 유저가 쓴 글이 맞으며, 삭제할 정보가 들어 있다.
			RestRecMenuVO item = list.get(0); //index는 1개 밖에 없기 때문에 0
			
			if(item.getMenu_pic() != null && !item.getMenu_pic().equals("")) {
				File file = new File(realpath + item.getMenu_pic());
				if(file.exists()) {
					file.delete(); //파일이 있으면 삭제
						return mapper.delRecMenu(param);
				} else {
					return 0; //파일이 없다면 삭제 되면 안됨
				}
			}
		}
		return mapper.delRecMenu(param);
	}
		
}

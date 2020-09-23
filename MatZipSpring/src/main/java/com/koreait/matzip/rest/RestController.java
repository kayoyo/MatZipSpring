package com.koreait.matzip.rest;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Controller
@RequestMapping("/rest")
public class RestController {
	
	@Autowired
	private RestService service;
	
	@RequestMapping("/map")
	public String restMap(Model model) {
		model.addAttribute(Const.TITLE, "지도보기");
		model.addAttribute(Const.VIEW, "rest/restMap"); //jsp 파일 경로
		return ViewRef.TEMP_MEUE_TEMP;
	}
	
	@RequestMapping(value="/ajaxGetList", produces="application/json; charset=UTF-8")
	@ResponseBody 
	public List<RestDMI> ajaxGetList(RestPARAM param) {
		System.out.println("sw_lat : " + param.getSw_lat());
		System.out.println("sw_lng : " + param.getSw_lng());
		System.out.println("ne_lat : " + param.getNe_lat());
		System.out.println("ne_lng : " + param.getNe_lng());
		
		return service.selRestList(param);
	}
	
	@RequestMapping("/reg")
	public String restReg(Model model) {
		
		model.addAttribute("categoryList", service.selCodeList());
		model.addAttribute(Const.TITLE, "등록하기");
		model.addAttribute(Const.VIEW, "rest/restReg");
		return ViewRef.TEMP_MEUE_TEMP;	
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	public String restReg(RestPARAM param, HttpSession hs) {
		
		param.setI_user(SecurityUtils.getLoginUserPk(hs));
		
		int result = service.insRest(param); 
		
		if(result == 1) {
			return "redirect:/rest/map";
		}
		return "redirect:/rest/restReg";
		
	}
	
	@RequestMapping("/detail")
	public String detail(RestPARAM param, Model model) {
		
		RestDMI data = service.selDetailRest(param);
		
		RestRecMenuVO vo = new RestRecMenuVO();
		vo.setI_rest(param.getI_rest());
		//변화하는 값만 넣어줌
		
		List<RestRecMenuVO> recMenuList = service.selRecMenuList(param);
		String[] cssList = {"restaurant"};
		
		model.addAttribute("recMenuList", recMenuList);
		model.addAttribute("css", cssList);
		model.addAttribute("data", data);
		model.addAttribute(Const.TITLE, data.getNm());
		model.addAttribute(Const.VIEW, "rest/restDetail");
		
		return ViewRef.TEMP_MEUE_TEMP;	
	}
	
	@RequestMapping("/del")
	public String delRest(RestPARAM param, HttpSession hs) {
		
		int loginI_user = SecurityUtils.getLoginUserPk(hs);
		param.setI_user(loginI_user);
		
		int result = 1;
		
		try { //오류가 생겼을 때 쿼리문 노출 방지
			service.delRestTran(param);
		} catch(Exception e) {
			result = 0;
		}
		
		System.out.println("result : " + result);
		
		return "redirect:/";	
	}
	
	@RequestMapping(value="/recMenus", method=RequestMethod.POST)
	public String recMenus(MultipartHttpServletRequest mReq, RedirectAttributes ra) {
			
		int i_rest = service.insRecMenus(mReq);
		
		ra.addAttribute("i_rest", i_rest); // "redirect:/rest/detail?i_rest=" + i_rest 와 같다
		return "redirect:/rest/detail";
	}
	
	@RequestMapping("/ajaxDelRecMenu")
	@ResponseBody
	public int ajaxDelRecMenu(RestPARAM param, HttpSession hs) {
		
		String path = "/resources/img/rest/" + param.getI_user() + "/rec_menu/"; //파일 저장 경로
		String realPath = hs.getServletContext().getRealPath(path);
		param.setI_user(SecurityUtils.getLoginUserPk(hs)); //로그인 유저 정보 담기
		return service.delRecMenu(param);
	}
	
	
	
	
	

}

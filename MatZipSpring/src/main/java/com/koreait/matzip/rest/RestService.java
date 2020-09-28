package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Service
public class RestService {

	@Autowired
	private RestMapper mapper;

	@Autowired
	private CommonMapper cMapper;

	public List<RestDMI> selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}

	public List<RestRecMenuVO> selRecMenuList(RestPARAM param) {
		return mapper.selRecMenuList(param);
	}
	
	public List<RestRecMenuVO> selRestMenus(RestPARAM param) {
		return mapper.selRestMenus(param);
	}

	public List<CodeVO> selCodeList() {
		CodeVO vo = new CodeVO();
		vo.setI_m(1); // 음식점 카테고리 코드
		return cMapper.selCodeList(vo);
	}
	
	public RestDMI selDetailRest(RestPARAM param) {
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
		if(param.getMenu_pic() != null && !"".equals(param.getMenu_pic())) {
			String path = Const.REALPATH + "/resources/img/rest/" + param.getI_rest() + "/menus/";
			
//			System.out.println(path);
//			System.out.println(param.getI_rest());
//			System.out.println(param.getSeq());
//			System.out.println(param.getMenu_pic());
			
			if(FileUtils.delFile(path + param.getMenu_pic())) {
				return mapper.delRestMenu(param);				
			} else {
				return Const.FAIL;
			}
		}
		return mapper.delRestMenu(param); //파일이 없더라도 삭제는 되야 한다.
	}

	public int delRecMenu(RestPARAM param, String REALPATH) {
		List<RestRecMenuVO> list = mapper.selRecMenuList(param);
		if (list.size() == 1) { // 로그인 유저가 쓴 글이 맞으며, 삭제할 정보가 들어 있다.
			RestRecMenuVO item = list.get(0); // index는 1개 밖에 없기 때문에 0
			
			if (item.getMenu_pic() != null && !item.getMenu_pic().equals("")) {
				File file = new File(REALPATH + item.getMenu_pic());
				if (file.exists()) {
					if (file.delete()) {// 파일이 있으면 삭제
						return mapper.delRestRecMenu(param);
					} else {
						return 0; // 파일이 없다면 삭제 되면 안됨
						
					}
				}
			}
		}
		return mapper.delRestRecMenu(param);
	}
	
	public void addHits(RestPARAM param, HttpServletRequest req) {
		
		String myIp = req.getRemoteAddr(); //현재 이 글을 보고 있는 유저의 ip값
		ServletContext ctx = req.getServletContext(); //application은 공용(서버당 1개 생성)
		
		int i_user = SecurityUtils.getLoginUserPk(req);
		
		
		String currentRestReadIp = (String)ctx.getAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest());
		if(currentRestReadIp == null || !currentRestReadIp.equals(myIp)) {
			
			param.setI_user(i_user); //내가 쓴 글이면 조회수 올라가지 않게 함(where절 부분)
			mapper.updAddHits(param);
			
			ctx.setAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest(), myIp);
			
		}
	}
	
	public int insRest(RestPARAM param) {
		
		return mapper.insRest(param);
	}

	public int insMenus(RestFile param, int i_user) {

		if (_authFail(param.getI_rest(), i_user)) {
			return Const.FAIL;
		}

		//System.out.println("통과");

		String path = Const.REALPATH + "/resources/img/rest/" + param.getI_rest() + "/menus/";

		for (MultipartFile file : param.getMenu_pic()) {

			RestRecMenuVO vo = new RestRecMenuVO();

			String saveFileNm = FileUtils.saveFile(path, file);

			vo.setMenu_pic(saveFileNm);
			vo.setI_rest(param.getI_rest());
			mapper.insMenus(vo);

		}
		return Const.SUCCESS;

	}

	public int insRecMenus(MultipartHttpServletRequest mReq) {

		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		int i_user = SecurityUtils.getLoginUserPk(mReq.getSession());

		if (_authFail(i_rest, i_user)) {
			return Const.FAIL; //리턴값이 0이니까 아래 메소드 실행 불가 
		}

		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");

		String path = Const.REALPATH + "/resources/img/rest/" + i_rest + "/rec_menu/";

		List<RestRecMenuVO> list = new ArrayList();

		for (int i = 0; i < menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);

			String menu_nm = menuNmArr[i];
			// System.out.println(menu_nm);
			int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]);
			// System.out.println(menu_price);
			vo.setI_rest(i_rest);
			vo.setMenu_nm(menu_nm);
			vo.setMenu_price(menu_price);

			// 파일 각 저장
			MultipartFile file = fileList.get(i);
			String saveFileNm = FileUtils.saveFile(path, file);
			vo.setMenu_pic(saveFileNm);
		}

		for (RestRecMenuVO vo : list) {
			mapper.insRestRecMenu(vo);
		}

		return i_rest;
	}


	private boolean _authFail(int i_rest, int i_user) {
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest); 

		RestDMI dbResult = mapper.selDetailRest(param); //게시글을 쓴 i_user 값을 가져옴
		
		if (dbResult == null || dbResult.getI_user() != i_user) { //로그인이 안되어있거나(게스트), 로그인 유저랑 게시글 쓴 유저랑 다르다면
			return true;
		}

		return false;
	}

}

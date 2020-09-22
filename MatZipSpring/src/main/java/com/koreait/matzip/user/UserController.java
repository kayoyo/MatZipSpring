package com.koreait.matzip.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpSession hs) {
		hs.invalidate();
		return "redirect:/";
	}
	
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute(Const.TITLE, "로그인");
		model.addAttribute(Const.VIEW, "user/login");
		
		return ViewRef.TEMP_DEFAULT;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(UserPARAM param, HttpSession hs, RedirectAttributes ra) {
		//System.out.println("id : " + param.getUser_id());
		//System.out.println("pw : " + param.getUser_pw());
		
		int result = service.login(param);
		
		if(result == Const.SUCCESS) {
			hs.setAttribute(Const.LOGIN_USER, param);
			return "redirect:/rest/map";
		}
		
		String msg = null;
		if(result == Const.NO_ID) {
			msg = "아이디를 확인 해 주세요";
		} else if(result == Const.NO_PW) {
			msg = "비밀번호를 확인 해 주세요";
		}
		param.setMsg(msg);
		ra.addFlashAttribute("data", param); //세션에 값이 저장되고 응답되면 초기화됨
		return "redirect:/user/login";
	}
	
	@RequestMapping(value="/join", method = RequestMethod.GET)
	public String join(Model model, @RequestParam(required=false, defaultValue="0") int err) {
		//System.out.println("err : " + err);
		
		if(err > 0) {
			model.addAttribute("msg", "에러가 발생했습니다.");
		}
		model.addAttribute(Const.TITLE, "회원가입");
		model.addAttribute(Const.VIEW, "user/join");
		
		return ViewRef.TEMP_DEFAULT;
	}
	
	@RequestMapping(value="/join", method = RequestMethod.POST )
	public String join(UserVO param, RedirectAttributes ra) { //join.jsp의 입력값을 request.getparameter 했을 때의 결과가 담겨져 있음
		int result = service.join(param); 
		
		if(result == 1) {
			return "redirect:/user/login";
		}
		ra.addAttribute("err", result); //쿼리스트링에 값을 넣어줌
		return "redirect:/user/join";
		
		//return "redirect:/user/join?err=" + result;
	}
	
	@RequestMapping(value="/ajaxIdChk", method = RequestMethod.POST)
	@ResponseBody
	public String ajaxIdChk(@RequestBody UserPARAM param) {
		//System.out.println("user_id : " + param.getUser_id());
		int result = service.login(param);
		return String.valueOf(result); //@ResponseBody의 결과: jsp파일이나, 주소를 찾지않고 return값 자체를 응답함
		
	}
}


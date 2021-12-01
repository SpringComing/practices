package com.douzone.mysite.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.security.AuthUser;
import com.douzone.mysite.service.BoardService;
import com.douzone.mysite.service.UserService;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.mysite.vo.PageVo;
import com.douzone.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String list(
			Model model, 
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd) {
		PageVo pageVo = new PageVo(1L);
		pageVo.setCount(boardService.getCountBoard(null));
		List<BoardVo> list = null;

		if (kwd != null && !kwd.replaceAll(" ", "").equals("")) {
			pageVo.setCount(boardService.getCountBoard(kwd));
			model.addAttribute("kwd", kwd);
		}

		if (pIndex != null) {
			if (pIndex > pageVo.getAblepIndex()) {
				pageVo.setpIndex(pageVo.getAblepIndex());
			} else if (pIndex < 1) {
				pageVo.setpIndex(1L);
			} else {
				pageVo.setpIndex(pIndex);
			}
		}

		if (kwd != null && !kwd.replaceAll(" ", "").equals("")) {
			list = boardService.getBoard(pageVo.getpIndex(), pageVo.getLines(), kwd);
		}

		else {
			list = boardService.getBoard(pageVo.getpIndex(), pageVo.getLines());
		}

		model.addAttribute("list", list);
		model.addAttribute("pageVo", pageVo);
		return "board/list";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(
			Model model, 
			@RequestParam(value= "no", required = false) Long no,
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd, 
			HttpServletRequest request,
			HttpServletResponse response) {

		BoardVo vo = boardService.getFindNo(no);
		String COOKIE_NAME = "visitView" + no;
		int visitState = 0; // 없음

		// 쿠키 읽기
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName())) {
					visitState = 1;
				}
			}
		}

		if (visitState == 0) {
			boardService.updateHit(no);
			// 쿠키 쓰기
			Cookie cookie = new Cookie(COOKIE_NAME, String.valueOf(1));
			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(5 * 60); // 5분
			response.addCookie(cookie);
		}
		model.addAttribute("pIndex", pIndex);
		model.addAttribute("kwd", kwd);
		model.addAttribute("vo", vo);
		return "board/view";

	}

	@Auth(role = "USER")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(
			Model model,
			@AuthUser UserVo authUser, 
			@RequestParam(value= "no", required = false) Long no,
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd) {

		BoardVo vo = boardService.getFindNo(no);
		model.addAttribute("pIndex", pIndex);
		model.addAttribute("kwd", kwd);
		if (authUser.getNo() == vo.getUserNo()) {	
			model.addAttribute("no", no);
			return "board/delete";
		}
		return "board/accessState";

	}

	@Auth(role = "USER")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(
			Model model, 
			@AuthUser UserVo authUser, 
			@RequestParam(value= "no", required = false) Long no,
			@RequestParam("password") String password,
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd) {

		BoardVo vo = boardService.getFindNo(no);
		String state = "비밀번호가 틀렸습니다.";
		UserVo userVo = userService.getUser(vo.getUserNo());
		System.out.println(no);
		System.out.println(vo);
		System.out.println(userVo);
		System.out.println(userVo.getPassword());
		if (userVo.getPassword().equals(password)) {

			if (boardService.updateDeleteUpdate(vo)) {
				state = "삭제되었습니다.";
			} else {
				state = "삭제되지 않았습니다.";
			}
		}
		model.addAttribute("pIndex", pIndex);
		model.addAttribute("kwd", kwd);
		model.addAttribute("state", state);
		return "board/accessState";
	}

	@Auth
	@RequestMapping(value = "/modify")
	public String modify(
			Model model,
			@AuthUser UserVo authUser, 
			@RequestParam(value = "no", required = false) Long no,
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd) {
		BoardVo vo = boardService.getFindNo(no);
		model.addAttribute("pIndex", pIndex);
		model.addAttribute("kwd", kwd);
		if (vo.getUserNo() == authUser.getNo()) {
			model.addAttribute("vo", vo);
			return "board/modify";
		}
		return "board/accessState";
	}

	@Auth
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(
			@AuthUser UserVo authUser, 
			Model model, 
			@RequestParam(value= "no", required = false) Long no,
			@ModelAttribute BoardVo boardVo, 
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd) throws IOException {
		BoardVo vo = boardService.getFindNo(no);
		vo.setTitle(boardVo.getTitle());
		vo.setContents(boardVo.getContents());
		boardService.updateBoard(vo);

		return "redirect:/board/view?no=" + no + "&pIndex=" + pIndex + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
	}

	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(
			Model model,
			@RequestParam(value= "no", required = false) Long no,
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd) {
		model.addAttribute("pIndex", pIndex);
		model.addAttribute("kwd", kwd);
		model.addAttribute("no", no);
		return "board/write";
	}
	
	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(
			@AuthUser UserVo authUser,  
			Model model,
			@RequestParam(value = "no", required = false) Long no,
			@ModelAttribute BoardVo boardVo, 
			@RequestParam(value = "pIndex", required = false) Long pIndex,
			@RequestParam(value = "kwd", required = false) String kwd) throws IOException {

		BoardVo vo = new BoardVo();
		Long groupNo = null;

		if (no != null) { // 댓글
			BoardVo parentVo = boardService.getFindNo(no);
			vo.setGroupNo(parentVo.getGroupNo());
			vo.setOrderNo(parentVo.getOrderNo() + 1);
			vo.setDepth(parentVo.getDepth() + 1);
			boardService.updateOrderNo(vo);
		}

		else { // 새글 group_no = ?, order_no = 1, depth = 0; // 첫 글쓰기
			groupNo = boardService.getFindMaxGroupNo();
			++groupNo;
			vo.setGroupNo(groupNo);
			vo.setOrderNo(1L);
			vo.setDepth(0L);
		}

		if (boardVo.getTitle().equals("")) {
			vo.setTitle("[없음]");
		} else {
			vo.setTitle(boardVo.getTitle());
		}
		
		vo.setContents(boardVo.getContents());
		vo.setUserNo(authUser.getNo());
		boardService.addBoard(vo);

		return "redirect:/board?pIndex=" + pIndex + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
	}

}

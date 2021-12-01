package com.douzone.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.vo.BoardVo;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	public List<BoardVo> getBoard(Long pIndex, Long lines) {
		return boardRepository.findLimit(pIndex, lines);
	}

	public List<BoardVo> getBoard(Long pIndex, Long lines, String kwd) {
		return boardRepository.findTitle(pIndex, lines, kwd);
	}

	public Long getCountBoard(String kwd) {
		return boardRepository.countVo(kwd);
	}

	public BoardVo getFindNo(Long no) {
		return boardRepository.findNo(no);
	}

	public void updateHit(Long no) {
		boardRepository.updateHit(no);
	}

	public boolean updateDeleteUpdate(BoardVo vo) {
		return boardRepository.deleteUpdate(vo);
	}

	public void updateBoard(BoardVo vo) {
		boardRepository.update(vo);
	}

	public void updateOrderNo(BoardVo vo) {
		boardRepository.updateOrderNo(vo);
	}

	public Long getFindMaxGroupNo() {
		return boardRepository.findMaxGroupNo();
	}

	public void addBoard(BoardVo vo) {
		boardRepository.insert(vo);
	}

}

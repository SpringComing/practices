package com.douzone.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.mysite.exception.BoardRepositoryException;
import com.douzone.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	@Autowired
	private SqlSession sqlSession;
	
	public Long countVo(String kwd) {
		return sqlSession.selectOne("board.countVo", kwd);
	}

	public List<BoardVo> findLimit(Long pIndex, Long lines) throws BoardRepositoryException {
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("startIndex", (pIndex - 1) * lines);
		map.put("lines", lines);
		System.out.println(sqlSession.selectList("board.findLimit", map));
		return sqlSession.selectList("board.findLimit", map);
	}

	public BoardVo findNo(Long no) throws BoardRepositoryException {
		return sqlSession.selectOne("board.findNo", no);
	}

	public Long findMaxGroupNo() throws BoardRepositoryException {
		return sqlSession.selectOne("board.findMaxGroupNo");
	}

	public List<BoardVo> findTitle(Long pIndex, Long lines, String kwd) throws BoardRepositoryException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startIndex", (pIndex-1)*lines);
		map.put("lines", lines);
		map.put("kwd", "%"+kwd+"%");
		return sqlSession.selectList("board.findTitle", map);
	}

	public boolean insert(BoardVo vo) {
		int count = sqlSession.insert("board.insert", vo);
		return count == 1;
	}

	public boolean deleteUpdate(BoardVo vo) {
		int count = sqlSession.update("board.deleteUpdate", vo);
		return count == 1;
	}

	public boolean update(BoardVo vo) {
		int count = sqlSession.update("board.update", vo);
		return count == 1;
	}

	public boolean updateOrderNo(BoardVo vo) {
		int count = sqlSession.update("board.updateOrderNo", vo);
		return count == 1;
	}

	public boolean updateHit(Long no) {
		int count = sqlSession.update("board.updateHit", no);
		return count == 1;
	}

}

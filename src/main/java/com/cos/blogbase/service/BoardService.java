package com.cos.blogbase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blogbase.dto.ReplySaveRequestDto;
import com.cos.blogbase.model.Board;
import com.cos.blogbase.model.User;
import com.cos.blogbase.repository.BoardRepository;
import com.cos.blogbase.repository.ReplyRepository;

@Service
public class BoardService {

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private ReplyRepository replyRepository;

	@Transactional
	public void 글쓰기(Board board, User user) { // title, content
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional(readOnly = true)
	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없음.");
		});
	}

	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}

	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없음.");
		}); // 영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수로 종료시(Service가 종료될 때) 트랜잭션이 종료된다. 이때 더티체킹 -> 자동 업데이트가 flush
	}

	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
		replyRepository.mSave(replySaveRequestDto.getUserId(), replySaveRequestDto.getBoardId(),
				replySaveRequestDto.getContent());
	}
	
	@Transactional
	public void 댓글삭제(int replyId) {
		replyRepository.deleteById(replyId);
	}
}

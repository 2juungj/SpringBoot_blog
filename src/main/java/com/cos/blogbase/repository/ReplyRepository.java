package com.cos.blogbase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.blogbase.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

	// 인터페이스 안에서는 public이 생략 가능하다.
	@Modifying // 없으면 SELECT 쿼리로 인식하므로 해당 어노테이션을 붙여서 INSERT 쿼리로 인식하게 한다.
	@Query(value = "INSERT INTO reply(userId, boardId, content, createDate) VALUES(?1, ?2, ?3, now())", nativeQuery = true)
	int mSave(int userId, int boardId, String content); // JDBC가 INSERT, UPDATE, DELETE를 수행하면 기본적으로 업데이트된 행의 개수를 리턴해준다. -1은 오류

}

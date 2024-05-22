package com.cos.blogbase.model;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob // 대용량 데이터
	private String content; // 섬머노트 라이브러리 <html>태그가 섞여서 디자인 된다.
	
	private int count; // 조회수
	
	@ManyToOne // Many = Board, User = One 한명의 유저가 여러 게시글을 작성할 수 있다.
	@JoinColumn(name = "userId")
	private User user; // DB는 오브젝트를 저장할 수 없어서 FK를 사용한다. 자바는 오브젝트를 저장할 수 있다.
	
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // mappedBy: 연관관계의 주인이 아니다.(FK가 아니다.) DB에 칼럼을 만들지 마라. FK는 board이다.
	@JsonIgnoreProperties({"board"}) // 순환참조 방지
	@OrderBy("id desc") // id를 기준으로 오름차순 정렬
	private List<Reply> replys;
	
	@CreationTimestamp
	private Timestamp createDate;
}

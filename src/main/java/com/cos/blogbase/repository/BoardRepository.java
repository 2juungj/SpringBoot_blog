package com.cos.blogbase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blogbase.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
}

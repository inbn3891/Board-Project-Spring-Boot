package com.study.demo.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.study.demo.DTO.BoardRequest;
import com.study.demo.DTO.BoardSearchRequest;
import com.study.demo.Domain.Board;

@Mapper
public interface BoardMapper {
    void save(BoardRequest params);

    Board findById(Long id);

    void update(BoardRequest params);

    void deleteById(Long id);

    List<Board> findAll(BoardSearchRequest params);

    void updateHit(final Long id);

    int count(BoardSearchRequest params);
}

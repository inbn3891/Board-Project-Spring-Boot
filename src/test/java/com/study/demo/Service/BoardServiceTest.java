package com.study.demo.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.demo.DTO.BoardRequest;
import com.study.demo.DTO.BoardResponse;
import com.study.demo.DTO.BoardSearchRequest;
import com.study.demo.Domain.Board; // Board Entity import
import com.study.demo.mapper.BoardMapper;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    
     @InjectMocks
     private BoardServiceImpl boardService;
    
     @Mock
     private BoardMapper boardMapper;

    // 테스트용 DTO 생성
     private BoardRequest createSampleBoardRequest() {
         BoardRequest request = new BoardRequest();
         request.setTitle("Sample Title");
         request.setContent("Sample Content");
         request.setWriter("Sample Writer");
         return request;
     }
    
    // 📌 Mapper가 반환할 Board Entity 생성 (findById, findAll용)
    private Board createSampleBoardEntity(Long id, int hit) {
        // 실제 Board Domain 객체의 필드와 생성 로직에 맞게 구현해야 합니다.
        Board board = new Board(); // 가상의 Board Entity 객체 생성
        board.setId(id);
        board.setTitle("Test Title " + id);
        board.setContent("Test Content");
        board.setWriter("Test Writer");
        board.setHit(hit);
        board.setCreatedDate(LocalDateTime.now());
        return board;
    }


     @Test
     @DisplayName("게시글 저장 성공 테스트")
     void savePostTest() {
         // given
         BoardRequest request = createSampleBoardRequest();

         // when
         boardService.savePost(request);

         // then
         // 📌 boardMapper.save(request)가 1번 호출되었는지 검증 (Mapper 정의와 일치)
         then(boardMapper).should(times(1)).save(request);
     }

     @Test
     @DisplayName("게시글 ID로 조회 및 조회수 증가 테스트")
     void findPostByIdAndHitTest() {
         // given
         Long postId = 1L;
         int initialHit = 0;
        
        // 📌 Mapper가 반환할 Board Entity 객체를 Mocking
         Board mockEntity = createSampleBoardEntity(postId, initialHit);

         // Mocking: findById가 호출되면 Board Entity를 반환하도록 설정
         given(boardMapper.findById(postId)).willReturn(mockEntity); 

         // when
         BoardResponse result = boardService.findPostById(postId);

         // then
         // 1. boardMapper.updateHit(postId) 메서드가 1번 호출되었는지 검증
         then(boardMapper).should(times(1)).updateHit(postId);

         // 2. boardMapper.findById(postId) 메서드가 1번 호출되었는지 검증
         then(boardMapper).should(times(1)).findById(postId);
    
         // 3. 반환된 DTO의 ID가 일치하는지 확인 (Service의 변환 로직 검증)
         assertEquals(postId, result.getId());
        assertEquals(mockEntity.getTitle(), result.getTitle());
     }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void updatePostTest() {
        // given
        BoardRequest request = createSampleBoardRequest();
        request.setId(2L);
        
        // when
        boardService.updatePost(request);
        
        // then
        // 📌 boardMapper.update(request) 메서드가 1번 호출되었는지 검증
        then(boardMapper).should(times(1)).update(request);
    }
    
    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void deletePostTest() {
        // given
        Long postId = 3L;
        
        // when
        Long resultId = boardService.deletePost(postId);
        
        // then
        // 📌 boardMapper.deleteById(postId) 메서드가 1번 호출되었는지 검증
        then(boardMapper).should(times(1)).deleteById(postId);
        assertEquals(postId, resultId);
    }
    
    @Test
    @DisplayName("게시글 전체 조회 (페이징/검색 포함) 테스트")
    void findAllPostsTest() {
        // given
        BoardSearchRequest params = new BoardSearchRequest(); // 빈 검색 조건
        Board entity1 = createSampleBoardEntity(10L, 5);
        Board entity2 = createSampleBoardEntity(11L, 3);
        
        // Mocking: findAll이 호출되면 두 개의 Entity를 포함한 List를 반환하도록 설정
        given(boardMapper.findAll(params)).willReturn(Arrays.asList(entity1, entity2));
        
        // when
        List<BoardResponse> results = boardService.findAllPosts(params);
        
        // then
        // 1. boardMapper.findAll(params)가 1번 호출되었는지 검증
        then(boardMapper).should(times(1)).findAll(params);
        
        // 2. 반환된 리스트의 크기가 Entity 개수와 일치하는지 검증
        assertEquals(2, results.size());
        
        // 3. Service가 Entity를 DTO로 변환했는지 검증
        assertEquals(entity1.getId(), results.get(0).getId());
        assertEquals(entity2.getTitle(), results.get(1).getTitle());
    }

    @Test
    @DisplayName("게시글 전체 개수 조회 테스트")
    void countPostsTest() {
        // given
        BoardSearchRequest params = new BoardSearchRequest();
        int expectedCount = 42;
        
        // Mocking: count가 호출되면 42를 반환하도록 설정
        given(boardMapper.count(params)).willReturn(expectedCount);
        
        // when
        int resultCount = boardService.countPosts(params);
        
        // then
        // 1. boardMapper.count(params)가 1번 호출되었는지 검증
        then(boardMapper).should(times(1)).count(params);
        
        // 2. 반환된 개수가 예상 값과 일치하는지 검증
        assertEquals(expectedCount, resultCount);
    }
}
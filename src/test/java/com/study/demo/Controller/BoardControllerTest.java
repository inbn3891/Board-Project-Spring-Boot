package com.study.demo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.demo.Service.BoardService;
import com.study.demo.DTO.BoardRequest;
import com.study.demo.DTO.BoardResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

// BoardController만 테스트에 로드합니다.
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    // HTTP 요청을 시뮬레이션하는 객체
    @Autowired
    private MockMvc mockMvc;

    // JSON 직렬화를 위한 객체
    @Autowired
    private ObjectMapper objectMapper;

    // Controller가 의존하는 Service는 Mock 객체로 대체합니다.
    @MockBean
    private BoardService boardService;

    // 테스트용 DTO 생성
    private BoardRequest createSampleBoardRequest() {
        BoardRequest request = new BoardRequest();
        request.setTitle("Controller Test Title");
        request.setContent("Controller Test Content");
        request.setWriter("Tester");
        return request;
    }
    @Test
        @DisplayName("게시글 목록 페이지 조회")
        void openBoardListTest() throws Exception {
            // given: Service의 findAllPosts 호출은 성공한다고 가정 (MockBean 사용)
            
            // when & then
            mockMvc.perform(get("/board/list"))
                    .andDo(print()) // 요청/응답 전체 로그 출력
                    .andExpect(status().isOk()) // HTTP 상태 코드 200 검증
                    .andExpect(view().name("board/list")) // View 이름 검증
                    .andExpect(model().attributeExists("response")) // Model에 'response' 객체가 있는지 검증
                    .andExpect(model().attributeExists("params")); // Model에 'params' 객체가 있는지 검증
        }

    @Test
    @DisplayName("새 글 작성 (POST) 후 리다이렉트 검증")
    void savePostRedirectTest() throws Exception {
        // given
        BoardRequest request = createSampleBoardRequest();
        
        // Service가 호출될 때 성공적으로 처리된다고 가정
        given(boardService.savePost(any(BoardRequest.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/board/save")
                        .param("title", request.getTitle()) // form 데이터를 파라미터로 전달
                        .param("content", request.getContent())
                        .param("writer", request.getWriter()))
                .andDo(print())
                .andExpect(status().is3xxRedirection()) // HTTP 상태 코드 302(Redirect) 검증
                .andExpect(redirectedUrl("/board/list")); // 리다이렉트 경로 검증
        
        // Service 메서드가 1번 호출되었는지 검증
        then(boardService).should(times(1)).savePost(any(BoardRequest.class));
    }


    @Test
    @DisplayName("새 글 작성 (POST) 실패 시 Validation 에러 검증")
    void savePostValidationFailTest() throws Exception {
        // given
        // 제목이 없는 Request (Validation 실패 유도)
        BoardRequest invalidRequest = new BoardRequest();
        invalidRequest.setTitle(""); // NotBlank에 걸릴 빈 문자열
        invalidRequest.setContent("내용");
        invalidRequest.setWriter("작성자");
        
        // Service는 호출되지 않도록 가정 (Validation 단계에서 막힘)
        
        // when & then
        mockMvc.perform(post("/board/save")
                        .param("title", invalidRequest.getTitle())
                        .param("content", invalidRequest.getContent())
                        .param("writer", invalidRequest.getWriter()))
                .andDo(print())
                .andExpect(status().isOk()) // 📌 리다이렉트되지 않고 200 OK로 돌아와야 함 (board/write 뷰)
                .andExpect(view().name("board/write")) // 📌 board/write 뷰로 돌아왔는지 검증
                .andExpect(model().attributeHasFieldErrors("post", "title")); // 📌 Model에 'post' 객체의 'title' 필드에 에러가 있는지 검증

        // Service 메서드가 호출되지 않았는지 검증
        then(boardService).should(times(0)).savePost(any(BoardRequest.class));
    }
    
    
    @Test
    @DisplayName("게시글 상세 조회 페이지 검증")
    void openBoardViewTest() throws Exception {
        // given
        Long postId = 1L;
        // Service가 findPostById 호출 시 BoardResponse를 반환한다고 가정
        given(boardService.findPostById(postId))
            .willReturn(new BoardResponse(postId, "제목", "내용", "작성자", 10, null, null));

        // when & then
        mockMvc.perform(get("/board/view/{id}", postId))
                .andDo(print())
                .andExpect(status().isOk()) // HTTP 상태 코드 200 검증
                .andExpect(view().name("board/view")) // View 이름 검증
                .andExpect(model().attributeExists("post")); // Model에 'post' 객체가 있는지 검증

        // Service 메서드가 1번 호출되었는지 검증
        then(boardService).should(times(1)).findPostById(postId);
    }
    
    @Test
    @DisplayName("게시글 삭제 요청 후 리다이렉트 검증")
    void deletePostRedirectTest() throws Exception {
        // given
        Long postId = 1L;
        // Service가 deletePost 호출 시 성공적으로 ID를 반환한다고 가정
        given(boardService.deletePost(postId)).willReturn(postId);

        // when & then
        // DELETE 요청을 시뮬레이션
        mockMvc.perform(post("/board/delete/{id}", postId)
                        .with(request -> { // Spring 6에서 DELETE를 POST로 시뮬레이션할 때 사용 (HiddenHttpMethodFilter 가정)
                            request.setMethod("DELETE");
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().is3xxRedirection()) // 리다이렉트 검증
                .andExpect(redirectedUrl("/board/list")); // 목록 페이지로 리다이렉트 검증
        
        // Service 메서드가 1번 호출되었는지 검증
        then(boardService).should(times(1)).deletePost(postId);
    }

    // 테스트에 필요한 BoardResponse Mock 객체 생성
    private BoardResponse createSampleBoardResponse(Long id) {
        return new BoardResponse(id, "View Title", "View Content", "Viewer", 5, null, null);
    }
    
    // --- 게시글 수정(Edit) 관련 테스트 ---

    @Test
    @DisplayName("게시글 수정 페이지 조회 검증")
    void openBoardEditTest() throws Exception {
        // given
        Long postId = 1L;
        // Service가 findPostById 호출 시 BoardResponse를 반환한다고 가정
        given(boardService.findPostById(postId)).willReturn(createSampleBoardResponse(postId));

        // when & then
        mockMvc.perform(get("/board/edit/{id}", postId))
                .andDo(print())
                .andExpect(status().isOk()) // HTTP 상태 코드 200 검증
                .andExpect(view().name("board/edit")) // 📌 board/edit 뷰로 이동했는지 검증
                .andExpect(model().attributeExists("post")); // 📌 Model에 'post' 객체(기존 데이터)가 있는지 검증

        // Service 메서드가 1번 호출되었는지 검증
        then(boardService).should(times(1)).findPostById(postId);
    }

    @Test
    @DisplayName("게시글 수정 (PATCH) 성공 후 상세 페이지 리다이렉트 검증")
    void editPostSuccessTest() throws Exception {
        // given
        Long postId = 1L;
        BoardRequest request = createSampleBoardRequest(); // 유효한 수정 데이터
        request.setId(postId);
        
        // Service가 updatePost 호출 시 성공적으로 ID를 반환한다고 가정
        given(boardService.updatePost(any(BoardRequest.class))).willReturn(postId);

        // when & then
        // PATCH 요청을 시뮬레이션
        mockMvc.perform(post("/board/edit/{id}", postId)
                        .param("id", String.valueOf(postId))
                        .param("title", "수정된 제목")
                        .param("content", request.getContent())
                        .param("writer", request.getWriter())
                        .with(req -> { // HiddenHttpMethodFilter를 이용한 PATCH 요청 시뮬레이션
                            req.setMethod("PATCH");
                            return req;
                        }))
                .andDo(print())
                .andExpect(status().is3xxRedirection()) // 리다이렉트 검증
                .andExpect(redirectedUrl("/board/view/" + postId)); // 📌 상세 페이지로 리다이렉트 검증

        // Service 메서드가 1번 호출되었는지 검증
        then(boardService).should(times(1)).updatePost(any(BoardRequest.class));
    }

    @Test
    @DisplayName("게시글 수정 (PATCH) 실패 시 Validation 에러 검증")
    void editPostValidationFailTest() throws Exception {
        // given
        Long postId = 2L;
        // Validation 실패 시 Controller가 원본 데이터를 다시 Model에 담아야 하므로 Mocking 필요
        given(boardService.findPostById(postId)).willReturn(createSampleBoardResponse(postId));
        
        // Validation 실패를 유도하는 Request (제목 누락)
        BoardRequest invalidRequest = new BoardRequest();
        invalidRequest.setId(postId);
        invalidRequest.setTitle(""); // NotBlank 실패
        invalidRequest.setContent("유효한 내용");
        invalidRequest.setWriter("유효한 작성자");
        
        // when & then
        mockMvc.perform(post("/board/edit/{id}", postId)
                        .param("id", String.valueOf(postId))
                        .param("title", invalidRequest.getTitle())
                        .param("content", invalidRequest.getContent())
                        .param("writer", invalidRequest.getWriter())
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isOk()) // 📌 리다이렉트되지 않고 200 OK로 돌아와야 함
                .andExpect(view().name("board/edit")) // 📌 board/edit 뷰로 돌아왔는지 검증
                .andExpect(model().attributeHasFieldErrors("boardRequest", "title")); // 📌 Model에 에러가 있는지 검증

        // Service의 updatePost 메서드는 호출되지 않아야 합니다.
        then(boardService).should(times(0)).updatePost(any(BoardRequest.class));
        // Controller가 Validation 실패 시 원본 데이터를 로드하기 위해 findPostById가 호출되어야 합니다.
        then(boardService).should(times(1)).findPostById(postId);
    }
}
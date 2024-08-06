package study.board.unittest.board;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import study.board.domain.board.application.BoardService;
import study.board.global.enums.SortType;
import study.board.global.common.res.ApiResponse;
import study.board.domain.board.api.BoardController;
import study.board.domain.board.dto.req.BoardDeleteRequestDto;
import study.board.domain.board.dto.req.BoardRequestDto;
import study.board.domain.board.dto.res.BoardResponseDto;
import study.board.domain.board.dto.req.BoardSearchCondition;

@WebMvcTest(BoardController.class)
@DisplayName("게시판 컨트롤러 계층 단위테스트")
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    private BoardRequestDto boardRequestDto;
    private BoardResponseDto boardResponseDto;

    @BeforeEach
    void setup() {
        boardRequestDto = new BoardRequestDto("Test Title", "jjh", "1234", "Test content");
        boardResponseDto = new BoardResponseDto("Test Title", "jjh", "Test content",
            LocalDateTime.now());
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 등록")
    void publish_board() throws Exception {
        when(boardService.publishBoard(any(BoardRequestDto.class))).thenReturn(boardResponseDto);

        mockMvc.perform(post("/board")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardRequestDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(
                ApiResponse.createResponse(true, boardResponseDto, "게시글 등록 완료", HttpStatus.CREATED)
                    .getBody())));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 전체 조회")
    void read_all_boards() throws Exception {
        // 반환할 값 설정
        boardResponseDto = new BoardResponseDto("Test Title", "jjh", "Test content",
            LocalDateTime.now());
        when(boardService.readAll(any(BoardSearchCondition.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(boardResponseDto)));

        ResultActions resultActions = mockMvc.perform(get("/board")
            .contentType(MediaType.APPLICATION_JSON)
            .param("title", "") // 검색 조건 암거나 추가
            .param("userName", "")
            .param("content", "")
            .param("sortType", SortType.ASC.toString())
            .param("page", "0")
            .param("size", "5"));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(
                ApiResponse.createResponse(true, new PageImpl<>(List.of(boardResponseDto)),
                    "게시글 전체 조회 성공", HttpStatus.OK).getBody()
            )));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 단건 조회")
    void readOne() throws Exception {
        // given
        Long boardId = 1L;
        BoardResponseDto responseDto = new BoardResponseDto("Test Title", "jjh", "Test content", LocalDateTime.now());
        given(boardService.readOne(boardId)).willReturn(responseDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/board/{boardId}", boardId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(
                ApiResponse.createResponse(true, responseDto, "게시글 조회 성공", HttpStatus.OK).getBody())));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 수정")
    void update() throws Exception {
        // given
        Long boardId = 1L;
        BoardRequestDto requestDto = new BoardRequestDto("Updated Title", "jjh", "1234", "Updated content");
        BoardResponseDto responseDto = new BoardResponseDto("Updated Title", "jjh", "Updated content", LocalDateTime.now());
        given(boardService.update(boardId, requestDto)).willReturn(responseDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/board/{boardId}", boardId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(
                ApiResponse.createResponse(true, responseDto, "게시글 수정 성공", HttpStatus.OK).getBody())));
    }

    @Test
    @WithMockUser
    @DisplayName("게시글 삭제")
    void delete() throws Exception {
        // given
        Long boardId = 1L;
        BoardDeleteRequestDto requestDto = new BoardDeleteRequestDto("1234");
        given(boardService.delete(boardId, requestDto)).willReturn(boardId);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/{boardId}", boardId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isNoContent())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(
                ApiResponse.createResponse(true, boardId, "게시글 삭제 성공", HttpStatus.NO_CONTENT).getBody())));
    }


}
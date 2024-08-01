package study.board.unittest.board;


import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import study.board.application.BoardService;
import study.board.global.ApiResponse;
import study.board.presentation.BoardController;
import study.board.presentation.dto.BoardRequestDto;
import study.board.presentation.dto.BoardResponseDto;

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
    @WithMockUser// 인증된 사용자로 테스트 수행
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
    @WithMockUser  // 인증된 사용자로 테스트 수행
    @DisplayName("게시글 전체 조회")
    void read_all_boards() throws Exception {
        when(boardService.readAll()).thenReturn(List.of(boardResponseDto));

        mockMvc.perform(get("/board")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(
                ApiResponse.createResponse(true, List.of(boardResponseDto), "게시글 전체 조회 성공",
                    HttpStatus.OK).getBody())));
    }
}
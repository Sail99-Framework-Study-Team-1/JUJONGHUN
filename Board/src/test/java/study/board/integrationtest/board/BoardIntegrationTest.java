package study.board.integrationtest.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import study.board.application.BoardService;
import study.board.domain.entity.Board;
import study.board.domain.repo.BoardRepository;
import study.board.presentation.dto.BoardDeleteRequestDto;
import study.board.presentation.dto.BoardRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@DisplayName("board 도메인 통합 테스트")
class BoardIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private BoardRequestDto boardRequestDto;

    private Long boardId;


    @BeforeEach
    public void setup() throws Exception {
        // 게시글 등록
        boardRequestDto = new BoardRequestDto("Test Title", "jjh", "1234", "Test content");

        ResultActions resultActions = mockMvc.perform(post("/board")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(boardRequestDto)));

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // db에서 등록한거 꺼내오기
        Board savedBoard = boardRepository.findAll().get(0);

        boardId = savedBoard.getId();
    }


    @Test
    @DisplayName("[성공] 게시글 등록")
    void success_publish_board() {

        // db에서 등록한거 꺼내오기
        Board savedBoard = boardRepository.findAll().get(0);

        assertThat(savedBoard)
            .as("게시글 검증")
            .isNotNull()
            .extracting(Board::getTitle, Board::getUserName, Board::getPassword, Board::getContent)
            .satisfies(values -> {
                assertThat(values.get(0)).isEqualTo(boardRequestDto.title());
                assertThat(values.get(1)).isEqualTo(boardRequestDto.userName());
                assertThat(values.get(2))
                    .matches(password -> passwordEncoder
                        .matches(boardRequestDto.password(), (String) password));
                assertThat(values.get(3)).isEqualTo(boardRequestDto.content());
            });
    }

    @Test
    @DisplayName("[성공] 게시글 전체 조회")
    void success_read_all_boards() throws Exception {

        // 게시글 전체 조회
        ResultActions resultActions = mockMvc.perform(get("/board")
            .contentType(MediaType.APPLICATION_JSON));

        // 검증
        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.content[0].title").value(boardRequestDto.title()))
            .andExpect(jsonPath("$.data.content[0].userName").value(boardRequestDto.userName()))
            .andExpect(jsonPath("$.data.content[0].content").value(boardRequestDto.content()))
            .andExpect(jsonPath("$.data.content[0].createdAt").exists()); // createdAt 필드가 존재하는지 확인
    }


    @Test
    @DisplayName("[성공] 게시글 수정")
    void success_update_board() throws Exception {
        BoardRequestDto updateRequestDto = new BoardRequestDto("Updated Title", "jjh", "1234",
            "Updated content");

        ResultActions resultActions = mockMvc.perform(put("/board/{id}", boardId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequestDto)));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.title").value(updateRequestDto.title()))
            .andExpect(jsonPath("$.data.userName").value(updateRequestDto.userName()))
            .andExpect(jsonPath("$.data.content").value(updateRequestDto.content()))
            .andExpect(jsonPath("$.data.createdAt").exists()); // createdAt 필드가 존재하는지 확인

        Board updatedBoard = boardRepository.findById(boardId)
            .orElseThrow(() -> new AssertionError("게시글이 데이터베이스에 존재하지 않습니다."));

        assertThat(updatedBoard)
            .as("게시글 검증")
            .isNotNull()
            .extracting(Board::getTitle, Board::getUserName, Board::getPassword, Board::getContent)
            .satisfies(values -> {
                assertThat(values.get(0)).isEqualTo(updateRequestDto.title());
                assertThat(values.get(1)).isEqualTo(updateRequestDto.userName());
                assertThat(values.get(2))
                    .matches(password -> passwordEncoder
                        .matches(updateRequestDto.password(), (String) password));
                assertThat(values.get(3)).isEqualTo(updateRequestDto.content());
            });
    }


    @Test
    @WithMockUser
    @DisplayName("[실패] 게시글 등록 : 잘못된 요청")
    void failure_publish_board_with_invalid_data() throws Exception {
        BoardRequestDto invalidRequestDto = new BoardRequestDto("", "", "", "");
        mockMvc.perform(post("/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[성공] 게시글 삭제")
    void success_delete_board() throws Exception {

        BoardDeleteRequestDto deleteRequestDto = new BoardDeleteRequestDto("1234");

        ResultActions resultActions = mockMvc.perform(delete("/board/{id}", boardId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(deleteRequestDto)));

        resultActions
            .andExpect(status().isNoContent());

        // 삭제된 게시글 조회 시 404 응답
        mockMvc.perform(get("/board/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[실패] 게시글 수정 : 비밀번호 불일치")
    void failure_update_board_with_wrong_password() throws Exception {
        BoardRequestDto updateRequestDto = new BoardRequestDto("Updated Title", "jjh", "wrong_password",
            "Updated content");

        mockMvc.perform(put("/board/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestDto)))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[실패] 게시글 삭제 : 비밀번호 불일치")
    void failure_delete_board_with_wrong_password() throws Exception {
        BoardDeleteRequestDto deleteRequestDto = new BoardDeleteRequestDto("wrong_password");

        mockMvc.perform(delete("/board/{id}", boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deleteRequestDto)))
            .andExpect(status().isForbidden());
    }

}

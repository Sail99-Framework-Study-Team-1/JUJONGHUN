package study.board.integrationtest.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.board.application.BoardService;
import study.board.domain.entity.Board;
import study.board.domain.repo.BoardRepository;
import study.board.presentation.dto.BoardRequestDto;

@SpringBootTest
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

    @BeforeEach
    public void setup() {
        boardRequestDto = new BoardRequestDto("Test Title", "jjh", "1234", "Test content");
    }

    @Test
    @DisplayName("[성공] 게시글 등록")
    void success_publish_board() throws Exception {
        var resultActions = mockMvc.perform(post("/board")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(boardRequestDto)));

        resultActions
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // db에서 등록한거 꺼내오기
        var savedBoard = boardRepository.findAll().get(0);

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
        mockMvc.perform(post("/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(boardRequestDto)))
            .andExpect(status().isCreated());

        var resultActions = mockMvc.perform(get("/board")
            .contentType(MediaType.APPLICATION_JSON));

        resultActions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("[실패] 게시글 등록 : 잘못된 요청")
    void failure_publish_board_with_invalid_data() throws Exception {
        var invalidRequestDto = new BoardRequestDto("", "", "", "");
        mockMvc.perform(post("/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDto)))
            .andExpect(status().isBadRequest());
    }
}

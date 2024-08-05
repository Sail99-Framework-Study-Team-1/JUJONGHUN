package study.board;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardApplicationTests {

    @Test
    void contextLoads() {
        // ApplicationContext가 정상적으로 로드되는지 테스트
        assertThat(true).isTrue();
    }

}

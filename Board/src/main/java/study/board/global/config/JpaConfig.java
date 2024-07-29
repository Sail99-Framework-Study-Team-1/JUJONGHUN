package study.board.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
// BaseEntity 사용을 위한 config (EnableJpaAuditing)
public class JpaConfig {
}

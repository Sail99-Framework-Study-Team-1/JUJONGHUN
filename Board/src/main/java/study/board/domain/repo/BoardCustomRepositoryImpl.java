package study.board.domain.repo;

import static study.board.domain.entity.QBoard.board;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import study.board.domain.enums.SortType;
import study.board.presentation.dto.BoardResponseDto;
import study.board.presentation.dto.BoardSearchCondition;

@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<BoardResponseDto> findBoardPage(BoardSearchCondition condition, Pageable pageable) {
        List<BoardResponseDto> content = jpaQueryFactory.select(
                Projections.constructor(BoardResponseDto.class,
                    board.title,
                    board.userName,
                    board.content,
                    board.createdAt
                    ))
            .from(board)
            .where(
                isActive(),
                titleContains(condition.title()),
                userNameContains(condition.userName()),
                contentContains(condition.content())
            )
            .orderBy(getSortOrder(condition.sortType()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(board.count())
            .from(board)
            .where(
                isActive(),
                titleContains(condition.title()),
                userNameContains(condition.userName()),
                contentContains(condition.content())
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isActive() {
        return board.isActive.eq(true);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? board.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression userNameContains(String userName) {
        return userName != null ? board.userName.containsIgnoreCase(userName) : null;
    }

    private BooleanExpression contentContains(String content) {
        return content != null ? board.content.containsIgnoreCase(content) : null;
    }

    private OrderSpecifier<?> getSortOrder(SortType sortType) {
        if (sortType == null) {
            return board.createdAt.desc();
        }
        return switch (sortType) {
            case ASC -> board.createdAt.asc();
            case DESC -> board.createdAt.desc();
        };
    }
}

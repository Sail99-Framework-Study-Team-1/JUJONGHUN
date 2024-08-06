package study.board.domain.board.dto.req;

import study.board.global.enums.SortType;

public record BoardSearchCondition (
    String title,
    String userName,
    String content,
    SortType sortType
){

}

package study.board.presentation.dto;

import study.board.domain.enums.SortType;

public record BoardSearchCondition (
    String title,
    String userName,
    String content,
    SortType sortType
){

}

package com.blog.dto;

import lombok.*;

import java.util.List;


/**
 * 페이징 처리를 위해 생성한 클래스입니다.
 * 기존의 HashMap에 객체를 담아서 보내는 방식은 코드의 가독성이나
 * 코드길이가 너무 길어지는 단점이 존재하여 객체를 직접 생성하여 보내는 방식으로 변경했습니다.
 * 페이징처리된 View에 출력할 데이터를 가지고있는 PagingList와
 * 각각 View의 페이지 버튼을 출력하기위한 int형 데이터 3가지
 *  검색어 입력 데이터를 가진 문자열 객체 1개를 가지고 있습니다.
 */
@Getter
@AllArgsConstructor
@Builder
public class PagingList {
    private List<?> pagingList;
    private int nowPage;
    private int endPage;
    private int startPage;

    private String nowSearchText;






}

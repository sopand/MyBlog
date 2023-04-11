package com.blog.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PagingList {

    private List<?> pagingList;
    private int nowPage;
    private int endPage;
    private int startPage;





}

package com.study.demo.DTO;

import java.util.List;

import lombok.Getter;

@Getter
public class PagingResponse<T> {
    private List<T> list;
    private int totalCount;
    private int totalPageCount;
    private int currentPage;
    private int pageSize;
    private int startPage;
    private int endPage;

    public PagingResponse(List<T> list, int totalCount, BoardSearchRequest params) {
        this.list = list;
        this.totalCount = totalCount;
        this.currentPage = params.getPage();
        this.pageSize = params.getPageSize();

        this.totalPageCount = (int) Math.ceil((double) totalCount / pageSize);

        int pageBlocakSize = 10;
        this.startPage = ((currentPage - 1) / pageBlocakSize) * pageBlocakSize + 1;
        this.endPage = Math.min(startPage + pageBlocakSize - 1, totalPageCount);
    }
}

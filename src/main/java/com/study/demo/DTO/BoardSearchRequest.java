package com.study.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchRequest {
    private String searchType;
    private String keyword; 
    private int page = 1;
    private int pageSize = 10;

    public int getOffset() {
        return (page - 1) * pageSize;
    }
}

package com.study.demo.DTO;

import java.time.LocalDateTime;
import lombok.Getter;


@Getter
public class BoardResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String writer;
    private final int hit;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public BoardResponse(Long id, String title, String content, String writer, int hit, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.hit = hit;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}

package com.study.demo.Domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int hit;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}

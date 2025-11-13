package com.study.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequest {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private String content;
    @NotBlank(message = "작성자는 필수 입력 항목입니다.")
    private String writer;
}

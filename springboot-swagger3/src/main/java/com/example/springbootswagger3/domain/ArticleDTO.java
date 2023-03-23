package com.example.springbootswagger3.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleDTO {
    @ApiModelProperty(value = "作者Id")
    private String authorId;
    @ApiModelProperty(value = "文章ID")
    private Integer id;
}
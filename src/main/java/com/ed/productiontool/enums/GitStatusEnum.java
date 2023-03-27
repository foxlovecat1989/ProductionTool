package com.ed.productiontool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum GitStatusEnum {
    ADD("A", "新增狀態"),
    MODIFIED("M", "修改狀態"),
    DELETE("D", "刪除狀態"),
    ERROR("E", "錯誤狀態");

    private final String status;
    private final String description;

    public static GitStatusEnum getEnum(String status) {
        return Arrays.stream(GitStatusEnum.values())
                .filter(value -> value.getStatus().equals(status.toUpperCase()))
                .findAny().orElse(ERROR);
    }
}

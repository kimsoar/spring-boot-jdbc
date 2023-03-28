package me.jdbc.basic.JDBC.Basic.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result2 {
    private String cursorName;
    private Integer no;
}

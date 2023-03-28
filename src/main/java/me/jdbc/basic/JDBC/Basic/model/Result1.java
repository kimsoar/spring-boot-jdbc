package me.jdbc.basic.JDBC.Basic.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Result1 {

    private String cursorName;
    private Integer no;
    private LocalDateTime currentTimestamp;
}

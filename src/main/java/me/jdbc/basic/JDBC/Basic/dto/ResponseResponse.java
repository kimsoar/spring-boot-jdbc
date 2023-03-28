package me.jdbc.basic.JDBC.Basic.dto;

import lombok.Getter;
import lombok.Setter;
import me.jdbc.basic.JDBC.Basic.model.PgTable;
import me.jdbc.basic.JDBC.Basic.model.Result1;
import me.jdbc.basic.JDBC.Basic.model.Result2;

import java.util.List;

@Getter
@Setter
public class ResponseResponse {
    private List<Result1> result1List;
    private List<Result2> result2List;
    private List<PgTable> pgTableList;
}

package me.jdbc.basic.JDBC.Basic.controller;

import me.jdbc.basic.JDBC.Basic.dao.MultiRefcusorWithToMapDao;
import me.jdbc.basic.JDBC.Basic.dto.CustomerRequest;
import me.jdbc.basic.JDBC.Basic.dto.ResponseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private MultiRefcusorWithToMapDao multiRefcusorWithToMapDao;

    @GetMapping
    public ResponseEntity<ResponseResponse> test() {
        ResponseResponse dto = multiRefcusorWithToMapDao.getResponseDto();
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping
    public ResponseEntity<String> createCustomer(@RequestBody CustomerRequest customerRequest) throws SQLException {
        multiRefcusorWithToMapDao.createCustomer(customerRequest);

        return ResponseEntity.ok().body("created");
    }
}

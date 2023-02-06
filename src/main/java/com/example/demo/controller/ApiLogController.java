package com.example.demo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class ApiLogController {

    final HttpServletResponse response;

    @GetMapping("")
    void log(@RequestParam String file) throws IOException {
        val in = new FileInputStream(file);
        val out = response.getOutputStream();
        FileCopyUtils.copy(in, out);
        in.close();
        response.flushBuffer();
    }
}

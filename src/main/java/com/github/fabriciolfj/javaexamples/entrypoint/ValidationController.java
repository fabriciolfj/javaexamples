package com.github.fabriciolfj.javaexamples.entrypoint;

import com.github.fabriciolfj.javaexamples.config.ConstructPreDestroyConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/validation")
@RestController
public class ValidationController {

    private final ConstructPreDestroyConfig send;

    @GetMapping
    public void test() {
        send.test();
    }
}

package study.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping("/hello") //Get 요청
    public String hello(){
        return "hello"; //바디 영역의 응답 메시지로 보냄. text/plain (plain은 일반 타입)
    }
}

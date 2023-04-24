package study.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    // /reverse?words=hello,world,mirim
    // 문자열 "mirim,world,hello"를 표시하도록 핸들러 메서드 구현
    @GetMapping("/reverse")
    public String reqest(@RequestParam("words") String words) {
        String str = "";
        // split을 활용하여 "," 별로 문자를 자른 훈 배열에 넣어줌.
        String word[] = words.split(",");
        for(int i = word.length-1; i >= 0; i--){
            str += word[i];
            // ","를 통해서 각 자리 구분 => 조건은 마지막 자리에는 ","가 출력되지 않도록 해줌.
            if(i != 0) str += ",";
        }
        return str;
    }
}
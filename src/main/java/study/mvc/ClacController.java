package study.mvc;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.xml.crypto.Data;
import java.nio.channels.DatagramChannel;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class DateClass{
    private String date;
    private String time;

    public String getDate() { return date; }
    public String getTime() { return time; }

    public void setDATE(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }

    public DateClass(String date, String time){
        this.date = date;
        this.time = time;
    }

}
@RestController
@RequestMapping("/calc")
public class ClacController{
    @GetMapping("/{mas}")
    public String reqest3(@PathVariable("mas") String mas,
                          @RequestParam("num1") Integer num1,
                          @RequestParam("num2") Integer num2) throws Exception{
        int num = 0;
        if(mas.equals("mul")) {
            num = num1 * num2;
        }
        else if(mas.equals("add")) {
            num = num1 + num2;
        }
        else if(mas.equals("sub")){
            num = num1 - num2;
        }else{
            throw new Exception("해당 연산자는 지원하지 않습니다.");
        }
        return "" + num;
//        return String.valueOf(num);
    }
    int count = 0;
    @PostMapping("/count")
    public String counts(){
        count++;
        return "" + count;
    }
//    @GetMapping(value = "/now")
//    public DateClass dateprin(){
//        LocalDateTime now = LocalDateTime.now();
//        return new DateClass(
//                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
//                now.format(DateTimeFormatter.ofPattern("hh:mm:ss"))
//        );
//    }

    @GetMapping(value = "/now")
    public Map<String, Object> dateprin(){
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> map = new HashMap<>();
        map.put("date", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        map.put("time", now.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
        return map;
    }
}

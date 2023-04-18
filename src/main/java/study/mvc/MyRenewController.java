package study.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;

class Api {
    private String quote;
    public String getQuote() {return quote;}
    public String setQuote() { this.quote = quote;}
}

class Student {
    private String id;
    private String name;
    private Integer age;
    public String getId() { return id; }
    public String getName() { return name; }
    public Integer getAge() { return age; }

    public void setValue1(String id) { this.id = id; }
    public void setValue2(String name) { this.name = name; }
    public void setValue3(Integer age) { this.age = age; }
}

class MyJsonData {
    private String value1;
    private Integer value2;
    public String getValue1() { return value1; }
    public Integer getValue2() { return value2; }
    public void setValue1(String value1) { this.value1 = value1; }
    public void setValue2(Integer value2) { this.value2 = value2; }
}

// 커맨드 객체로 사용될 클래스 정의
class MyCommandObject {
    private String value1;
    private Integer value2;
    // 반드시 세터 메서드가 있어야 함
    public void setValue1(String value1) { this.value1 = value1; }
    public void setValue2(Integer value2) { this.value2 = value2; }

    // 오른쪽 클릭해서 Generate - toString()
    @Override
    public String toString() {
        return "MyCommandObject{" +
                "value1='" + value1 + '\'' +
                ", value2=" + value2 +
                '}';
    }
}

@RestController //해당 컨트롤러가 RESTful 웹 서비스를 처리하는 컨트롤러임을 명시합니다.
@RequestMapping("/renew") //해당 메소드나 클래스가 어떤 요청 URI에 매핑되는지를 지정합니다.
public class MyRenewController {
    // produces 옵션을 통해서 미디어 타입 지정 가능 (유추해서 자동으로 지정하게 할 수도 있지만가급적 써주는 것을 권장)
    @GetMapping(value = "/echo", produces = MediaType.TEXT_PLAIN_VALUE)
    // 반환한 문자열이 바로 응답 메시지의 바디 데이터에 삽입될 수 있도록 @ResponseBody 어노테이션 추가 (@RestController를 사용하면 생략 가능)
    @ResponseBody  //RestController 사용시 붙여주지 않아도 됨. 그러나 Controller 사용 시에는 무조건 붙임.
    // 요청 메시지의 바디 영역에 있는 메시지가 byte[] content에 들어가고
    // 그 후 String 변환 후 응답 메시지의 바디 영역에 메시지를 받겠다.
    public String echo(@RequestBody byte[] content) {
        // 메서드 정보 접근할 필요 없음 (GET 메서드)
        // 주소 정보 접근할 필요 없음 (@PathVariable 사용)
        // 쿼리 스트링 정보 접근할 필요 없음 (@RequestParam 사용)
        // 프로토콜, HTTP 버전 정보 접근할 필요가 보통 없음
        // 헤더 정보 접근할 필요 없음 (@RequestHeader 사용)
        // 요청 메시지의 바디 데이터 접근은 @RequestBody 어노테이션을 이용해서 전달받을 수 있음
        String bytesToString = new String(content, StandardCharsets.UTF_8);
        System.out.println(bytesToString);
        // Content-Type 헤더의 경우 produces 옵션을 제공하여 미디어 타입 지정 가능
        return bytesToString;
    }

    @GetMapping(value = "/hello-html", produces = MediaType.TEXT_HTML_VALUE)
    // 반환 코드도 마찬가지로 그냥 성공적으로 메서드에서 값을 반환하면 자동으로 200이 됨
    @ResponseStatus(HttpStatus.OK)
    public String helloHTML() {
        return "<h1>Hello</h1>";
    }
// Q) XML, JSON 값으로도 반환하게 핸들러 만들어보기
    @GetMapping(value = "/hello-xml", produces = MediaType.APPLICATION_XML_VALUE)
    // 반환 코드도 마찬가지로 그냥 성공적으로 메서드에서 값을 반환하면 자동으로 200이 됨
    @ResponseStatus(HttpStatus.OK)
    public String helloXMl() {
        return "<text>Hello</text>";
}
    @GetMapping(value = "/hello-json", produces = MediaType.APPLICATION_JSON_VALUE)
    // 반환 코드도 마찬가지로 그냥 성공적으로 메서드에서 값을 반환하면 자동으로 200이 됨
    @ResponseStatus(HttpStatus.OK)
    public String helloJson() {
        return "{ \"data\": \"Hello\" }";
    }

//    @GetMapping(value = "/echo-repeat", produces = MediaType.TEXT_PLAIN_VALUE)
//// @RequestHeader 어노테이션을 통해서 X-Repeat-Count에 적힌 숫자 정보 가져오고 없으면 1로 초기화
//    public String echoRepeat(@RequestParam("word") String word,
//                             @RequestHeader(value = "X-Repeat-Count", defaultValue = "1") Integer repeatCount)
//    {
//        String result = "";
//        for(int i=0;i<repeatCount;i++) {
//            result += word;
//        }
//        return result;
//    }

//    @GetMapping(value = "/echo-repeat/{cnt}", produces = MediaType.TEXT_PLAIN_VALUE)
//    public String echoRepeat(@PathVariable(value = "cnt", required = true) Integer cnt,
//                             @RequestParam("word") String word)
//    {
//        String result = "";
//        for(int i=0;i<cnt;i++) {
//            result += word;
//        }
//        return result;
//    }

    //RequestParam
    @GetMapping(value = "/echo-repeat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String echoRepeat(@RequestParam("word") String word,
                             @RequestParam("count") Integer count)
    {
        String result = "";
        for(int i=0;i<count;i++) {
            result += word;
        }
        return result;
    }

    @GetMapping(value = "/dog-image", produces = MediaType.IMAGE_JPEG_VALUE)
    // 바이트배열을 응답 메시지인 바디 영역에 보내기만 하면 된다.
    public byte[] dogImage() throws IOException {
    // resources 폴더의 static 폴더에 이미지 있어야 함
        File file = ResourceUtils.getFile("classpath:static/dog.jpg");
    // 파일의 바이트 데이터 모두 읽어오기
        byte[] bytes = Files.readAllBytes(file.toPath());
        return bytes;
    }

    @GetMapping(value = "/dog-image-file", produces =
            MediaType.APPLICATION_OCTET_STREAM_VALUE)
    // 헤더를 직접 조정하고 싶은 경우 ResponseEntity 타입을 반환하도록 설정 가능
    // (꺽쇠 안에는 응답 메시지의 바디 데이터에 포함될 타입을 지정)
    public ResponseEntity<byte[]> dogImageFile() throws IOException {
        File file = ResourceUtils.getFile("classpath:static/dog.jpg");
        byte[] bytes = Files.readAllBytes(file.toPath());
        String filename = "dog.jpg";
        // 헤더 값 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);
        // ResponseEntity는 바디, 헤더 영역과 상태코드를 직접 지정해주고 싶을 때 사용한다.
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private ArrayList<String> wordList = new ArrayList<>();
    // 위의 ArrayList에 단어를 추가하는 메서드
    @PostMapping(value = "/words")
    // 요청하는 요청 본문을 requestBody라고 함.
    public void addWord(@RequestBody String bodyString) {
        String[] words = bodyString.split("\n");
        for(String w: words) wordList.add(w.trim());
    }
    // 저장된 모든 단어 보여주기
    @GetMapping("/words")
    public String showWords() {
        return String.join(",", wordList);
    }

    @PostMapping("/test")
    // @ModelAttribute를 타입 앞에 붙여주고 메서드의 파라미터 값으로 전달되게 함
    // 커맨드 객체. MyCommandObject는 첫번째는 쿼리 스트링, 두번째는 폼을 가지고 만들 수 있음.
    public String commandObjectTest(@ModelAttribute MyCommandObject myCommandObject)
    {
        return myCommandObject.toString();
    }


    // 요청 메시지의 Content-Type이 "applicaion/json"인 요청을 받아들이기 위해서 consumes를 MediaType.APPLICATION_JSON_VALUE로 설정
    // 응답 메시지의 Content-Type이 "applicaion/json"이므로 produces를MediaType.APPLICATION_JSON_VALUE로 설정
    @PostMapping(value = "/json-test",
            // Content-type Json 타입 이다.
            consumes = MediaType.APPLICATION_JSON_VALUE, //consumes: 요청 메시지의 content-type이 json(역질렬화)
            produces = MediaType.APPLICATION_JSON_VALUE)  // produces: 응답 메시지의 content-type이 json(직렬화)
    // 반환 타입으로 MyJsonData를 설정하였고 ResponseBody 어노테이션을 통해 해당 값이 메시지컨버터를 통해서 직렬화되어야 함을 알림
    @ResponseBody //응답 메시지의 데이터값
    // RequestBody 어노테이션을 통해 요청 메시지의 바디에 포함된 JSON 문자열이 메시지 컨버터를통해서 역직렬화되어 객체로 변환되어야 함을 알림
    public MyJsonData jsonTest(@RequestBody MyJsonData myJsonData) {
        System.out.println(myJsonData);
        return myJsonData;
    }

//test01
    @PostMapping(value="/student-test",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Student student(@RequestBody Student student){
        System.out.println(student);
        return student;
    }
    // test02
//    @PostMapping(value="/student-test",
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.TEXT_PLAIN_VALUE)
//    @ResponseBody
//    public String student(@RequestBody Student student){
//        System.out.println(student);
//        return student.toString();
//    }

    @GetMapping(value = "/github/{user}", produces =
            MediaType.TEXT_PLAIN_VALUE)
    public String githubUser(@PathVariable("user") String user) {
        RestTemplate restTemplate = new RestTemplate();
// 요청 메시지 생성 및 설정
        // HttpsHeader를 지정할 객체
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Entity는 스프링에서 제공하는 응답, 요청 메시지를 객체화 한 것이다.
        RequestEntity<String> requestEntity = new RequestEntity<>(
                null, null, HttpMethod.GET,
                URI.create("https://api.github.com/users/" + user));
// 응답 메시지
        // exchange에서 요청을 하는 것이다.
        ResponseEntity<String> response = restTemplate.exchange(requestEntity,
                String.class);
        String responseBody = response.getBody();
        return responseBody;
    }

        @GetMapping(value = "/api", produces = MediaType.TEXT_PLAIN_VALUE)
    public String apiStory() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<String> requestEntity = new RequestEntity<>(
                null, null, HttpMethod.GET,
                URI.create("https://api.kanye.rest"));
// 응답 메시지
        // exchange에서 요청을 하는 것이다.
        ResponseEntity<String> response = restTemplate.exchange(requestEntity,
                String.class);
        String responseBody = response.getBody();
        return responseBody;
    }
    @PostMapping(value="/api-test",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Api api(){

        return student;
    }
}

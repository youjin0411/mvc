package study.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MyController {
    //RequestMapping 어느테이션은 원래 사용했던 기본적인 방식이다.
    // 보통은 GetMapping을 사용하는 것이 더 낫다.
    @RequestMapping(value = "/hello",  method = RequestMethod.GET)
    public void hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //request 요청 메시지를 객체화 시키고, 응답 메시지를 객체화 시킴.
        // response 객체를 리턴하지 않고 void로 주면 자동으로 응답 메시지가 요청한 것으로 이동함.
        response.setStatus(200); //HttpStatus.Ok.value()로 상수로도 값을 넣을 수 있다.
        //Header 영역을 Context-Type이고, 형식은 text이다.
        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.setHeader("Content-Length", "5");
        // 바디 영역에 Hello를 추가함.
        response.getWriter().write("Hello");
    }

    @GetMapping("/echo")
    public void echo(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String method = request.getMethod();
        System.out.println("Method : " + method);

        // 쿼리 스트링 정보 접근 => Query String : null
//        String query = request.getQueryString();
//        HashMap<String, String> map = new HashMap<>();
//        String[] str1 = query.split("&");
//        String[] str2;
//        for(String p: str1){
//            String[] tmp = p.split("=");
//            map.put(tmp[0], tmp[1]);
//        }
//        System.out.println("Query String : " + query);
//        System.out.println(map);
//        // 프로토콜, HTTP 버전 정보 접근  => Protocol : HTTP/1.1
//        String protocol = request.getProtocol();
//        System.out.println("Protocol : " + protocol);

//        // 헤더 정보 접근
//        System.out.println("Headers");
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames != null && headerNames.hasMoreElements()) {
//            String h = headerNames.nextElement();
//            System.out.println(h + " : " + request.getHeader(h));
//        }

        // 요청 메시지의 바디 데이터 접근
        // 1. 바디 데이터를 추가하기 위해서는 POSTMAN과 같은 클라이언트 프로그램을 사용해야 함
        // 2. 일반적으로, GET 요청은 바디 데이터를 추가하지 않는 것이 권장되며, 특정 데이터를 보내기 위해서 쿼리스트링을 이용함
        byte[] bytes = request.getInputStream().readAllBytes();
        String bytesToString = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(bytesToString);

        // bytesToString 응답 헤더 설정
        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.getWriter().write(bytesToString);
    }

    @GetMapping("/hello-html")
    public void helloHTML(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        // 만약 해당 내용을 text/plain으로 바꾸면?
        // text/plain으로 변경하면 <h1>Hello</h1> 그대로 출력됨.
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.getWriter().write("<h1>Hello</h1>");
    }

    @GetMapping("/hello-xml")
    public void helloXML(HttpServletResponse response) throws IOException {
        // 상태 코드와 관련된 상수를 제공하므로 이용해도 무방함
        response.setStatus(HttpStatus.OK.value());
        // "text/xml"이 아님을 유의
        response.setHeader("Content-Type", "application/xml; charset=utf-8");
        response.getWriter().write("<text>Hello</text>");
    }
    @GetMapping("/hello-json")
    public void helloJSON(HttpServletResponse response) throws IOException {
    // 성공적으로 리소스를 찾아서 돌려주면서 404 코드를 돌려줘도, 스프링 쪽에서는 속사정을 알 방법이 없으니 허용하고 잘 동작함
    // (리소스가 존재하지 않는 이유를 json 같은걸로 설명할 수도 있으므로, HTTP 스펙 상에서도 204와는 달리, 404 코드를 돌려줄 때
    // 바디 데이터를 포함하지 않아야 된다고 명시하지 않았음. 단, 웹 브라우저에서는 404이므로 문제라고 인식함, 그렇다고 해도 4XX
    // 에러에 대한 처리는 프로그래머가 해야 함)
        response.setStatus(200);
    // "text/json"이 아님을 유의
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write("{ \"data\": \"Hello\" }");
    }

    @GetMapping("/echo-repeat")
    public void echoRepeat(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setStatus(200);
        response.setHeader("Content-Type", "text/plain");
        // X-Repeat-Count에 적힌 숫자 정보 가져오고 없으면 1로 초기화
        int loopCount = Integer.parseInt(request.getHeader("X-Repeat-Count") == null
                ? "1" : request.getHeader("X-Repeat-Count"));
        // 쿼리 스트링 정보 가져와서
        String query = request.getQueryString();
        // 쿼리 스트링 나누고
        String[] querySplit = query.split("&");
        String result = "";
        // 각 쿼리 스트링 정보들을 X-Repeat-Count만큼 반복해서 보여주기
        for(String s : querySplit) {
            for(int i=0;i<loopCount;i++) {
                String[] tmp = s.split("=");
                result += tmp[0] + "," + tmp[1] + "\n";
            }
        }
        response.getWriter().write(result.trim());
    }

    @GetMapping("/dog-image")
    public void dogImage(HttpServletResponse response) throws IOException {
    // resources 폴더의 static 폴더에 이미지 있어야 함
        File file = ResourceUtils.getFile("classpath:static/dog.jpg");
    // 파일의 바이트 데이터 모두 읽어오기
        byte[] bytes = Files.readAllBytes(file.toPath());
        response.setStatus(200);
    // 응답 메시지의 데이터가 JPEG 압축 이미지임을 설정
        response.setHeader("Content-Type", "image/jpeg");
    // 바이트 데이터 쓰기 (여기서는 텍스트 데이터를 전송하지 않기 떄문에 Writer 대신OutputStream을 이용)
        // 바이트 배열 -> 바이너리 파일은 getOutputStream으로 바이트 배열을 받는다.
        response.getOutputStream().write(bytes);
    }

    @GetMapping("/dog-image-file")
    public void dogImageFile(HttpServletResponse response) throws IOException {
        File file = ResourceUtils.getFile("classpath:static/dog.jpg");
        byte[] bytes = Files.readAllBytes(file.toPath());
        response.setStatus(200);
// 임의의 바이너리 데이터임을 알려주는 MIME 타입 설정
        response.setHeader("Content-Type", "application/octet-stream");
// Content-Length는 자동으로 파일 크기만큼 설정해주지만 여기서는 그냥 넣었음
// (Q) 만약 바이트 크기를 제대로 주지 않으면 어떻게 될까?)
        // Content-Length는 주지 않으면 자동 설정됨. 굳이 손 되지 마라
        response.setHeader("Content-Length", bytes.length + "");
// 다운로드 될 파일 이름 설정
        String filename = "dog.jpg";
        response.setHeader("Content-Disposition", "attachment; filename=" +
                filename);
        response.getOutputStream().write(bytes);
    }
    //이곳은 원래 DB가 들어가야 할 자리
    private ArrayList<String> wordList = new ArrayList<>();
    // 위의 ArrayList에 단어를 추가하는 메서드
    @PostMapping("/words")
    public void addWord(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String bodyString =
                request.getReader().lines().collect(Collectors.joining(System.lineSeparator())); //body 데이터에 엔터 포함 모두 보내주는 것 lineSeparator
        String[] words = bodyString.split("\n");
        for(String w : words) wordList.add(w.trim());
    // 201 CREATED 응답 코드 발생시키기
        response.setStatus(201);
    // 생성된 리소스를 확인할 수 있는 URL 알려주기 (Location 헤더 굳이 안 붙여도 기능적으로 차이는 없음)
        response.setHeader("Location", "/words");
    }
    // 저장된 모든 단어 보여주기
    @GetMapping("/words")
    public void showWords(HttpServletResponse response) throws IOException {
        String allWords = String.join(",", wordList);
        response.setStatus(200);
        response.getWriter().write(allWords);
    }
}

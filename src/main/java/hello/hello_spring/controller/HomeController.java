package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /* 웹 브라우저에서 "localhost:8080"이 들어오면(끝에 "/"가 있는거나 마찬가지) 내장 톰캣 서버를 통해서 스프링 컨테이너가 이 요청을 받음.
    스프링 컨테이너는 "/" 관련 컨트롤러가 있는지 모든 컨트롤러들을 다 살펴봄
    있으면 해당 매핑된 컨트롤러 실행 -> home.html 화면 띄움, 없으면 static의 index.html 화면 띄움
    그래서 이전에는 "localhost:8080" 치면 index.html 화면이 띄워졌던 것이고 현재는 "/" 컨트롤러가 매핑됨으로써 home.html 화면이 띄워지는 것임 */
    @GetMapping("/") //도메인(홈화면) url. localhost:8080으로 들어오면 딱 보이는 첫화면
    public String home() {
        return "home";
    }
}

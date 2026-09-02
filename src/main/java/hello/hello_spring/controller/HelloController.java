package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) { //스프링이 Model을 만들어서 넣어줌
        model.addAttribute("data", "hello!!"); //model에 "data"라는 이름의 "hello!!"라는 값을 넣어놓음
        return "hello"; //hello.html 화면을 실행시킴 (이때, model(data:hello!!)도 화면으로 넘김)
    }
}

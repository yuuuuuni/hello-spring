package hello.hello_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) { //스프링이 Model을 만들어서 넣어줌
        model.addAttribute("data", "hello!!"); //model에 "data"라는 이름의 "hello!!"라는 값을 넣어놓음
        return "hello"; //hello.html 화면을 실행시킴 (이때, model(data:hello!!)도 화면으로 넘김)
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) { //? 뒤에 오는 "name" 파라미터의 값을 String name에 담음
        model.addAttribute("name", name); //model에 담으면 그 데이터를 view에서 보여줌 model(name:spring!)
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody //http 통신 프로토콜의 body부에 리턴 데이터를 직접 넣겠다는 의미
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name; //"hello spring!" 이런식으로 문자 데이터 그대로를 반환
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello; //이렇게 객체를 반환하고 @ResponseBody 라고 해놓으면 JSON("키":"값") 형식으로 반환됨 ex. {"name":"spring!"}
    }

    static class Hello { //정적 중첩 클래스(바깥 클래스 객체에 종속되지 않는 중첩 클래스) static이 붙으면 Hello가 HelloController 객체에 종속되지 않음.
        private String name; //보안을 위해 외부에서 바로 쓰지 못하도록 private으로 막아두고 getter와 setter 메서드를 통해 값을 가져오거나 세팅함

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

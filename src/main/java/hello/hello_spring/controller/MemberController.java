package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//스프링이 처음에 뜰 때 스프링 컨테이너(통)가 생기는데 이렇게 @Controller 애노테이션을 붙여놓으면 스프링이 스프링 컨테이너에 MemberController 객체를 생성해서 넣어두고 관리를 함(=스프링 컨테이너에서 스프링 빈이 관리된다고 표현함)
@Controller
public class MemberController {

    //MemberController가 MemberService를 가져다 써야하므로 new MemberService()해서 쓸 수도 있음
    //그런데, 스프링이 관리를 하게 되면 모든 객체를 스프링 컨테이너에 등록을 하고 스프링 컨테이너로부터 받아서 사용하도록 코드를 수정해야 함
    //왜냐하면 MemberController 뿐만 아니라 다른 여러 컨트롤러들이 MemberService를 가져다 쓸 수도 있는데 보다시피 MemberService는 별 기능이 없음
    //따라서, 여러 개의 MemberService 객체를 new 해서 생성할 필요가 없음. 하나만 생성해서 같이 공용으로 사용하면 됨
    //private final MemberService memberService = new MemberService();
    private final MemberService memberService;

    //new MemberService() 대신 생성자를 만들고 @Autowired 애노테이션을 달아주면 됨
    //이렇게 생성자를 만들고 @Autowired 애노테이션을 달아주면 스프링이 뜨면서 MemberController가 생성이 될 때 스프링이 스프링 빈에 등록되어 있는 MemberService 객체를 가져와서 넣어줌!
    //이게 바로 DI(Dependency Injection, 의존성 주입)임. 의존 관계를 외부(스프링)에서 주입해주는 것임!
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
/*
    의존성 주입이 이루어지는 원리 (MemberController -> MemberService)
    여기서 MemberController는 MemberService가 필요함(MemberController와 MemberService는 연결되어 있어야함)
    (참고: 처음에 스프링이 뜨면서 @Controller, @Service, @Repository를 보고 얘네 모두를 객체 생성하면서 스프링 컨테이너에 스프링 빈으로 등록함)
    이 경우 스프링이 뜨면서 MemberController 객체를 생성할 때 @Controller를 보고 "컨트롤러네!"하고 스프링 컨테이너에 등록하면서 생성자를 호출함
    이때 @Autowired가 있으므로 스프링이 "MemberService가 필요하구나!"하고 스프링 컨테이너에 있는 MemberService 객체를 MemberController 생성자의 memberService 변수에 넣어주는 것임(즉, 컨트롤러에 서비스를 주입하는 것)
*/

    @GetMapping("/members/new") //기본적으로 url 창에 주소 입력하고 엔터치면 이동하는 방식. 주로 데이터를 조회할 때 사용
    public String createForm() {
        return "members/createMemberForm";
    }

    //url은 같지만 Get인지 Post인지에 따라서 다르게 매핑할 수 있음
    @PostMapping("/members/new") //데이터를 <form>에 넣어서 전달할 때 사용하는 방식. 주로 데이터를 등록할 때 사용
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/"; //회원 가입이 끝나면 홈 화면으로 보냄
    }
}

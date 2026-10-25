package hello.hello_spring.controller;

public class MemberForm {
    private String name; //스프링이 createMemberForm.html의 <input> 태그의 name의 값이 "name"인걸 보고 input 창에 들어온 데이터를(setName()을 호출해서) 이 name 필드에 넣어줌

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

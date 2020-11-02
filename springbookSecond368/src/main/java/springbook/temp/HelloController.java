package springbook.temp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HelloController implements Controller
{
    @Autowired HelloSpring helloSpring; //부모 컨텍스트이니 루트 컨텍스트로부터 HelloSpring 빈을 DI 받음

    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception
    {
        String name = req.getParameter("name"); //사용자 요청 해석

        String message = this.helloSpring.sayHello(name);   //비즈니스 로직 호출

        Map<String,Object> model = new HashMap<String, Object>();
        model.put("message",message);   //모델 정보 생성

        return new ModelAndView("/WEB-INF/view/hello.jsp",model);
    }
}

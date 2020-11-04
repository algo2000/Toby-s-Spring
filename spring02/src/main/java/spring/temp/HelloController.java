package spring.temp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class HelloController implements Controller
{
    @Autowired
    HelloSpring helloSpring;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String name = request.getParameter("name");

        String message = this.helloSpring.sayHello(name);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("message",message);

        return new ModelAndView("/hello",model);
    }
}

//@Controller
//public class HelloController
//{
//    @RequestMapping("/hello")
//    public String hello(@RequestParam("name") String name, ModelMap map)
//    {
//        map.put("message", "Hello " + name);
//        return "/WEB-INF/view/hello.jsp";
//    }
//}

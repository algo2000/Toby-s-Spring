package springbook.learningtest.spring.ioc.bean;

import org.springframework.stereotype.Component;

@Component("myAnnotatedHello")  //따로 입력을 안해줄 경우 클래스 이름의 맨앞만 소문자로 해서 빈의 아이디가 생성된다.
public class AnnotatedHello
{

}

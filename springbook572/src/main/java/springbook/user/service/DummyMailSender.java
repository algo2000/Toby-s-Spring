package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

//테스트용 MailSender 상속 클래스
public class DummyMailSender implements MailSender
{
    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException
    {
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException
    {
    }
}

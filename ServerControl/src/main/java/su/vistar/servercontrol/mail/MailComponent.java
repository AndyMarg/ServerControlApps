package su.vistar.servercontrol.mail;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailComponent {

//============================================================================================    
    /**
     * Отправляет сообщение на почту,по заданным параметрам.
     *
     * @param login Логин почты отправителя
     * @param password Пароль почты отправителя
     * @param from Адрес почты,с которого будет отправлено письмо
     * @param to Адрес почты,куда будет отправлено письмо
     * @param content Содержание сообщения
     * @param subject Тема сообщения
     * @param smtpPort Simple Mail Transfer Protocol,порт
     * @param smtpHost Simple Mail Transfer Protocol,хост
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     *
     *
     */
    public static void sendSimpleMessage(String login, String password, String from, List<String> to, String content, String subject, int smtpPort, String smtpHost)
            throws MessagingException, UnsupportedEncodingException {
        //Аутентификация по заданным параметрам
        Authenticator auth = new MyAuthenticator(login, password);

        Properties props = new Properties();
        //Получение списка адресатов для отправки сообщения
        InternetAddress[] myList = new InternetAddress[to.size()];
        for (int i = 0; i < to.size(); i++) {
            String address = to.get(i);
            InternetAddress internetAddress = new InternetAddress(address);
            myList[i] = internetAddress;
        }

        props.put("mail.smtp.port", smtpPort);
        //Протокол шифрования
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, auth);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        //Добавление адресатов
        msg.addRecipients(Message.RecipientType.TO, myList);
        msg.setSubject(subject);
        msg.setText(content);

        //Отправка сообщения
        Transport.send(msg);
    }

}

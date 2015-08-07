package su.vistar.servercontrol.mail;

import su.vistar.servercontrol.utils.settings.MailSettings;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.mail.MessagingException;
import su.vistar.servercontrol.utils.ServiceLocator;

public class MessageSender {

    //============================================================================================    
    /**
     * Заполняет параметры для отправки сообщения.
     *
     * @param content Имя сервер компонента
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    public static void sendMessage(String content) throws MessagingException, UnsupportedEncodingException {

        String login;
        String password;
        String from;
        List<String> to;
        String subject = "Проверка работоспобности сервера.";
        int smtpPort;
        String smtpHost;
        //Получение настроек для отправки сообщения
        MailSettings mailSettings = ServiceLocator.getSettingsManager().getMailSettings();
        login = mailSettings.login;
        password = mailSettings.password;
        from = mailSettings.adress_from;
        to = mailSettings.adress_to;
        smtpPort = mailSettings.smtpPort;
        smtpHost = mailSettings.stmpHost;

        //Отправка  готового сообщения
        MailComponent.sendSimpleMessage(login, password, from, to, content, subject, smtpPort, smtpHost);
    }
}

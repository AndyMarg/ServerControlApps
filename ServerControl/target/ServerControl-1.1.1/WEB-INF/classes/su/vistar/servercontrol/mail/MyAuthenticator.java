package su.vistar.servercontrol.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {

    private final String user;
    private final String password;

//================================================================================
    //Конструктор
    MyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

//================================================================================= 
    //Получение данных для аутентификации
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String myUser = this.user;
        String myPassword = this.password;
        return new PasswordAuthentication(myUser, myPassword);
    }
}

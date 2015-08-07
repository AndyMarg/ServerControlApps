package su.vistar.servercontrol.servercomponents;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.mail.MessagingException;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.exception.CheckFailureException;
import su.vistar.servercontrol.exception.CheckerIsNotReadyException;
import su.vistar.servercontrol.mail.MessageSender;
import su.vistar.servercontrol.servercomponents.CurrentStateForComponent.ComponentState;
import su.vistar.servercontrol.utils.ServiceLocator;

public class CheckManager {

    private static final LoggerManager log = new LoggerManager();

//=====================================================================================
    /**
     * Функция вызова в потоках цикла проверки активности севера.
     *
     * @param checker Сервер компонент,для которого проводится цикл проверки
     * @param period Период проверки(время через которое проверка повторяется)
     */
    public void scheduleCheck(final ServerComponent checker, long period) {
        ServiceLocator.getScheduledExecutorService().scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                //Цикл проверки сервер компонента
                checkCycle(checker);
            }
        }, 0, period, TimeUnit.MINUTES);
    }

//======================================================================================
    /**
     * Цикл проверки активности севера.
     *
     * @param checker Сервер компонент,для которого проводится цикл проверки
     */
    private void checkCycle(ServerComponent checker) {
        //Вызов функции проверки для компонента checker
        ComponentState componentState = new ComponentState(true, false, null, null, null, null, null);
        //Выброс исключения,при получении ошибки проверки
        try {
            componentState = checker.performCheck();
        //Получение текущего статуса для сервер компонента при ошибке проверки
        } catch (CheckFailureException e) {
            
            if (e instanceof CheckerIsNotReadyException) {
                log.fine(e, "Компонент проверки не готов {0}", checker.serverName);
                componentState.statusDescription = "Компонент не готов";
            } else {
                log.severe(e, "Ошибка проверки {0}", checker.serverName);
                componentState.statusDescription = "Ошибка проверки!";
            }
            
            componentState.isCheckError = true;
            componentState.checkErrorDescription = e.description;
            componentState.lastCheckTime = new Date();
            componentState.lastSuccessCheckTime = checker.tempLastSuccessCheckTime;
            checker.currentState.componentState.put(checker.serverName, componentState);
            return;
        }

        //Получение статуса активности и последующая отправка сообщения,при статусе "Не работает"
        boolean isServerWorks = componentState.isActive;
        //При успешной проверке устанавливаем значение isMessageSend в false,
        //для отправления единственного сообщения об ошибке сервера
        if (isServerWorks) {
            checker.isMessageSend = false;
            return;
        //Если статус сервера "Не работает!" и сообщение еще не было отправлено
        //(isMessageSend false) то отправляем сообщение об ошибке
        } else if (isServerWorks == false && checker.isMessageSend == false) {
            try {
                try {
                    //Заполнение текста сообщения
                    String content = "Сервер " + checker.serverName + ": "
                            + componentState.statusDescription;
                    MessageSender.sendMessage(content);
                    checker.isMessageSend = true;
                } catch (UnsupportedEncodingException ue) {
                    log.severe(ue, "UnsupportedEncodingException!");
                }
            } catch (MessagingException me) {
                log.severe(me, "Can't send message!");
            }
        } else {
            return;
        }
    }
}

package su.vistar.servercontrol.httputils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import su.vistar.logging.LoggerManager;
import su.vistar.servercontrol.exception.ConnectionException;
import su.vistar.servercontrol.exception.TimeoutException;

public class HttpGetConnection {

    private static final LoggerManager log = new LoggerManager();

    /**
     * Получение html страницы для нахождения условий проверки активности
     * сервера,а также получение кода состояния HTTP.
     *
     * @param url адрес ресурса по котором уаходится страница для получения
     * условий проверки
     * @return объект GetHtmlPageResult с полями htmlPage и httpStatus.
     */
    public static GetHtmlPageResult getHtmlPageAndStatus(String url) {

        String htmlPage = null;
        int status = 0;

        try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
            
            // установка таймаутов запроса
            RequestConfig requestConfig = 
                    RequestConfig.custom()
                    .setConnectionRequestTimeout(10000)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();

            // настройка конфигурации GET запроса
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(requestConfig);

            // выполняем запрос, получаем содержимое в виде строки и записываем его в переменную htmlPage            
            try (CloseableHttpResponse response = httpclient.execute(httpget);) {
                
                status = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputstrem = entity.getContent()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputstrem));
                        StringBuffer bufferString = new StringBuffer();
                        int nextSymbol = reader.read();
                        while (nextSymbol != -1) {
                            bufferString.append((char) nextSymbol);
                            nextSymbol = reader.read();
                        }
                        htmlPage = bufferString.toString();

                    } catch (IOException ex) {
                        log.severe(ex, "Can't read content from html response from url " + url);
                    }
                }

            } catch (SocketTimeoutException e) {
                throw new TimeoutException("Can't connect with " + url + ".Error: " + e);
            } catch (ConnectException exc) {
                throw new ConnectionException("Can't connect with " + url + ".Error: " + exc);
            }
        } catch (IOException ex) {
            log.severe(ex, "Can't connect with " + url);
        }

        return new GetHtmlPageResult(htmlPage, status);
    }

    public static class GetHtmlPageResult {

        public String htmlPage;
        public int httpStatus;

        public GetHtmlPageResult(String htmlPage, int httpStatus) {
            this.htmlPage = htmlPage;
            this.httpStatus = httpStatus;
        }
    }
}

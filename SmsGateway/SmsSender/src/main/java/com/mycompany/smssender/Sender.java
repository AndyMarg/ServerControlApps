package com.mycompany.smssender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.apache.http.HttpVersion.HTTP;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import su.vistar.logging.LoggerManager;

public class Sender {

    private static final LoggerManager log = new LoggerManager();
    private static final String SENDER_SERVICE_URL = "";
    private static final String SENDER_SERVLET_URL = "http://192.168.10.192:8084/SmsGateway/GetSmsGateway";

    public final boolean production;
    public final String senderName;
    public final String applicationName;
    public final String applicationKey;

    public Sender(boolean production,String senderName,String applicationName,String applicationKey) {
        this.production = production;
        this.applicationKey = applicationKey;
        this.applicationName = applicationName;
        this.senderName = senderName;
    }
    
    public boolean sendSms(String phoneNumbers, String smsText, String comment) {
        try {
            Map<String,String> smsInfo = setInfoMap(senderName,applicationName,applicationKey,phoneNumbers, smsText, comment);
            return sendHttpReqest0(smsInfo);
        } catch (Throwable t) {
            log.severe(t,"Ошибка при отправке смс!");
            return false;
        }
    }
    
    private boolean sendHttpReqest0(Map<String, String> smsInfo) {
        
        
        String url;
        if(production == true){
            url = SENDER_SERVICE_URL;
        }
        else{
            url = SENDER_SERVLET_URL;
        }

        String statusString;
        Boolean status = null;
        String json = getJsonString(smsInfo);
        String request = "command=reqAddMessage&json=" + json;

        try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
            //установка таймаута запроса
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(10000).setConnectTimeout(10000).setSocketTimeout(10000).build();
            //Делаем POST запрос по заданному url
            HttpPost httppost = new HttpPost(url);
            httppost.setConfig(requestConfig);
            httppost.addHeader("User-Agent", USER_AGENT);
            //Задаем параметры запроса в виде строки json
            StringEntity se = new StringEntity(request, "UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP, "application/json"));
            httppost.setEntity(se);

            //Получаем содержимое в виде строки и записываем его в переменную htmlPage
            try (CloseableHttpResponse response = httpclient.execute(httppost);) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputstrem = entity.getContent()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputstrem));
                        StringBuilder bulderString = new StringBuilder();
                        int nextSymbol = reader.read();
                        while (nextSymbol != -1) {
                            bulderString.append((char) nextSymbol);
                            nextSymbol = reader.read();
                        }
                        statusString = bulderString.toString();
                        Pattern pattern = Pattern.compile("[a-z]*\":\"([a-z]*)", Pattern.CASE_INSENSITIVE);
                        Matcher m = pattern.matcher(statusString);
                        while (m.find()) {
                            //Получаем количество миллисекунд и переводим его в дату
                            status = Boolean.parseBoolean(m.group(1));
                        }
                    } catch (IOException ex) {
                        log.severe(ex, "Ошибка парсинга строки,полученной в ответе запроса ");
                    }
                }
            } catch (SocketTimeoutException e) {
                log.severe(e, "Таймаут запроса для url: " + url);

            }

        } catch (IOException exc) {
            log.severe(exc, "Не удалось подключится к " + url);

        }

        if (status == null) {
            status = false;
        }

        return status;
    }

    private String getJsonString(Map<String, String> smsInfo) {
        String senderName = smsInfo.get("senderName");
        String applicationName = smsInfo.get("applicationName");
        String applicationKey = smsInfo.get("applicationKey");
        String phoneNumbers = smsInfo.get("phoneNumbers");
        String smsText = smsInfo.get("smsText");
        String comment = smsInfo.get("comment");

        String json = "{\"sd_name\":\"" + senderName + "\""
                + ",\"app_name\":\"" + applicationName + "\","
                + "\"app_key\":\"" + applicationKey + "\","
                + "\"msg_phone_numbers\":\"" + phoneNumbers + "\","
                + "\"msg_text\":\"" + smsText + "\","
                + "\"msg_comment\":\"" + comment + "\"}";

        return json;
    }

    public Map<String, String> setInfoMap(String senderName, String applicationName, String applicationKey, String phoneNumbers, String smsText, String comment) {

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("senderName", senderName);
        infoMap.put("applicationName", applicationName);
        infoMap.put("applicationKey", applicationKey);
        infoMap.put("phoneNumbers", phoneNumbers);
        infoMap.put("smsText", smsText);
        infoMap.put("comment", comment);

        return infoMap;
    }

}

package com.tsecho.bots.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Slf4j
@Getter
@Setter
public class PingTask {
    @Value("https://www.google.com")
    private String url;

    @Scheduled(fixedRateString = "${pingtask.period}")
    public void pingMe(){
        try{
            URL url = new URL(getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            log.info("Ping {}, OK: response code {}", url.getHost(), connection.getResponseCode());
            connection.disconnect();
        }catch(IOException e){
            log.error("Ping Failed");
            e.printStackTrace();
        }
    }
}

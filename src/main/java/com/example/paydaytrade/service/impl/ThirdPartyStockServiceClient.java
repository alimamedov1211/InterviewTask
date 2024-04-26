package com.example.paydaytrade.service.impl;

import com.example.paydaytrade.entity.Stock;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class ThirdPartyStockServiceClient {

    private final ObjectMapper objectMapper;

    @Value("${rapid.api.key}")
    private String apiKey;

    @Value("${rapid.api.url}")
    private String apiUrl;


    public String getStockDataJson() throws IOException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();

        String result = client.prepare("GET", apiUrl)
                .setHeader("X-RapidAPI-Key", apiKey)
                .setHeader("X-RapidAPI-Host", "twelve-data1.p.rapidapi.com")
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBody)
                .join();
        client.close();

        return result;
    }

    public List<Stock> getStockData() throws IOException {

        String json = getStockDataJson();
        JSONObject jsonObject = new JSONObject(json);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Stock> stockList = objectMapper.readValue(jsonObject
                                                    .getJSONArray("data")
                                                    .toString(),objectMapper.getTypeFactory()
                                                    .constructCollectionType(List.class, Stock.class));

        for(int i = 0 ; i < stockList.size();i++){
            int price = ThreadLocalRandom.current().nextInt(100,1000);
            stockList.get(i).setPrice(price);
        }

        return stockList;

    }


}

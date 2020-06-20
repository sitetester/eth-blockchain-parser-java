package com.example.client;

import com.example.entity.Block;
import com.example.entity.request.BlockByNumberParams;
import com.example.entity.response.BlockByNumber;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class InfuraEthClient {

    private final String fullUrl;
    private static final String BASE_URL = "https://mainnet.infura.io/v3/";

    public InfuraEthClient(String projectId) {

        this.fullUrl = BASE_URL + projectId;
    }

    public CompletableFuture<Block> getBlockByNumber(int number) {

        return CompletableFuture.supplyAsync(() -> {
            Object[] params = new Object[]{intToHex(number), true};
            var blockByNumberParams = new BlockByNumberParams(params, "eth_getBlockByNumber");
            var json = new Gson().toJson(blockByNumberParams);

            HttpResponse<String> response = null;
            try {
                response = HttpClient.newHttpClient().send(buildPOSTRequest(json), HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
            assert response != null;
            var blockByNumberResponse = new Gson().fromJson(response.body(), BlockByNumber.class);

            return blockByNumberResponse.result;
        });
    }


    private HttpRequest buildPOSTRequest(String json) {

        return HttpRequest.newBuilder()
                .uri(URI.create(this.fullUrl))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .setHeader("Content-Type", "application/json")
                .build();
    }

    private String intToHex(int number) {
        return "0x" + Integer.toHexString(number).toUpperCase();
    }
}

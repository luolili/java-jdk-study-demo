package com.luo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * <h1>http client demo</h1>
 */
public class HttpClientTest {

    public static void main(String[] args) throws Exception {

        String uri = "http://t.weather.sojson.com/api/weather/city/101030100";
        //syncGet(uri);
        asyncGet(uri);
    }

    private static void asyncGet(String uri) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest.Builder builder1 = builder.uri(URI.create(uri));
        HttpRequest request = builder1.build();
        CompletableFuture<HttpResponse<String>> completableFuture =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        completableFuture.whenComplete((resp, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
            } else {

                System.out.println(resp.statusCode());
                System.out.println(resp.body());

            }
        }).join();
    }

    //同步get方法
    private static void syncGet(String uri) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        HttpRequest.Builder builder1 = builder.uri(URI.create(uri));
        HttpRequest request = builder1.build();
        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());


    }
}

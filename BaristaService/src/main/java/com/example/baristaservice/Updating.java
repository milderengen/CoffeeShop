package com.example.baristaservice;

import com.example.orderservice.ENUMS.OrderStatus;
import com.example.orderservice.Order;
import com.example.orderservice.OrderRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class Updating {

    public void updateOrderStatus(Long orderId, OrderStatus newStatus) throws IOException {
        URL url = new URL("http://localhost:8080/OrderService_war_exploded/api/orders/find/" + orderId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        StringBuilder responseContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseContent.append(responseLine.trim());
            }
        }
        conn.disconnect();
        String jsonResponse = responseContent.toString();
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(jsonResponse, Order.class);
        order.setOrderStatus(newStatus);

        if(newStatus.equals(OrderStatus.PICKED_UP)){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonPayload = objectMapper.writeValueAsString(order);
                URL Url = new URL("http://localhost:8080/OrderService_war_exploded/api/orders/sendNotification");
                HttpURLConnection connection = (HttpURLConnection) Url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(order);

        URL uri = new URL("http://localhost:8080/OrderService_war_exploded/api/orders/update");
        HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoOutput(true);

        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println(responseCode);

        httpURLConnection.disconnect();
    }
}


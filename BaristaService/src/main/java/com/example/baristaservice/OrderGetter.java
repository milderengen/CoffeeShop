package com.example.baristaservice;

import com.example.orderservice.ENUMS.OrderStatus;
import com.example.orderservice.OrderRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.orderservice.Order;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OrderGetter {

    private static final Updating updating = new Updating();
    private static final String ENDPOINT_URL = "http://localhost:8080/OrderService_war_exploded/api/orders";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        while (true) {
            try {
                URL url = new URL(ENDPOINT_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                List<Order> orders = objectMapper.readValue(reader, new TypeReference<List<Order>>() {});
                reader.close();

                for (Order order : orders) {
                    updating.updateOrderStatus(order.getOrderNumber(),OrderStatus.IN_PREPARATION);
                    Thread.sleep(1500);
                    updating.updateOrderStatus(order.getOrderNumber(),OrderStatus.FINISHED);
                    Thread.sleep(500);
                    updating.updateOrderStatus(order.getOrderNumber(),OrderStatus.PICKED_UP);
                    Thread.sleep(10000);
                }

                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

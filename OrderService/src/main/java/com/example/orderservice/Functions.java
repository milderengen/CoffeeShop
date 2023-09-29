package com.example.orderservice;

import com.example.orderservice.ENUMS.*;

import java.util.Arrays;

public class Functions {
    public String verifyInput(Order order){
        if(order.getCoffeeSize()!= CoffeeSize.Medium || order.getCoffeeSize()!=CoffeeSize.Small || order.getCoffeeSize()!=CoffeeSize.Large){
            return "Wrong size, use Small/Medium/Large size!";

        }
        if(Arrays.stream(OrderType.values())
                .noneMatch(e -> e == order.getOrderType())){
            return "Wrong Type, use onSite/takeaway order type!";
        };
        if(Arrays.stream(CoffeeType.values())
                .noneMatch(e -> e == order.getCoffeeType())){
            return "We dont sell that yet, you can order black, latte, cappuccino and espresso";
        };
        if(Arrays.stream(MilkType.values())
                .noneMatch(e -> e == order.getMilkType())){
            return "Pick one of our offered types of milk, you can order cow Milk,soy Milk and almond Milk";
        };
        if(order.getOrderStatus()!= OrderStatus.IN_PREPARATION){
            return "You cannot interfere with OrderStatus";
        }
        return "OK";
    }
}

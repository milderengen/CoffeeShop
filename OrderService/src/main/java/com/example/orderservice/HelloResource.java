package com.example.orderservice;

import com.example.orderservice.ENUMS.*;
import com.example.orderservice.Functions;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Path("/orders")
public class HelloResource {
    @Inject
    private OrderRepo orderRepository;
    private final Functions functions = new Functions();

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/update")
    public Order update(Order order){
        System.out.println(order.getOrderStatus());
        orderRepository.save(order);
        return orderRepository.findById(order.getOrderNumber());
    }
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public String createOrder(Order order){
        System.out.println(order);
        orderRepository.save(order);
        return "OK";
    }
//    @POST
//    @Produces("text/plain")
//    public String dummy(){
//        Order order = new Order();
//        order.setCoffeeSize(CoffeeSize.Medium);
//        order.setCoffeeType(CoffeeType.espresso);
//        order.setOrderType(OrderType.onSite);
//        order.setMilkType(MilkType.cowMilk);
//        order.setPrice(2.22);
//        orderRepository.save(order);
//        return "OK";
//    }
    @POST
    @Consumes("application/json")
    @Path("/sendNotification")
    public void fireNotification(Order order){
            if (order != null) {
                try {
                    FileWriter writer = new FileWriter("notifications.txt", true); // true means append mode
                    writer.write("Order " + order.getOrderNumber() + " was picked up.\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                orderRepository.delete(order);
        }
    }
    @GET
    @Consumes("application/json")
    @Path("/find/{orderId}")
    public Order findById(@PathParam("orderId") String orderNumber){
        return orderRepository.findById(Long.parseLong(orderNumber));
    }

    @GET
    @Produces("application/json")
    public List<Order> getPendingOrders() {
        return orderRepository.findAllPendingOrders();
    }



    @DELETE
    @Path("/cancel/{orderNumber}")
    public Response cancelOrder(@PathParam("orderNumber") String orderNumber) {
        Order order = orderRepository.findById(Long.parseLong(orderNumber));
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (OrderStatus.IN_PREPARATION == order.getOrderStatus()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("com.example.Order is already in preparation and cannot be canceled.")
                    .build();
        }

        orderRepository.delete(order);
        return Response.noContent().build();
    }
}
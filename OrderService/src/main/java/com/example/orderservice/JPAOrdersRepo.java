package com.example.orderservice;

import com.example.orderservice.ENUMS.OrderStatus;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class JPAOrdersRepo implements OrderRepo{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("coffeeShop");
    EntityManager entityManager = emf.createEntityManager();

    @Override
    @Transactional
    public Order save(Order order) {
        entityManager.getTransaction().begin();

        // Construct the SQL statement
        String sql = "INSERT INTO Order_table(coffeeSize, coffeeType, milkType, orderType, orderStatus, price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // Prepare and execute the query
        entityManager.createNativeQuery(sql)
                .setParameter(1, order.getCoffeeSize().toString())
                .setParameter(2, order.getCoffeeType().toString())
                .setParameter(3, order.getMilkType().toString())
                .setParameter(4, order.getOrderType().toString())
                .setParameter(5, order.getOrderStatus().toString())
                .setParameter(6, order.getPrice())
                .executeUpdate();

        // Commit the transaction
        entityManager.getTransaction().commit();
        return order;
    }
    @Override
    @Transactional
    public void update(Order order){
        entityManager.getTransaction().begin();

        Order managedOrder = entityManager.find(Order.class, order.getOrderNumber());
        if(managedOrder != null) {
            managedOrder.setOrderStatus(order.getOrderStatus());
            entityManager.merge(managedOrder);
        }

        entityManager.getTransaction().commit();

    }





    @Override
    public List<Order> findAllPendingOrders() {
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE o.orderStatus = :status", Order.class);
        query.setParameter("status", OrderStatus.WAITING);
        return query.getResultList();
    }

    @Override
    public Order findById(Long id) {
        return entityManager.find(Order.class, id);
    }
    @Override
    public List<Order> findAllOrders(){
        TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o", Order.class);
        return query.getResultList();
    }

    @Override
    public String delete(Order order) {
        if(order.getOrderStatus().equals(OrderStatus.WAITING)){
            return "BAD REQUEST";
        }
        entityManager.remove(order);
        return "OK";
    }
}

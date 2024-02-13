/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.mbeans;

import com.entities.Customers;
import com.entities.OrderDetails;
import com.entities.OrderDetailsPK;
import com.entities.Orders;
import com.entities.Products;
import com.sessionbeans.OrderDetailsFacadeLocal;
import com.sessionbeans.OrdersFacadeLocal;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author DELL
 */
@Named(value = "cartMB")
@SessionScoped
public class CartMB implements Serializable {

    @EJB
    private OrderDetailsFacadeLocal orderDetailsFacade;

    @EJB
    private OrdersFacadeLocal ordersFacade;
    
    private HashMap<Products, Integer> orderDetailsList;
    private ArrayList<OrderDetails> orderDetailsListDisplay;
    private OrderDetails orderDetails;
    private OrderDetailsPK orderDetailsPK;
    private Orders order;
    
    public CartMB() {
        orderDetailsList = new HashMap<Products, Integer>();
        orderDetailsListDisplay = new ArrayList<OrderDetails>();
    }
    
    public void renderCart() {
        orderDetailsListDisplay = new ArrayList<OrderDetails>();
        for (Map.Entry<Products, Integer> itemHashMapEntry : orderDetailsList.entrySet()) {
            orderDetails = new OrderDetails();
            orderDetails.setProducts(itemHashMapEntry.getKey());
            orderDetails.setQuantity(itemHashMapEntry.getValue());
            orderDetailsListDisplay.add(orderDetails);
        }
        //return orderDetailsListDisplay;
    }
    
    public void removeCart(OrderDetails od) {
        orderDetailsList.remove(od.getProducts());
        renderCart();
    }
    
    
    public void plusQuantity(OrderDetails od) {
        od.plus(od.getProducts().getProductStock());
        orderDetailsList.put(od.getProducts(), od.getQuantity());
        renderCart();
    }
    public void minusQuantity(OrderDetails od) {
        od.minus();
        if (od.getQuantity() == 0) {
            System.out.println(od);
            removeCart(od);
        }
        else {
            orderDetailsList.put(od.getProducts(), od.getQuantity());
        }
        renderCart();
    }
    
    public int totalPrice() {
        int total = 0;
        for (OrderDetails od : orderDetailsListDisplay) {
            total+= (od.getProducts().getProductPrice() * od.getQuantity());
        }
        return total;
    }
    
    public String addCart(Products product, Integer quantity) {
        if (quantity != 0) {
            orderDetailsList.put(product, quantity);
            renderCart();
        }
        return "index?faces-redirect=true";
    }

    public String confirmCart(Customers customer) {
        order = new Orders(ordersFacade.findMaxOrderID() + 1, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()), totalPrice(), "Pending");
        order.setUsername(customer);
        ordersFacade.create(order);
        for (OrderDetails od : orderDetailsListDisplay) {
           orderDetailsPK = new OrderDetailsPK(order.getOrderID(), od.getProducts().getProductID());
           od.setOrderDetailsPK(orderDetailsPK);
           od.setOrders(order);
           orderDetailsFacade.create(od);
        }
        resetCart();
        return "index?faces-redirect=true";
    }
    
    public ArrayList<OrderDetails> getOrderDetailsListDisplay() {
        return orderDetailsListDisplay;
    }

    public void setOrderDetailsListDisplay(ArrayList<OrderDetails> orderDetailsListDisplay) {
        this.orderDetailsListDisplay = orderDetailsListDisplay;
    }
    
    public void resetCart() {
        orderDetailsList.clear();
        orderDetailsListDisplay.clear();
    }
    
    public void resetCartWhenLogout(Customers c) {
        if (c == null) {
            resetCart();
        }
    }
}

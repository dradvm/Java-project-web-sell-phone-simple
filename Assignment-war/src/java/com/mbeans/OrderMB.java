/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.mbeans;

import com.entities.OrderDetails;
import com.entities.Orders;
import com.entities.Products;
import com.sessionbeans.OrderDetailsFacadeLocal;
import com.sessionbeans.OrdersFacadeLocal;
import com.sessionbeans.ProductsFacadeLocal;
import java.util.Collection;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author DELL
 */
@Named(value = "orderMB")
@Dependent
public class OrderMB {

    @EJB
    private ProductsFacadeLocal productsFacade;

    @EJB
    private OrderDetailsFacadeLocal orderDetailsFacade;

    @EJB
    private OrdersFacadeLocal ordersFacade;

    public OrderMB() {
        
    }
    
    public Collection<Orders> showAllOrder() {
        return ordersFacade.findAll();
    }
    
    public String confirmOrder(Orders o) {
        if (o.getStateOfOrder().equals("Pending")) {
            boolean check = true;
            for (OrderDetails od : showDisplayItemList(o)) {
                if (od.getProducts().getProductStock() - od.getQuantity() < 0) {
                    check = false;
                }
            }
            if (check) {
                
                for (OrderDetails od : showDisplayItemList(o)) {
                    od.getProducts().setProductStock(od.getProducts().getProductStock() - od.getQuantity());
                    productsFacade.edit(od.getProducts());
                }
                o.setStateOfOrder("Confirm");
                ordersFacade.edit(o);
            }
        }
        return "manageOrder?faces-redirect=true";
    }
    
    public String rejectOrder(Orders o) {
        if (o.getStateOfOrder().equals("Pending")) {
            o.setStateOfOrder("Reject");
            ordersFacade.edit(o);

        }
        return "manageOrder?faces-redirect=true";
    }
    
    public Collection<OrderDetails> showDisplayItemList(Orders o) {
        return orderDetailsFacade.findByOrderID(o.getOrderID());
    }
}

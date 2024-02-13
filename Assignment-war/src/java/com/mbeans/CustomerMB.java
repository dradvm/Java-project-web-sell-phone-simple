/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.mbeans;

import com.entities.Customers;
import com.sessionbeans.CustomersFacadeLocal;
import java.io.Serializable;
import java.util.Collection;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author DELL
 */
@Named(value = "customerMB")
@SessionScoped
public class CustomerMB implements Serializable{

    @EJB
    private CustomersFacadeLocal customersFacade;
    
    private Customers customer;
    
    public CustomerMB() {
        customer = null;
    }
    
    public Collection<Customers> showAllCustomer() {
        return customersFacade.findAllExceptAdmin();
    }

    public String showUpdateCustomerForm(Customers c) {
        customer = c;
        return "manageCustomer";
    }
    
    public String updateCustomer() {
        customersFacade.edit(customer);
        System.out.println(customer);
        customer = null;
        return "manageCustomer";
    }
    
    public String deleteCustomer(Customers c) {
        customersFacade.remove(c);
        return "manageCustomer";
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }
    
    
}

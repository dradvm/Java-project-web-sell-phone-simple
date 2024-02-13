/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.mbeans;

import com.entities.OrderDetails;
import com.entities.Orders;
import com.entities.Products;
import com.sessionbeans.ProductsFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author DELL
 */
@Named(value = "productMB")
@SessionScoped
public class ProductMB implements Serializable {

    @EJB
    private ProductsFacadeLocal productsFacade;
    private Products productDetails;
    private String productFind;
    private Collection<Products> productList;
    private int quantity;
    
    public ProductMB() {
        productDetails = new Products();
        productFind = "";
        productList = new ArrayList<Products>();
    }


    public void showAllProduct() {
        productList = productsFacade.findProductListWithFind(productFind);
    }
    
    public void showProductDetails(Products p) {
        productDetails = p;
        quantity = 0;
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "productDetails.xhtml?faces-redirect=true");
    }
    
    public void search() {
        showAllProduct();
    }
    

    public Products getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(Products productDetails) {
        this.productDetails = productDetails;
    }
    
    public void plusQuantity() {
        if (quantity < productDetails.getProductStock())  {
            quantity++;
        }
    }
    public void minusQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }
    
    public Products getProductsFindByID(int productID) {
        return productsFacade.find(productID);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductFind(String productFind) {
        this.productFind = productFind;
        System.out.println(productFind);
    }

    public String getProductFind() {
        return productFind;
    }

    public void setProductList(Collection<Products> productList) {
        this.productList = productList;
    }

    public Collection<Products> getProductList() {
        search();
        return productList;
    }
}

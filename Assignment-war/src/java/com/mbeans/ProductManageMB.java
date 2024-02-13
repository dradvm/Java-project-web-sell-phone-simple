/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.mbeans;

import com.entities.Products;
import com.sessionbeans.ProductsFacadeLocal;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

/**
 *
 * @author DELL
 */
@Named(value = "productManageMB")
@SessionScoped
public class ProductManageMB implements Serializable{

    @EJB
    private ProductsFacadeLocal productsFacade;

    private Part productImg;

    @Inject
    private ServletContext context;

    private Products product;

    public ProductManageMB() {
        product = new Products();
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Collection<Products> showAllProduct() {
        return productsFacade.findAll();
    }

    public Part getProductImg() {
        return productImg;
    }

    public void setProductImg(Part productImg) {
        this.productImg = productImg;
    }

    public void upload() {
        if (productImg != null) {
            try (InputStream input = productImg.getInputStream()) {
                String arr[] = product.getProductImage().split("/");
                String filename = arr[arr.length-1];
                String path = context.getRealPath("/resources/images/products/") + "\\" + filename;
                System.out.println(path);
                FileOutputStream outputStream = new FileOutputStream(path);
                try (BufferedInputStream in = new BufferedInputStream(input); 
                        BufferedOutputStream out = new BufferedOutputStream(outputStream)) {

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }

            } catch (IOException e) {
                // Xử lý ngoại lệ
            }
        }
    }
    public String showProductAddForm() {
        this.product = new Products();
        productImg = null;
        return "manageProductForm?faces-redirect=true";
    }
    
    public String showProductUpdateForm(Products p) {
        this.product = p;
        productImg = null;
        return "manageProductForm?faces-redirect=true";
    }

    public String addProduct() {
        product.setProductID(productsFacade.findMaxProductID()+1);
        product.setProductImage("resources/images/products/product-item" + product.getProductID() + ".jpg");
        productsFacade.create(product);
        upload();
        return "manageProduct?faces-redirect=true";
    }
    public String updateProduct() {
        productsFacade.edit(product);
        upload();
        return "manageProduct?faces-redirect=true";
    }
    
    public void deleteProduct(Products p) {
        productsFacade.remove(p);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.mbeans;

import com.entities.Customers;
import com.sessionbeans.CustomersFacadeLocal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Named(value = "loginMB")
@SessionScoped
public class LoginMB implements Serializable {

    @EJB
    private CustomersFacadeLocal customersFacade;

    private Customers userLogined;
    private Customers user;

    @Size(min = 1, max = 10)
    private String repeatPassword;

    public LoginMB() {
        userLogined = null;
        user = new Customers();
    }

    public void signUp() {
        if (customersFacade.find(user.getUsername()) != null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "That username already exist!!");
            FacesContext.getCurrentInstance().addMessage("signUpForm:username", msg);
            user.setUsername("");
        } else if (!user.getPassword().equals(repeatPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Repeat password does not match!!");
            FacesContext.getCurrentInstance().addMessage("signUpForm:repeatPassword", msg);
            user.setPassword("");
            repeatPassword = "";

        } else {
            customersFacade.create(user);
            userLogined = user;
            resetForm();
            directIfLogined();
        }

    }

    public void login() {
        if (customersFacade.find(user.getUsername()) == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Username does not exist, Please try again");
            FacesContext.getCurrentInstance().addMessage("loginForm:username", msg);
        } else if (!customersFacade.find(user.getUsername()).getPassword().equals(user.getPassword())) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Password incorrect!! Please try again");
            FacesContext.getCurrentInstance().addMessage("loginForm:password", msg);
            user.setPassword("");
        } else {
            userLogined = user;
            resetForm();
            directIfLogined();
        }

    }

    public void resetForm() {
        user = new Customers();
        repeatPassword = "";
    }

    public void directIfLogined() {
        if (isLogin()) {                
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml?faces-redirect=true");

        }
    }
    public void directIfNotLogined() {
        if (!isLogin()) {                
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml?faces-redirect=true");

        }
        
    }

    public void directIfAdmin() {
        if (isAdmin()) {
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "index.xhtml?faces-redirect=true");
        }
    }

    public String logout() {
        userLogined = null;
        return "index?faces-redirect=true";
    }

    public boolean isAdmin() {
        if (!isLogin()) {
            return false;
        }
        return (userLogined.getUsername().equals("admin"));
    }

    public boolean isLogin() {
        return userLogined != null;
    }

    public void setUser(Customers user) {
        this.user = user;
    }

    public Customers getUser() {
        return user;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setUserLogined(Customers userLogined) {
        this.userLogined = userLogined;
    }

    public Customers getUserLogined() {
        return userLogined;
    }

}

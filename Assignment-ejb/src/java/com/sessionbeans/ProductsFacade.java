/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sessionbeans;

import com.entities.Products;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author DELL
 */
@Stateless
public class ProductsFacade extends AbstractFacade<Products> implements ProductsFacadeLocal {

    @PersistenceContext(unitName = "Assignment-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductsFacade() {
        super(Products.class);
    }

    public int findMaxProductID() {
        try {
            Query query = em.createQuery("SELECT max (p.productID) FROM Products p");
            int maxid = (int) query.getSingleResult();
            return maxid;
        } catch (NoResultException e) {
            return 0;
        }
    }

    public Collection<Products> findProductListWithFind(String productFind) {
        if (productFind.trim().equals("")) {
            return findAll();
        }
        productFind = "%" + productFind + "%";
        Query query = em.createQuery("SELECT p FROM Products p where p.productName like :productFind");
        query.setParameter("productFind", productFind);
        return query.getResultList();
    }
}

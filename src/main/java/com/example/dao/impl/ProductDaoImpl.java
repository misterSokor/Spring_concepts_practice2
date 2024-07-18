package com.example.dao.impl;

import com.example.dao.ProductDao;
import com.example.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {
    private final SessionFactory factory;
    @Autowired
    public ProductDaoImpl(SessionFactory factory) {
        this.factory = factory;
    }
    @Override
    public Product save(Product product) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
            return product;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't insert Product entity", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Product get(Long id) {
        try (Session session = factory.openSession()) {
            return session.get(Product.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Can't get Product entity by id: " + id, e);
        }
    }

    @Override
    public List<Product> findAll() {
        try (Session session = factory.openSession()) {
            Query<Product> query = session.createQuery("from Product", Product.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get all Product entities", e);
        }
    }
}

package org.example;


import org.example.domain.Category;
import org.example.domain.item.Book;
import org.example.domain.item.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Category cat1 = new Category();
            cat1.setName("cat1");
            em.persist(cat1);

            Category cat2 = new Category();
            cat2.setName("cat2");
            em.persist(cat2);

            Book book = new Book();
            book.setName("jpa");
            book.setAuthor("Kim");
            em.persist(book);

            Movie movie = new Movie();
            movie.setName("movie");
            movie.setDirector("Chung");
            em.persist(movie);

            em.flush();
            em.clear();

            List<Category> categories =
                em.createQuery("select c from Category c join fetch c.items", Category.class).getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emFactory.close();
    }
}
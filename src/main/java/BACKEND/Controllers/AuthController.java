package BACKEND.Controllers;

import BACKEND.Models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

public class AuthController {

    private final SessionFactory sessionFactory;
    private User user;

    public AuthController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User authenticate(String email, String password) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        User user = null;

        try {
            transaction = session.beginTransaction();
            user = (User) session.createQuery("from User where email=:email")
                    .setParameter("email", email)
                    .uniqueResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (user == null) {
            // Username not found
            return null;
        }

        // Compare hashed password with plaintext password using BCrypt
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            // Password does not match
            return null;
        }
    }

}

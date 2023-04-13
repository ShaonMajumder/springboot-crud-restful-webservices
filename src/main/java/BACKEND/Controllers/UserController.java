package BACKEND.Controllers;

import BACKEND.Models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserController implements ControllerInterface<User>{

    private final SessionFactory sessionFactory;
    private final ControllerHelper controller;

    private User user;

    private String salt = "";

    public UserController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.controller = new ControllerHelper(sessionFactory);
        this.controller.setModelName("User");
    }

    @Override
    public User getModel() {
        return user;
    }

    @Override
    public void setModel(User user) {
        this.user = user;
    }

    @Override
    public List<User> getAllModels() {
        return controller.getAllModels();
    }

    @Override
    public User getById(int id) throws ClassNotFoundException {
        return (User) controller.getById(id);
    }

    @Override
    public int create(Object... args) {
        if (args.length < 5) {
            System.out.println("Insufficient arguments provided");
            return -1;
        }

        String username = (String) args[0];
        String password = (String) args[1];
        String email = (String) args[2];
        String firstName = (String) args[3];
        String lastName = (String) args[4];

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int userId = 0;

        try {
            transaction = session.beginTransaction();
            User user = new User(username, password, email, firstName, lastName);
            String salt;
            if (this.salt != "") {
                salt = this.salt;
            } else {
                salt = BCrypt.gensalt();
            }
            user.setPassword(BCrypt.hashpw(user.getPassword(), salt));
            session.persist(user);
            userId = user.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return userId;
    }

    @Override
    public int create(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int userId = 0;

        try {
            transaction = session.beginTransaction();
            String salt;
            if (this.salt != "") {
                salt = this.salt;
            } else {
                salt = BCrypt.gensalt();
            }
            user.setPassword(BCrypt.hashpw(user.getPassword(), salt));
            session.persist(user);
            userId = user.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return userId;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }

    @Override
    public boolean updateCore(User user) {
        if (user == null) {
            System.out.println("No user selected");
            return false;
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean updated = false;

        try {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            updated = true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return updated;
    }

    @Override
    public boolean updateByNewModel(User updatedUser) {
        return this.updateCore(updatedUser);
    }

    @Override
    public boolean updateByNewData(int userId, Object... args ) {
        if (args.length < 4) {
            System.out.println("Insufficient arguments provided");
            return false;
        }
        
        String newUsername = (String) args[0];
        String newPassword = (String) args[1];
        String newEmail = (String) args[2];
        String newFirstName = (String) args[3];
        String newLastName = (String) args[4];

        User updatedUser = new User(newUsername, newPassword, newEmail, newFirstName, newLastName);
        updatedUser.setId(userId);
        return this.updateCore(updatedUser);
    }

    @Override
    public boolean update() {
        return this.updateCore(this.user);
    }

    @Override
    public boolean delete(int id) throws ClassNotFoundException {
        return controller.delete(id);
    }
}

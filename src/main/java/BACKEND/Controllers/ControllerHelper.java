package BACKEND.Controllers;

import BACKEND.Models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ControllerHelper {

    private final SessionFactory sessionFactory;
    private String model;

    private User user;

    public ControllerHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setModelName(String objectName) {
        this.model = objectName;
    }

    public User getModel() {
        return user;
    }


    public void setModel(Object user) {
        this.user = (User) user;
    }

    public List getAllModels() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List objects = null;

        try {
            transaction = session.beginTransaction();
            objects = session.createQuery("from "+this.model).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return objects;
    }

    public Object getById(int id) throws ClassNotFoundException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        Object object = null;
        Class<?> modelClass = null;

        try {
            modelClass = Class.forName("BACKEND.Models." + this.model);
        } catch (ClassNotFoundException e) {
            // Handle the exception here
            System.out.println(this.model + " Class not found !");
            return null;
        }

        try {
            transaction = session.beginTransaction();
            object = session.get(modelClass, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }





        return object;
    }

    public int create(String username, String password, String email, String firstName, String lastName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int userId = 0;

        try {
            transaction = session.beginTransaction();
            User user = new User(username, password, email, firstName, lastName);
            userId = (int) session.save(user);
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

    public int create(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int userId = 0;

        try {
            transaction = session.beginTransaction();
            userId = (int) session.save(user);
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

//    @Override
//    public boolean updateCore(Object user) {
//        if (user instanceof User) {
//            System.out.println("obj is not an instance of User");
//            return false;
//        }
//
//        if (user == null) {
//            System.out.println("No user selected");
//            return false;
//        }
//
//        Session session = sessionFactory.openSession();
//        Transaction transaction = null;
//        boolean updated = false;
//
//        try {
//            transaction = session.beginTransaction();
//            session.update(user);
//            transaction.commit();
//            updated = true;
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            session.close();
//        }
//
//        return updated;
//    }

//    public boolean updateByNewModel(User updatedUser) {
//        return this.updateCore(updatedUser);
//    }

//    public boolean updateByNewData(int userId, String newUsername, String newPassword, String newEmail, String newFirstName, String newLastName) {
//        User updatedUser = new User(newUsername, newPassword, newEmail, newFirstName, newLastName);
//        updatedUser.setId(userId);
//        return this.updateCore(updatedUser);
//    }

//    public boolean update() {
//        return this.updateCore(this.user);
//    }



    /**
     * Deletes a permission by its ID.
     *
     * @param id The ID of the permission to delete.
     * @return true if the permission was deleted successfully, false otherwise.
     */
    public boolean delete(int id) throws ClassNotFoundException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean deleted = false;
        Object object = null;
        Class<?> modelClass = null;

        try {
            modelClass = Class.forName("BACKEND.Models." + this.model);
        } catch (ClassNotFoundException e) {
            // Handle the exception here
            System.out.println(this.model + " Class not found !");
            return false;
        }

        try {
            transaction = session.beginTransaction();
            object = session.get(modelClass, id);

            if (object != null) {
                session.delete(object);
                transaction.commit();
                deleted = true;
            } else {
                System.out.println(this.model + " with id " + id + " not found");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return deleted;
    }


}

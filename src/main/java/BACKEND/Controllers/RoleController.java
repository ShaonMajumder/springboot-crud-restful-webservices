package BACKEND.Controllers;

import BACKEND.Models.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class RoleController implements ControllerInterface<Role> {

    private final SessionFactory sessionFactory;
    private final ControllerHelper controller;

    private Role role;

    public RoleController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.controller = new ControllerHelper(sessionFactory);
        this.controller.setModelName("Role");
    }

    @Override
    public Role getModel() {
        return role;
    }

    @Override
    public void setModel(Role role) {
        this.role = role;
    }

    @Override
    public List<Role> getAllModels() {
        return controller.getAllModels();
    }

    @Override
    public Role getById(int id) throws ClassNotFoundException {
        return (Role) controller.getById(id);
    }

    @Override
    public int create(Object... args) {
        if (args.length < 2) {
            System.out.println("Insufficient arguments provided");
            return -1;
        }

        String name = (String) args[0];
        String description = (String) args[1];

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int roleId = 0;

        try {
            transaction = session.beginTransaction();
            Role role = new Role(name, description);
            session.persist(role);
            roleId = role.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return roleId;
    }

    @Override
    public int create(Role role) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int roleId = 0;

        try {
            transaction = session.beginTransaction();
            session.persist(role);
            roleId = role.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return roleId;
    }

    @Override
    public boolean updateCore(Role role) {
        if (role == null) {
            System.out.println("No role selected");
            return false;
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean updated = false;

        try {
            transaction = session.beginTransaction();
            session.merge(role);
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
    public boolean updateByNewModel(Role updatedRole) {
        return this.updateCore(updatedRole);
    }

    @Override
    public boolean updateByNewData(int roleId, Object... args) {
        if (args.length < 2) {
            System.out.println("Insufficient arguments provided");
            return false;
        }

        String name = (String) args[0];
        String description = (String) args[1];

        Role role = new Role(name, description);
        role.setId(roleId);

        return this.updateCore(role);
    }

    @Override
    public boolean update() {
        return this.updateCore(this.role);
    }

    @Override
    public boolean delete(int id) throws ClassNotFoundException {
        return controller.delete(id);
    }
}



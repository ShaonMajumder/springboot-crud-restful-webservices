package BACKEND.Controllers;

import BACKEND.Models.Permission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.List;

public class PermissionController implements ControllerInterface<Permission> {

    private final SessionFactory sessionFactory;
    private final ControllerHelper controller;

    private Permission permission;

    public PermissionController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.controller = new ControllerHelper(sessionFactory);
        this.controller.setModelName("Permission");
    }

    @Override
    public Permission getModel() {
        return permission;
    }

    @Override
    public void setModel(Permission permission) {
        this.permission = permission;
    }

    @Override
    public List<Permission> getAllModels() {
        return controller.getAllModels();
    }

    @Override
    public Permission getById(int id) throws ClassNotFoundException {
        return (Permission) controller.getById(id);
    }

    @Override
    public int create(Object... args) {
        if (args.length < 2) {
            System.out.println("Insufficient arguments provided");
            return -1;
        }

        String name = (String) args[0];
        String description = (String) args[1];
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        Permission permission = new Permission(name, description);
        permission.setCreatedAt(createdAt);

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int permissionId = 0;

        try {
            transaction = session.beginTransaction();
            session.persist(permission);
            permissionId = permission.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return permissionId;
    }

    @Override
    public int create(Permission permission) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int permissionId = 0;

        try {
            transaction = session.beginTransaction();
            session.persist(permission);
            permissionId = permission.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return permissionId;
    }

    @Override
    public boolean updateCore(Permission permission) {
        if (permission == null) {
            System.out.println("No permission selected");
            return false;
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean updated = false;

        try {
            transaction = session.beginTransaction();
            session.merge(permission);
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
    public boolean updateByNewModel(Permission updatedPermission) {
        return this.updateCore(updatedPermission);
    }

    @Override
    public boolean updateByNewData(int permissionId, Object... args) {
        if (args.length < 2) {
            System.out.println("Insufficient arguments provided");
            return false;
        }

        String name = (String) args[0];
        String description = (String) args[1];

        Permission permission = new Permission(name, description);
        permission.setId(permissionId);

        return this.updateCore(permission);
    }

    @Override
    public boolean update() {
        return this.updateCore(this.permission);
    }

    @Override
    public boolean delete(int id) throws ClassNotFoundException {
        return controller.delete(id);
    }
}

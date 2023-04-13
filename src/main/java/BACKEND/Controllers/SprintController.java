package BACKEND.Controllers;

import BACKEND.Models.Sprint;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Date;
import java.util.List;

public class SprintController implements ControllerInterface<Sprint> {

    private final SessionFactory sessionFactory;
    private final ControllerHelper controller;

    private Sprint sprint;

    public SprintController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.controller = new ControllerHelper(sessionFactory);
        this.controller.setModelName("Sprint");
    }

    @Override
    public Sprint getModel() {
        return sprint;
    }

    @Override
    public void setModel(Sprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public List<Sprint> getAllModels() {
        return controller.getAllModels();
    }

    @Override
    public Sprint getById(int id) throws ClassNotFoundException {
        return (Sprint) controller.getById(id);
    }

    @Override
    public int create(Object... args) {
        if (args.length < 4) {
            System.out.println("Insufficient arguments provided");
            return -1;
        }

        String name = (String) args[0];
        String description = (String) args[1];
        Date startDate = (Date) args[2];
        Date endDate = (Date) args[3];

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int sprintId = 0;

        try {
            transaction = session.beginTransaction();
            Sprint sprint = new Sprint(name, description, startDate, endDate);
            session.persist(sprint);
            sprintId = sprint.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return sprintId;
    }

    @Override
    public int create(Sprint sprint) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int sprintId = 0;

        try {
            transaction = session.beginTransaction();
            session.persist(sprint);
            sprintId = sprint.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return sprintId;
    }

    @Override
    public boolean updateCore(Sprint sprint) {
        if (sprint == null) {
            System.out.println("No sprint selected");
            return false;
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean updated = false;

        try {
            transaction = session.beginTransaction();
            session.merge(sprint);
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
    public boolean updateByNewModel(Sprint updatedSprint) {
        return this.updateCore(updatedSprint);
    }

    @Override
    public boolean updateByNewData(int sprintId, Object... args) {
        if (args.length < 4) {
            System.out.println("Insufficient arguments provided");
            return false;
        }

        String name = (String) args[0];
        String description = (String) args[1];
        Date startDate = (Date) args[2];
        Date endDate = (Date) args[3];

        Sprint updatedSprint = new Sprint(name, description, startDate, endDate);
        updatedSprint.setId(sprintId);
        return this.updateCore(updatedSprint);
    }

    @Override
    public boolean update() {
        return this.updateCore(this.sprint);
    }

    @Override
    public boolean delete(int id) throws ClassNotFoundException {
        return controller.delete(id);
    }
}

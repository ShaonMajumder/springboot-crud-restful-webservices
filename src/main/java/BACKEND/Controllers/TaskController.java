package BACKEND.Controllers;

import BACKEND.Models.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TaskController implements ControllerInterface<Task> {

    private final SessionFactory sessionFactory;
    private ControllerHelper controller;

    private Task task;

    public TaskController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.controller = new ControllerHelper(sessionFactory);
        this.controller.setModelName("Task");
    }

    @Override
    public Task getModel() {
        return task;
    }

    @Override
    public void setModel(Task task) {
        this.task = task;
    }

    @Override
    public List<Task> getAllModels() {
        return controller.getAllModels();
    }

    @Override
    public Task getById(int id) throws ClassNotFoundException {
        return (Task) controller.getById(id);
    }

    @Override
    public int create(Object... args) {
        if (args.length < 4) {
            System.out.println("Insufficient arguments provided");
            return -1;
        }

        String name = (String) args[0];
        String description = (String) args[1];
        int points = (int) args[2];
        double duration = (double) args[3];


        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int taskId = 0;

        try {
            transaction = session.beginTransaction();
            Task task = new Task(name, description, points, duration);
            taskId = (int) session.save(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return taskId;
    }

    @Override
    public int create(Task task) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int taskId = 0;

        try {
            transaction = session.beginTransaction();
            taskId = (int) session.save(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return taskId;
    }

    @Override
    public boolean updateCore(Task task) {
        if (task == null) {
            System.out.println("No task selected");
            return false;
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean updated = false;

        try {
            transaction = session.beginTransaction();
            session.update(task);
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
    public boolean updateByNewModel(Task updatedTask) {
        return this.updateCore(updatedTask);
    }

    @Override
    public boolean updateByNewData(int taskId, Object... args) {
        if (args.length < 4) {
            System.out.println("Insufficient arguments provided");
            return false;
        }

        String newName = (String) args[0];
        String newDescription = (String) args[1];
        int newPoints = (int) args[2];
        double newDuration = (double) args[3];

        Task updatedTask = new Task(newName, newDescription, newPoints, newDuration);
        updatedTask.setId(taskId);
        return this.updateCore(updatedTask);
    }

    @Override
    public boolean update() {
        return this.updateCore(this.task);
    }

    @Override
    public boolean delete(int id) throws ClassNotFoundException {
        return controller.delete(id);
    }
}

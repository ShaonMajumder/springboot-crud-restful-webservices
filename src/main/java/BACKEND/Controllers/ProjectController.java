package BACKEND.Controllers;

import BACKEND.Models.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ProjectController implements ControllerInterface<Project> {

    private final SessionFactory sessionFactory;
    private final ControllerHelper controller;

    private Project project;

    public ProjectController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.controller = new ControllerHelper(sessionFactory);
        this.controller.setModelName("Project");
    }

    @Override
    public Project getModel() {
        return project;
    }

    @Override
    public void setModel(Project project) {
        this.project = project;
    }

    @Override
    public List<Project> getAllModels() {
        return controller.getAllModels();
    }

    @Override
    public Project getById(int id) throws ClassNotFoundException {
        return (Project) controller.getById(id);
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
        int projectId = 0;

        try {
            transaction = session.beginTransaction();
            Project project = new Project(name, description);
            session.persist(project);
            projectId = project.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return projectId;
    }

    @Override
    public int create(Project project) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        int projectId = 0;

        try {
            transaction = session.beginTransaction();
            session.persist(project);
            projectId = project.getId();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return projectId;
    }

    @Override
    public boolean updateCore(Project project) {
        if (project == null) {
            System.out.println("No project selected");
            return false;
        }

        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        boolean updated = false;

        try {
            transaction = session.beginTransaction();
            session.merge(project);
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
    public boolean updateByNewModel(Project updatedProject) {
        return this.updateCore(updatedProject);
    }

    @Override
    public boolean updateByNewData(int projectId, Object... args) {
        if (args.length < 2) {
            System.out.println("Insufficient arguments provided");
            return false;
        }

        String name = (String) args[0];
        String description = (String) args[1];

        Project updatedProject = new Project(name, description);
        updatedProject.setId(projectId);
        return this.updateCore(updatedProject);
    }

    @Override
    public boolean update() {
        return this.updateCore(this.project);
    }

    @Override
    public boolean delete(int id) throws ClassNotFoundException {
        return controller.delete(id);
    }
}

package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;

import static be.dafke.BusinessModelDao.XMLReader.*;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class ProjectsIO {
    public static void readProjects(Projects projects, Accounts accounts, AccountTypes accountTypes, File accountingFolder) {
        File projectsFolder = new File(accountingFolder, "Projects");
        File xmlFile = new File(accountingFolder, "Projects.xml");
        Element rootElement = getRootElement(xmlFile, PROJECTS);
        for (Element element : getChildren(rootElement, PROJECT)) {

            String name = getValue(element, NAME);
            Project project = new Project(name, accounts, accountTypes);

            try {
                projects.addBusinessObject(project);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }

        for(Project project:projects.getBusinessObjects()){
            readProject(project, accounts, projectsFolder);
        }
    }

    public static void readProject(Project project, Accounts accounts, File projectsFolder) {
        String projectName = project.getName();
        File xmlFile = new File(projectsFolder, projectName+".xml");
        Element rootElement = getRootElement(xmlFile, PROJECT);
        for (Element element : getChildren(rootElement, ACCOUNT)) {

            String name = getValue(element, NAME);
            Account account = accounts.getBusinessObject(name);

            try {
                project.addBusinessObject(account);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

}

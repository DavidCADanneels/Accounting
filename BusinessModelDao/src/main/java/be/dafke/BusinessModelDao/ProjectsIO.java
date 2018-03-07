package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class ProjectsIO {

    public static void readProjects(Accounting accounting) {
        Projects projects = accounting.getProjects();
        Accounts accounts = accounting.getAccounts();
        AccountTypes accountTypes = accounting.getAccountTypes();
        File projectsFolder = new File(XML_PATH+accounting.getName()+"/"+PROJECTS);
        File xmlFile = new File(XML_PATH+accounting.getName()+"/"+PROJECTS+ XML_EXTENSION);
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
        File xmlFile = new File(projectsFolder, projectName+ XML_EXTENSION);
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

    public static void writeProjects(Projects projects, File accountingFolder){
        File projectsFile = new File(accountingFolder, PROJECTS+ XML_EXTENSION);
        File projectsFolder = new File(accountingFolder, PROJECTS);
        try{
            Writer writer = new FileWriter(projectsFile);
            writer.write(getXmlHeader(PROJECTS, 2));
            for(Project project: projects.getBusinessObjects()) {
                writer.write(
                        "  <"+PROJECT+">\n" +
                        "    <"+NAME+">"+project.getName()+"</"+NAME+">\n" +
                        "  </"+PROJECT+">\n"
                );
            }
            writer.write("</"+PROJECTS+">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Projects.class.getName()).log(Level.SEVERE, null, ex);
        }

        projectsFolder.mkdirs();
        for(Project project: projects.getBusinessObjects()) {
            writeProject(project, projectsFolder);
        }
    }

    private static void writeProject(Project project, File projectsFolder) {
        File projectFile = new File(projectsFolder, project.getName()+ XML_EXTENSION);
        try {
            Writer writer = new FileWriter(projectFile);
            writer.write(getXmlHeader(PROJECT, 3));
            for(Account account:project.getBusinessObjects()) {
                writer.write(
                        "  <"+ACCOUNT+">\n" +
                        "    <"+NAME+">"+account.getName()+"</"+NAME+">\n" +
                        "  </"+ACCOUNT+">\n"
                );
            }
            writer.write("</"+PROJECT+">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

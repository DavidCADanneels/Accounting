package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader 

class ProjectsIO {

    static void readProjects(Accounting accounting) {
        Projects projects = accounting.getProjects()
        Accounts accounts = accounting.accounts
        AccountTypes accountTypes = accounting.accountTypes
        File projectsFolder = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$PROJECTS")
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$PROJECTS$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, PROJECTS)
        for (Element element : getChildren(rootElement, PROJECT)) {

            String name = getValue(element, NAME)
            Project project = new Project(name, accounts, accountTypes)

            try {
                projects.addBusinessObject(project)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }

        for(Project project:projects.businessObjects){
            readProject(project, accounts, projectsFolder)
        }
    }

    static void readProject(Project project, Accounts accounts, File projectsFolder) {
        String projectName = project.name
        File xmlFile = new File(projectsFolder, "$projectName$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, PROJECT)
        for (Element element : getChildren(rootElement, ACCOUNT)) {

            String name = getValue(element, NAME)
            Account account = accounts.getBusinessObject(name)

            try {
                project.addBusinessObject(account)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeProjects(Accounting accounting){
        Projects projects = accounting.getProjects()
        File projectsFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$PROJECTS$XML_EXTENSION")
        File projectsFolder = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$PROJECTS")
        try{
            Writer writer = new FileWriter(projectsFile)
            writer.write getXmlHeader(PROJECTS, 2)
            for(Project project: projects.businessObjects) {
                writer.write """\
  <$PROJECT>
    <$NAME>$project.name</$NAME>
  </$PROJECT>
"""
            }
            writer.write"""\
</$PROJECTS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Projects.class.name).log(Level.SEVERE, null, ex)
        }

        projectsFolder.mkdirs()
        for(Project project: projects.businessObjects) {
            writeProject(project, projectsFolder)
        }
    }

    static void writeProject(Project project, File projectsFolder) {
        File projectFile = new File(projectsFolder, project.name+ XML_EXTENSION)
        try {
            Writer writer = new FileWriter(projectFile)
            writer.write getXmlHeader(PROJECT, 3)
            for(Account account:project.businessObjects) {
                writer.write(
                        "  <$ACCOUNT>\n" +
                                "    <$NAME>$account.name</$NAME>\n" +
                                "  </$ACCOUNT>\n"
                )
            }
            writer.write("</$PROJECT>\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Project.class.name).log(Level.SEVERE, null, ex)
        }
    }
}

package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Project;
import be.dafke.Accounting.Objects.Accounting.Projects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 4:59
 */
public class AccountsSAXParser {
    public static void readAccounts(Accounts accounts, Projects projects, File accountingFile){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(accountingFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node accountsNode = doc.getElementsByTagName("Accounts").item(0);

            String xmlLocation = doc.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            accounts.setFolder(xmlLocation);

            accountsFromXML(accounts, projects, (Element) accountsNode);

        } catch (IOException io) {
            io.printStackTrace();
            FileSystemView.getFileSystemView().createFileObject("Accounting.xml");
            System.out.println(accountingFile.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void accountsFromXML(Accounts accounts, Projects projects, Element accountsElement){
        NodeList accountsNode = accountsElement.getElementsByTagName("Account");
        for (int i = 0; i < accountsNode.getLength(); i++) {
            Element element = (Element)accountsNode.item(i);
            String account_name = element.getElementsByTagName("account_name").item(0).getChildNodes().item(0).getNodeValue();
            String account_type = element.getElementsByTagName("account_type").item(0).getChildNodes().item(0).getNodeValue();

            Account.AccountType type = Account.AccountType.valueOf(account_type);
            try{
                Account account = accounts.addAccount(account_name, type);
                NodeList projectNodeList = element.getElementsByTagName("account_project");
                if(projectNodeList.getLength()>0){
                    String account_project = projectNodeList.item(0).getChildNodes().item(0).getNodeValue();
                    Project project = projects.get(account_project);
                    if (project == null) {
                        project = new Project(account_project);
                        projects.put(account_project, project);
                    }
                    project.addAccount(account);
                }
            } catch (DuplicateNameException e) {
                System.err.println("There is already an account with the name \""+account_name+"\".");
            } catch (EmptyNameException e) {
                System.err.println("The name of the account is empty.");
            }
        }
    }
}

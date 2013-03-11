package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.AccountType;
import be.dafke.Accounting.Objects.Accounting.AccountTypes;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Movement;
import be.dafke.Accounting.Objects.Accounting.Project;
import be.dafke.Accounting.Objects.Accounting.Projects;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 4:59
 */
public class AccountsSAXParser {

    // READ
    //
    public static void readAccounts(Accounts accounts, AccountTypes accountTypes, Projects projects){
        try {
            File file = accounts.getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Element rootElement = (Element) doc.getElementsByTagName("Accounts").item(0);
            NodeList nodeList = rootElement.getElementsByTagName("Account");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);
                File xmlFile = Utils.getFile(element, "xml");
                File htmlFile = Utils.getFile(element, "html");
                String account_name = Utils.getValue(element, "account_name");
                String account_type = Utils.getValue(element, "account_type");

                AccountType type = accountTypes.getBusinessObject(account_type);
                try{
                    Account account = new Account();
                    account.setName(account_name.trim());
                    account.setAccountType(type);
                    accounts.addBusinessObject(account);
                    account.setXmlFile(xmlFile);
                    account.setHtmlFile(htmlFile);
                    String account_project = Utils.getValue(element, "account_project");
                    if(account_project!=null){
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

        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    //
    public static void writeAccounts(Accounts accounts) {
        try {
            Writer writer = new FileWriter(accounts.getXmlFile());

            writer.write(accounts.getXmlHeader());

            writer.write("<Accounts>\r\n");
            for(Account account : accounts.getBusinessObjects()) {
                if(account.getType().equals("Account")){
                    writer.write("  <Account>\r\n");
                    writer.write("    <xml>" + account.getXmlFile() + "</xml>\r\n");
                    if(account.getHtmlFile()!=null){
                        writer.write("    <html>" + account.getHtmlFile() + "</html>\r\n");
                    }
                    writer.write("    <account_name>" + account.getName() + "</account_name>\r\n");
                    writer.write("    <account_type>" + account.getAccountType() + "</account_type>\r\n");
                    writer.write("  </Account>\r\n");
                }
            }
            writer.write("</Accounts>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Account account:accounts.getBusinessObjects()){
//            TODO: add isSavedXML
//            if(account.isSavedXML()){
            writeAccount(account);
//            }
        }
    }
    //
    private static void writeAccount(Account account){
        try {
            Writer writer = new FileWriter(account.getXmlFile());

            writer.write(account.getXmlHeader());

            writer.write("<Account>\r\n" + "  <name>" + account.getName() + "</name>\r\n");
            for(Movement movement : account.getMovements()){
                writer.write("  <action id=\""+movement.getBooking().getTransaction().getId()+"\">\r\n");
                writer.write("    <nr>" + movement.getBooking().getTransaction().getAbbreviation() + movement.getBooking().getTransaction().getId() + "</nr>\r\n");
                writer.write("    <journal_xml>" + movement.getBooking().getTransaction().getJournal().getXmlFile() + "</journal_xml>\r\n");
                writer.write("    <journal_html>" + movement.getBooking().getTransaction().getJournal().getHtmlFile() + "</journal_html>\r\n");
                writer.write("    <date>" + Utils.toString(movement.getBooking().getTransaction().getDate()) + "</date>\r\n");
                writer.write("    <" + (movement.isDebit() ? "debit" : "credit") + ">"
                                     + movement.getAmount().toString()
                               + "</" + (movement.isDebit() ? "debit" : "credit") + ">\r\n");
                writer.write("    <description>" + movement.getBooking().getTransaction().getDescription() + "</description>\r\n");
                writer.write("  </action>\r\n");
            }
            BigDecimal saldo = account.getSaldo();
            String resultType =(saldo.compareTo(BigDecimal.ZERO)<0)?"credit":"debit";
            writer.write("  <closed type = \"" + resultType + "\">\r\n" + "    <debitTotal>" + account.getDebetTotal() + "</debitTotal>\r\n"
                    + "    <creditTotal>" + account.getCreditTotal() + "</creditTotal>\r\n"
                    + "    <saldo>" + saldo.abs() + "</saldo>\r\n  </closed>\r\n");
            writer.write("</Account>");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

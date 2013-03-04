package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Booking;
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
    public static void readAccounts(Accounts accounts, Projects projects){
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
                String xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
                String htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
                String account_name = element.getElementsByTagName("account_name").item(0).getChildNodes().item(0).getNodeValue();
                String account_type = element.getElementsByTagName("account_type").item(0).getChildNodes().item(0).getNodeValue();

                Account.AccountType type = Account.AccountType.valueOf(account_type);
                try{
                    Account account = accounts.addAccount(account_name, type);
                    account.setXmlFile(new File(xmlFile));
                    account.setHtmlFile(new File(htmlFile));
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

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE Accounts SYSTEM \"" + accounts.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + accounts.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<Accounts>\r\n");
            for(Account account : accounts.getAllAccounts()) {
                writer.write("  <Account>\r\n");
                writer.write("    <xml>" + account.getXmlFile() + "</xml>\r\n");
                writer.write("    <html>" + account.getHtmlFile() + "</html>\r\n");
                writer.write("    <account_name>" + account.getName() + "</account_name>\r\n");
                writer.write("    <account_type>" + account.getType() + "</account_type>\r\n");
                writer.write((account.getProject() == null ? "" : "      <account_project>"
                        + account.getProject() + "</account_project>\r\n"));
                writer.write("  </Account>\r\n");
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
        for(Account account:accounts.getAllAccounts()){
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

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE Account SYSTEM \"" + account.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + account.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<Account>\r\n" + "  <name>" + account.getName() + "</name>\r\n");
            for(Booking booking : account.getBookings()){
                writer.write("  <action id=\""+booking.getId()+"\">\r\n");
                writer.write("    <nr>" + booking.getAbbreviation() + booking.getId() + "</nr>\r\n");
                writer.write("    <journal_xml>" + booking.getJournal().getXmlFile() + "</journal_xml>\r\n");
                writer.write("    <journal_html>" + booking.getJournal().getHtmlFile() + "</journal_html>\r\n");
                writer.write("    <date>" + Utils.toString(booking.getDate()) + "</date>\r\n");
                writer.write("    <" + (booking.isDebit() ? "debit" : "credit") + ">"
                                     + booking.getAmount().toString()
                               + "</" + (booking.isDebit() ? "debit" : "credit") + ">\r\n");
                writer.write("    <description>" + booking.getDescription() + "</description>\r\n");
                writer.write("  </action>\r\n");
            }
            BigDecimal saldo = account.saldo();
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

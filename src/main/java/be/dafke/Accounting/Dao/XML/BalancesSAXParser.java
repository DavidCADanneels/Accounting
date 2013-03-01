package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Balance;
import be.dafke.Accounting.Objects.Accounting.Balances;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:05
 */
public class BalancesSAXParser {
    // READ
    public static void readBalances(Balances balances){
        File file = balances.getXmlFile();
        if(file == null || !file.exists()){
            System.err.println(file.getAbsolutePath() + "not found");
        }
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Element element = (Element) doc.getElementsByTagName("Balances").item(0);
            String xmlLocation = element.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            String xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            String htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            balances.setFolder(xmlLocation);
            balances.setXmlFile(new File(xmlFile));
            balances.setHtmlFile(new File(htmlFile));

        } catch (IOException io) {
            io.printStackTrace();
//            FileSystemView.getFileSystemView().createFileObject("Balances.xml");
//            System.out.println(file.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    public static void writeBalances(Balances balances) {
        try {
            Writer writer = new FileWriter(balances.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Balances SYSTEM \""
                    + balances.getDtdFile().getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + balances.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n" + "<Balances>\r\n");
            writer.write("  <location>" + balances.getFolder() + "</location>\r\n");
            writer.write("  <xml>" + balances.getXmlFile() + "</xml>\r\n");
            writer.write("  <html>" + balances.getHtmlFile() + "</html>\r\n");
            for(Balance balance:balances.getBalances()){
                writer.write("    <Balance>\r\n");
                writer.write("      <name>" + balance.getName() + "</name>\r\n");
                writer.write("      <xml>" + balance.getXmlFile() + "</xml>\r\n");
                writer.write("      <html>" + balance.getHtmlFile() + "</html>\r\n");
                writer.write("    </Balance>\r\n");
            }
            writer.write("</Balances>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Balance balance : balances.getBalances()){
            toXML(balance);
        }
    }
    //
    private static void toXML(Balance balance){
        System.out.println("Balances.TOXML(" + balance.toString() + ")");
        try {
            Writer writer = new FileWriter(balance.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + balance.getXslFile().getCanonicalPath() + "\"?>\r\n"
                    + "<balance>\r\n  <name>" + balance.getName() //$NON-NLS-1$ //$NON-NLS-2$
                    + "</name>\r\n  <left>" + balance.getLeftName() + "</left>\r\n  <right>" + balance.getRightName() + "</right>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ArrayList<Account> leftAccounts = balance.getLeftAccounts();
            ArrayList<Account> rightAccounts = balance.getRightAccounts();

            int nrLeft = leftAccounts.size();
            int nrRight = rightAccounts.size();
            int max;
            if (nrLeft > nrRight) {
                max = nrLeft;
            } else {
                max = nrRight;
            }
            for(int i = 0; i < max; i++) {
                writer.write("  <line>\r\n"); //$NON-NLS-1$
                if (i < nrLeft) {
                    writer.write("    <name1>" + leftAccounts.get(i).toString() + "</name1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                    writer.write("    <amount1>" + leftAccounts.get(i).saldo() + "</amount1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    writer.write("    <name1></name1>\r\n"); //$NON-NLS-1$
                    writer.write("    <amount1></amount1>\r\n"); //$NON-NLS-1$
                }
                if (i < nrRight) {
                    writer.write("    <amount2>" + BigDecimal.ZERO.subtract(rightAccounts.get(i).saldo()) //$NON-NLS-1$
                            + "</amount2>\r\n"); //$NON-NLS-1$
                    writer.write("    <name2>" + rightAccounts.get(i).toString() + "</name2>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    writer.write("    <amount2></amount2>\r\n"); //$NON-NLS-1$
                    writer.write("    <name2></name2>\r\n"); //$NON-NLS-1$
                }
                writer.write("  </line>\r\n"); //$NON-NLS-1$
            }
            writer.write("</balance>"); //$NON-NLS-1$
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

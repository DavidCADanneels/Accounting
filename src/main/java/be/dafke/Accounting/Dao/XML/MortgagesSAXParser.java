package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;
import be.dafke.Accounting.Objects.Mortgage.Mortgages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:07
 */
public class MortgagesSAXParser {
    // READ
    //
    public static void readMortgages(Accounting accounting, File accountingFile){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(accountingFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node mortgagesNode = doc.getElementsByTagName("Mortgages").item(0);

            String xmlLocation = doc.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            String xmlFile = doc.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            String htmlFile = doc.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            Mortgages mortgages = accounting.getMortgages();
            mortgages.setFolder(xmlLocation);
            mortgages.setXmlFile(new File(xmlFile));
            mortgages.setHtmlFile(new File(htmlFile));

            mortgagesFromXML(accounting, (Element) mortgagesNode);

        } catch (IOException io) {
            io.printStackTrace();
            FileSystemView.getFileSystemView().createFileObject("Mortgages.xml");
            System.out.println(accountingFile.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    private static void mortgagesFromXML(Accounting accounting, Element mortgagesElement) {
        NodeList mortgagesNode = mortgagesElement.getElementsByTagName("Mortgage");
        for (int i = 0; i < mortgagesNode.getLength(); i++) {
            Element element = (Element)mortgagesNode.item(i);
            String mortgageName = element.getAttribute("name");
            String total = element.getAttribute("total");
            String nrPayed = element.getElementsByTagName("nrPayed").item(0).getChildNodes().item(0).getNodeValue();
            String capital_account = element.getElementsByTagName("capital_account").item(0).getChildNodes().item(0).getNodeValue();
            String intrest_account = element.getElementsByTagName("intrest_account").item(0).getChildNodes().item(0).getNodeValue();
            System.out.println("Mortgages: "+" | "+mortgageName+" | "+total+" | "+nrPayed+" | "+capital_account+" | "+intrest_account);
            BigDecimal amount = new BigDecimal(total);
            Mortgage mortgage = new Mortgage(mortgageName, amount);
            int nr = Integer.valueOf(nrPayed);
            mortgage.setPayed(nr);
            Account capital = accounting.getAccounts().get(capital_account);
            mortgage.setCapitalAccount(capital);
            Account intrest = accounting.getAccounts().get(intrest_account);
            mortgage.setIntrestAccount(intrest);
            accounting.getMortgages().addMortgageTable(mortgageName, mortgage);
        }
        Mortgages mortgages = accounting.getMortgages();
        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), mortgages.getFolder());
        File mortgagesFiles[] = FileSystemView.getFileSystemView().getFiles(xmlFolder, false);
        for(File mortgagesFile : mortgagesFiles) {
            String mortgageName = mortgagesFile.getName().replaceAll(".xml", "");
            Mortgage mortgage = accounting.getMortgages().getMortgage(mortgageName);
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(new MortgageContentHandler(mortgage));
                reader.setErrorHandler(new FoutHandler());
                reader.parse(mortgagesFile.getAbsolutePath());
            } catch (IOException io) {
//				FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//				System.out.println(journalFile + " has been created");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // WRITE
    //
    public static void writeMortgages(Mortgages mortgages) {
        try {
            Writer writer = new FileWriter(mortgages.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Mortgages SYSTEM \""
                    + mortgages.getDtdFile().getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + mortgages.getXslFile().getCanonicalPath() + "\"?>\r\n" + "<Mortgages>\r\n");
            writer.write("  <location>" + mortgages.getFolder() + "</location>\r\n");
            writer.write("  <xml>" + mortgages.getXmlFile() + "</xml>\r\n");
            writer.write("  <html>" + mortgages.getHtmlFile() + "</html>\r\n");
            for(Mortgage mortgage : mortgages.getMortgages()) {
                writer.write("  <Mortgage name=\"" + mortgage.toString() + "\" total=\"" + mortgage.getStartCapital() + "\">\r\n");
                writer.write("    <nrPayed>" + mortgage.getNrPayed() + "</nrPayed>\r\n");
                writer.write("    <capital_account>" + mortgage.getCapitalAccount() + "</capital_account>\r\n");
                writer.write("    <intrest_account>" + mortgage.getIntrestAccount() + "</intrest_account>\r\n");
                writer.write("  </Mortgage>\r\n");
            }
            writer.write("</Mortgages>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Mortgage mortgage:mortgages.getMortgages()){
//            TODO: add isSavedXML
//            if(journal.isSavedXML()){
            toXML(mortgage);
//            }
        }
    }
    //
    private static void toXML(Mortgage mortgage) {
        System.out.println("Mortgages.TOXML(" + mortgage.toString() + ")");
        try {
            Writer writer = new FileWriter(mortgage.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + mortgage.getXslFile().getCanonicalPath() + "\"?>\r\n"
                    + "<mortgageTable name=\"" + mortgage.toString() + "\">\r\n");
            int teller = 1;
            for(Vector<BigDecimal> vector : mortgage.getTable()) {
                writer.write("  <line>\r\n" + "    <nr>" + teller + "</nr>\r\n" + "    <mensuality>"
                        + vector.get(0) + "</mensuality>\r\n" + "    <intrest>" + vector.get(1) + "</intrest>\r\n"
                        + "    <capital>" + vector.get(2) + "</capital>\r\n" + "    <restCapital>" + vector.get(3)
                        + "</restCapital>\r\n  </line>\r\n");
                teller++;
            }
            writer.write("</mortgageTable>");
            writer.flush();
            writer.close();
            // setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

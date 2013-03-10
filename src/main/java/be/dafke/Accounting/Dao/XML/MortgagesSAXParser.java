package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Mortgage;
import be.dafke.Accounting.Objects.Accounting.Mortgages;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
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
    public static void readMortgages(Mortgages mortgages, Accounts accounts){
        try {
            File file = mortgages.getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Element mortgagesElement = (Element)doc.getElementsByTagName("Mortgages").item(0);
            NodeList mortgagesNode = mortgagesElement.getElementsByTagName("Mortgage");
            for (int i = 0; i < mortgagesNode.getLength(); i++) {
                Element element = (Element)mortgagesNode.item(i);

                String name = Utils.getValue(element, "name");
                File xmlFile = Utils.getFile(element, "xml");
                File htmlFile = Utils.getFile(element, "html");
                String total = Utils.getValue(element, "total");
                String nrPayed = Utils.getValue(element, "nrPayed");
                String capitalName = Utils.getValue(element, "capital_account_name");
//                File capitalXml = Utils.getFile(element, "capital_account_xml");
//                File capitalHtml = Utils.getFile(element, "capital_account_html");
                String intrestName = Utils.getValue(element, "intrest_account_name");
//                File intrestXml = Utils.getFile(element, "intrest_account_xml");
//                File intrestHtml = Utils.getFile(element, "intrest_account_html");

                BigDecimal amount = new BigDecimal(total);
                Mortgage mortgage = new Mortgage();
                mortgage.setName(name);
                mortgage.setStartCapital(amount);
                mortgage.setXmlFile(xmlFile);
                mortgage.setHtmlFile(htmlFile);
                int nr = Integer.valueOf(nrPayed);
                mortgage.setPayed(nr);
                if(capitalName!=null){
                    Account capital = accounts.getBusinessObject(capitalName);
                    mortgage.setCapitalAccount(capital);
                }
                if(intrestName!=null){
                    Account intrest = accounts.getBusinessObject(intrestName);
                    mortgage.setIntrestAccount(intrest);
                }
                try {
                    mortgages.addBusinessObject(mortgage);
                } catch (EmptyNameException e) {
                    System.err.println("Mortgage name is empty.");
                } catch (DuplicateNameException e) {
                    System.err.println("Mortgage name already exist.");
                }
                readMortgage(mortgage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    private static void readMortgage(Mortgage mortgage){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(new MortgageContentHandler(mortgage));
            reader.setErrorHandler(new FoutHandler());
            reader.parse(mortgage.getXmlFile().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    //
    public static void writeMortgages(Mortgages mortgages) {
        try {
            Writer writer = new FileWriter(mortgages.getXmlFile());

            writer.write(mortgages.getXmlHeader());

            writer.write("<Mortgages>\r\n");
            for(Mortgage mortgage : mortgages.getBusinessObjects()) {
                writer.write("  <Mortgage>\r\n");
                writer.write("    <name>" + mortgage.toString() + "</name>\r\n");
                writer.write("    <xml>" + mortgage.getXmlFile() + "</xml>\r\n");
                if(mortgage.getHtmlFile()!=null){
                    writer.write("    <html>" + mortgage.getHtmlFile() + "</html>\r\n");
                }
                writer.write("    <total>" + mortgage.getStartCapital() + "</total>\r\n");
                writer.write("    <nrPayed>" + mortgage.getNrPayed() + "</nrPayed>\r\n");
                if(mortgage.getCapitalAccount()!=null){
                    writer.write("    <capital_account_name>" + mortgage.getCapitalAccount() + "</capital_account_name>\r\n");
                    writer.write("    <capital_account_xml>" + mortgage.getCapitalAccount().getXmlFile() + "</capital_account_xml>\r\n");
                    writer.write("    <capital_account_html>" + mortgage.getCapitalAccount().getHtmlFile() + "</capital_account_html>\r\n");
                }
                if(mortgage.getIntrestAccount()!=null){
                    writer.write("    <intrest_account_name>" + mortgage.getIntrestAccount() + "</intrest_account_name>\r\n");
                    writer.write("    <intrest_account_xml>" + mortgage.getIntrestAccount().getXmlFile() + "</intrest_account_xml>\r\n");
                    writer.write("    <intrest_account_html>" + mortgage.getIntrestAccount().getHtmlFile() + "</intrest_account_html>\r\n");
                }
                writer.write("  </Mortgage>\r\n");
            }
            writer.write("</Mortgages>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Mortgage mortgage:mortgages.getBusinessObjects()){
//            TODO: add isSavedXML
//            if(journal.isSavedXML()){
            writeMortgage(mortgage);
//            }
        }
    }
    //
    private static void writeMortgage(Mortgage mortgage) {
        System.out.println("Mortgages.TOXML(" + mortgage.toString() + ")");
        try {
            Writer writer = new FileWriter(mortgage.getXmlFile());

            writer.write(mortgage.getXmlHeader());

            writer.write("<Mortgage>\r\n");
            writer.write("  <name>" + mortgage.toString() + "</name>\r\n");
            int teller = 1;
            for(Vector<BigDecimal> vector : mortgage.getTable()) {
                writer.write("  <line>\r\n" + "    <nr>" + teller + "</nr>\r\n" + "    <mensuality>"
                        + vector.get(0) + "</mensuality>\r\n" + "    <intrest>" + vector.get(1) + "</intrest>\r\n"
                        + "    <capital>" + vector.get(2) + "</capital>\r\n" + "    <restCapital>" + vector.get(3)
                        + "</restCapital>\r\n  </line>\r\n");
                teller++;
            }
            writer.write("</Mortgage>");
            writer.flush();
            writer.close();
            // setSaved(true);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

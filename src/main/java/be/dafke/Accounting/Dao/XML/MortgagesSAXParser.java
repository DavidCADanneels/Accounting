package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Mortgage;
import be.dafke.Accounting.Objects.Accounting.Mortgages;
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
    public static void readMortgages(Accounting accounting){
        try {
            File file = accounting.getMortgages().getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();


            String xmlFile = doc.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            String htmlFile = doc.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            Mortgages mortgages = accounting.getMortgages();
            mortgages.setXmlFile(new File(xmlFile));
            mortgages.setHtmlFile(new File(htmlFile));

            Element mortgagesElement = (Element)doc.getElementsByTagName("Mortgages").item(0);
            NodeList mortgagesNode = mortgagesElement.getElementsByTagName("Mortgage");
            for (int i = 0; i < mortgagesNode.getLength(); i++) {
                Element element = (Element)mortgagesNode.item(i);

                String name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
                htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
                String total = element.getElementsByTagName("total").item(0).getChildNodes().item(0).getNodeValue();
                String nrPayed = element.getElementsByTagName("nrPayed").item(0).getChildNodes().item(0).getNodeValue();
                String capitalName = element.getElementsByTagName("capital_account_name").item(0).getChildNodes().item(0).getNodeValue();
//                String capitalXml = element.getElementsByTagName("capital_account_xml").item(0).getChildNodes().item(0).getNodeValue();
//                String capitalHtml = element.getElementsByTagName("capital_account_html").item(0).getChildNodes().item(0).getNodeValue();
                String intrestName = element.getElementsByTagName("intrest_account_name").item(0).getChildNodes().item(0).getNodeValue();
//                String intrestXml = element.getElementsByTagName("intrest_account_xml").item(0).getChildNodes().item(0).getNodeValue();
//                String intrestHtml = element.getElementsByTagName("intrest_account_html").item(0).getChildNodes().item(0).getNodeValue();

                BigDecimal amount = new BigDecimal(total);
                Mortgage mortgage = new Mortgage(name, amount);
                mortgage.setXmlFile(new File(xmlFile));
                mortgage.setHtmlFile(new File(htmlFile));
                int nr = Integer.valueOf(nrPayed);
                mortgage.setPayed(nr);
                Account capital = accounting.getAccounts().get(capitalName);
                mortgage.setCapitalAccount(capital);
                Account intrest = accounting.getAccounts().get(intrestName);
                mortgage.setIntrestAccount(intrest);
                accounting.getMortgages().addMortgageTable(name, mortgage);
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
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Mortgages SYSTEM \""
                    + mortgages.getDtdFile().getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + mortgages.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n" + "<Mortgages>\r\n");
            writer.write("  <xml>" + mortgages.getXmlFile() + "</xml>\r\n");
            writer.write("  <html>" + mortgages.getHtmlFile() + "</html>\r\n");
            for(Mortgage mortgage : mortgages.getMortgages()) {
                writer.write("  <Mortgage>\r\n");
                writer.write("    <name>" + mortgage.toString() + "</name>\r\n");
                writer.write("    <xml>" + mortgage.getXmlFile() + "</xml>\r\n");
                writer.write("    <html>" + mortgage.getHtmlFile() + "</html>\r\n");
                writer.write("    <total>" + mortgage.getStartCapital() + "</total>\r\n");
                writer.write("    <nrPayed>" + mortgage.getNrPayed() + "</nrPayed>\r\n");
                writer.write("    <capital_account_name>" + mortgage.getCapitalAccount() + "</capital_account_name>\r\n");
                writer.write("    <capital_account_xml>" + mortgage.getCapitalAccount().getXmlFile() + "</capital_account_xml>\r\n");
                writer.write("    <capital_account_html>" + mortgage.getCapitalAccount().getHtmlFile() + "</capital_account_html>\r\n");
                writer.write("    <intrest_account_name>" + mortgage.getIntrestAccount() + "</intrest_account_name>\r\n");
                writer.write("    <intrest_account_xml>" + mortgage.getIntrestAccount().getXmlFile() + "</intrest_account_xml>\r\n");
                writer.write("    <intrest_account_html>" + mortgage.getIntrestAccount().getHtmlFile() + "</intrest_account_html>\r\n");
                writer.write("  </Mortgage>\r\n");
            }
            writer.write("</Mortgages>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
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
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

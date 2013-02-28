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
import java.io.IOException;
import java.math.BigDecimal;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:07
 */
public class MortgagesSAXParser {
    public static void readMortgages(Accounting accounting, File accountingFile){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(accountingFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node mortgagesNode = doc.getElementsByTagName("Mortgages").item(0);

            String xmlLocation = doc.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            accounting.getMortgages().setFolder(xmlLocation);

            mortgagesFromXML(accounting, (Element) mortgagesNode);

        } catch (IOException io) {
            io.printStackTrace();
            FileSystemView.getFileSystemView().createFileObject("Mortgages.xml");
            System.out.println(accountingFile.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}

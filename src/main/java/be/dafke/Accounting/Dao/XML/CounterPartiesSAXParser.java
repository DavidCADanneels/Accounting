package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Coda.BankAccount;
import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;
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
 * Time: 5:09
 */
public class CounterPartiesSAXParser {
    public static void readCounterparties(CounterParties counterParties, Accounts accounts, File bankingFile){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(bankingFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node counterpartiesNode = doc.getElementsByTagName("Counterparties").item(0);
//            String xslLocation = counterpartiesNode.getAttributes().getNamedItem("xsl").getNodeValue();
//            accounting.setLocationXSL(new File(xslLocation));

            String xmlLocation = doc.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            counterParties.setFolder(xmlLocation);

            counterpartiesFromXML(counterParties, accounts, (Element) counterpartiesNode);

        } catch (IOException io) {
            io.printStackTrace();
            FileSystemView.getFileSystemView().createFileObject("Banking.xml");
            System.out.println(bankingFile.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void counterpartiesFromXML(CounterParties counterParties, Accounts accounts, Element counterpartiesElement) {
        NodeList counterparties = counterpartiesElement.getElementsByTagName("Counterparty");
        for (int i = 0; i < counterparties.getLength(); i++) {
            Element element = (Element)counterparties.item(i);
            String counterparty_name = element.getAttribute("name");
            CounterParty counterParty = new CounterParty(counterparty_name);

            NodeList accountNodeList = element.getElementsByTagName("AccountName");
            if(accountNodeList.getLength()>0){
                String accountName = accountNodeList.item(0).getChildNodes().item(0).getNodeValue();
                Account account = accounts.get(accountName);
                counterParty.setAccount(account);
            }
            NodeList aliasNodeList = element.getElementsByTagName("Alias");
            for(int j=0;j<aliasNodeList.getLength();j++){
                String alias = aliasNodeList.item(j).getChildNodes().item(0).getNodeValue();
                counterParty.addAlias(alias);
            }
            NodeList bankAccountNodeList = element.getElementsByTagName("BankAccount");
            for(int j=0;j<bankAccountNodeList.getLength();j++){
                String accountName = bankAccountNodeList.item(j).getChildNodes().item(0).getNodeValue();
                BankAccount bankAccount = new BankAccount(accountName);
                counterParty.addAccount(bankAccount);
                NodeList bicNodeList = element.getElementsByTagName("BIC");
                if(bicNodeList.getLength()>0){
                    String bic = bicNodeList.item(0).getChildNodes().item(0).getNodeValue();
                    bankAccount.setBic(bic);
                }
                NodeList currencyNodeList = element.getElementsByTagName("Currency");
                if(currencyNodeList.getLength()>0 && currencyNodeList.item(0).getChildNodes() != null
                        && currencyNodeList.item(0).getChildNodes().getLength()>0){
                    String currency = currencyNodeList.item(0).getChildNodes().item(0).getNodeValue();
                    bankAccount.setCurrency(currency);
                }
            }
            counterParties.addCounterParty(counterParty);
        }
    }
}

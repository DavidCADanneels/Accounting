package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.BankAccount;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:09
 */
public class CounterPartiesSAXParser {
    // READ
    //
    public static void readCounterparties(CounterParties counterParties, Accounts accounts){
        try {
            File file = counterParties.getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Element rootElement = (Element) doc.getElementsByTagName("CounterParties").item(0);
            NodeList nodeList = rootElement.getElementsByTagName("CounterParty");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);
                String counterparty_name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
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

        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    public static void writeCounterparties(CounterParties counterParties) {
        try {
            Writer writer = new FileWriter(counterParties.getXmlFile());

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE CounterParties SYSTEM \"" + counterParties.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + counterParties.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<CounterParties>\r\n");
            for(CounterParty counterParty : counterParties.getCounterParties()) {
                writer.write("  <CounterParty>\r\n");
                writer.write("    <name>" + counterParty.getName()+"</name>\r\n");
                if(counterParty.getAliases()!=null){
                    for(String alias : counterParty.getAliases()){
                        writer.write("    <Alias>"+alias+"</Alias>\r\n");
                    }
                }
                if(counterParty.getAccount()!=null){
                    writer.write("    <AccountName>" + counterParty.getAccount().getName() + "</AccountName>\r\n");
                    writer.write("    <AccountXml>" + counterParty.getAccount().getXmlFile() + "</AccountXml>\r\n");
                    writer.write("    <AccountHtml>" + counterParty.getAccount().getHtmlFile() + "</AccountHtml>\r\n");
                }
                if(counterParty.getBankAccounts()!=null){
                    for(BankAccount account : counterParty.getBankAccounts().values()) {
                        if(account.getAccountNumber()!=null){
                            writer.write("    <BankAccount>" + account.getAccountNumber() + "</BankAccount>\r\n");
                        }
                        if(account.getBic()!=null){
                            writer.write("    <BIC>" + account.getBic() + "</BIC>\r\n");
                        }
                        if(account.getCurrency()!=null){
                            writer.write("    <Currency>" + account.getCurrency() + "</Currency>\r\n");
                        }
                    }
                }
                writer.write("  </CounterParty>\r\n");
            }
            writer.write("</CounterParties>\r\n");
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

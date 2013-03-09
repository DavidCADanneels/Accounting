package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.BankAccount;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.CounterParty;
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
                CounterParty counterParty = new CounterParty();
                counterParty.setName(counterparty_name);

                String accountName = Utils.getValue(element, "AccountName");
                if(accountName!=null){
                    Account account = accounts.getBusinessObject(accountName);
                    counterParty.setAccount(account);
                }
                for(String alias: Utils.getValues(element, "Alias")){
                    counterParty.addAlias(alias);
                }
                NodeList bankAccountNodeList = element.getElementsByTagName("BankAccount");
                for(int j=0;j<bankAccountNodeList.getLength();j++){
                    Element bankAccountElement = (Element)bankAccountNodeList.item(j);
                    String accountNumber = Utils.getValue(bankAccountElement,"AccountNumber");
                    String bic = Utils.getValue(bankAccountElement, "BIC");
                    String currency = Utils.getValue(bankAccountElement, "Currency");
                    BankAccount bankAccount = new BankAccount(accountNumber);
                    bankAccount.setBic(bic);
                    bankAccount.setCurrency(currency);
                    counterParty.addAccount(bankAccount);
                }
                try {
                    counterParties.addBusinessObject(counterParty);
                } catch (EmptyNameException e) {
                    System.err.println("The Name of the CounterParty cannot be empty");
                } catch (DuplicateNameException e) {
                    System.err.println("The Name of the CounterParty already exists");
                }
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

            writer.write(counterParties.getXmlHeader());

            writer.write("<CounterParties>\r\n");
            for(CounterParty counterParty : counterParties.getBusinessObjects()) {
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
                    if(counterParty.getAccount().getHtmlFile()!=null){
                        writer.write("    <AccountHtml>" + counterParty.getAccount().getHtmlFile() + "</AccountHtml>\r\n");
                    }
                }
                if(counterParty.getBankAccounts()!=null){
                    for(BankAccount account : counterParty.getBankAccounts().values()) {
                        writer.write("    <BankAccount>\r\n");
                        if(account.getAccountNumber()!=null){
                            writer.write("      <AccountNumber>" + account.getAccountNumber() + "</AccountNumber>\r\n");
                        }
                        if(account.getBic()!=null && !account.getBic().equals("")){
                            writer.write("      <BIC>" + account.getBic() + "</BIC>\r\n");
                        }
                        if(account.getCurrency()!=null && !account.getCurrency().equals("")){
                            writer.write("      <Currency>" + account.getCurrency() + "</Currency>\r\n");
                        }
                        writer.write("    </BankAccount>\r\n");
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

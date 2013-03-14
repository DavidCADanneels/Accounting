package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Balance;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Mortgage;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;
import be.dafke.Accounting.Objects.WriteableBusinessObject;

import java.io.FileWriter;
import java.io.Writer;

/**
 * User: Dafke
 * Date: 20/01/13
 * Time: 10:22
 */
public class AccountingsSAXParser {

    public static void writeAccountings(Accountings accountings) {
        accountings.createDefaultValuesIfNull();
        accountings.writeCollection();
        for(Accounting accounting : accountings.getBusinessObjects()) {
            writeAccountingFile(accounting);

            writeAccounting(accounting);
        }

        toHtml(accountings);
    }

    private static void writeAccounting(Accounting accounting){
        System.out.println("Accounts.TOXML(" + accounting.toString() + ")");
        accounting.getAccounts().writeCollection();
        for(Account account : accounting.getAccounts().getBusinessObjects()){
            AccountsSAXParser.writeAccount(account);
        }

        System.out.println("Balances.TOXML(" + accounting.toString() + ")");
        accounting.getBalances().writeCollection();
        for(Balance balance : accounting.getBalances().getBusinessObjects()){
            BalancesSAXParser.writeBalance(balance);
        }

        System.out.println("Journals.TOXML(" + accounting.toString() + ")");
        accounting.getJournals().writeCollection();
        for(Journal journal : accounting.getJournals().getBusinessObjects()){
            JournalsSAXParser.writeJournal(journal);
        }

        System.out.println("Mortgages.TOXML(" + accounting.toString() + ")");
        accounting.getMortgages().writeCollection();
        for(Mortgage mortgage:accounting.getMortgages().getBusinessObjects()){
            MortgagesSAXParser.writeMortgage(mortgage);
        }

        System.out.println("Counterparties.TOXML(" + accounting.toString() + ")");
        accounting.getCounterParties().writeCollection();

        System.out.println("Statements.TOXML(" + accounting.toString() + ")");
        accounting.getStatements().writeCollection();
    }

    private static void writeAccountingFile(Accounting accounting) {
        try {
            Writer writer = new FileWriter(accounting.getXmlFile());
            writer.write(accounting.getXmlHeader());
            writer.write("<Accounting>\r\n");
            writer.write("  <name>" + accounting.getName() + "</name>\r\n");
            for(String key:accounting.getKeys()) {
                WriteableBusinessCollection<WriteableBusinessObject> writeableBusinessCollection = accounting.getBusinessObject(key);
                System.out.println("writing: " + key);
                writer.write("  <" + key + ">\r\n");
                writer.write("    <name>" + key + "</name>\r\n");
                writer.write("    <xml>" + writeableBusinessCollection.getXmlFile() + "</xml>\r\n");
                if(writeableBusinessCollection.getHtmlFile()!=null){
                    writer.write("    <html>" + writeableBusinessCollection.getHtmlFile() + "</html>\r\n");
                }
                writer.write("  </" + key + ">\r\n");
            }
            writer.write("</Accounting>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void toHtml(Accountings accountings){
        accountings.xmlToHtml();
        for(Accounting accounting:accountings.getBusinessObjects()){
            if(accounting.getHtmlFolder()!=null && !accounting.getHtmlFolder().getPath().equals("null")){
                toHtml(accounting);
            }
        }
    }

    private static void toHtml(Accounting accounting){
        if(accounting.getHtmlFolder() != null){

            accounting.xmlToHtml();

            for(String key : accounting.getKeys()){
                WriteableBusinessCollection<WriteableBusinessObject> collection = accounting.getBusinessObject(key);
                collection.xmlToHtml();
                if(collection.getHtmlFolder()!=null){
                    for(WriteableBusinessObject writeableBusinessObject : collection.getBusinessObjects()){
//                        TODO: add isSavedHTML
//                        if(writeableBusinessObject.isSavedHTML()){
                            writeableBusinessObject.xmlToHtml();
//                        }
                    }
                }
            }
        }
    }
}

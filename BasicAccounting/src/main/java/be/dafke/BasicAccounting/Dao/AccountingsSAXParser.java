package be.dafke.BasicAccounting.Dao;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Balance;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.ObjectModel.WriteableBusinessCollection;
import be.dafke.ObjectModel.WriteableBusinessObject;

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
//            accounting.writeCollection();

            for(Account account : accounting.getAccounts().getBusinessObjects()){
                AccountsSAXParser.writeAccount(account);
            }
            for(Balance balance : accounting.getBalances().getBusinessObjects()){
                BalancesSAXParser.writeBalance(balance);
            }

            for(Journal journal : accounting.getJournals().getBusinessObjects()){
                JournalsSAXParser.writeJournal(journal);
            }
            for(Mortgage mortgage:accounting.getMortgages().getBusinessObjects()){
                MortgagesSAXParser.writeMortgage(mortgage);
            }
        }

        toHtml(accountings);
    }

    private static void toHtml(Accountings accountings){
        accountings.xmlToHtml();
        for(Accounting accounting:accountings.getBusinessObjects()){
            if(accounting.getHtmlFolder()!=null && !accounting.getHtmlFolder().getPath().equals("null")){
                accounting.xmlToHtml();

                for(WriteableBusinessCollection<WriteableBusinessObject> collection : accounting.getBusinessObjects()){
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
}

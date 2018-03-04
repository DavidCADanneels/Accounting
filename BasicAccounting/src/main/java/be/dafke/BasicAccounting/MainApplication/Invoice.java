package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetails;
import be.dafke.BasicAccounting.Accounts.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.Accounts.AccountSelector;
import be.dafke.BasicAccounting.Accounts.AccountsMenu;
import be.dafke.BasicAccounting.Accounts.AccountsTable.AccountsTableGUI;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.Balances.BalancesMenu;
import be.dafke.BasicAccounting.Balances.TestBalance;
import be.dafke.BasicAccounting.Coda.CodaMenu;
import be.dafke.BasicAccounting.Contacts.ContactSelector;
import be.dafke.BasicAccounting.Contacts.ContactsGUI;
import be.dafke.BasicAccounting.Contacts.ContactsMenu;
import be.dafke.BasicAccounting.Journals.*;
import be.dafke.BasicAccounting.Mortgages.MorgagesMenu;
import be.dafke.BasicAccounting.Mortgages.MortgageGUI;
import be.dafke.BasicAccounting.Mortgages.MortgagesGUI;
import be.dafke.BasicAccounting.Projects.ProjectsMenu;
import be.dafke.BasicAccounting.VAT.VATFieldsGUI;
import be.dafke.BasicAccounting.VAT.VATMenu;
import be.dafke.BasicAccounting.VAT.VATTransactionsGUI;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.PDFCreator;
import be.dafke.BusinessModelDao.XMLReader;
import be.dafke.BusinessModelDao.XMLWriter;
import org.apache.fop.apps.FOPException;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.JSplitPane.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Invoice {
    public static void main(String[] args) {
        String xslFile = "data/accounting/xsl/goods.xsl";
        String xmlFile = "data/accounting/xml/invoice.xml";
        String pdfFile = "data/accounting/xml/goods.pdf";
        createInvoice(xmlFile, xslFile, pdfFile);
    }


    public static void createInvoice(String xmlFile, String xslFile, String pdfFile){
        PDFCreator creator = new PDFCreator();
        try {
            creator.convertToPDF(xmlFile, xslFile, pdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FOPException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
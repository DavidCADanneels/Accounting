package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModelDao.ObjectModelSAXParser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * User: Dafke
 * Date: 26/02/13
 * Time: 6:36
 */
public class AccountingActionListener extends WindowAdapter implements ActionListener {

    public static final String SAVE_ALL = "SaveAll";
    protected final Accountings accountings;
    public static final String MAIN = "MainPanel";
    public static final String JOURNAL_MANAGEMENT = "JournalManagement";
    public static final String NEW_ACCOUNTING = "NewAccounting";
    public static final String OPEN_ACCOUNTING = "OpenAccounting";

    public AccountingActionListener(Accountings accountings){
        this.accountings = accountings;
    }

    @Override
    public void windowClosing(WindowEvent we) {
        saveData();
        ComponentMap.closeAllFrames();
    }

    public void saveData(){
        File xmlFolder = accountings.getXmlFolder();
        xmlFolder.mkdirs();
        ObjectModelSAXParser.writeCollection(accountings, xmlFolder, 0);

        // TODO: check this location
        File xslFolder = new File("BasicAccounting/src/main/resources/xsl");
        File htmlFolder = accountings.getHtmlFolder();
        htmlFolder.mkdirs();

        ObjectModelSAXParser.toHtml(accountings, xmlFolder, xslFolder, htmlFolder);

        // TODO: remove this by refactoring Extension and write methods
        for(Accounting accounting : accountings.getBusinessObjects()){
            for(AccountingExtension extension: accounting.getExtensions()){
                File rootFolder = new File(accountings.getXmlFolder(), "Accountings");
                File subFolder = new File(rootFolder, accounting.getName());
                extension.extendWriteCollection(accounting, subFolder);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if(actionCommand.equals(SAVE_ALL)){
            saveData();
        } else if (actionCommand.equals(NEW_ACCOUNTING)) {
            String name = JOptionPane.showInputDialog(null, "Enter a name");
            try {
                Accounting accounting = new Accounting();
                accounting.setName(name);
                accountings.addBusinessObject(accounting);
                accountings.setCurrentObject(name);
                JOptionPane.showMessageDialog(null, "Please create a Journal.");
                String key = accounting.toString()+ JOURNAL_MANAGEMENT;
                ComponentMap.getDisposableComponent(key).setVisible(true);
            } catch (DuplicateNameException e) {
                JOptionPane.showMessageDialog(null, "There is already an accounting with the name \""+name+"\".\r\n"+
                        "Please provide a new name.");
            } catch (EmptyNameException e) {
                JOptionPane.showMessageDialog(null, "The name cannot be empty.\r\nPlease provide a new name.");
            }
            ComponentMap.refreshAllFrames();
        } else if(actionCommand.startsWith(OPEN_ACCOUNTING)){
            String accountingName = actionCommand.replaceAll(OPEN_ACCOUNTING, "");
            accountings.setCurrentObject(accountingName);
            ComponentMap.refreshAllFrames();
        }
    }
}

package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Journals.Management.JournalManagementGUI
import be.dafke.Accounting.BasicAccounting.Journals.Management.JournalTypeManagementGUI
import be.dafke.Accounting.BasicAccounting.Journals.New.NewJournalDialog
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalDetailsGUI
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.JournalsIO
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class JournalsMenu extends JMenu {
    JMenuItem add, manage, types, generatePdf

    JournalsMenu() {
        super(getBundle("Accounting").getString("JOURNALS"))
//        this.journalEditPanel = journalEditPanel
//        setMnemonic(KeyEvent.VK_P)
        add = new JMenuItem(getBundle("Accounting").getString("ADD_JOURNAL"))
        add.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            NewJournalDialog newJournalDialog = new NewJournalDialog(Session.activeAccounting.accounts, Session.activeAccounting.journals, Session.activeAccounting.journalTypes, Session.activeAccounting.accountTypes)
            newJournalDialog.setLocation(locationOnScreen)
            newJournalDialog.visible = true
        })
        add.enabled = false

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNALS"))
        manage.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            JournalManagementGUI journalManagementGUI = JournalManagementGUI.getInstance(Session.activeAccounting.accounts, Session.activeAccounting.journals, Session.activeAccounting.journalTypes, Session.activeAccounting.accountTypes)
            journalManagementGUI.setLocation(locationOnScreen)
            journalManagementGUI.visible = true
        })
        manage.enabled = false

        types = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"))
        types.addActionListener({ e ->
            Point locationOnScreen = getLocationOnScreen()
            JournalTypeManagementGUI journalTypeManagementGUI = JournalTypeManagementGUI.getInstance(Session.activeAccounting.accounts, Session.activeAccounting.journalTypes, Session.activeAccounting.accountTypes)
            journalTypeManagementGUI.setLocation(locationOnScreen)
            journalTypeManagementGUI.visible = true
        })
        types.enabled = false

        generatePdf = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"))
        generatePdf.addActionListener({ e -> JournalsIO.writeJournalPdfFiles(Session.activeAccounting) })
        generatePdf.enabled = false

        add(add)
        add(manage)
        add(types)
        add(generatePdf)
    }

    void refresh() {
        boolean enabledButton = Session.activeAccounting.journals!=null
        add.enabled = enabledButton
        manage.enabled = enabledButton
        types.enabled = enabledButton
        generatePdf.enabled = enabledButton
        fireJournalDataChanged()
    }

    void fireJournalDataChanged(){
        removeAll()
        add(add)
        add(manage)
        add(types)
        add(generatePdf)
        if(Session.activeAccounting.journals){
            addSeparator()
            Session.activeAccounting.journals.businessObjects.stream()
                    .forEach({ journal ->
                        JMenuItem details = new JMenuItem(journal.name)
                        details.addActionListener({ e ->
                            Point locationOnScreen = getLocationOnScreen()
                            JournalDetailsGUI.getJournalDetails locationOnScreen, journal
                        })
                        add(details)
                    })
            addSeparator()
            JMenuItem master = new JMenuItem("Master")
            // FIXME: create other viewer for Master Transactions (no colors, what about ID's? use "MA1 (DIV1)" or just "DIV1" (or "MA1")
            // (or use BusinessCollection<Transaction> iso Journal in JournalDetailsGUI)
            master.addActionListener({ e ->
                Point locationOnScreen = getLocationOnScreen()
                JournalDetailsGUI.getJournalDetails locationOnScreen, Session.activeAccounting.transactions
            })
            add(master)
        }
    }
}

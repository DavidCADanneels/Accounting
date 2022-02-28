package be.dafke.Accounting.BasicAccounting.VAT

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Utils.Utils

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class VATFieldsGUI extends JFrame {
//    static VATFieldsGUI gui1 = null
    final VATFieldsPanel vatFieldsPanel

    static VATFieldsGUI getInstance() {
        Integer[] quartals = [ 1,2,3,4]
        int nr = JOptionPane.showOptionDialog(null, "Select Quartal", "Quartal",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, quartals, null)
        List<Transaction> transactions = null
        Transactions allTransactions = Session.activeAccounting.transactions
//        allTransactions.getBusinessObjects().each {it -> println("date: ${Utils.toString(it.date)}")}
//        allTransactions.getBusinessObjects().each {it -> println("date: ${Utils.toString(it.date)}")}
        if (nr == 0) {
            transactions = Session.activeAccounting.transactions.getBusinessObjects { it ->
                Utils.toString(it.date).contains('/1/') ||
                Utils.toString(it.date).contains('/2/') ||
                Utils.toString(it.date).contains('/3/')
            }
        } else if (nr == 1) {
            transactions = Session.activeAccounting.transactions.getBusinessObjects { it ->
                Utils.toString(it.date).contains('/4/') ||
                        Utils.toString(it.date).contains('/5/') ||
                        Utils.toString(it.date).contains('/6/')
            }
        } else if (nr == 2) {
            transactions = Session.activeAccounting.transactions.getBusinessObjects { it ->
                Utils.toString(it.date).contains('/7/') ||
                        Utils.toString(it.date).contains('/8/') ||
                        Utils.toString(it.date).contains('/9/')
            }
        } else if (nr == 3) {
            transactions = Session.activeAccounting.transactions.getBusinessObjects { it ->
                Utils.toString(it.date).contains('/10/') ||
                        Utils.toString(it.date).contains('/11/') ||
                        Utils.toString(it.date).contains('/12/')
            }
        }
        transactions.each {it -> println("date: ${Utils.toString(it.date)}")}
        VATFields vatFields = VATTransactions.getVatSummary(transactions)
        VATFieldsGUI gui1 = new VATFieldsGUI(vatFields)
        Main.addFrame(gui1)
        gui1

    }

    VATFieldsGUI(VATFields vatFields) {
        super(getBundle("VAT").getString("VAT_FIELDS"))
        vatFieldsPanel = new VATFieldsPanel(vatFields)
        setContentPane(vatFieldsPanel)
        pack()
    }
}

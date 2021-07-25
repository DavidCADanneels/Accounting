package be.dafke.Accounting.BasicAccounting.VAT

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class VATFieldsGUI extends JFrame {
    static VATFieldsGUI gui1 = null
    final VATFieldsPanel vatFieldsPanel

    static VATFieldsGUI getInstance() {
        if(gui1==null){
            gui1 = new VATFieldsGUI(null)
            Main.addFrame(gui1)
        }
        gui1
    }

    VATFieldsGUI(List<Transaction> transactions) {
        super(getBundle("VAT").getString("VAT_FIELDS"))
        vatFieldsPanel = new VATFieldsPanel(transactions)
        setContentPane(vatFieldsPanel)
        pack()
    }
}

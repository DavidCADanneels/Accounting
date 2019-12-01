package be.dafke.Accounting.BasicAccounting.VAT

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class VATFieldsGUI extends JFrame {
    static final HashMap<VATFields, VATFieldsGUI> vatGuis = new HashMap<>()
    final VATFieldsPanel vatFieldsPanel

    static VATFieldsGUI getInstance(VATFields vatFields, Accounting accounting) {
        VATFieldsGUI gui
        gui = vatGuis.get(vatFields)
        if(gui==null){
            gui = new VATFieldsGUI(vatFields, accounting, null)
            vatGuis.put(vatFields,gui)
            Main.addFrame(gui)
        }
        gui
    }

    static VATFieldsGUI getInstance(List<Transaction> transactions, Accounting accounting) {
        VATFieldsGUI gui
        VATFields vatFields = new VATFields()
        vatFields.addDefaultFields()
        for(Transaction transaction : transactions) {
            ArrayList<VATBooking> vatBookings = transaction.vatBookings
            for (VATBooking vatBooking : vatBookings) {
                VATField originalVatField = vatBooking.vatField
                String name = originalVatField.name
                VATField newVatField = vatFields.getBusinessObject(name)
                VATMovement vatMovement = vatBooking.vatMovement
                newVatField.addBusinessObject(vatMovement)
            }
        }
        gui = vatGuis.get(vatFields)
        if(gui==null){
            gui = new VATFieldsGUI(vatFields, accounting, transactions)
            vatGuis.put(vatFields,gui)
            Main.addFrame(gui)
        }
        gui
    }

    VATFieldsGUI(VATFields vatFields, Accounting accounting, List<Transaction> transactions) {
        super(getBundle("VAT").getString("VAT_FIELDS"))
        vatFieldsPanel = new VATFieldsPanel(vatFields, accounting, transactions)
        setContentPane(vatFieldsPanel)
        updateVATFields()
        pack()
    }

    static void fireVATFieldsUpdated(){
        for(VATFieldsGUI vatFieldsGUI : vatGuis.values()) {
            if (vatFieldsGUI != null) {
                vatFieldsGUI.updateVATFields()
            }
        }
    }

    void updateVATFields(){
        vatFieldsPanel.updateVATFields()
    }
}

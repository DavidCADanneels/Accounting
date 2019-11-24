package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

public class VATFieldsGUI extends JFrame {
    private static final HashMap<VATFields, VATFieldsGUI> vatGuis = new HashMap<>();
    private final VATFieldsPanel vatFieldsPanel;

    public static VATFieldsGUI getInstance(VATFields vatFields, Accounting accounting) {
        VATFieldsGUI gui;
        gui = vatGuis.get(vatFields);
        if(gui==null){
            gui = new VATFieldsGUI(vatFields, accounting, null);
            vatGuis.put(vatFields,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static VATFieldsGUI getInstance(List<Transaction> transactions, Accounting accounting) {
        VATFieldsGUI gui;
        VATFields vatFields = new VATFields();
        vatFields.addDefaultFields();
        for(Transaction transaction : transactions) {
            ArrayList<VATBooking> vatBookings = transaction.getVatBookings();
            for (VATBooking vatBooking : vatBookings) {
                VATField originalVatField = vatBooking.getVatField();
                String name = originalVatField.getName();
                VATField newVatField = vatFields.getBusinessObject(name);
                VATMovement vatMovement = vatBooking.getVatMovement();
                newVatField.addBusinessObject(vatMovement);
            }
        }
        gui = vatGuis.get(vatFields);
        if(gui==null){
            gui = new VATFieldsGUI(vatFields, accounting, transactions);
            vatGuis.put(vatFields,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    private VATFieldsGUI(VATFields vatFields, Accounting accounting, List<Transaction> transactions) {
        super(getBundle("VAT").getString("VAT_FIELDS"));
        vatFieldsPanel = new VATFieldsPanel(vatFields, accounting, transactions);
        setContentPane(vatFieldsPanel);
        updateVATFields();
        pack();
    }

    public static void fireVATFieldsUpdated(){
        for(VATFieldsGUI vatFieldsGUI : vatGuis.values()) {
            if (vatFieldsGUI != null) {
                vatFieldsGUI.updateVATFields();
            }
        }
    }

    public void updateVATFields(){
        vatFieldsPanel.updateVATFields();
    }
}

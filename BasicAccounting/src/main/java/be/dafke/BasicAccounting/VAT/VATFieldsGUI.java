package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATField;
import be.dafke.BusinessModel.VATFields;
import be.dafke.BusinessModel.VATMovement;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.BusinessModel.VATTransactions;

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

    public static VATFieldsGUI getInstance(List<VATTransaction> selectedVatTransactions, Accounting accounting) {
        VATFieldsGUI gui;
        VATFields vatFields = new VATFields();
        vatFields.addDefaultFields();
        VATTransactions vatTransactions = new VATTransactions(vatFields);
        for(VATTransaction vatTransaction : selectedVatTransactions){
//        selectedVatTransactions.forEach(vatTransaction -> {
            VATTransaction newVatTransaction = new VATTransaction();
            ArrayList<VATBooking> vatBookings = vatTransaction.getBusinessObjects();
            vatBookings.forEach(vatBooking -> {
                VATField originalVatField = vatBooking.getVatField();
                String name = originalVatField.getName();
                VATField newVatField = vatFields.getBusinessObject(name);
                VATMovement originalVatMovement = vatBooking.getVatMovement();
//                VATMovement newVatMovement = new VATMovement(originalVatMovement.getAmount());
                VATBooking newBooking = new VATBooking(newVatField, originalVatMovement);
                newVatTransaction.addBusinessObject(newBooking);
            });
            vatTransactions.addBusinessObject(newVatTransaction);
        }
//        );
        gui = vatGuis.get(vatFields);
        if(gui==null){
            gui = new VATFieldsGUI(vatFields, accounting, selectedVatTransactions);
            vatGuis.put(vatFields,gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    private VATFieldsGUI(VATFields vatFields, Accounting accounting, List<VATTransaction> selectedVatTransactions) {
        super(getBundle("VAT").getString("VAT_FIELDS"));
        vatFieldsPanel = new VATFieldsPanel(vatFields, accounting, selectedVatTransactions);
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

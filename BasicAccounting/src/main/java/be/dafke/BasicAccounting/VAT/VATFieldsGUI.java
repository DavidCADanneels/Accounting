package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.VATBooking;
import be.dafke.BusinessModel.VATField;
import be.dafke.BusinessModel.VATFields;
import be.dafke.BusinessModel.VATMovement;
import be.dafke.BusinessModel.VATTransaction;
import be.dafke.BusinessModel.VATTransactions;
import be.dafke.BusinessModelDao.VATWriter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static be.dafke.BusinessModelDao.VATWriter.Period.QUARTER;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.BoxLayout.Y_AXIS;

/**
 * Created by ddanneels on 28/12/2016.
 */
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

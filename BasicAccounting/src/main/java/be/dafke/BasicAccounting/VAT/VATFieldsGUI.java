package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.Contacts.ContactSelector;
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
    public static final String CREATE_FILE = "Create file";
    public static final String SALES_AT_0 = "Sales at 0%";
    public static final String SALES_AT_6 = "Sales at 6%";
//    public static final String SALES_AT_12 = "Sales at 12%";
    public static final String SALES_AT_21 = "Sales at 21%";
    public static final String TAX_ON_SALES_0_3 = "Tax on Sales (0-3)";
    public static final String CN_ON_SALES = "CN on Sales";
    public static final String TAX_ON_CN = "Tax on CN";
    public static final String PURCHASE_OF_SUPPLIES = "Purchase of supplies";
    public static final String PURCHASE_OF_SERVICES = "Purchase of services";
    public static final String PURCHASE_OF_INVESTMENTS = "Purchase of investments";
    public static final String TAX_ON_PURCHASES_81_83 = "Tax on Purchases (81-83)";
    public static final String CN_ON_PURCHASES = "CN on Purchases";
    private Accounting accounting;
    private HashMap<String,JTextField> textFields = new HashMap<>();
    List<VATTransaction> selectedVatTransactions;

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
            VATTransaction newVatTransaction = new VATTransaction(vatTransaction.getDate());
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

    private VATFields vatFields;

    private VATFieldsGUI(VATFields vatFields, Accounting accounting, List<VATTransaction> selectedVatTransactions) {
        super(getBundle("VAT").getString("VAT_FIELDS"));
        this.selectedVatTransactions = selectedVatTransactions;
        this.accounting = accounting;
        this.vatFields = vatFields;
        JPanel left = createSalesPanel();
        JPanel right = createPurchasePanel();
        JPanel totals = createTotalsPanel();
        JPanel buttonPanel = createButtonPanel();

        setContentPane(createContentPanel(left,right,totals, buttonPanel));

        updateVATFields();
        pack();
    }

    private JPanel createFieldPanel(String nr, String description){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel line1 = new JPanel();

        line1.add(new JLabel(nr));
        JTextField textField = new JTextField(10);
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textFields.put(nr,textField);
        line1.add(textField);
        textField.setToolTipText(description);

        JPanel line2 = new JPanel();
        line2.add(new JLabel(description));

        panel.add(line2);
        panel.add(line1);
        return panel;
    }

    public void updateVATFields(){
        for (String nr: textFields.keySet()){
            JTextField textField = textFields.get(nr);
            VATField vatField = vatFields.getBusinessObject(nr);
            BigDecimal amount = vatField.getSaldo();
            if (textField != null){
                if(amount != null) {
                    textField.setText(amount.toString());
                } else {
                    textField.setText("");
                }
            }
        }
    }

    public static void fireVATFieldsUpdated(){
        for(VATFieldsGUI vatFieldsGUI : vatGuis.values()) {
            if (vatFieldsGUI != null) {
                vatFieldsGUI.updateVATFields();
            }
        }
    }

    private JPanel createContentPanel(JPanel left, JPanel right, JPanel totals, JPanel buttonsPanel){
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content,Y_AXIS));
        content.add(left);
        content.add(right);
        content.add(totals);
        content.add(buttonsPanel);
        return content;
    }

    private JPanel createSalesPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Sales"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));

        JPanel left = createSalesMainPanel();
        JPanel right = createSalesTaxAndCNPanel();

        panel.add(left);
        panel.add(right);

        return panel;
    }

    private JPanel createSalesMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("0", SALES_AT_0));
        panel.add(createFieldPanel("1", SALES_AT_6));
//        panel.add(createFieldPanel("2", SALES_AT_12));
        panel.add(createFieldPanel("3", SALES_AT_21));
        return panel;
    }

    private JPanel createSalesTaxAndCNPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("54", TAX_ON_SALES_0_3));
        panel.add(createFieldPanel("49", CN_ON_SALES));
        panel.add(createFieldPanel("64", TAX_ON_CN));
        return panel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Purchases"));
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));

        JPanel left = createPurchaseMainPanel();
        JPanel right = createPurchaseTaxAndCNPanel();

        panel.add(left);
        panel.add(right);

        return panel;
    }

    private JPanel createPurchaseMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("81", PURCHASE_OF_SUPPLIES));
        panel.add(createFieldPanel("82", PURCHASE_OF_SERVICES));
        panel.add(createFieldPanel("83", PURCHASE_OF_INVESTMENTS));
        return panel;
    }

    private JPanel createPurchaseTaxAndCNPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("59", TAX_ON_PURCHASES_81_83));
        panel.add(createFieldPanel("85", CN_ON_PURCHASES));
        panel.add(createFieldPanel("63", TAX_ON_CN));
        return panel;
    }

    private JPanel createTotalsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Totals"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JPanel left = createTotalsLeftPanel();
        JPanel right = createTotalsRightPanel();

        panel.add(left);
        panel.add(right);

        return panel;
    }

    private JPanel createTotalsLeftPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("XX", "54+63"));
        panel.add(createFieldPanel("YY", "59+64"));
        return panel;
    }

    private JPanel createTotalsRightPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("71", "XX-YY"));
        panel.add(createFieldPanel("72", "YY-XX"));
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        JTextField year = new JTextField(6);
        JTextField nr = new JTextField(4);
        panel.add(new JLabel("Year:"));
        panel.add(year);
        panel.add(new JLabel("Month/Quarter:"));
        panel.add(nr);
        JButton button = new JButton(CREATE_FILE);
        button.addActionListener(e -> {
            Contact companyContact = accounting.getCompanyContact();
            if (companyContact == null) {
                ContactSelector contactSelector = ContactSelector.getContactSelector(accounting.getContacts());
                contactSelector.setVisible(true);
                companyContact = contactSelector.getSelection();
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                VATWriter.writeVATFields(vatFields, selectedFile, year.getText(), nr.getText(), companyContact, QUARTER);
                VATTransactions vatTransactions = accounting.getVatTransactions();
                vatTransactions.registerVATTransactions(selectedVatTransactions);
                Main.fireVATFieldsUpdated();
            }
        });
        panel.add(button);
        return panel;
    }
}

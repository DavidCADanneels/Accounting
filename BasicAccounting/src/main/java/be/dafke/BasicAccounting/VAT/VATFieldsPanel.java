package be.dafke.BasicAccounting.VAT;

import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.VATWriter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import static be.dafke.BusinessModelDao.VATWriter.Period.QUARTER;
import static javax.swing.BoxLayout.Y_AXIS;

public class VATFieldsPanel extends JPanel {
    public static final String CREATE_FILE = "Create file";
    public static final String SALES_AT_0 = "Sales at 0%";
    public static final String SALES_AT_6 = "Sales at 6%";
    public static final String SALES_AT_12 = "Sales at 12%";
    public static final String SALES_AT_21 = "Sales at 21%";
    public static final String TAX_ON_SALES_0_3 = "Tax on Sales (0-3)";
    public static final String CN_ON_SALES = "CN on Sales";
    public static final String TAX_ON_CN = "Tax on CN";
    public static final String TAX_ON_ICL = "Tax on ICL";
    public static final String SALES_ICL_TURNOVER = "Turnover ICL";
    public static final String PURCHASE_OF_SUPPLIES = "Purchase of supplies";
    public static final String PURCHASE_OF_SERVICES = "Purchase of services";
    public static final String PURCHASE_OF_INVESTMENTS = "Purchase of investments";
    public static final String PURCHASE_ICL = "Purchase ICL";
    public static final String TAX_ON_PURCHASES_81_83 = "Tax on Purchases (81-83)";
    public static final String CN_ON_PURCHASES = "CN on Purchases";
    private Accounting accounting;
    private VATFields vatFields;
    private HashMap<String,JTextField> textFields = new HashMap<>();
    List<VATTransaction> selectedVatTransactions;

    public VATFieldsPanel(VATFields vatFields, Accounting accounting, List<VATTransaction> selectedVatTransactions) {
        this.accounting = accounting;
        this.selectedVatTransactions = selectedVatTransactions;
        this.accounting = accounting;
        this.vatFields = vatFields;
        JPanel left = createSalesPanel();
        JPanel right = createPurchasePanel();
        JPanel totals = createTotalsPanel();
        JPanel buttonPanel = createButtonPanel();
        setLayout(new BoxLayout(this,Y_AXIS));
        add(left);
        add(right);
        add(totals);
        add(buttonPanel);

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
        panel.add(createFieldPanel("2", SALES_AT_12));
        panel.add(createFieldPanel("3", SALES_AT_21));
        return panel;
    }

    private JPanel createSalesTaxAndCNPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("54", TAX_ON_SALES_0_3));
        panel.add(createFieldPanel("49", CN_ON_SALES));
        panel.add(createFieldPanel("64", TAX_ON_CN));
        panel.add(createFieldPanel("46", SALES_ICL_TURNOVER));
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
        panel.add(createFieldPanel("86", PURCHASE_ICL));
        return panel;
    }

    private JPanel createPurchaseTaxAndCNPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.add(createFieldPanel("59", TAX_ON_PURCHASES_81_83));
        panel.add(createFieldPanel("85", CN_ON_PURCHASES));
        panel.add(createFieldPanel("63", TAX_ON_CN));
        panel.add(createFieldPanel("55", TAX_ON_ICL));
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
        panel.add(createFieldPanel("XX", "54+55+63"));
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
                // TODO: replace companyContact by Contact of type 'OWN'
                ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting.getContacts(), Contact.ContactType.ALL);
                contactSelectorDialog.setVisible(true);
                companyContact = contactSelectorDialog.getSelection();
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("XML_EXTENSION files", "xml"));
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

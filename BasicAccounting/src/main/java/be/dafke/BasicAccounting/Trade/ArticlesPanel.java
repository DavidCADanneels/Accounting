package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

public class ArticlesPanel extends JPanel {
    private final JButton add;
    private final SelectableTable<Article> table;
    private TableColumn supplierColumn;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private final ArticlesDetailsDataTableModel articlesDataTableModel;

    public ArticlesPanel(Articles articles, Contacts contacts) {
        this.contacts = contacts;
        articlesDataTableModel = new ArticlesDetailsDataTableModel(this, articles);
        table = new SelectableTable<>(articlesDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));

        comboBox = new JComboBox<>();
        fireSupplierAddedOrRemoved();
        supplierColumn = table.getColumnModel().getColumn(ArticlesDetailsDataTableModel.SUPPLIER_COL);
        supplierColumn.setCellEditor(new DefaultCellEditor(comboBox));

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        add = new JButton("Add Article");
        add(add, BorderLayout.NORTH);
        add.addActionListener( e -> {
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"));
            if (name != null) {
                try {
                    articles.addBusinessObject(new Article(name));
                    articlesDataTableModel.fireTableDataChanged();
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_NAME_EMPTY);
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_DUPLICATE_NAME, name.trim());
                }
            }
        });
    }

    public void fireSupplierAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(Contact::isSupplier).forEach(contact -> comboBox.addItem(contact));
        articlesDataTableModel.fireTableDataChanged();
    }
}
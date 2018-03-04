package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountsTable.AccountDataTableModel;
import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Articles;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class GoodsPanel extends JPanel {
    private final Articles articles;
    private final JButton add;
    private final SelectableTable<Article> table;
    private final GoodsDataTableModel goodsDataTableModel;

    public GoodsPanel(Articles articles) {
        this.articles = articles;
        goodsDataTableModel = new GoodsDataTableModel(articles);
        table = new SelectableTable<>(goodsDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        add = new JButton("Add Article");
        add(add, BorderLayout.NORTH);
        add.addActionListener( e -> {
            String name = JOptionPane.showInputDialog(getBundle("Accounting").getString("NAME_LABEL"));
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(getBundle("Accounting").getString("NAME_LABEL"));
            if (name != null) {
                try {
                    articles.addBusinessObject(new Article(name));
                    goodsDataTableModel.fireTableDataChanged();
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(ActionUtils.ARTICLE_NAME_EMPTY);
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(ActionUtils.ARTICLE_DUPLICATE_NAME, name.trim());
                }
            }
        });
    }
}
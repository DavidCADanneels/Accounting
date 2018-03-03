package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountsTable.AccountDataTableModel;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Articles;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

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
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        add = new JButton("Add Article");
        add(add, BorderLayout.NORTH);
        add.addActionListener( e -> {
            try {
                articles.addBusinessObject(new Article("New"));
                goodsDataTableModel.fireTableDataChanged();
            } catch (EmptyNameException e1) {
                e1.printStackTrace();
            } catch (DuplicateNameException e1) {
                e1.printStackTrace();
            }
        });
    }
}
package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.IngredientOrder
import be.dafke.Accounting.BusinessModel.IngredientOrderItem
import be.dafke.Accounting.BusinessModel.IngredientOrders
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.DefaultCellEditor
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.table.TableColumn
import java.awt.BorderLayout
import java.awt.Dimension

class IngredientOrderCreatePanel extends JPanel {
    IngredientOrder ingredientOrder
    Ingredients ingredients
    Articles articles
    JComboBox<Article> comboBox
    final IngredientOrderCreateDataTableModel ingredientOrderCreateDataTableModel

    IngredientOrderCreatePanel(Accounting accounting) {
        this.ingredients = accounting.ingredients
        articles = accounting.articles
        ingredientOrder = new IngredientOrder()
        ingredientOrder.setIngredients(ingredients)

        ingredientOrderCreateDataTableModel = new IngredientOrderCreateDataTableModel()
        ingredientOrderCreateDataTableModel.setOrder(ingredientOrder)
        ingredientOrderCreateDataTableModel.setIngredients(ingredients)
        SelectableTable<IngredientOrderItem> table = new SelectableTable<>(ingredientOrderCreateDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        comboBox = new JComboBox<>()
        fireArticleAddedOrRemoved()
        TableColumn articleColumn = table.getColumnModel().getColumn(IngredientOrderViewDataTableModel.ARTICLE_COL)
        articleColumn.setCellEditor(new DefaultCellEditor(comboBox))


        JButton orderButton = new JButton("Add Ingredient Order")
        orderButton.addActionListener({ e ->
            IngredientOrders ingredientOrders = accounting.ingredientOrders
            try {
                ingredientOrder.removeEmptyOrderItems()
                IngredientOrder existing = ingredientOrders.getBusinessObject(ingredientOrder.name)
                if (existing == null) {
                    ingredientOrders.addBusinessObject(ingredientOrder)
                }
                ingredientOrder = new IngredientOrder()
                ingredientOrder.setIngredients(ingredients)
                ingredientOrderCreateDataTableModel.setOrder(ingredientOrder)
                SalesOrdersOverviewGUI.fireSalesOrderAddedOrRemovedForAccounting(accounting)
            } catch (EmptyNameException | DuplicateNameException e1) {
                e1.printStackTrace()
            }
        })
        JPanel south = new JPanel(new BorderLayout())
        south.add(orderButton, BorderLayout.SOUTH)

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())

        add(scrollPane, BorderLayout.CENTER)
        add(south, BorderLayout.SOUTH)
    }

    void fireArticleAddedOrRemoved() {
        comboBox.removeAllItems()
        articles.businessObjects.forEach({ article -> comboBox.addItem(article) })
        ingredientOrderCreateDataTableModel.fireTableDataChanged()
    }

    void setIngredientOrder(IngredientOrder ingredientOrder) {
        this.ingredientOrder = ingredientOrder
        ingredientOrderCreateDataTableModel.setOrder(ingredientOrder)
//        ingredientOrderCreateDataTableModel.fireTableDataChanged()
    }


}

package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import javax.swing.table.TableColumn
import javax.swing.table.TableColumnModel
import java.awt.*

import static java.util.ResourceBundle.getBundle

class ArticlesPanel extends JPanel {
    final JButton addGood
    final JButton addService
//    final JButton addIngredient
    final SelectableTable<Article> table
    JComboBox<Contact> supplierComboBox
    JComboBox<Ingredient> ingredientComboBox
    Accounting accounting
    final ArticlesDataTableModel articlesDataTableModel

    ArticlesPanel(Accounting accounting) {
        this.accounting = accounting
        Articles articles = accounting.articles
        Goods goods = accounting.goods
        Services services = accounting.services
        articlesDataTableModel = new ArticlesDataTableModel(this, articles)
        table = new SelectableTable<>(articlesDataTableModel)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))

        supplierComboBox = new JComboBox<>()
        ingredientComboBox = new JComboBox<>()

        fireSupplierAddedOrRemoved()
        fireIngredientsAddedOrRemoved()
        TableColumnModel columnModel = table.getColumnModel()
        TableColumn supplierColumn = columnModel.getColumn(ArticlesDataTableModel.SUPPLIER_COL)
        TableColumn ingredientColumn = columnModel.getColumn(ArticlesDataTableModel.INGREDIENT_COL)
        supplierColumn.setCellEditor(new DefaultCellEditor(supplierComboBox))
        ingredientColumn.setCellEditor(new DefaultCellEditor(ingredientComboBox))


        JScrollPane scrollPane = new JScrollPane(table)
        layout = new BorderLayout()
        add scrollPane, BorderLayout.CENTER

        addGood = new JButton("Add Good")
        addService = new JButton("Add Service")
        JPanel panel = new JPanel()
        panel.add addGood
        panel.add addService
        add panel, BorderLayout.NORTH
        addGood.addActionListener({ e ->
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            if (name != null) {
                try {
                    def good = new Good(name)
                    goods.addBusinessObject(good)
                    articles.addBusinessObject(good)
                    articlesDataTableModel.fireTableDataChanged()
                    Main.fireArticleAddedOrRemoved(accounting)
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_NAME_EMPTY)
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_DUPLICATE_NAME, name.trim())
                }
            }
        })
        addService.addActionListener({ e ->
            String name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            while (name != null && name.equals(""))
                name = JOptionPane.showInputDialog(this, getBundle("Accounting").getString("NAME_LABEL"))
            if (name != null) {
                try {
                    def service = new Service(name)
                    services.addBusinessObject(service)
                    articles.addBusinessObject(service)
                    articlesDataTableModel.fireTableDataChanged()
                    Main.fireArticleAddedOrRemoved(accounting)
                } catch (EmptyNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_NAME_EMPTY)
                } catch (DuplicateNameException ex) {
                    ActionUtils.showErrorMessage(this, ActionUtils.ARTICLE_DUPLICATE_NAME, name.trim())
                }
            }
        })
        fireTableUpdate()
    }

    void fireSupplierAddedOrRemoved() {
        supplierComboBox.removeAllItems()
        Contacts contacts = accounting.contacts
        contacts.getBusinessObjects{it.supplier}.forEach({ contact -> supplierComboBox.addItem(contact) })
    }


    void fireIngredientsAddedOrRemoved() {
        ingredientComboBox.removeAllItems()
        Ingredients ingredients = accounting.ingredients
        ingredients.businessObjects.forEach({ ingredient -> ingredientComboBox.addItem(ingredient) })
    }

    void fireTableUpdate(){
        articlesDataTableModel.fireTableDataChanged()
    }
}

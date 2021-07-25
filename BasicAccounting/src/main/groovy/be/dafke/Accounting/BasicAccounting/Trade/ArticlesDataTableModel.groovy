package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Good
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTableModel

import java.awt.*
import java.util.List

import static java.util.ResourceBundle.getBundle

class ArticlesDataTableModel extends SelectableTableModel<Article> {
    static int UNIT_NAME_COL = 0
    static int INGREDIENT_COL = 1
    static int AMOUNT_COL = 2
    static int SUPPLIER_COL = 3
    static int PURCHASE_PRICE_COL = 4
    static int PURCHASE_VAT_COL = 5
    static int HS_COL = 6
    static int ITEMS_PER_UNIT_COL = 7
    static int SALE_ITEM_EXCL_COL = 8
    static int SALES_VAT_COL = 9
    static int SALE_ITEM_INCL_COL = 10
    static int NR_OF_COL = 11
    final Component parent
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    List<Integer> editableColumns = new ArrayList<>()

    ArticlesDataTableModel(Component parent) {
        this.parent = parent
        setColumnNames()
        setColumnClasses()
        setEditableColumns()
    }

    void setEditableColumns() {
        editableColumns.add(UNIT_NAME_COL)
        editableColumns.add(ITEMS_PER_UNIT_COL)
        editableColumns.add(HS_COL)
        editableColumns.add(PURCHASE_PRICE_COL)
        editableColumns.add(PURCHASE_VAT_COL)
        editableColumns.add(SALES_VAT_COL)
        editableColumns.add(SUPPLIER_COL)
        editableColumns.add(INGREDIENT_COL)
        editableColumns.add(AMOUNT_COL)
        editableColumns.add(SALE_ITEM_INCL_COL)
    }

    void setColumnClasses() {
        columnClasses.put(UNIT_NAME_COL, String.class)
        columnClasses.put(ITEMS_PER_UNIT_COL, Integer.class)
        columnClasses.put(HS_COL, String.class)
        columnClasses.put(PURCHASE_PRICE_COL, BigDecimal.class)
        columnClasses.put(PURCHASE_VAT_COL, Integer.class)
        columnClasses.put(SALES_VAT_COL, Integer.class)
        columnClasses.put(SUPPLIER_COL, Contact.class)
        columnClasses.put(INGREDIENT_COL, Ingredient.class)
        columnClasses.put(AMOUNT_COL, BigDecimal.class)
        columnClasses.put(SALE_ITEM_EXCL_COL, BigDecimal.class)
        columnClasses.put(SALE_ITEM_INCL_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(UNIT_NAME_COL, getBundle("Accounting").getString("ARTICLE_UNIT_NAME"))
        columnNames.put(ITEMS_PER_UNIT_COL, getBundle("Accounting").getString("ITEMS_PER_UNIT"))
        columnNames.put(HS_COL, getBundle("Accounting").getString("ARTICLE_HS"))
        columnNames.put(PURCHASE_PRICE_COL, getBundle("Accounting").getString("PURCHASE_PRICE"))
        columnNames.put(PURCHASE_VAT_COL, getBundle("Accounting").getString("PURCHASE_VAT"))
        columnNames.put(SALES_VAT_COL, getBundle("Accounting").getString("SALES_VAT"))
        columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"))
        columnNames.put(INGREDIENT_COL, getBundle("Accounting").getString("INGREDIENT"))
        columnNames.put(AMOUNT_COL, getBundle("Accounting").getString("AMOUNT"))
        columnNames.put(SALE_ITEM_EXCL_COL, getBundle("Accounting").getString("SALE_ITEM_EXCL"))
        columnNames.put(SALE_ITEM_INCL_COL, getBundle("Accounting").getString("SALE_ITEM_INCL"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Article article = getObject(row, col)
        if(article==null) return null
        if (col == UNIT_NAME_COL) return article.name
        if (col == PURCHASE_VAT_COL) return article.getPurchaseVatRate()
        if (col == SALES_VAT_COL) return article.salesVatRate
        if (col == HS_COL) {
            if(article instanceof Good) {
                Good good = (Good)article
                return good.getHSCode()
            }
            return null
        }
        if (col == PURCHASE_PRICE_COL) return article.purchasePrice
        if(col == INGREDIENT_COL) return article.getIngredient()
        if(col == AMOUNT_COL) return article.ingredientAmount
        if (col == SUPPLIER_COL) return article.supplier
        if (col == ITEMS_PER_UNIT_COL) return article.itemsPerUnit
        if (col == SALE_ITEM_EXCL_COL) {
            BigDecimal salesPriceSingleWithoutVat = article.getSalesPriceItemWithoutVat()
            return salesPriceSingleWithoutVat?salesPriceSingleWithoutVat:BigDecimal.ZERO
        }
        if (col == SALE_ITEM_INCL_COL) {
            BigDecimal salesPriceSingleWithVat = article.getSalesPriceItemWithVat()
            return salesPriceSingleWithVat?salesPriceSingleWithVat:BigDecimal.ZERO
        }
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        Session.activeAccounting.articles.businessObjects.size()?:0
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        editableColumns.contains(col)
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Article article = getObject(row,col)
        if(col == PURCHASE_PRICE_COL){
            BigDecimal purchasePrice = (BigDecimal) value
            if(purchasePrice.scale()<2)
                purchasePrice = purchasePrice.setScale(2)
            article.setPurchasePrice(purchasePrice)
        }
        if(col == HS_COL){
            if(article instanceof Good){
                Good good = (Good) article
                good.setHSCode((String) value)
            }
        }
        if(col == PURCHASE_VAT_COL){
            article.setPurchaseVatRate((Integer) value)
        }
        if(col == SALES_VAT_COL){
            article.setSalesVatRate((Integer) value)
        }
        if(col == SUPPLIER_COL){
            article.setSupplier((Contact) value)
        }
        if(col == INGREDIENT_COL) {
            article.setIngredient((Ingredient) value)
        }
        if(col == AMOUNT_COL) {
            article.setIngredientAmount((BigDecimal) value)
        }
        if(col == ITEMS_PER_UNIT_COL){
            article.setItemsPerUnit((Integer) value)
        }
        if (col == SALE_ITEM_INCL_COL) {
            BigDecimal amount = (BigDecimal) value
            article.setSalesPriceItemWithVat(amount.setScale(2))
        }
        if(col == UNIT_NAME_COL) {
            String oldName = article.name
            String newName = (String) value
            if (newName != null && !oldName.trim().equals(newName.trim())) {
                try {
                    Session.activeAccounting.articles.modifyName(oldName, newName)
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_DUPLICATE_NAME, newName.trim())
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_NAME_EMPTY)
                }
            }
        }
        fireTableDataChanged()
    }

    @Override
    Article getObject(int row, int col) {
        Session.activeAccounting.articles.businessObjects.get(row)
    }
}

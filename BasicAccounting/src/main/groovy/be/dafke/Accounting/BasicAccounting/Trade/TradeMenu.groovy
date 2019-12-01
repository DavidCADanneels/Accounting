package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.Contacts

import javax.swing.JMenu
import javax.swing.JMenuItem
import java.awt.event.KeyEvent

import static java.util.ResourceBundle.getBundle

class TradeMenu extends JMenu {
    private JMenuItem articlesTable, stockTable, salesOrders, purchaseOrders, promoOrders, ingredientsOrders, stockHistoryTable

    private Articles articles
    private Contacts contacts
    private Accounting accounting

    TradeMenu() {
        super(getBundle("Accounting").getString("TRADE"))
        setMnemonic(KeyEvent.VK_T)

        articlesTable = new JMenuItem(getBundle("Accounting").getString("ARTICLES"))
        articlesTable.setMnemonic(KeyEvent.VK_A)
        articlesTable.addActionListener({ e ->
            ArticlesGUI articlesGUI = ArticlesGUI.showArticles(accounting)
            articlesGUI.setLocation(getLocationOnScreen())
            articlesGUI.setVisible(true)
        })
        articlesTable.setEnabled(false)

        stockTable = new JMenuItem(getBundle("Accounting").getString("STOCK"))
        stockTable.setMnemonic(KeyEvent.VK_S)
        stockTable.addActionListener({ e ->
            StockGUI stockGUI = StockGUI.showStock(accounting)
            stockGUI.setLocation(getLocationOnScreen())
            stockGUI.setVisible(true)
        })
        stockTable.setEnabled(false)

        stockHistoryTable = new JMenuItem(getBundle("Accounting").getString("STOCK_HISTORY"))
        stockHistoryTable.setMnemonic(KeyEvent.VK_H)
        stockHistoryTable.addActionListener({ e ->
            StockHistoryGUI stockGUI = StockHistoryGUI.showStockHistory(accounting)
            stockGUI.setLocation(getLocationOnScreen())
            stockGUI.setVisible(true)
        })
        stockHistoryTable.setEnabled(false)

        purchaseOrders = new JMenuItem(getBundle("Accounting").getString("POS"))
        purchaseOrders.setMnemonic(KeyEvent.VK_P)
        purchaseOrders.addActionListener({ e ->
            PurchaseOrdersOverviewGUI purchaseOrdersOverviewGUI = PurchaseOrdersOverviewGUI.showPurchaseOrderGUI(accounting)
            purchaseOrdersOverviewGUI.setLocation(getLocationOnScreen())
            purchaseOrdersOverviewGUI.setVisible(true)
        })
        purchaseOrders.setEnabled(false)

        salesOrders = new JMenuItem(getBundle("Accounting").getString("SOS"))
        salesOrders.setMnemonic(KeyEvent.VK_S)
        salesOrders.addActionListener({ e ->
            SalesOrdersOverviewGUI salesOrdersGUI = SalesOrdersOverviewGUI.showSalesOrderGUI(accounting)
            salesOrdersGUI.setLocation(getLocationOnScreen())
            salesOrdersGUI.setVisible(true)
        })
        salesOrders.setEnabled(false)

        promoOrders = new JMenuItem(getBundle("Accounting").getString("PROMO_ORDERS"))
        promoOrders.setMnemonic(KeyEvent.VK_R)
        promoOrders.addActionListener({ e ->
            PromoOrdersOverviewGUI promoOrdersGUI = PromoOrdersOverviewGUI.showPromoOrderGUI(accounting)
            promoOrdersGUI.setLocation(getLocationOnScreen())
            promoOrdersGUI.setVisible(true)
        })
        promoOrders.setEnabled(false)

        ingredientsOrders = new JMenuItem(getBundle("Accounting").getString("INGREDIENTS_ORDERS"))
        ingredientsOrders.setMnemonic(KeyEvent.VK_I)
        ingredientsOrders.addActionListener({ e ->
            IngredientOrdersOverviewGUI ingredientOrdersOverviewGUI = IngredientOrdersOverviewGUI.showIngredientOrdersGUI(accounting)
            ingredientOrdersOverviewGUI.setLocation(getLocationOnScreen())
            ingredientOrdersOverviewGUI.setVisible(true)
        })
        ingredientsOrders.setEnabled(false)

        add(articlesTable)
        add(stockTable)
        add(stockHistoryTable)
        add(purchaseOrders)
        add(salesOrders)
        add(promoOrders)
        add(ingredientsOrders)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
        setContacts(accounting==null?null:accounting.getContacts())
        setArticles(accounting==null?null:accounting.getArticles())
        stockTable.setEnabled(accounting!=null)
        stockHistoryTable.setEnabled(accounting!=null)
        purchaseOrders.setEnabled(accounting!=null)
        salesOrders.setEnabled(accounting!=null)
        promoOrders.setEnabled(accounting!=null)
        ingredientsOrders.setEnabled(accounting!=null)
    }

    void setContacts(Contacts contacts) {
        this.contacts = contacts
    }

    void setArticles(Articles articles){
        this.articles = articles
        articlesTable.setEnabled(articles!=null)
    }
}

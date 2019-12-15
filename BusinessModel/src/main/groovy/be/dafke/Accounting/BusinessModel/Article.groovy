package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.Utils.Utils

import java.util.function.Predicate

class Article extends BusinessObject{
    String HSCode = ""
    Integer purchaseVatRate = 0
    Integer salesVatRate = 6
    Integer itemsPerUnit = 1
    BigDecimal purchasePrice = null
    BigDecimal salesPriceItemWithVat = null
    Contact supplier
    Ingredient ingredient
    BigDecimal ingredientAmount = BigDecimal.ZERO
    ArrayList<StockOrder> stockOrders = new ArrayList()
    ArrayList<PromoOrder> promoOrders = new ArrayList()
    ArrayList<IngredientOrder> ingredientOrders = new ArrayList()
    ArrayList<PurchaseOrder> purchaseOrders = new ArrayList()
    ArrayList<SalesOrder> salesOrders = new ArrayList()

    Article(Article article, Contacts contacts){
        this(article.name)
        HSCode = article.HSCode
        purchaseVatRate = article.purchaseVatRate
        salesVatRate = article.salesVatRate
        itemsPerUnit = article.itemsPerUnit
        purchasePrice = article.purchasePrice
        salesPriceItemWithVat = article.salesPriceItemWithVat
        String supplierName = article.supplier.name
        supplier = contacts.getBusinessObject(supplierName)
    }

    Article(String name){
        setName(name)
    }

    void setHSCode(String HSCode) {
        this.HSCode = HSCode
    }

    void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate
    }

    void setPurchaseVatRate(Integer purchaseVatRate) {
        this.purchaseVatRate = purchaseVatRate
    }

    void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice
    }

    void setSupplier(Contact supplier) {
        this.supplier = supplier
    }

    void setItemsPerUnit(Integer itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit
    }

    void setSalesPriceItemWithVat(BigDecimal salesPriceItemWithVat) {
        this.salesPriceItemWithVat = salesPriceItemWithVat
    }

    void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient
    }

    void setIngredientAmount(BigDecimal ingredientAmount) {
        this.ingredientAmount = ingredientAmount
    }

    BigDecimal getIngredientAmount() {
        ingredientAmount
    }

    Ingredient getIngredient() {
        ingredient
    }

    Integer getItemsPerUnit() {
        itemsPerUnit
    }

    Contact getSupplier() {
        supplier
    }

    BigDecimal getPurchasePrice(){
        purchasePrice
    }

    String getHSCode() {
        HSCode
    }

    Integer getSalesVatRate() {
        salesVatRate
    }

    BigDecimal getSalesPriceItemWithVat() {
        salesPriceItemWithVat
    }

    BigDecimal getSalesPriceItemWithoutVat() {
        salesPriceItemWithVat?salesPriceItemWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN):null
    }

    Integer getPurchaseVatRate() {
        purchaseVatRate
    }

    // 6 -> 1.06
    BigDecimal getSalesFactor(){
        Utils.getFactor(salesVatRate)
    }

    static Predicate<Article> ofSupplier(Contact supplier) {
        { article -> article.supplier == supplier }
    }

    static Predicate<Article> withOrders(){
        { article -> article.purchaseOrders.size() > 0 || article.salesOrders.size() > 0 }
    }

    static Predicate<Article> inStock(){
        { article -> article.getNrInStock() > 0 }
    }

    ArrayList<PurchaseOrder> getPurchaseOrders() {
        purchaseOrders
    }

    ArrayList<SalesOrder> getSalesOrders() {
        salesOrders
    }

    ArrayList<StockOrder> getStockOrders() {
        stockOrders
    }

    ArrayList<PromoOrder> getPromoOrders() {
        promoOrders
    }

    ArrayList<IngredientOrder> getIngredientOrders() {
        ingredientOrders
    }

    void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrders.add(purchaseOrder)
    }

    void addStockOrder(StockOrder stockOrder) {
        stockOrders.add(stockOrder)
    }

    void addPromoOrder(PromoOrder promoOrder) {
        promoOrders.add(promoOrder)
    }

    void addIngredientOrder(IngredientOrder ingredientOrder) {
        ingredientOrders.add(ingredientOrder)
    }

    void addSalesOrder(SalesOrder salesOrder) {
        salesOrders.add(salesOrder)
    }
}

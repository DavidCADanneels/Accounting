package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject
import be.dafke.Utils.Utils

import java.util.function.Predicate

class Article extends BusinessObject{
    private String HSCode = ""
    private Integer purchaseVatRate = 0
    private Integer salesVatRate = 6
    private Integer itemsPerUnit = 1
    private BigDecimal purchasePrice = null
    private BigDecimal salesPriceItemWithVat = null
    private Contact supplier
    private Ingredient ingredient
    private BigDecimal ingredientAmount = BigDecimal.ZERO
    private ArrayList<StockOrder> stockOrders = new ArrayList()
    private ArrayList<PromoOrder> promoOrders = new ArrayList()
    private ArrayList<IngredientOrder> ingredientOrders = new ArrayList()
    private ArrayList<PurchaseOrder> purchaseOrders = new ArrayList()
    private ArrayList<SalesOrder> salesOrders = new ArrayList()

    private Integer initStock = 0
    private Integer nrPromo = 0
    private Integer nrAdded = 0
    private Integer nrRemoved = 0
    private Integer nrOrderedForSO = 0
    private Integer nrOrderedByPO = 0

    Article(Article article, Contacts contacts){
        this(article.getName())
        HSCode = article.HSCode
        purchaseVatRate = article.purchaseVatRate
        salesVatRate = article.salesVatRate
        itemsPerUnit = article.itemsPerUnit
        purchasePrice = article.purchasePrice
        salesPriceItemWithVat = article.salesPriceItemWithVat
        String supplierName = article.supplier.getName()
        supplier = contacts.getBusinessObject(supplierName)
        nrRemoved = 0
        nrAdded = 0
        // TODO: copy open SO and PO ?
        nrOrderedByPO = article.getNrOrderedByPO()
        nrOrderedForSO = article.getNrOrderedForSO()
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
        if(salesPriceItemWithVat ==null) null
        salesPriceItemWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN)
    }

    Integer getPurchaseVatRate() {
        purchaseVatRate
    }

    // 6 -> 1.06
    BigDecimal getSalesFactor(){
        Utils.getFactor(salesVatRate)
    }

    static Predicate<Article> ofSupplier(Contact supplier) {
        { article -> article.getSupplier() == supplier }
    }

    static Predicate<Article> withOrders(){
        { article -> article.getPurchaseOrders().size() > 0 || article.getSalesOrders().size() > 0 }
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

    void setPoOrdered(int numberOfItems) {
        nrOrderedByPO += numberOfItems
    }

    void setPoCnOrdered(int numberOfItems) {
        nrOrderedByPO -= numberOfItems
    }

    void setPoDelivered(int numberOfItems) {
        nrOrderedByPO-=numberOfItems
        nrAdded+=numberOfItems
    }

    void setPoCnDelivered(int numberOfItems) {
        nrOrderedByPO+=numberOfItems
        nrAdded-=numberOfItems
    }

    void setStockOrderDelivered(int numberOfItems) {
        nrAdded+=numberOfItems
        initStock+=numberOfItems
    }

    void setPromoOrderDelivered(int numberOfItems) {
        nrRemoved+=numberOfItems
        // TODO: remove below line if 'PromoOrder extends Order' is implemented
//        nrOrderedForSO-=numberOfItems
        nrPromo+=numberOfItems
    }

    void addSalesOrder(SalesOrder salesOrder) {
        salesOrders.add(salesOrder)
    }

    void setSoOrdered(int numberOfItems) {
        nrOrderedForSO += numberOfItems
    }

    void setSoCnOrdered(int numberOfItems) {
        nrOrderedForSO -= numberOfItems
    }

    void setSoDelivered(int numberOfItems) {
        nrOrderedForSO-=numberOfItems
        nrRemoved+=numberOfItems
    }

    void setSoCnDelivered(int numberOfItems) {
        nrOrderedForSO+=numberOfItems
        nrRemoved-=numberOfItems
    }

    Integer getNrInStock() {
        nrAdded - nrRemoved
    }

    Integer getNrAdded() {
        nrAdded
    }

    Integer getNrRemoved() {
        nrRemoved
    }

    Integer getNrOrderedForSO() {
        nrOrderedForSO
    }

    Integer getNrOrderedByPO() {
        nrOrderedByPO
    }

    Integer getInitStock() {
        initStock
    }

    Integer getNrPromo() {
        nrPromo
    }
}

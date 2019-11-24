package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

import java.util.function.Predicate

import static java.math.BigDecimal.ROUND_HALF_DOWN

class OrderItem extends BusinessObject{
    private int numberOfUnits, numberOfItems
    private Article article
    private BigDecimal purchasePriceForUnit
    private BigDecimal salesPriceForItem
    private int itemsPerUnit = 0
    private Integer salesVatRate = null
    private Integer purchaseVatRate = null
    private PurchaseOrder purchaseOrder
    private Order order

    OrderItem(Integer numberOfItems, Article article, Order order) {
        this(numberOfItems, article, article.getName(), order)
    }

    OrderItem(Integer numberOfItems, Article article, String name, Order order) {
        setName(name)
        this.numberOfItems = numberOfItems
        this.order = order
        this.article = article
    }

    boolean isDeletable() {
        numberOfItems==0 && numberOfUnits==0
    }

    static Predicate<OrderItem> withSalesVatRate(Integer vatRate){
        { orderItem -> orderItem.getSalesVatRate() == vatRate }
    }

    static Predicate<OrderItem> withPurchaseVatRate(Integer vatRate){
        { orderItem -> orderItem.getPurchaseVatRate() == vatRate }
    }

    static Predicate<OrderItem> containsArticle(Article article){
        { orderItem -> orderItem.getArticle() == article }
    }

    int getItemsPerUnit() {
        itemsPerUnit!=0?itemsPerUnit:article==null?0:article.getItemsPerUnit()
    }

    void setItemsPerUnit(int itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit
    }

    int getNumberOfUnits() {
        numberOfUnits
    }

    int getNumberOfItems() {
        numberOfItems
    }

    void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems
    }

    void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits
        this.numberOfItems = numberOfUnits*itemsPerUnit
    }

    void addNumberOfItems(int numberOfItems) {
        this.numberOfItems += numberOfItems
    }

    void removeNumberOfItems(int numberOfItems) {
        this.numberOfItems -= numberOfItems
    }


    void calculateNumberOfItems(){
        numberOfItems = numberOfUnits*getItemsPerUnit()
    }

    Article getArticle() {
        article
    }

    // ============ SALES ==========

    void setSalesPriceForItem(BigDecimal salesPriceForItem) {
        this.salesPriceForItem = salesPriceForItem
    }

    BigDecimal getSalesPriceForItem() {
        salesPriceForItem !=null? salesPriceForItem :article.getSalesPriceItemWithVat()
    }

    BigDecimal getSalesPriceWithoutVat() {
        getSalesPriceWithoutVat(getSalesPriceForItem())
    }

    BigDecimal getSalesPriceWithVat() {
        getSalesPriceWithVat(getSalesPriceForItem())
    }

    BigDecimal getSalesVatAmount() {
        getSalesVatAmount(getSalesPriceForItem())
    }

    BigDecimal getSalesVatAmount(BigDecimal itemPrice){
        BigDecimal salesPriceWithoutVat = getSalesPriceWithoutVat(itemPrice)
        salesPriceWithoutVat.multiply(getSalesPercentage())
    }

    private BigDecimal getSalesPercentage() {
        new BigDecimal(getSalesVatRate()).divide(new BigDecimal(100))
    }

    // 1.06
    BigDecimal getSalesFactor(){
        BigDecimal.ONE.add(getSalesPercentage())
    }

    BigDecimal getSalesPriceWithoutVat(BigDecimal itemPrice) {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat(itemPrice)
        salesPriceWithVat.divide(getSalesFactor(),ROUND_HALF_DOWN)
    }

    BigDecimal getSalesPriceWithVat(BigDecimal itemPrice) {
        if(itemPrice==null) BigDecimal.ZERO
        itemPrice.multiply(new BigDecimal(numberOfItems))
    }

    Integer getSalesVatRate() {
        salesVatRate!=null?salesVatRate:article==null?null:article.getSalesVatRate()
    }

    void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate
    }

    // ========== PURCHASE =============


    BigDecimal getPurchasePriceForItem() {
        BigDecimal purchasePriceForUnit = getPurchasePriceForUnit()
        if(purchasePriceForUnit==null) null
        int itemsPerUnit = getItemsPerUnit()
        if (itemsPerUnit == 1) purchasePriceForUnit
        purchasePriceForUnit.divide(new BigDecimal(itemsPerUnit), ROUND_HALF_DOWN)
    }

    BigDecimal getPurchasePriceForUnit() {
        purchasePriceForUnit!=null?purchasePriceForUnit:article.getPurchasePrice()
    }

    void setPurchasePriceForUnit(BigDecimal purchasePriceForUnit) {
        this.purchasePriceForUnit = purchasePriceForUnit
    }

    BigDecimal getPurchasePriceWithVat(int number, BigDecimal unitPrice) {
        BigDecimal purchasePriceWithVat = unitPrice.multiply(new BigDecimal(number))
        purchasePriceWithVat.multiply(getPurchaseFactor())
    }

    BigDecimal getPurchaseVatAmount(int number, BigDecimal unitPrice){
        BigDecimal purchasePriceWithoutVat = unitPrice.multiply(new BigDecimal(number))
        purchasePriceWithoutVat.multiply(getPurchasePercentage())
    }

    BigDecimal getStockValue(){
        BigDecimal itemPrice = getPurchasePriceForItem()
        itemPrice==null?null:itemPrice.multiply(new BigDecimal(numberOfItems))
    }

    BigDecimal getPurchasePriceWithoutVat() {
        BigDecimal unitPrice = getPurchasePriceForUnit()
        unitPrice==null?null:unitPrice.multiply(new BigDecimal(numberOfUnits))
    }

    BigDecimal getPurchasePriceWithVat() {
        BigDecimal unitPrice = getPurchasePriceForUnit()
        unitPrice==null?null:getPurchasePriceWithVat(numberOfUnits, unitPrice)
    }

    BigDecimal getPurchaseVatAmount() {
        BigDecimal unitPrice = getPurchasePriceForUnit()
        unitPrice==null?null:getPurchaseVatAmount(numberOfUnits, unitPrice)
    }

    private BigDecimal getPurchasePercentage() {
        Integer purchaseVatRate = getPurchaseVatRate()
        purchaseVatRate==null?null:new BigDecimal(purchaseVatRate).divide(new BigDecimal(100))
    }

    // 1.06
    BigDecimal getPurchaseFactor(){
        BigDecimal purchasePercentage = getPurchasePercentage()
        purchasePercentage==null?null:BigDecimal.ONE.add(purchasePercentage)
    }

    Integer getPurchaseVatRate() {
//		Contact supplier = article.getSupplier()
//		if(supplier.isIntraCommun() && supplier.isInternational){
//			0
//		} else {
//        purchaseVatRate!=null?purchaseVatRate:article.getPurchaseVatRate()
//		}
        // TODO: getSupplier().isInternational or IntraComm -> 0%
        purchaseVatRate!=null?purchaseVatRate:article.getPurchaseVatRate()
    }

    void setPurchaseVatRate(Integer purchaseVatRate) {
        this.purchaseVatRate = purchaseVatRate
    }

    void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder
    }

    PurchaseOrder getPurchaseOrder() {
        purchaseOrder
    }

    Order getOrder() {
        order
    }
}

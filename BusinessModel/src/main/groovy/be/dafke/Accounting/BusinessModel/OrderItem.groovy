package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

import java.util.function.Predicate

import static java.math.BigDecimal.ROUND_HALF_DOWN

class OrderItem extends BusinessObject{
    int numberOfUnits, numberOfItems
    Article article
    BigDecimal purchasePriceForUnit
    BigDecimal salesPriceForItem
    int itemsPerUnit = 0
    Integer salesVatRate = null
    Integer purchaseVatRate = null
    PurchaseOrder purchaseOrder
    Order order

    OrderItem(Integer numberOfItems, Article article, Order order) {
        this(numberOfItems, article, article.name, order)
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
    static Predicate<OrderItem> isGood(){
        { orderItem -> orderItem.article instanceof Good }
    }

    static Predicate<OrderItem> isService(){
        { orderItem -> orderItem.article instanceof Service }
    }


    static Predicate<OrderItem> withSalesVatRate(Integer vatRate){
        { orderItem -> orderItem.salesVatRate == vatRate }
    }

    static Predicate<OrderItem> withPurchaseVatRate(Integer vatRate){
        { orderItem -> orderItem.getPurchaseVatRate() == vatRate }
    }

    static Predicate<OrderItem> containsArticle(Article article){
        { orderItem -> orderItem.article == article }
    }

    int getItemsPerUnit() {
        itemsPerUnit!=0?itemsPerUnit:article==null?0:article.itemsPerUnit
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
        salesPriceForItem?:article.getSalesPriceItemWithVat()
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
        salesPriceWithoutVat.multiply(salesPercentage)
    }

    BigDecimal getSalesPercentage() {
        new BigDecimal(getSalesVatRate()).divide(new BigDecimal(100))
    }

    // 1.06
    BigDecimal getSalesFactor(){
        BigDecimal.ONE.add(salesPercentage)
    }

    BigDecimal getSalesPriceWithoutVat(BigDecimal itemPrice) {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat(itemPrice)
        salesPriceWithVat.divide(salesFactor,ROUND_HALF_DOWN)
    }

    BigDecimal getSalesPriceWithVat(BigDecimal itemPrice) {
        if(itemPrice==null) return BigDecimal.ZERO
        itemPrice.multiply(new BigDecimal(numberOfItems))
    }

    Integer getSalesVatRate() {
        salesVatRate?:article?article.salesVatRate:null
    }

    void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate
    }

    // ========== PURCHASE =============

    BigDecimal getPurchasePriceForItem() {
        BigDecimal purchasePriceForUnit = getPurchasePriceForUnit()
        if(purchasePriceForUnit==null) return null
        int itemsPerUnit = getItemsPerUnit()
        if (itemsPerUnit == 1) return purchasePriceForUnit
        purchasePriceForUnit.divide(new BigDecimal(itemsPerUnit), ROUND_HALF_DOWN)
    }

    BigDecimal getPurchasePriceForUnit() {
        purchasePriceForUnit?purchasePriceForUnit:article.purchasePrice
    }

    void setPurchasePriceForUnit(BigDecimal purchasePriceForUnit) {
        this.purchasePriceForUnit = purchasePriceForUnit
    }

    BigDecimal getPurchasePriceWithoutVat(int number, BigDecimal unitPrice) {
        unitPrice.multiply(new BigDecimal(number))
    }

    BigDecimal getPurchasePriceWithVat(int number, BigDecimal unitPrice) {
        BigDecimal purchasePriceWithVat = unitPrice.multiply(new BigDecimal(number))
        purchasePriceWithVat.multiply(getPurchaseFactor())
    }

    BigDecimal getPurchaseVatAmount(int number, BigDecimal unitPrice){
        BigDecimal purchasePriceWithoutVat = unitPrice.multiply(new BigDecimal(number))
        def percentage = getPurchasePercentage()
        if(percentage==null || percentage.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO
        purchasePriceWithoutVat.multiply(percentage)
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
        unitPrice?getPurchasePriceWithVat(numberOfUnits, unitPrice):null
    }

    BigDecimal getPurchaseVatAmount() {
        BigDecimal unitPrice = getPurchasePriceForUnit()
        unitPrice?getPurchaseVatAmount(numberOfUnits, unitPrice):null
    }

    BigDecimal getPurchasePercentage() {
        Integer purchaseVatRate = getPurchaseVatRate()
        purchaseVatRate?new BigDecimal(purchaseVatRate).divide(new BigDecimal(100)):BigDecimal.ZERO
    }

    // 1.06
    BigDecimal getPurchaseFactor(){
        BigDecimal purchasePercentage = getPurchasePercentage()
        purchasePercentage?BigDecimal.ONE.add(purchasePercentage):BigDecimal.ONE
    }

    Integer getPurchaseVatRate() {
//		Contact supplier = article.supplier
//		if(supplier.isIntraCommun() && supplier.isInternational){
//			0
//		} else {
//        purchaseVatRate?:article.getPurchaseVatRate()
//		}
        // TODO: getSupplier().isInternational or IntraComm -> 0%
        purchaseVatRate?purchaseVatRate:article.getPurchaseVatRate()
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

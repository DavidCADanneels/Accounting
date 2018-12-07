package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class OrderItem extends BusinessObject{
    private int numberOfUnits, numberOfItems;
    private Article article;
    private BigDecimal purchasePriceForUnit;
    private BigDecimal salesPriceForItem;
    private BigDecimal salesPriceForUnit;
    private int itemsPerUnit = 0;
    private Integer salesVatRate = null;
    private Integer purchaseVatRate = null;
    private PurchaseOrder purchaseOrder;
    private Order order;

    public OrderItem(Integer numberOfUnits, Integer numberOfItems, Article article, Order order) {
        this.numberOfUnits = numberOfUnits;
        this.numberOfItems = numberOfItems;
        this.order = order;
        this.article = article;
    }

    public OrderItem(Integer number, Article article) {
        setName(article.getName());
        this.numberOfUnits = number==null?0:number;
        this.numberOfItems = number==null?0:number*getItemsPerUnit();
        this.article = article;
    }

    public boolean isDeletable() {
        return numberOfItems==0 && numberOfUnits==0;
    }

    public static Predicate<OrderItem> withSalesVatRate(Integer vatRate){
        return orderItem -> orderItem.getSalesVatRate() == vatRate;
    }

    public static Predicate<OrderItem> withPurchaseVatRate(Integer vatRate){
        return orderItem -> orderItem.getPurchaseVatRate() == vatRate;
    }

    public static Predicate<OrderItem> containsArticle(Article article){
        return orderItem -> orderItem.getArticle() == article;
    }

    public int getItemsPerUnit() {
        return itemsPerUnit!=0?itemsPerUnit:article==null?0:article.getItemsPerUnit();
    }

    public void setItemsPerUnit(int itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public void addNumberOfItems(int numberOfItems) {
        this.numberOfItems += numberOfItems;
    }

    public void addNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits += numberOfUnits;
    }

    public void removeNumberOfItems(int numberOfItems) {
        this.numberOfItems -= numberOfItems;
    }

    public void removeNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits -= numberOfUnits;
    }

    public void calculateNumberOfItems(){
        numberOfItems = numberOfUnits*getItemsPerUnit();
    }

    public void calculateNumberOfUnits(){
        numberOfUnits = numberOfItems/getItemsPerUnit();
    }

    public Article getArticle() {
        return article;
    }

    // ============ SALES ==========

    public void setSalesPriceForItem(BigDecimal salesPriceForItem) {
        this.salesPriceForItem = salesPriceForItem;
    }

    public BigDecimal getSalesPriceForItem() {
        return salesPriceForItem !=null? salesPriceForItem :article.getSalesPriceItemWithVat();
    }

    public void setSalesPriceForUnit(BigDecimal salesPriceForUnit) {
        this.salesPriceForUnit = salesPriceForUnit;
    }

    public BigDecimal getSalesPriceForUnit() {
        return salesPriceForUnit !=null? salesPriceForUnit :article.getSalesPriceUnitWithVat();
    }

    public BigDecimal getSalesPriceWithoutVat() {
        return getSalesPriceWithoutVat(numberOfItems, getSalesPriceForItem(), getSalesPriceForUnit());
    }

    public BigDecimal getSalesPriceWithVat() {
        return getSalesPriceWithVat(numberOfItems, getSalesPriceForItem(), getSalesPriceForUnit());
    }

    public BigDecimal getSalesVatAmount() {
        return getSalesVatAmount(numberOfItems, getSalesPriceForItem(), getSalesPriceForUnit());
    }

    private BigDecimal getSalesVatAmount(int number, BigDecimal itemPrice, BigDecimal unitPrice){
        BigDecimal salesPriceWithoutVat = getSalesPriceWithoutVat(number, itemPrice, unitPrice);
        return salesPriceWithoutVat.multiply(getSalesPercentage());
    }

    private BigDecimal getSalesPercentage() {
        return new BigDecimal(getSalesVatRate()).divide(new BigDecimal(100));
    }

    // return 1.06
    BigDecimal getSalesFactor(){
        return BigDecimal.ONE.add(getSalesPercentage());
    }

    private BigDecimal getSalesPriceWithoutVat(int number, BigDecimal itemPrice, BigDecimal unitPrice) {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat(number, itemPrice, unitPrice);
        return salesPriceWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN);
    }

    private BigDecimal getSalesPriceWithVat(int number, BigDecimal itemPrice, BigDecimal unitPrice) {
        if(itemPrice==null||unitPrice==null) return BigDecimal.ZERO;
        int nrOfUnits = number/getItemsPerUnit();
        int remainingItems = number%getItemsPerUnit();
        BigDecimal priceForUnits = unitPrice.multiply(new BigDecimal(nrOfUnits));
        BigDecimal priceForItems = itemPrice.multiply(new BigDecimal(remainingItems));
        return priceForUnits.add(priceForItems);
    }
    public Integer getSalesVatRate() {
        return salesVatRate!=null?salesVatRate:article==null?null:article.getSalesVatRate();
    }

    public void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate;
    }

    // ========== PURCHASE =============


    public BigDecimal getPurchasePriceForUnit() {
        return purchasePriceForUnit!=null?purchasePriceForUnit:article.getPurchasePrice();
    }

    public void setPurchasePriceForUnit(BigDecimal purchasePriceForUnit) {
        this.purchasePriceForUnit = purchasePriceForUnit;
    }


    private BigDecimal getPurchasePriceWithVat(int number, BigDecimal unitPrice) {
        BigDecimal purchasePriceWithVat = getPurchasePriceWithoutVat(number, unitPrice);
        return purchasePriceWithVat.multiply(getPurchaseFactor());
    }

    private BigDecimal getPurchasePriceWithoutVat(int nrOfUnits, BigDecimal unitPrice) {
        return unitPrice.multiply(new BigDecimal(nrOfUnits));
    }

    private BigDecimal getPurchaseVatAmount(int number, BigDecimal unitPrice){
        BigDecimal purchasePriceWithoutVat = getPurchasePriceWithoutVat(number, unitPrice);
        return purchasePriceWithoutVat.multiply(getPurchasePercentage());
    }

    public BigDecimal getPurchasePriceWithoutVat() {
        return getPurchasePriceWithoutVat(numberOfUnits, getPurchasePriceForUnit());
    }

    public BigDecimal getPurchasePriceWithVat() {
        return getPurchasePriceWithVat(numberOfUnits, getPurchasePriceForUnit());
    }

    public BigDecimal getPurchaseVatAmount() {
        return getPurchaseVatAmount(numberOfUnits, getPurchasePriceForUnit());
    }

    private BigDecimal getPurchasePercentage() {
        return new BigDecimal(getPurchaseVatRate()).divide(new BigDecimal(100));
    }

    // return 1.06
    BigDecimal getPurchaseFactor(){
        return BigDecimal.ONE.add(getPurchasePercentage());
    }

    public Integer getPurchaseVatRate() {
//		Contact supplier = article.getSupplier();
//		if(supplier.isIntraCommun() && supplier.isInternational){
//			return 0;
//		} else {
//        return purchaseVatRate!=null?purchaseVatRate:article.getPurchaseVatRate();
//		}
        // TODO: getSupplier().isInternational or IntraComm -> 0%
        return purchaseVatRate!=null?purchaseVatRate:article.getPurchaseVatRate();
    }

    public void setPurchaseVatRate(Integer purchaseVatRate) {
        this.purchaseVatRate = purchaseVatRate;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public Order getOrder() {
        return order;
    }
}

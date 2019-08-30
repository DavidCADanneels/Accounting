package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class OrderItem extends BusinessObject{
    private int numberOfUnits, numberOfItems;
    private Article article;
    private BigDecimal purchasePriceForUnit;
    private BigDecimal salesPriceForItem;
    private int itemsPerUnit = 0;
    private Integer salesVatRate = null;
    private Integer purchaseVatRate = null;
    private PurchaseOrder purchaseOrder;
    private Order order;

    public OrderItem(Integer numberOfItems, Article article, Order order) {
        this(numberOfItems, article, article.getName(), order);
    }

    public OrderItem(Integer numberOfItems, Article article, String name, Order order) {
        setName(name);
        this.numberOfItems = numberOfItems;
        this.order = order;
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
        this.numberOfItems = numberOfUnits*itemsPerUnit;
    }

    public void addNumberOfItems(int numberOfItems) {
        this.numberOfItems += numberOfItems;
    }

    public void removeNumberOfItems(int numberOfItems) {
        this.numberOfItems -= numberOfItems;
    }


    public void calculateNumberOfItems(){
        numberOfItems = numberOfUnits*getItemsPerUnit();
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

    public BigDecimal getSalesPriceWithoutVat() {
        return getSalesPriceWithoutVat(getSalesPriceForItem());
    }

    public BigDecimal getSalesPriceWithVat() {
        return getSalesPriceWithVat(getSalesPriceForItem());
    }

    public BigDecimal getSalesVatAmount() {
        return getSalesVatAmount(getSalesPriceForItem());
    }

    private BigDecimal getSalesVatAmount(BigDecimal itemPrice){
        BigDecimal salesPriceWithoutVat = getSalesPriceWithoutVat(itemPrice);
        return salesPriceWithoutVat.multiply(getSalesPercentage());
    }

    private BigDecimal getSalesPercentage() {
        return new BigDecimal(getSalesVatRate()).divide(new BigDecimal(100));
    }

    // return 1.06
    BigDecimal getSalesFactor(){
        return BigDecimal.ONE.add(getSalesPercentage());
    }

    private BigDecimal getSalesPriceWithoutVat(BigDecimal itemPrice) {
        BigDecimal salesPriceWithVat = getSalesPriceWithVat(itemPrice);
        return salesPriceWithVat.divide(getSalesFactor(),ROUND_HALF_DOWN);
    }

    private BigDecimal getSalesPriceWithVat(BigDecimal itemPrice) {
        if(itemPrice==null) return BigDecimal.ZERO;
        return itemPrice.multiply(new BigDecimal(numberOfItems));
    }

    public Integer getSalesVatRate() {
        return salesVatRate!=null?salesVatRate:article==null?null:article.getSalesVatRate();
    }

    public void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate;
    }

    // ========== PURCHASE =============


    public BigDecimal getPurchasePriceForItem() {
        BigDecimal purchasePriceForUnit = getPurchasePriceForUnit();
        if(purchasePriceForUnit==null) return null;
        int itemsPerUnit = getItemsPerUnit();
        if (itemsPerUnit == 1) return purchasePriceForUnit;
        return purchasePriceForUnit.divide(new BigDecimal(itemsPerUnit), ROUND_HALF_DOWN);
    }

    public BigDecimal getPurchasePriceForUnit() {
        return purchasePriceForUnit!=null?purchasePriceForUnit:article.getPurchasePrice();
    }

    public void setPurchasePriceForUnit(BigDecimal purchasePriceForUnit) {
        this.purchasePriceForUnit = purchasePriceForUnit;
    }

    private BigDecimal getPurchasePriceWithVat(int number, BigDecimal unitPrice) {
        BigDecimal purchasePriceWithVat = unitPrice.multiply(new BigDecimal(number));
        return purchasePriceWithVat.multiply(getPurchaseFactor());
    }

    private BigDecimal getPurchaseVatAmount(int number, BigDecimal unitPrice){
        BigDecimal purchasePriceWithoutVat = unitPrice.multiply(new BigDecimal(number));
        return purchasePriceWithoutVat.multiply(getPurchasePercentage());
    }

    public BigDecimal getStockValue(){
        BigDecimal itemPrice = getPurchasePriceForItem();
        return itemPrice==null?null:itemPrice.multiply(new BigDecimal(numberOfItems));
    }

    public BigDecimal getPurchasePriceWithoutVat() {
        BigDecimal unitPrice = getPurchasePriceForUnit();
        return unitPrice==null?null:unitPrice.multiply(new BigDecimal(numberOfUnits));
    }

    public BigDecimal getPurchasePriceWithVat() {
        BigDecimal unitPrice = getPurchasePriceForUnit();
        return unitPrice==null?null:getPurchasePriceWithVat(numberOfUnits, unitPrice);
    }

    public BigDecimal getPurchaseVatAmount() {
        BigDecimal unitPrice = getPurchasePriceForUnit();
        return unitPrice==null?null:getPurchaseVatAmount(numberOfUnits, unitPrice);
    }

    private BigDecimal getPurchasePercentage() {
        Integer purchaseVatRate = getPurchaseVatRate();
        return purchaseVatRate==null?null:new BigDecimal(purchaseVatRate).divide(new BigDecimal(100));
    }

    // return 1.06
    BigDecimal getPurchaseFactor(){
        BigDecimal purchasePercentage = getPurchasePercentage();
        return purchasePercentage==null?null:BigDecimal.ONE.add(purchasePercentage);
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

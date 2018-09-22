package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class OrderItem extends BusinessObject{
    private int numberOfUnits, numberOfItems;
    private Article article;
    private BigDecimal priceForItem;
    private BigDecimal priceForUnit;
    private int itemsPerUnit = 0;
    private Integer salesVatRate = null;

    public OrderItem(Integer numberOfUnits, Integer numberOfItems, Article article) {
        this.numberOfUnits = numberOfUnits;
        this.numberOfItems = numberOfItems;
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

    public void setPriceForItem(BigDecimal priceForItem) {
        this.priceForItem = priceForItem;
    }

    public BigDecimal getPriceForItem() {
        return priceForItem!=null?priceForItem:article.getSalesPriceItemWithVat();
    }

    public void setPriceForUnit(BigDecimal priceForUnit) {
        this.priceForUnit = priceForUnit;
    }

    public BigDecimal getPriceForUnit() {
        return priceForUnit!=null?priceForUnit:article.getSalesPriceUnitWithVat();
    }

    public BigDecimal getSalesPriceWithoutVat() {
        return getSalesPriceWithoutVat(numberOfItems, getPriceForItem(), getPriceForUnit());
    }

    public BigDecimal getSalesPriceWithVat() {
        return getSalesPriceWithVat(numberOfItems, getPriceForItem(), getPriceForUnit());
    }

    public BigDecimal getSalesVatAmount() {
        return getSalesVatAmount(numberOfItems, getPriceForItem(), getPriceForUnit());
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

    public int getItemsPerUnit() {
        return itemsPerUnit!=0?itemsPerUnit:article==null?0:article.getItemsPerUnit();
    }

    public void setItemsPerUnit(int itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    public Integer getSalesVatRate() {
        return salesVatRate!=null?salesVatRate:article==null?null:article.getSalesVatRate();
    }

    public void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate;
    }
}

package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class OrderItem extends BusinessObject{
    private int numberOfUnits, numberOfItems;
    private Article article;
    private BigDecimal priceForItem;
    private BigDecimal priceForUnit;

    public OrderItem(Integer numberOfUnits, Integer numberOfItems, Article article) {
        this.numberOfUnits = numberOfUnits;
        this.numberOfItems = numberOfItems;
        this.article = article;
    }

    public OrderItem(Integer number, Article article) {
        setName(article.getName());
        this.numberOfUnits = number==null?0:number;
        this.numberOfItems = number==null?0:number*article.getItemsPerUnit();
        this.article = article;
    }

    public boolean isDeletable() {
        return numberOfItems==0 && numberOfUnits==0;
    }

    public static Predicate<OrderItem> withSalesVatRate(Integer vatRate){
        return orderItem -> orderItem.article!=null && orderItem.article.getSalesVatRate() == vatRate;
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
        numberOfItems = numberOfUnits*article.getItemsPerUnit();
    }

    public void calculateNumberOfUnits(){
        numberOfUnits = numberOfItems/article.getItemsPerUnit();
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
}

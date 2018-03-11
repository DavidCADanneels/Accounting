package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.util.function.Predicate;

public class OrderItem extends BusinessObject{
    private int numberOfUnits, numberOfItems;
    private Article article;

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
        this.numberOfItems = numberOfItems;
    }

    public void addNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits += numberOfUnits;
    }

    public void calculateNumberOfItems(){
        numberOfItems = numberOfUnits*article.getItemsPerUnit();
    }

    public void calculateNumberOfUnits(){
        numberOfItems = numberOfUnits/article.getItemsPerUnit();
    }

    public Article getArticle() {
        return article;
    }

    public static Predicate<OrderItem> ofSupplier(Contact supplier) {
        return orderItem -> orderItem.article.getSupplier() == supplier;
    }

    public static Predicate<OrderItem> forCustomer(Contact customer) {
        return orderItem -> orderItem.article.getCustomer() == customer;
    }
}

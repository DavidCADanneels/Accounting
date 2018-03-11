package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

public class OrderItem extends BusinessObject{
    private int number;
    private Article article;

    public OrderItem(Integer number, Article article) {
        this.number = number==null?0:number;
        this.article = article;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Article getArticle() {
        return article;
    }
}

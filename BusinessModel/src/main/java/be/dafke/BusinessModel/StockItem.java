package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

public class StockItem extends BusinessObject{
    private int number;
    private Article article;

    public StockItem(int number, Article article) {
        this.number = number;
        this.article = article;
    }

    public int getNumber() {
        return number;
    }

    public Article getArticle() {
        return article;
    }
}

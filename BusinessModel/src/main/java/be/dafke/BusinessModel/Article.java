package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class Article extends BusinessObject{
    private String HSCode = "";
    private String itemName = "";
    private Integer purchaseVatRate = 0;
    private Integer salesVatRate = 6;
    private Integer itemsPerUnit = 1;
    private BigDecimal purchasePrice = null;
    private BigDecimal salesPriceItemWithVat = null;
    private BigDecimal salesPriceUnitWithVat = null;
    private Contact supplier;

    public Article(String name){
        setName(name);
    }

    public void setHSCode(String HSCode) {
        this.HSCode = HSCode;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setSalesVatRate(Integer salesVatRate) {
        this.salesVatRate = salesVatRate;
    }

    public void setPurchaseVatRate(Integer purchaseVatRate) {
        this.purchaseVatRate = purchaseVatRate;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSupplier(Contact supplier) {
        this.supplier = supplier;
    }

    public void setItemsPerUnit(Integer itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }


    public void setSalesPriceItemWithVat(BigDecimal salesPriceItemWithVat) {
        this.salesPriceItemWithVat = salesPriceItemWithVat;
    }

    public void setSalesPriceUnitWithVat(BigDecimal salesPriceUnitWithVat) {
        this.salesPriceUnitWithVat = salesPriceUnitWithVat;
    }

    public Integer getItemsPerUnit() {
        return itemsPerUnit;
    }

    public Contact getSupplier() {
        return supplier;
    }

    public BigDecimal getPurchasePrice(){
        return purchasePrice;
    }

    public String getHSCode() {
        return HSCode;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getSalesVatRate() {
        return salesVatRate;
    }

    public BigDecimal getSalesPriceItemWithVat() {
        return salesPriceItemWithVat;
    }

    public BigDecimal getSalesPriceUnitWithVat() {
        return salesPriceUnitWithVat;
    }

    public BigDecimal getSalesPriceItemWithoutVat() {
        if(salesPriceItemWithVat ==null) return null;
        return salesPriceItemWithVat.divide(getSalesFactor(),BigDecimal.ROUND_HALF_DOWN);
    }

    public BigDecimal getSalesPriceUnitWithoutVat() {
        if(salesPriceUnitWithVat ==null) return null;
        BigDecimal salesFactor = getSalesFactor();
        return salesPriceUnitWithVat.divide(salesFactor, BigDecimal.ROUND_HALF_DOWN);
    }

    public Integer getPurchaseVatRate() {
        return purchaseVatRate;
    }

    BigDecimal getPurchasePrice(int number){
        return purchasePrice.multiply(new BigDecimal(number));
    }

    // 6 -> 0.06
    private BigDecimal getPurchasePercentage(){
        return Utils.getPercentage(purchaseVatRate);
    }

    // 6 -> 0.06
    BigDecimal getSalesPercentage(){
        return Utils.getPercentage(salesVatRate);
    }

    // 6 -> 1.06
    private BigDecimal getPurchaseFactor(){
        return Utils.getFactor(purchaseVatRate);
    }

    // 6 -> 1.06
    BigDecimal getSalesFactor(){
        return Utils.getFactor(salesVatRate);
    }

    BigDecimal getPurchasePriceWithVat(){
        return purchasePrice.multiply(getPurchaseFactor());
    }

    BigDecimal getPurchasePriceWithVat(int number){
        return getPurchasePriceWithVat().multiply(new BigDecimal(number));
    }

    public BigDecimal getUnitProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice);
    }

    public BigDecimal getItemProfit(BigDecimal salesprice){
        return salesprice.subtract(purchasePrice.divide(new BigDecimal(itemsPerUnit), BigDecimal.ROUND_HALF_DOWN));
    }

    BigDecimal getPurchaseVat(){
        return purchasePrice.multiply(getPurchasePercentage());
    }

    BigDecimal getPurchaseVat(int number){
        return getPurchaseVat().multiply(new BigDecimal(number));
    }

    public static Predicate<Article> ofSupplier(Contact supplier) {
        return article -> article.getSupplier() == supplier;
    }
}

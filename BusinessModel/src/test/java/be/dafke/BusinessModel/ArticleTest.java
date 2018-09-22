package be.dafke.BusinessModel;

import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;

public class ArticleTest {

    private final BigDecimal purchasePriceWithoutVat = new BigDecimal(100).setScale(2);
    private final BigDecimal purchasePriceVat = new BigDecimal(6).setScale(2);
    private final BigDecimal purchasePriceWithVat = new BigDecimal(106).setScale(2);

    private final BigDecimal purchasePriceWithoutVatFive = new BigDecimal(500).setScale(2);
    private final BigDecimal purchasePriceVatFive = new BigDecimal(30).setScale(2);
    private final BigDecimal purchasePriceWithVatFive = new BigDecimal(530).setScale(2);

    private final BigDecimal salesPriceSingleWithoutVat = new BigDecimal(300).setScale(2);
    private final BigDecimal salesPriceSingleVat = new BigDecimal(18).setScale(2);
    private final BigDecimal salesPriceSingleWithVat = new BigDecimal(318).setScale(2);

    private final BigDecimal salesPricePromoWithoutVat = new BigDecimal(200).setScale(2);
    private final BigDecimal salesPricePromoVat = new BigDecimal(12).setScale(2);
    private final BigDecimal salesPricePromoWithVat = new BigDecimal(212).setScale(2);

    private final BigDecimal salesPriceSingleFiveWithoutVat = new BigDecimal(1500).setScale(2);
    private final BigDecimal salesPriceSingleFiveVat = new BigDecimal(90).setScale(2);
    private final BigDecimal salesPriceSingleFiveWithVat = new BigDecimal(1590).setScale(2);

    private final BigDecimal salesPricePromoFiveWithoutVat = new BigDecimal(1000).setScale(2);
    private final BigDecimal salesPricePromoFiveVat = new BigDecimal(60).setScale(2);
    private final BigDecimal salesPricePromoFiveWithVat = new BigDecimal(1060).setScale(2);

    private final BigDecimal salesPricePromoTenWithoutVat = new BigDecimal(2000).setScale(2);
    private final BigDecimal salesPricePromoTenVat = new BigDecimal(120).setScale(2);
    private final BigDecimal salesPricePromoTenWithVat = new BigDecimal(2120).setScale(2);

    private final String hsCode = "HS-Code";
    private final Integer vatRate = 21;

    @Test
    public void settersAndGetters() {
        Article article = createArticle();
        article.setHSCode(hsCode);
        article.setPurchaseVatRate(vatRate);
        assertEquals(hsCode, article.getHSCode());
        assertEquals(vatRate, article.getPurchaseVatRate());
    }

    @Test
    public void purchasePrices() {
        Article article = createArticle();
        article.setPurchaseVatRate(6);

        assertEquals(purchasePriceWithVat, article.getPurchasePriceWithVat().setScale(2));
        assertEquals(purchasePriceVat, article.getPurchaseVat().setScale(2));
        assertEquals(purchasePriceWithoutVat, article.getPurchasePrice().setScale(2));

        assertEquals(purchasePriceWithVatFive, article.getPurchasePriceWithVat(5).setScale(2));
        assertEquals(purchasePriceVatFive, article.getPurchaseVat(5).setScale(2));
        assertEquals(purchasePriceWithoutVatFive, article.getPurchasePrice(5).setScale(2));
    }

    private Article createArticle(){
        Article article = new Article("name");
        article.setPurchasePrice(purchasePriceWithoutVat);
        article.setSalesPriceItemWithVat(salesPriceSingleWithVat);
        article.setSalesPriceUnitWithVat(salesPricePromoWithVat);
        return article;
    }
}

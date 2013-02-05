package be.dafke.Accounting.Objects.Mortgage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author David C.A. Danneels
 */
public class Calculate {

	public static ArrayList<Vector<BigDecimal>> createFixedAmountTable(BigDecimal startKapitaal, int aantalMaanden,
			BigDecimal mensualiteit, BigDecimal maandPercentage) {
		maandPercentage = maandPercentage.divide(BigDecimal.valueOf(100));
		ArrayList<Vector<BigDecimal>> aflossingsTabel = new ArrayList<Vector<BigDecimal>>();
		BigDecimal roundedMensualiteit = mensualiteit.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleSom = roundedMensualiteit.multiply(new BigDecimal(aantalMaanden));
		BigDecimal totaalIntrest = totaleSom.subtract(startKapitaal);
		System.out.println("totaalIntrest: " + totaalIntrest);
		System.out.println("totaleSom: " + totaleSom);
		BigDecimal restKapitaal = startKapitaal;
		BigDecimal intrest;
		BigDecimal kapitaal;
		for(int i = 1; i < aantalMaanden; i++) {
			intrest = restKapitaal.multiply(maandPercentage);
			intrest = intrest.setScale(2, RoundingMode.HALF_UP);
			kapitaal = roundedMensualiteit.subtract(intrest);
			restKapitaal = restKapitaal.subtract(kapitaal);

			Vector<BigDecimal> vector = new Vector<BigDecimal>();
			vector.add(roundedMensualiteit);
			vector.add(intrest);
			vector.add(kapitaal);
			vector.add(restKapitaal);
			aflossingsTabel.add(vector);
//			System.out.println(i + "  " + roundedMensualiteit + " = " + intrest + " + " + kapitaal + " ==> "
//					+ restKapitaal);
		}
		kapitaal = restKapitaal;
		intrest = roundedMensualiteit.subtract(kapitaal);
		restKapitaal = restKapitaal.subtract(kapitaal);
		Vector<BigDecimal> vector = new Vector<BigDecimal>();
		vector.add(roundedMensualiteit);
		vector.add(intrest);
		vector.add(kapitaal);
		vector.add(restKapitaal);
		aflossingsTabel.add(vector);
//		System.out.println(aantalMaanden + "  " + roundedMensualiteit + " = " + intrest + " + " + kapitaal + " ==> "
//				+ restKapitaal);
//		System.out.println(restKapitaal);
		return aflossingsTabel;
	}

	public static BigDecimal berekenMaandPercentage(BigDecimal jaarPercentage, int aantalPerJaar) {
		BigDecimal part1 = BigDecimal.ONE.add(jaarPercentage.divide(BigDecimal.valueOf(100)));
		BigDecimal part2 = BigDecimal.ONE.divide(new BigDecimal(aantalPerJaar), 10, RoundingMode.HALF_UP);
		BigDecimal percentage = new BigDecimal(Math.pow(part1.doubleValue(), part2.doubleValue()) - 1);
		// percentage = percentage.setScale(10, RoundingMode.HALF_UP); // needed for car
		percentage = percentage.multiply(BigDecimal.valueOf(100L));
		percentage = percentage.setScale(3, RoundingMode.HALF_UP); // OK for AXA
//		System.out.println("maandpercentage: " + percentage.doubleValue());
		return percentage;
	}

	public static BigDecimal berekenJaarPercentage(BigDecimal maandPercentage, int aantalPerJaar) {
		BigDecimal part1 = BigDecimal.ONE.add(maandPercentage.divide(new BigDecimal(100)));
		BigDecimal part2 = new BigDecimal(aantalPerJaar);
		BigDecimal percentage = new BigDecimal(Math.pow(part1.doubleValue(), part2.doubleValue()) - 1);
		// percentage = percentage.setScale(10, RoundingMode.HALF_UP); // needed for car
		percentage = percentage.multiply(BigDecimal.valueOf(100L));
		percentage = percentage.setScale(3, RoundingMode.HALF_UP); // OK for AXA
		// System.out.println("jaarpercentage: " + percentage.doubleValue());
		return percentage;
	}

	public static BigDecimal berekenMensualiteit(BigDecimal startKapitaal, BigDecimal maandPercentage, int aantalMaanden) {
		maandPercentage = maandPercentage.divide(BigDecimal.valueOf(100));
		BigDecimal noemer = new BigDecimal(1 - Math.pow(1 + maandPercentage.doubleValue(), -aantalMaanden));
		BigDecimal factor = maandPercentage.divide(noemer, 10, RoundingMode.HALF_UP);
		BigDecimal mens = startKapitaal.multiply(factor);
//			mensualiteit = mensualiteit.setScale(4, RoundingMode.HALF_UP);
		// System.out.println("mensualiteit: " + mens);
		return mens;
	}

	public static BigDecimal getTotalIntrest(List<Vector<BigDecimal>> table) {
		BigDecimal result = BigDecimal.ZERO;
		for(Vector<BigDecimal> vector : table) {
			result = result.add(vector.get(1));
		}
		return result;
	}

	public static BigDecimal getTotalToPay(List<Vector<BigDecimal>> table) {
		BigDecimal result = BigDecimal.ZERO;
		for(Vector<BigDecimal> vector : table) {
			result = result.add(vector.get(0));
		}
		return result;
	}

	public static ArrayList<Vector<BigDecimal>> createDegressiveAmountTable(BigDecimal startKapitaal,
			int aantalMaanden, BigDecimal maandPercentage) {
		maandPercentage = maandPercentage.divide(BigDecimal.valueOf(100));
		ArrayList<Vector<BigDecimal>> aflossingsTabel = new ArrayList<Vector<BigDecimal>>();

		BigDecimal monthlyCapital = startKapitaal.divide(BigDecimal.valueOf(aantalMaanden), 2, RoundingMode.HALF_UP);
//		BigDecimal roundedMonthlyCapital = monthlyCapital.setScale(2, RoundingMode.HALF_UP);
		BigDecimal totaleSom = BigDecimal.ZERO;
		BigDecimal totaleIntrest = BigDecimal.ZERO;

//		BigDecimal roundedMensualiteit = mensualiteit.setScale(2, RoundingMode.HALF_UP);
//		BigDecimal totaleSom = roundedMensualiteit.multiply(new BigDecimal(aantalMaanden));
//		BigDecimal totaalIntrest = totaleSom.subtract(startKapitaal);
		BigDecimal restKapitaal = startKapitaal;
		BigDecimal intrest;
//		BigDecimal kapitaal;
		BigDecimal totaal;
		for(int i = 1; i < aantalMaanden; i++) {
			intrest = restKapitaal.multiply(maandPercentage);
			intrest = intrest.setScale(2, RoundingMode.HALF_UP);
//			kapitaal = roundedMensualiteit.subtract(intrest);
			restKapitaal = restKapitaal.subtract(monthlyCapital);
			totaal = monthlyCapital.add(intrest);

			totaleIntrest = totaleIntrest.add(intrest);
			totaleSom = totaleSom.add(totaal);

			Vector<BigDecimal> vector = new Vector<BigDecimal>();
//			vector.add(roundedMensualiteit);
			vector.add(totaal);
			vector.add(intrest);
//			vector.add(kapitaal);
			vector.add(monthlyCapital);
			vector.add(restKapitaal);
			aflossingsTabel.add(vector);
//			System.out.println(i + "  " + totaal/*roundedMensualiteit*/+ " = " + intrest + " + " + monthlyCapital/*kapitaal*/
//					+ " ==> " + restKapitaal);
		}
//		kapitaal = restKapitaal;
//		intrest = roundedMensualiteit.subtract(kapitaal);
		intrest = restKapitaal.multiply(maandPercentage);
		intrest = intrest.setScale(2, RoundingMode.HALF_UP);
//		restKapitaal = restKapitaal.subtract(kapitaal);
		totaal = restKapitaal.add(intrest);

		totaleIntrest = totaleIntrest.add(intrest);
		totaleSom = totaleSom.add(totaal);

		Vector<BigDecimal> vector = new Vector<BigDecimal>();
//		vector.add(roundedMensualiteit);
		vector.add(totaal);
		vector.add(intrest);
//		vector.add(kapitaal);
		vector.add(restKapitaal);
		vector.add(BigDecimal.ZERO);
		aflossingsTabel.add(vector);
		System.out.println("totaalIntrest: " + totaleIntrest);
		System.out.println("totaleSom: " + totaleSom);
//		System.out.println(aantalMaanden + "  " + roundedMensualiteit + " = " + intrest + " + " + kapitaal + " ==> "
//				+ restKapitaal);
//		System.out.println(restKapitaal);
		return aflossingsTabel;
	}
}
package be.dafke.Accounting.BusinessModel

class Calculate {

    static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP

    static Mortgage createFixedAmountTable(BigDecimal startKapitaal, int aantalMaanden, BigDecimal mensualiteit, BigDecimal maandPercentage) {
        maandPercentage = maandPercentage.divide(BigDecimal.valueOf(100))
        Mortgage aflossingsTabel = new Mortgage()
        BigDecimal roundedMensualiteit = mensualiteit.setScale(2, ROUNDING_MODE)
        BigDecimal totaleSom = roundedMensualiteit.multiply(new BigDecimal(aantalMaanden))
        BigDecimal totaalIntrest = totaleSom.subtract(startKapitaal)
        System.out.println("totaalIntrest: " + totaalIntrest)
        System.out.println("totaleSom: " + totaleSom)
        BigDecimal restKapitaal = startKapitaal
        BigDecimal intrest
        BigDecimal kapitaal
        for(int i = 1; i < aantalMaanden; i++) {
            intrest = restKapitaal.multiply(maandPercentage)
            intrest = intrest.setScale(2, ROUNDING_MODE)
            kapitaal = roundedMensualiteit.subtract(intrest)
            restKapitaal = restKapitaal.subtract(kapitaal)

            MortgageTransaction vector = new MortgageTransaction()
            vector.setNr(i)
            vector.setMensuality(roundedMensualiteit)
            vector.setIntrest(intrest)
            vector.setCapital(kapitaal)
            vector.setRestCapital(restKapitaal)
            aflossingsTabel.addBusinessObject(vector)
        }
        kapitaal = restKapitaal
        intrest = roundedMensualiteit.subtract(kapitaal)
        restKapitaal = restKapitaal.subtract(kapitaal)
        MortgageTransaction vector = new MortgageTransaction()
        vector.setNr(aantalMaanden)
        vector.setMensuality(roundedMensualiteit)
        vector.setIntrest(intrest)
        vector.setCapital(kapitaal)
        vector.setRestCapital(restKapitaal)
        aflossingsTabel.addBusinessObject(vector)
        aflossingsTabel
    }

    static BigDecimal berekenMaandPercentage(BigDecimal jaarPercentage, int aantalPerJaar) {
        BigDecimal part1 = BigDecimal.ONE.add(jaarPercentage.divide(BigDecimal.valueOf(100)))
        BigDecimal part2 = BigDecimal.ONE.divide(new BigDecimal(aantalPerJaar), 10, ROUNDING_MODE)
        BigDecimal percentage = new BigDecimal(Math.pow(part1.doubleValue(), part2.doubleValue()) - 1)
        // percentage = percentage.setScale(10, ROUNDING_MODE) // needed for car
        percentage = percentage.multiply(BigDecimal.valueOf(100L))
        percentage = percentage.setScale(3, ROUNDING_MODE) // OK for AXA
//		System.out.println("maandpercentage: " + percentage.doubleValue())
        percentage
    }

    static BigDecimal berekenJaarPercentage(BigDecimal maandPercentage, int aantalPerJaar) {
        BigDecimal part1 = BigDecimal.ONE.add(maandPercentage.divide(new BigDecimal(100)))
        BigDecimal part2 = new BigDecimal(aantalPerJaar)
        BigDecimal percentage = new BigDecimal(Math.pow(part1.doubleValue(), part2.doubleValue()) - 1)
        // percentage = percentage.setScale(10, ROUNDING_MODE) // needed for car
        percentage = percentage.multiply(BigDecimal.valueOf(100L))
        percentage = percentage.setScale(3, ROUNDING_MODE) // OK for AXA
        // System.out.println("jaarpercentage: " + percentage.doubleValue())
        percentage
    }

    static BigDecimal berekenMensualiteit(BigDecimal startKapitaal, BigDecimal maandPercentage, int aantalMaanden) {
        maandPercentage = maandPercentage.divide(BigDecimal.valueOf(100))
        BigDecimal noemer = new BigDecimal(1 - Math.pow(1 + maandPercentage.doubleValue(), -aantalMaanden))
        BigDecimal factor = maandPercentage.divide(noemer, 10, ROUNDING_MODE)
        BigDecimal mens = startKapitaal.multiply(factor)
//			mensualiteit = mensualiteit.setScale(4, ROUNDING_MODE)
        // System.out.println("mensualiteit: " + mens)
        mens
    }

    static Mortgage createDegressiveAmountTable(BigDecimal startKapitaal,
                                                       int aantalMaanden, BigDecimal maandPercentage) {
        maandPercentage = maandPercentage.divide(BigDecimal.valueOf(100))
        Mortgage aflossingsTabel = new Mortgage()

        BigDecimal monthlyCapital = startKapitaal.divide(BigDecimal.valueOf(aantalMaanden), 2, ROUNDING_MODE)
//		BigDecimal roundedMonthlyCapital = monthlyCapital.setScale(2, ROUNDING_MODE)
        BigDecimal totaleSom = BigDecimal.ZERO
        BigDecimal totaleIntrest = BigDecimal.ZERO

//		BigDecimal roundedMensualiteit = mensualiteit.setScale(2, ROUNDING_MODE)
//		BigDecimal totaleSom = roundedMensualiteit.multiply(new BigDecimal(aantalMaanden))
//		BigDecimal totaalIntrest = totaleSom.subtract(startKapitaal)
        BigDecimal restKapitaal = startKapitaal
        BigDecimal intrest
//		BigDecimal kapitaal
        BigDecimal totaal
        for(int i = 1; i < aantalMaanden; i++) {
            intrest = restKapitaal.multiply(maandPercentage)
            intrest = intrest.setScale(2, ROUNDING_MODE)
//			kapitaal = roundedMensualiteit.subtract(intrest)
            restKapitaal = restKapitaal.subtract(monthlyCapital)
            totaal = monthlyCapital.add(intrest)

            totaleIntrest = totaleIntrest.add(intrest)
            totaleSom = totaleSom.add(totaal)

            MortgageTransaction vector = new MortgageTransaction()
            vector.setNr(i)
//			vector.setMensuality((roundedMensualiteit)
            vector.setMensuality(totaal)
            vector.setIntrest(intrest)
//			vector.setCapital(kapitaal)
            vector.setCapital(monthlyCapital)
            vector.setRestCapital(restKapitaal)
            aflossingsTabel.addBusinessObject(vector)
//			System.out.println(i + "  " + totaal/*roundedMensualiteit*/+ " = " + intrest + " + " + monthlyCapital/*kapitaal*/
//					+ " ==> " + restKapitaal)
        }
//		kapitaal = restKapitaal
//		intrest = roundedMensualiteit.subtract(kapitaal)
        intrest = restKapitaal.multiply(maandPercentage)
        intrest = intrest.setScale(2, ROUNDING_MODE)
//		restKapitaal = restKapitaal.subtract(kapitaal)
        totaal = restKapitaal.add(intrest)

        totaleIntrest = totaleIntrest.add(intrest)
        totaleSom = totaleSom.add(totaal)

        MortgageTransaction vector = new MortgageTransaction()
        vector.setNr(aantalMaanden)
//		vector.setMensuality(roundedMensualiteit)
        vector.setMensuality(totaal)
        vector.setIntrest(intrest)
//		vector.setCapital(kapitaal)
        vector.setCapital(restKapitaal)
        vector.setRestCapital(BigDecimal.ZERO)
        aflossingsTabel.addBusinessObject(vector)
        System.out.println("totaalIntrest: " + totaleIntrest)
        System.out.println("totaleSom: " + totaleSom)
//		System.out.println(aantalMaanden + "  " + roundedMensualiteit + " = " + intrest + " + " + kapitaal + " ==> "
//				+ restKapitaal)
//		System.out.println(restKapitaal)
        aflossingsTabel
    }
}
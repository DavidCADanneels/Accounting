package be.dafke.Accounting.BusinessModel

enum Unit {
    PIECE ("Piece", ""),
    WEIGHT ("Weight", "g."),
    VOLUME ("Volume", "ml.")

    final String name
    final String symbol

    Unit(String name, String symbol) {
        this.name = name
        this.symbol = symbol
    }

    String getName() {
        name
    }

    String getSymbol() {
        symbol
    }

    String toString(){
        if(symbol.equals("")) name
        else name + " (" + symbol + ")"
    }
}

package be.dafke.BusinessModel;

public enum Unit {
    PIECE ("Piece", ""),
    WEIGHT ("Weight", "g."),
    VOLUME ("Volume", "l."),
    LENGTH ("Length", "m.");

    private final String name;
    private final String symbol;

    Unit(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String toString(){
        if(symbol.equals("")) return name;
        else return name + " (" + symbol + ")";
    }
}

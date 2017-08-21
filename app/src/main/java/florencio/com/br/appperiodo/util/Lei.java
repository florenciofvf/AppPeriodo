package florencio.com.br.appperiodo.util;

public class Lei {
    private final long toleranciaSaida;
    private final long excessoExtra;

    public Lei(long toleranciaSaida, long excessoExtra) {
        this.toleranciaSaida = toleranciaSaida;
        this.excessoExtra = excessoExtra;
    }

    public long getToleranciaSaida() {
        return toleranciaSaida;
    }

    public long getExcessoExtra() {
        return excessoExtra;
    }
}
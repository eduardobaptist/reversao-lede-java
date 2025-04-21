package lede;

public class Elemento {
    public Integer valor;
    public Integer anterior;
    public Integer proximo;

    public Elemento(Integer valor) {
        this.valor = valor;
        this.proximo = null;
        this.anterior = null;
    }

    @Override
    public String toString() {
        return "\n\tlede.Elemento{" +
                "valor=" + valor +
                ", anterior=" + anterior +
                ", proximo=" + proximo +
                "}";
    }
}

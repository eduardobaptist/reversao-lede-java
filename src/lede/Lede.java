package lede;

import java.util.ArrayList;

import static java.util.Objects.isNull;

public class Lede {

    private Integer inicio;
    private Integer fim;
    public ArrayList<Elemento> lista;

    public Lede() {
        this.inicio = null;
        this.fim = null;

        this.lista = new ArrayList<>(10);
    }

    public void incluir(Integer valor) {
        if (this.lista.size() < 10) {
            if (this.inicio == null) {
                this.lista.addFirst(new Elemento(valor));
                this.inicio = 0;
                this.fim = 0;
            } else {
                Integer anterior = null;
                Integer atual = inicio;

                while (atual != null && lista.get(atual).valor < valor) {
                    anterior = atual;
                    atual = lista.get(atual).proximo;
                }

                //nesse momento, "atual" é o primeiro maior valor que o valor do novo elemento

                this.lista.add(new Elemento(valor));
                int novo = this.lista.size() - 1;

                if (anterior == null) {
                    // anterior nao foi alimentado no while, porque nao achou nenhum valor na lista que seja menor que o valor novo, então ele é o primeiro
                    // inserindo no início
                    this.lista.get(novo).proximo = atual;
                    this.inicio = novo;
                    this.lista.get(atual).anterior = novo;
                } else if (isNull(atual)) {
                    // inserindo no fim
                    this.lista.get(novo).anterior = anterior;
                    this.lista.get(anterior).proximo = novo;
                } else {
                    // inserindo no "meio", depois do menor antes do maior
                    this.lista.get(novo).proximo = atual;
                    this.lista.get(novo).anterior = anterior;

                    this.lista.get(atual).anterior = novo;

                    this.lista.get(anterior).proximo = novo;
                }

                if (isNull(this.lista.get(novo).proximo)) {
                    this.fim = novo;
                }
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public String exibirInicioAoFim() {
        Integer atual = this.inicio;
        StringBuilder txt = new StringBuilder();

        txt.append("Elementos do início ao fim: ");

        while(atual != null) {
            Elemento item = this.lista.get(atual);

            if (isNull(item.proximo)) {
                txt.append(String.format("[%d]", item.valor));
            } else {
                txt.append(String.format("[%d] -> ", item.valor));
            }

            atual = item.proximo;
        }
        
        return txt.toString();
    }

    public String exibirFimAoInicio() {
        Integer atual = this.fim;
        StringBuilder txt = new StringBuilder();

        txt.append("Elementos do fim ao início: ");

        while(atual != null) {
            Elemento item = this.lista.get(atual);

            if (isNull(item.anterior)) {
                txt.append(String.format("[%d]", item.valor));
            } else {
                txt.append(String.format("[%d] -> ", item.valor));
            }

            atual = item.anterior;
        }

        return txt.toString();
    }

    public void inverterOrdem() {
        Integer atual = this.inicio;
        Integer inicio = atual;
        while (!isNull(atual)) {
            Integer proximo = this.lista.get(atual).proximo;

            this.lista.get(atual).proximo = this.lista.get(atual).anterior;
            this.lista.get(atual).anterior = proximo;

            atual = this.lista.get(atual).anterior;
        }

        this.inicio = this.fim;
        this.fim = inicio;
    }

    public void remover(Integer indice) {
        Elemento itemRemover  = this.lista.get(indice);

        Integer itemAnterior = itemRemover.anterior;
        Integer itemProximo = itemRemover.proximo;

        if (indice.equals(this.inicio)) {
            // item removido é o primeiro, então o início é o próximo do que era o primeiro
            this.inicio = itemProximo;
        }

        if (indice.equals(this.fim)) {
            // item removido é o último, então o fim é o anterior do que era o último
            this.fim = itemAnterior;
        }

        if (!isNull(itemAnterior)) {
            // próximo do anterior do que é removido agora é o que era o próximo dele
            this.lista.get(itemAnterior).proximo = itemProximo;
        }

        if (!isNull(itemProximo)) {
            // anterior do próximo do que é removido agora é o que era o anterior dele
            this.lista.get(itemProximo).anterior = itemAnterior;
        }

        this.lista.remove((int) indice);
    }

    @Override
    public String toString() {
        return "lede.Lede{" +
                "inicio=" + inicio +
                ", fim=" + fim +
                ", lista=" + lista +
                "\n}";
    }

    /*public static void main(String[] args) {
        Lede lede = new Lede();


        lede.incluir(1);
        lede.incluir(20);
        lede.incluir(4);
        lede.incluir(2);
        lede.incluir(5);

        //lede.inverterOrdem();
        System.out.println(lede);
        lede.exibirInicioAoFim();
        lede.exibirFimAoInicio();
        lede.remover(1);
    }*/
}

package com.zetacompute.models;

import java.util.Map;
import java.util.Objects;

public class NoResultado implements Expressao {
    private final NumeroComplexo valor;

    public NoResultado(NumeroComplexo valor) {
        this.valor = valor;
    }

    @Override
    public NumeroComplexo avaliar(Map<String, NumeroComplexo> variaveis) {
        return valor;
    }

    @Override
    public void exibirArvore() {
        exibirArvore("", false);
    }

    @Override
    public void exibirArvore(String prefixo, boolean isLeft) {
        System.out.println(prefixo + (isLeft ? "├── " : "└── ") + "Resultado: " + valor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoResultado)) return false;
        NoResultado that = (NoResultado) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}

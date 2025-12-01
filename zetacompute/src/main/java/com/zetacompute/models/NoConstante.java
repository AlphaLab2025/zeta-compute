package com.zetacompute.models;

import com.zetacompute.models.NumeroComplexo;
import java.util.Map;
import java.util.Objects;

public class NoConstante implements Expressao {
    private final NumeroComplexo valor;

    public NoConstante(NumeroComplexo valor) {
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
        System.out.println(prefixo + (isLeft ? "├── " : "└── ") + valor);
    }

    // Verifica se dois nós constantes são iguais (Requisito 3)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoConstante)) return false;
        NoConstante that = (NoConstante) o;
        return Objects.equals(valor, that.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
package com.zetacompute.models;

import com.zetacompute.models.NumeroComplexo;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    // Verificar se dois nós constantes são iguais (Requisito 3) Feito
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

    @Override
    public Set<String> getVariaveis() {
        return Collections.emptySet(); // Constante não tem variável
    }

    @Override
    public String toLisp() {
        // Retorna o número. Ex: "2+3i"
        return valor.toString();
    }

}
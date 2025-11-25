package com.zetacompute.models;

import com.zetacompute.models.NumeroComplexo;
import java.util.Map;
import java.util.Objects;

public class NoVariavel implements Expressao {
    private final String nome;

    public NoVariavel(String nome) {
        this.nome = nome;
    }

    @Override
    public NumeroComplexo avaliar(Map<String, NumeroComplexo> variaveis) {
        // Atende Requisito 5 (Detectar erros)
        if (!variaveis.containsKey(nome)) {
            throw new IllegalArgumentException("Erro: Variável '" + nome + "' não foi definida.");
        }
        return variaveis.get(nome);
    }

    @Override
    public void exibirArvore() {
        exibirArvore("", false);
    }

    @Override
    public void exibirArvore(String prefixo, boolean isLeft) {
        System.out.println(prefixo + (isLeft ? "├── " : "└── ") + "Var(" + nome + ")");
    }

    // Verifica se duas variáveis são a mesma (Requisito 3)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoVariavel)) return false;
        NoVariavel that = (NoVariavel) o;
        return Objects.equals(nome, that.nome);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
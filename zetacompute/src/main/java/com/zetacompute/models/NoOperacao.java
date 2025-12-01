package com.zetacompute.models;

import com.zetacompute.models.NumeroComplexo;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NoOperacao implements Expressao {

    public enum Operador {
        SOMA, SUBTRACAO, MULTIPLICACAO, DIVISAO, POTENCIA, RAIZ, CONJUGADO
    }

    private final Expressao esquerda;
    private final Expressao direita; // Pode ser null para operações unárias (Conjugado, Raiz)
    private final Operador operador;
    private final int parametroAuxiliar; // Usado para o 'n' da Raiz n-ésima

    // Construtor para Operações Binárias (+, -, *, /, **)
    public NoOperacao(Expressao esquerda, Expressao direita, Operador operador) {
        this.esquerda = esquerda;
        this.direita = direita;
        this.operador = operador;
        this.parametroAuxiliar = 0;
    }

    // Construtor para Raiz (Raiz n-ésima)
    public NoOperacao(Expressao esquerda, int grauRaiz) {
        this.esquerda = esquerda;
        this.direita = null;
        this.operador = Operador.RAIZ;
        this.parametroAuxiliar = grauRaiz;
    }
    
    // Construtor para Conjugado
    public NoOperacao(Expressao esquerda) {
        this.esquerda = esquerda;
        this.direita = null;
        this.operador = Operador.CONJUGADO;
        this.parametroAuxiliar = 0;
    }

    @Override
    public NumeroComplexo avaliar(Map<String, NumeroComplexo> variaveis) {
        NumeroComplexo valEsq = esquerda.avaliar(variaveis);
        
        // Operações Unárias
        if (operador == Operador.CONJUGADO) return valEsq.getConjugado();
        if (operador == Operador.RAIZ) return valEsq.raiz(parametroAuxiliar);

        // Operações Binárias
        NumeroComplexo valDir = direita.avaliar(variaveis);

        switch (operador) {
            case SOMA: return valEsq.somar(valDir);
            case SUBTRACAO: return valEsq.subtrair(valDir);
            case MULTIPLICACAO: return valEsq.multiplicar(valDir);
            case DIVISAO: return valEsq.dividir(valDir); // Exceções já tratadas na classe NumeroComplexo
            case POTENCIA: return valEsq.potencia((int) valDir.getReal()); // Assume expoente real inteiro
            default: throw new UnsupportedOperationException("Operador desconhecido");
        }
    }

    @Override
    public void exibirArvore() {
        System.out.println("Raiz: " + operadorToString());
        if (esquerda != null) esquerda.exibirArvore("", true);
        if (direita != null) direita.exibirArvore("", false);
    }

    @Override
    public void exibirArvore(String prefixo, boolean isLeft) {
        System.out.println(prefixo + (isLeft ? "├── " : "└── ") + operadorToString());
        
        String novoPrefixo = prefixo + (isLeft ? "│   " : "    ");
        
        if (esquerda != null) esquerda.exibirArvore(novoPrefixo, direita != null); // Se tiver direita, esquerda é 'true' (com braço)
        if (direita != null) direita.exibirArvore(novoPrefixo, false);
    }

    private String operadorToString() {
        if (operador == Operador.RAIZ) return "RAIZ(" + parametroAuxiliar + ")";
        return operador.toString();
    }

    // Requisito 3: Verificar igualdade de expressões (árvores) Feito
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoOperacao)) return false;
        NoOperacao that = (NoOperacao) o;
        return parametroAuxiliar == that.parametroAuxiliar &&
                operador == that.operador &&
                Objects.equals(esquerda, that.esquerda) &&
                Objects.equals(direita, that.direita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(esquerda, direita, operador, parametroAuxiliar);
    }

    @Override
    public Set<String> getVariaveis() {
        Set<String> vars = new HashSet<>();
        if (esquerda != null) vars.addAll(esquerda.getVariaveis());
        if (direita != null) vars.addAll(direita.getVariaveis());
        return vars;
    }
    @Override
    public String toLisp() {
        String opSimbolo = getSimboloLisp();
        
        if (direita == null) {
            // Operação Unária: (OP FILHO) -> Ex: (conj 2+3i)
            // Para raiz sera incluido o grau: (root 2 exp)
            if (operador == Operador.RAIZ) {
                return "(" + opSimbolo + " " + parametroAuxiliar + " " + esquerda.toLisp() + ")";
            }
            return "(" + opSimbolo + " " + esquerda.toLisp() + ")";
        } else {
            // Operação Binária: (OP ESQ DIR) -> Ex: (+ 2 3)
            return "(" + opSimbolo + " " + esquerda.toLisp() + " " + direita.toLisp() + ")";
        }
    }

    // Método auxiliar para converter o ENUM em símbolo LISP
    private String getSimboloLisp() {
        switch (operador) {
            case SOMA: return "+";
            case SUBTRACAO: return "-";
            case MULTIPLICACAO: return "*";
            case DIVISAO: return "/";
            case POTENCIA: return "^"; // ou "pow"
            case RAIZ: return "root";
            case CONJUGADO: return "conj";
            default: return "?";
        }
    }

}
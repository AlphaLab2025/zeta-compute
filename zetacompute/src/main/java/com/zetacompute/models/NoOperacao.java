package com.zetacompute.models;

import com.zetacompute.models.NumeroComplexo;

import java.util.HashSet;
import com.zetacompute.utils.NumberToPortuguese;
import java.util.Map;
import java.util.HashMap;
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
        int count = countResultados();
        String palavra = NumberToPortuguese.toPortuguese(count);
        String texto = (count == 1) ? "operação feita" : "operações feitas";
        System.out.println("Raiz: " + palavra + " " + texto);
        // Build ordinals for operation-with-result nodes
        Map<NoOperacao, Integer> ordinais = new HashMap<>();
        buildOrdinals(ordinais, new int[]{1});

        // Exibir filhos diretamente sem o nó conector
        exibirFilhosDirectamente("", ordinais);
    }

    /**
     * Exibe todos os filhos deste nó, sem mostrar o operador deste nó.
     * Usado para suprimir nós conectores na raiz.
     */
    private void exibirFilhosDirectamente(String prefixo, Map<NoOperacao, Integer> ordinais) {
        boolean hasRightChild = (direita != null);
        
        if (esquerda != null) {
            if (esquerda instanceof NoOperacao) ((NoOperacao) esquerda).exibirArvore(prefixo, hasRightChild, ordinais);
            else esquerda.exibirArvore(prefixo, hasRightChild);
        }
        if (direita != null) {
            if (direita instanceof NoOperacao) ((NoOperacao) direita).exibirArvore(prefixo, false, ordinais);
            else direita.exibirArvore(prefixo, false);
        }
    }

    @Override
    public void exibirArvore(String prefixo, boolean isLeft) {
        System.out.println(prefixo + (isLeft ? "├── " : "└── ") + operadorToString());
        
        String novoPrefixo = prefixo + (isLeft ? "│   " : "    ");
        
        if (esquerda != null) esquerda.exibirArvore(novoPrefixo, direita != null); // Se tiver direita, esquerda é 'true' (com braço)
        if (direita != null) direita.exibirArvore(novoPrefixo, false);
    }

    // Overload used when we have ordinals for operation-with-result nodes
    public void exibirArvore(String prefixo, boolean isLeft, Map<NoOperacao, Integer> ordinais) {
        String label;
        if (ordinais.containsKey(this)) {
            int idx = ordinais.get(this);
            String palavraIdx = NumberToPortuguese.toPortugueseOrdinalFeminine(idx);
            label = palavraIdx + " operação";
        } else {
            label = operadorToString();
        }
        // If this node is an operation-with-result marker (in ordinais) and its left is the actual operation
        if (ordinais.containsKey(this) && esquerda instanceof NoOperacao) {
            NoOperacao inner = (NoOperacao) esquerda;
            // Print label and operator on the same line: "Primeira operação: SOMA"
            System.out.println(prefixo + (isLeft ? "├── " : "└── ") + label + ": " + inner.operadorToString());

            String novoPrefixo = prefixo + (isLeft ? "│   " : "    ");

            // Print inner operands (inner.left and inner.right)
            if (inner.esquerda != null) {
                if (inner.esquerda instanceof NoOperacao) ((NoOperacao) inner.esquerda).exibirArvore(novoPrefixo, true, ordinais);
                else inner.esquerda.exibirArvore(novoPrefixo, true);
            }
            if (inner.direita != null) {
                // marcamos o segundo operando como não-final para que o resultado fique como último (└──)
                if (inner.direita instanceof NoOperacao) ((NoOperacao) inner.direita).exibirArvore(novoPrefixo, true, ordinais);
                else inner.direita.exibirArvore(novoPrefixo, true);
            }

            // Then print the result (this.direita) as the final child
            if (direita != null) direita.exibirArvore(novoPrefixo, false);
            return;
        }

        // Default printing
        System.out.println(prefixo + (isLeft ? "├── " : "└── ") + label);

        String novoPrefixo = prefixo + (isLeft ? "│   " : "    ");

        if (esquerda != null) {
            if (esquerda instanceof NoOperacao) ((NoOperacao) esquerda).exibirArvore(novoPrefixo, direita != null, ordinais);
            else esquerda.exibirArvore(novoPrefixo, direita != null);
        }
        if (direita != null) {
            if (direita instanceof NoOperacao) ((NoOperacao) direita).exibirArvore(novoPrefixo, false, ordinais);
            else direita.exibirArvore(novoPrefixo, false);
        }
    }

    // Preenche o mapa com índices (1-based) para cada NoOperacao que contém um NoResultado à direita
    private void buildOrdinals(Map<NoOperacao, Integer> ordinais, int[] counter) {
        if (esquerda instanceof NoOperacao) ((NoOperacao) esquerda).buildOrdinals(ordinais, counter);

        if (direita != null && direita.getClass().getSimpleName().equals("NoResultado")) {
            ordinais.put(this, counter[0]++);
        }

        if (direita instanceof NoOperacao) ((NoOperacao) direita).buildOrdinals(ordinais, counter);
    }

    private String operadorToString() {
        if (operador == Operador.RAIZ) return "RAIZ(" + parametroAuxiliar + ")";
        return operador.toString();
    }

    // Requisito 3: Verificar igualdade de expressões (árvores) Feito
    // Conta quantos nós de resultado existem na subárvore.
    private int countResultados() {
        int cnt = 0;

        if (esquerda != null) {
            if (esquerda instanceof NoOperacao) {
                cnt += ((NoOperacao) esquerda).countResultados();
            } else if (esquerda.getClass().getSimpleName().equals("NoResultado")) {
                cnt += 1;
            }
        }

        if (direita != null) {
            if (direita instanceof NoOperacao) {
                cnt += ((NoOperacao) direita).countResultados();
            } else if (direita.getClass().getSimpleName().equals("NoResultado")) {
                cnt += 1;
            }
        }

        return cnt;
    }

    // Requisito 3: Verificar igualdade de expressões (árvores)
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
package com.zetacompute.test;

import com.zetacompute.models.*;
import com.zetacompute.models.NoOperacao.Operador;
import com.zetacompute.models.NumeroComplexo;

public class Runner {
    public static void main(String[] args) {
        // montar operação 1: (3+4i) + (3+4i)
        Expressao a1 = new NoConstante(new NumeroComplexo(3,4));
        Expressao b1 = new NoConstante(new NumeroComplexo(3,4));
        Expressao op1 = new NoOperacao(a1, b1, Operador.SOMA);
        NumeroComplexo res1 = op1.avaliar(null);
        Expressao op1WithResult = new NoOperacao(op1, new NoResultado(res1), Operador.SOMA);

        // montar operação 2: (3+4i) - (3+4i)
        Expressao a2 = new NoConstante(new NumeroComplexo(3,4));
        Expressao b2 = new NoConstante(new NumeroComplexo(3,4));
        Expressao op2 = new NoOperacao(a2, b2, Operador.SUBTRACAO);
        NumeroComplexo res2 = op2.avaliar(null);
        Expressao op2WithResult = new NoOperacao(op2, new NoResultado(res2), Operador.SOMA);

        // raiz conectando as duas operações
        Expressao raiz = new NoOperacao(op1WithResult, op2WithResult, Operador.SOMA);

        System.out.println("--- Test Runner: Árvore de Operações ---\n");
        raiz.exibirArvore();
    }
}

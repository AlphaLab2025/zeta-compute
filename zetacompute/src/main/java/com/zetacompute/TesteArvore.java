package com.zetacompute;

import com.zetacompute.models.Expressao;
import com.zetacompute.models.NoConstante;
import com.zetacompute.models.NoOperacao;
import com.zetacompute.models.NoVariavel;
import com.zetacompute.models.NumeroComplexo;
import java.util.HashMap;
import java.util.Map;

public class TesteArvore {
    public static void main(String[] args) {
        // teste(2 + 5i) + (z * 3)
        
        // 1. Criar as folhas
        Expressao noA = new NoConstante(new NumeroComplexo(2, 5));
        Expressao noZ = new NoVariavel("z");
        Expressao no3 = new NoConstante(new NumeroComplexo(3, 0));

        // 2. Montar a árvore
        Expressao mult = new NoOperacao(noZ, no3, NoOperacao.Operador.MULTIPLICACAO);

        Expressao arvore = new NoOperacao(noA, mult, NoOperacao.Operador.SOMA);

        // Mostrar a árvore
        System.out.println("--- Árvore Visual ---");
        arvore.exibirArvore();

        // REQUISITO 7: Calcular variáveis
        Map<String, NumeroComplexo> variaveis = new HashMap<>();
        variaveis.put("z", new NumeroComplexo(1, 1)); 

        // REQUISITO 4: Executar expressão
        NumeroComplexo resultado = arvore.avaliar(variaveis);
        
        System.out.println("\nResultado para z=(1+1i): " + resultado); 
    }
}
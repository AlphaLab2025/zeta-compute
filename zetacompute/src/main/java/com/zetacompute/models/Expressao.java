package com.zetacompute.models;

import com.zetacompute.models.NumeroComplexo;
import java.util.Map;

public interface Expressao {
    
    // Calcula o valor final
    NumeroComplexo avaliar(Map<String, NumeroComplexo> variaveis);

    // Imprime a árvore visualmente 
    void exibirArvore();
    
    // Método auxiliar para a recursão da impressão
    void exibirArvore(String prefixo, boolean isLeft);
}
package com.zetacompute.models;

import com.zetacompute.models.NumeroComplexo;
import java.util.Map;
import java.util.Set;

public interface Expressao {
    NumeroComplexo avaliar(Map<String, NumeroComplexo> variaveis);
    void exibirArvore();
    void exibirArvore(String prefixo, boolean isLeft);
    Set<String> getVariaveis();

    String toLisp();
}
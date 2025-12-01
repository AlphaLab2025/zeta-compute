package com.zetacompute.models;

public class parser {

    private String source;
    private int pos = -1;
    private int ch;

    public parser(String source) {
        this.source = source;
    }

    public Expressao parse() {
        proximoChar();
        Expressao x = parseExpressao();
        if (pos < source.length()) {
            throw new IllegalArgumentException("Caractere inesperado: " + (char) ch);
        }
        return x;
    }

    // Avança para o próximo caractere
    private void proximoChar() {
        ch = (++pos < source.length()) ? source.charAt(pos) : -1;
    }

    // Verifica se o caractere atual é o esperado e avança. Pula espaços em branco.
    private boolean comer(int charToEat) {
        while (ch == ' ') proximoChar();
        if (ch == charToEat) {
            proximoChar();
            return true;
        }
        return false;
    }

    // --- Níveis do calculo ---

    // Nível 1: Soma e Subtração (Menor prioridade)
    private Expressao parseExpressao() {
        Expressao x = parseTermo();
        for (;;) {
            if (comer('+')){
                x = new NoOperacao(x, parseTermo(), NoOperacao.Operador.SOMA);
            }
            else if (comer('-')){
                x = new NoOperacao(x, parseTermo(), NoOperacao.Operador.SUBTRACAO);
            }
            else{
                return x;
            }
        }
    }

    // Nível 2: Multiplicação e Divisão
    private Expressao parseTermo() {
        Expressao x = parseFator();
        for (;;) {
            if (comer('*')) {
                x = new NoOperacao(x, parseFator(), NoOperacao.Operador.MULTIPLICACAO);
            }
            else if (comer('/')) {
                x = new NoOperacao(x, parseFator(), NoOperacao.Operador.DIVISAO);
            }
            else{
                return x;
            }
        }
    }

    // Nível 3: Potência (^)
    private Expressao parseFator() {
        Expressao x = parsePrimario();
        if (comer('^')) {
            x = new NoOperacao(x, parseFator(), NoOperacao.Operador.POTENCIA);
        }
        return x;
    }

    // Nível 4: Números, Variáveis, Parênteses e Funções (Maior prioridade)
    private Expressao parsePrimario() {

        boolean negativo = false;

        // captura + e -
        if (comer('-')){
            negativo = true;
        }
        else if (comer('+')){
            negativo = false;
        }

        // Parênteses
        if (comer('(')) {
            Expressao x = parseExpressao();
            if (!comer(')')){ 
                throw new IllegalArgumentException("Faltou fechar parênteses ')'");
            }
            if (negativo) {
                return new NoOperacao(new NoConstante(new NumeroComplexo(0, 0)), x, NoOperacao.Operador.SUBTRACAO);
            }
            return x;
        }

        // Funções e variáveis
        if (Character.isLetter(ch)) {
            String nome = lerIdentificador();

            // Caso especial: 'i' é imaginário
            if (nome.equals("i")) {
                Expressao x = new NoConstante(new NumeroComplexo(0, 1));
                if (negativo){
                    return new NoOperacao(new NoConstante(new NumeroComplexo(0, 0)), x, NoOperacao.Operador.SUBTRACAO);
                }
                return x;
            }

            // Função raiz(...)
            if (nome.equalsIgnoreCase("raiz")) {
                if (!comer('(')){
                    throw new IllegalArgumentException("Use raiz(expressao, grau)");
                }
                Expressao exp = parseExpressao();
                if (!comer(',')){
                    throw new IllegalArgumentException("Use raiz(expressao, grau)");
                }
                String grauStr = lerNumero();
                int grau = Integer.parseInt(grauStr);
                if (!comer(')')){
                    throw new IllegalArgumentException("Faltou ')' após raiz");
                }

                Expressao x = new NoOperacao(exp, grau); // mantive sua estrutura

                if (negativo){
                    return new NoOperacao(new NoConstante(new NumeroComplexo(0, 0)), x, NoOperacao.Operador.SUBTRACAO);
                }
                return x;
            }

            // Função conj(...)
            if (nome.equalsIgnoreCase("conj")) {
                if (!comer('(')){
                    throw new IllegalArgumentException("Use conj(expressao)");
                }
                Expressao exp = parseExpressao();
                if (!comer(')')){
                    throw new IllegalArgumentException("Faltou ')' após conj");
                }

                Expressao x = new NoOperacao(exp); // mantive sua estrutura

                if (negativo){
                    return new NoOperacao(new NoConstante(new NumeroComplexo(0, 0)), x, NoOperacao.Operador.SUBTRACAO);
                }
                return x;
            }

            // Variável simples
            Expressao x = new NoVariavel(nome);
            if (negativo){
                return new NoOperacao(new NoConstante(new NumeroComplexo(0, 0)), x, NoOperacao.Operador.SUBTRACAO);
            }
            return x;
        }

        // Números
        if (Character.isDigit(ch) || ch == '.') {
            String numStr = lerNumero();

            // Imaginário tipo 3i
            if (ch == 'i') {
                proximoChar();
                double valor = Double.parseDouble(numStr);
                Expressao x = new NoConstante(new NumeroComplexo(0, valor));
                if (negativo){
                    return new NoOperacao(new NoConstante(new NumeroComplexo(0, 0)), x, NoOperacao.Operador.SUBTRACAO);
                }
                return x;
            }

            // Real puro
            double valor = Double.parseDouble(numStr);
            Expressao x = new NoConstante(new NumeroComplexo(valor, 0));

            if (negativo){
                return new NoOperacao(new NoConstante(new NumeroComplexo(0, 0)), x, NoOperacao.Operador.SUBTRACAO);
            }
            return x;
        }

        throw new IllegalArgumentException("Caractere inválido: " + (char) ch);
    }

    // Métodos auxiliares de leitura de String
    private String lerIdentificador() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(ch) || Character.isDigit(ch)) {
            sb.append((char) ch);
            proximoChar();
        }
        return sb.toString();
    }

    private String lerNumero() {
        StringBuilder sb = new StringBuilder();
        while (Character.isDigit(ch) || ch == '.') {
            sb.append((char) ch);
            proximoChar();
        }
        return sb.toString();
    }
}

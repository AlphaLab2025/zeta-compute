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

    // --- Níveis do calculo  ---

    // Nível 1: Soma e Subtração (Menor prioridade)
    private Expressao parseExpressao() {
        Expressao x = parseTermo();
        for (;;) {
            if (comer('+')) x = new NoOperacao(x, parseTermo(), NoOperacao.Operador.SOMA);
            else if (comer('-')) x = new NoOperacao(x, parseTermo(), NoOperacao.Operador.SUBTRACAO);
            else return x;
        }
    }

    // Nível 2: Multiplicação e Divisão
    private Expressao parseTermo() {
        Expressao x = parseFator();
        for (;;) {
            if (comer('*')) x = new NoOperacao(x, parseFator(), NoOperacao.Operador.MULTIPLICACAO);
            else if (comer('/')) x = new NoOperacao(x, parseFator(), NoOperacao.Operador.DIVISAO);
            else return x;
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
        if (comer('(')) {
            Expressao x = parseExpressao();
            if (!comer(')')) throw new IllegalArgumentException("Faltou fechar parênteses ')'");
            return x;
        }

        // Funções: raiz(...) ou conj(...)
        // Verifica se é uma palavra (variável ou função)
        if (Character.isLetter(ch)) {
            String nome = lerIdentificador();
            
            // Caso especial: 'i' isolado é um número complexo, não variável
            if (nome.equals("i")) {
                 return new NoConstante(new NumeroComplexo(0, 1));
            }

            // Verifica se é função
            if (nome.equalsIgnoreCase("raiz")) {
                if (!comer('(')) throw new IllegalArgumentException("Use raiz(expressao, grau)");
                Expressao exp = parseExpressao();
                if (!comer(',')) throw new IllegalArgumentException("Use raiz(expressao, grau)");
                // Ler o grau (inteiro simples)
                String grauStr = lerNumero(); 
                int grau = Integer.parseInt(grauStr); // Assume grau inteiro
                if (!comer(')')) throw new IllegalArgumentException("Faltou ')' após raiz");
                return new NoOperacao(exp, grau);
            }
            
            if (nome.equalsIgnoreCase("conj")) {
                if (!comer('(')) throw new IllegalArgumentException("Use conj(expressao)");
                Expressao exp = parseExpressao();
                if (!comer(')')) throw new IllegalArgumentException("Faltou ')' após conj");
                return new NoOperacao(exp);
            }

            // Se não for função nem 'i', é variável
            return new NoVariavel(nome);
        }

        // Números (Reais ou Imaginários puros ex: 3, 4.5, 2i)
        if (Character.isDigit(ch) || ch == '.') {
            String numStr = lerNumero();
            // Verifica se tem 'i' no final (ex: 3i)
            if (ch == 'i') {
                proximoChar();
                double valor = Double.parseDouble(numStr);
                return new NoConstante(new NumeroComplexo(0, valor));
            }
            double valor = Double.parseDouble(numStr);
            return new NoConstante(new NumeroComplexo(valor, 0));
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

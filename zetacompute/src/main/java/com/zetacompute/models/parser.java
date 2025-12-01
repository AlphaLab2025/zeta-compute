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
        Expressao x;
        int startPos = this.pos;

        boolean negativo = comer('-');
        if (comer('+')) {
            negativo = false; // '+' sobrescreve '-'
        }

        if (comer('(')) {
            x = parseExpressao();
            if (!comer(')'))
                throw new IllegalArgumentException("Faltou fechar parênteses ')'");
            return x;
        }

        // Funções: raiz(...) ou conj(...)
        // Verifica se é uma palavra (variável ou função)
        if (Character.isLetter(ch)) {
            String nome = lerIdentificador();

            // Caso especial: 'i' isolado é um número complexo, não variável
            if (nome.equals("i")) {
                x = new NoConstante(new NumeroComplexo(0, 1));
            }
            // Verifica se é função
            else if (nome.equalsIgnoreCase("raiz")) {
                if (!comer('('))
                    throw new IllegalArgumentException("use raiz(expressao, grau)");
                Expressao exp = parseExpressao();
                if (!comer(','))
                    throw new IllegalArgumentException("Use raiz(expressao, grau)");
                
                Expressao grauExp = parseExpressao();
                if (!(grauExp instanceof NoConstante)) {
                    throw new IllegalArgumentException("O grau da raiz deve ser uma constante numérica.");
                }

                NumeroComplexo complexoGrau = ((NoConstante) grauExp).avaliar(null);
                if (Math.abs(complexoGrau.getImaginario()) > 1e-9) {
                    throw new IllegalArgumentException("O grau da raiz deve ser um número real (imaginário=0).");
                }
                int grau = (int) Math.round(complexoGrau.getReal());

                if (!comer(')'))
                    throw new IllegalArgumentException("Faltou ')' após raiz");
                x = new NoOperacao(exp, grau);
            }

            else if (nome.equalsIgnoreCase("conj")) {
                if (!comer('('))
                    throw new IllegalArgumentException("Uso incorreto da função conj: use conj(expressao)");
                Expressao exp = parseExpressao();
                if (!comer(')'))
                    throw new IllegalArgumentException("Faltou ')' após conj");
                x = new NoOperacao(exp);
            }

            else {
                // Se não for função nem 'i', é variável
                x = new NoVariavel(nome);
            }
        }

        // Números (Reais ou Imaginários puros ex: 3, 4.5, 2i)
        else if (Character.isDigit(ch) || ch == '.') {
            pos = startPos;
            ch = source.charAt(pos);
            String numStr = lerNumero();
            try {
                x = new NoConstante(NumeroComplexo.parse(numStr));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Formato de número complexo inválido na expressão: " + numStr);
            }
        }

        else {
            if (ch == -1) {
                throw new IllegalArgumentException("Expressão incompleta ou caractere inválido no final.");
            }
            throw new IllegalArgumentException("Caractere inválido: '" + (char) ch + "' na posição " + pos);
        }

        if (negativo) {
            return new NoOperacao(new NoConstante(new NumeroComplexo(-1, 0)), x, NoOperacao.Operador.MULTIPLICACAO);
        }
        return x;
    }

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

        if (ch == 'i') {
             sb.append((char) ch);
             proximoChar();
             return sb.toString();
        }
        
        if (ch == '+' || ch == '-') {
            sb.append((char) ch);
            proximoChar();
            
            while (Character.isDigit(ch) || ch == '.') {
                sb.append((char) ch);
                proximoChar();
            }
            
            if (ch == 'i') {
                 sb.append((char) ch);
                 proximoChar();
            }
        }        
        return sb.toString();
    }
}

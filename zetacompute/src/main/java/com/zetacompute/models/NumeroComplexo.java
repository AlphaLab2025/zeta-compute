package com.zetacompute.models;

import java.util.Objects;

/*
 * Representa um número complexo com parte real e imaginária (real + imaginario * i).
 * Fornece métodos para operações aritméticas, conjugado, potência e raiz n-ésima.
 */

public final class NumeroComplexo {

    private final double real;
    private final double imaginario;

/*
     * Converte uma String (ex: "3", "4i", "3+4i", "3-4i") em um NumeroComplexo.
     * Necessário para o parser ler a entrada do usuario.
     */
    public static NumeroComplexo parse(String s) {
        s = s.replace(" ", ""); // Remove espaços
        
        // Caso simples: apenas "i" ou "-i"
        if (s.equals("i")) return new NumeroComplexo(0, 1);
        if (s.equals("-i")) return new NumeroComplexo(0, -1);

        // Regex para separar partes. Ex: pega "-3", "+4i"
        // Nota: Parsing de complexos é chato, esta é uma implementação simplificada
        // que assume o formato a+bi ou a ou bi.
        try {
            if (s.endsWith("i")) {
                // Tem parte imaginária?
                int posSinal = Math.max(s.lastIndexOf('+'), s.lastIndexOf('-'));
                
                if (posSinal <= 0) { // Apenas imaginário (ex: "4i", "-4i")
                   String imStr = s.substring(0, s.length() - 1);
                   if (imStr.equals("+")) imStr = "1";
                   else if (imStr.equals("-")) imStr = "-1";
                   return new NumeroComplexo(0, Double.parseDouble(imStr));
                } else { 
                   // Real e Imaginário (ex: "3+4i")
                   double re = Double.parseDouble(s.substring(0, posSinal));
                   String imStr = s.substring(posSinal, s.length() - 1);
                   if (imStr.equals("+")) imStr = "1";
                   else if (imStr.equals("-")) imStr = "-1";
                   return new NumeroComplexo(re, Double.parseDouble(imStr));
                }
            } else {
                // Apenas Real
                return new NumeroComplexo(Double.parseDouble(s), 0);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de número complexo inválido: " + s);
        }
    }

    public NumeroComplexo(double real, double imaginario) {
        this.real = real;
        this.imaginario = imaginario;
    }

    // getters
    public double getReal() {
        return real;
    }

    public double getImaginario() {
        return imaginario;
    }

    /*
     * Retorna o conjugado do número complexo.
     * O conjugado de (a + bi) é (a - bi).
     * (Atende uma parte do Requisito 1)
     */
    public NumeroComplexo getConjugado() {
        return new NumeroComplexo(
                this.real,
                -this.imaginario);
    }

    // setters
    /*
     * Retorna a soma de dois números complexos.
     * (Atende uma parte do Requisito 1)
     */
    public NumeroComplexo somar(NumeroComplexo x) {
        return new NumeroComplexo(
                this.real + x.real,
                this.imaginario + x.imaginario);
    }

    /*
     * Retorna a subtração de dois números complexos.
     * (Atende uma parte do Requisito 1)
     */
    public NumeroComplexo subtrair(NumeroComplexo x) {
        return new NumeroComplexo(
                this.real - x.real,
                this.imaginario - x.imaginario);
    }

    /*
     * Retorna a multiplicação de dois números complexos.
     * (Atende uma parte do Requisito 1)
     */
    public NumeroComplexo multiplicar(NumeroComplexo x) {
        return new NumeroComplexo(
                this.real * x.real - this.imaginario * x.imaginario,
                this.real * x.imaginario + this.imaginario * x.real);
    }

    /*
     * Retorna a divisão de dois números complexos.
     * (Atende uma parte do Requisito 1 e Requisito 5)
     */
    // Nota -- Revisar a lógica de divisão para garantir que está correta
    public NumeroComplexo dividir(NumeroComplexo x) {
        double denominador = x.real * x.real + x.imaginario * x.imaginario; // módulo ao quadrado do número complexo
                                                                            // divisor
        double epsilon = 1e-9; // tolerância para comparação com zero

        if (Math.abs(denominador) < epsilon) {
            throw new ArithmeticException("Divisão por zero não é permitida para números complexos.");
        }

        return new NumeroComplexo(
                (this.real * x.real + this.imaginario * x.imaginario) / denominador,
                (this.imaginario * x.real - this.real * x.imaginario) / denominador);
    }

    /*
     * Retorna o número complexo elevado à potência inteira x.
     * (Atende uma parte do Requisito 1)
     */
    // Nota -- Revisar a lógica de potência para garantir que está correta
/*
     * Retorna o número complexo elevado à potência inteira x usando a Fórmula de De Moivre.
     * (Atende Requisito 1 de forma otimizada)
     */
    public NumeroComplexo potencia(int x) {
        if (x == 0) return new NumeroComplexo(1, 0);
        if (real == 0 && imaginario == 0) return new NumeroComplexo(0, 0); // 0^x = 0 (para x>0)

        // Converte para polar
        double modulo = Math.sqrt(real * real + imaginario * imaginario);
        double angulo = Math.atan2(imaginario, real);

        // Aplica a potência
        double novoModulo = Math.pow(modulo, x);
        double novoAngulo = angulo * x;

        // Volta para retangular
        double novoReal = novoModulo * Math.cos(novoAngulo);
        double novoImaginario = novoModulo * Math.sin(novoAngulo);

        return new NumeroComplexo(novoReal, novoImaginario);
    }
    /*
     * Retorna a raiz n-ésima principal do número complexo.
     * (Atende uma parte do Requisito 1)
     */
    // Nota -- Revisar a lógica de raiz para garantir que está correta
    public NumeroComplexo raiz(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("O índice da raiz deve ser um número inteiro positivo.");
        }

        // Cálculo da raiz n-ésima usando a forma polar
        double r = Math.sqrt(real * real + imaginario * imaginario); // módulo
        double theta = Math.atan2(imaginario, real); // argumento

        double raizM = Math.pow(r, 1.0 / n); // raiz do módulo
        double raizTheta = theta / n; // raiz do argumento

        double raizReal = raizM * Math.cos(raizTheta); // parte real
        double raizImaginario = raizM * Math.sin(raizTheta); // parte imaginária

        return new NumeroComplexo(raizReal, raizImaginario); // retorno da raiz n-ésima principal
    }

    /*
     * Representação em String do número complexo.
     * Formato: "a + bi" ou "a - bi" ou "a" ou "bi"
     */
    @Override
    public String toString() {
        if (imaginario == 0) {
            return String.format("%.2f", real);
        }
        if (real == 0) {
            return String.format("%.2fi", imaginario);
        }
        return String.format("%.2f %s %.2fi", real, (imaginario > 0 ? "+" : "-"), Math.abs(imaginario));
    }

    /*
     * Verifica a igualdade entre dois números complexos.
     * Dois números complexos são iguais se suas partes real e imaginária forem
     * iguais.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NumeroComplexo that = (NumeroComplexo) o;
        double epsilon = 1e-9;
        return Math.abs(this.real - that.real) < epsilon &&
                Math.abs(this.imaginario - that.imaginario) < epsilon;
    }

    /*
     * Gera o código hash para o número complexo.
     */
    @Override
    public int hashCode() {
        return Objects.hash(real, imaginario);
    }

}
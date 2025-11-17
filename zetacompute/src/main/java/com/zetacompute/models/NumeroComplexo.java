package com.zetacompute.models;

import java.util.Objects;

/*
 * Representa um número complexo com parte real e imaginária (real + imaginario * i).
 * Fornece métodos para operações aritméticas, conjugado, potência e raiz n-ésima.
 */

public final class NumeroComplexo {

    private final double real;
    private final double imaginario;

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
    public NumeroComplexo potencia(int x) {

        if (x == 0) {
            return new NumeroComplexo(1, 0); // qualquer número elevado a 0 é 1
        }

        if (x == 1) {
            return this; // qualquer número elevado a 1 é ele mesmo
        }

        if (x < 0) {
            NumeroComplexo inverso = new NumeroComplexo(1, 0).dividir(this); // calcular o inverso
            NumeroComplexo resultadoInverso = inverso.potencia(-x); // elevar o inverso ao valor positivo de x
            return resultadoInverso; // retornar o inverso do resultado
        }

        // Lógica para x > 1
        NumeroComplexo resultado = this;
        for (int i = 1; i < x; i++) {
            resultado = resultado.multiplicar(this);
        }
        return resultado;
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
package com.zetacompute.console;

import com.zetacompute.models.Expressao;
import com.zetacompute.models.NoConstante;
import com.zetacompute.models.NoOperacao;
import com.zetacompute.models.NoVariavel;
import com.zetacompute.models.NumeroComplexo;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class CalculadoraComplexaConsole {
    private Scanner scanner;

    public CalculadoraComplexaConsole() {
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   Calculadora de Números Complexos         ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        boolean continuar = true;
        while (continuar) {
            exibirMenu();
            int opcao = obterInteiro("Escolha uma opção: ");
            continuar = processarOpcao(opcao);
        }

        System.out.println("\nObrigado por usar a calculadora!");

        // Exibir exemplo de árvore (mesmo comportamento de TesteArvore)
        try {
            mostrarArvoreExemplo();
        } catch (Exception e) {
            System.out.println("Erro ao exibir a árvore de exemplo: " + e.getMessage());
        }

        scanner.close();
    }

    /**
     * Monta a mesma árvore usada em `TesteArvore` e a exibe, em seguida avalia
     * com z = (1 + 1i) e mostra o resultado.
     */
    private void mostrarArvoreExemplo() {
        // 1. Criar as folhas
        Expressao noA = new NoConstante(new NumeroComplexo(2, 5));
        Expressao noZ = new NoVariavel("z");
        Expressao no3 = new NoConstante(new NumeroComplexo(3, 0));

        // 2. Montar a árvore: (2+5i) + (z * 3)
        Expressao mult = new NoOperacao(noZ, no3, NoOperacao.Operador.MULTIPLICACAO);
        Expressao arvore = new NoOperacao(noA, mult, NoOperacao.Operador.SOMA);

        System.out.println("\n--- Árvore de Exemplo ---");
        arvore.exibirArvore();

        // Avaliar com z = 1+1i
        Map<String, NumeroComplexo> variaveis = new HashMap<>();
        variaveis.put("z", new NumeroComplexo(1, 1));
        NumeroComplexo resultado = arvore.avaliar(variaveis);
        System.out.println("\nResultado para z=(1+1i): " + resultado);
    }

    private void exibirMenu() {
        System.out.println("\n--- Menu Principal ---");
        System.out.println("1. Somar dois números complexos");
        System.out.println("2. Subtrair dois números complexos");
        System.out.println("3. Multiplicar dois números complexos");
        System.out.println("4. Dividir dois números complexos");
        System.out.println("5. Conjugado de um número complexo");
        System.out.println("6. Potência de um número complexo");
        System.out.println("7. Raiz n-ésima de um número complexo");
        System.out.println("0. Sair");
    }

    private boolean processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                operacaoSoma();
                break;
            case 2:
                operacaoSubtracao();
                break;
            case 3:
                operacaoMultiplicacao();
                break;
            case 4:
                operacaoDivisao();
                break;
            case 5:
                operacaoConjugado();
                break;
            case 6:
                operacaoPotencia();
                break;
            case 7:
                operacaoRaiz();
                break;
            case 0:
                return false;
            default:
                System.out.println("❌ Opção inválida! Tente novamente.");
        }
        return true;
    }

    private NumeroComplexo lerNumeroComplexo(String nome) {
        System.out.printf("\n--- Leitura de %s ---\n", nome);
        double real = obterDouble("Digite a parte real: ");
        double imaginario = obterDouble("Digite a parte imaginária: ");
        return new NumeroComplexo(real, imaginario);
    }

    private double obterDouble(String mensagem) {
        while (true) {
            try {
                System.out.printf(mensagem);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida! Digite um número válido.");
            }
        }
    }

    private int obterInteiro(String mensagem) {
        while (true) {
            try {
                System.out.printf(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida! Digite um número inteiro válido.");
            }
        }
    }

    private void operacaoSoma() {
        NumeroComplexo z1 = lerNumeroComplexo("Primeiro número (z1)");
        NumeroComplexo z2 = lerNumeroComplexo("Segundo número (z2)");
        NumeroComplexo resultado = z1.somar(z2);
        printf("\n✓ Resultado: (%s) + (%s) = %s\n", z1, z2, resultado);
    }

    private void operacaoSubtracao() {
        NumeroComplexo z1 = lerNumeroComplexo("Primeiro número (z1)");
        NumeroComplexo z2 = lerNumeroComplexo("Segundo número (z2)");
        NumeroComplexo resultado = z1.subtrair(z2);
        printf("\n✓ Resultado: (%s) - (%s) = %s\n", z1, z2, resultado);
    }

    private void operacaoMultiplicacao() {
        NumeroComplexo z1 = lerNumeroComplexo("Primeiro número (z1)");
        NumeroComplexo z2 = lerNumeroComplexo("Segundo número (z2)");
        NumeroComplexo resultado = z1.multiplicar(z2);
        printf("\n✓ Resultado: (%s) × (%s) = %s\n", z1, z2, resultado);
    }

    private void operacaoDivisao() {
        try {
            NumeroComplexo z1 = lerNumeroComplexo("Dividendo (z1)");
            NumeroComplexo z2 = lerNumeroComplexo("Divisor (z2)");
            NumeroComplexo resultado = z1.dividir(z2);
            printf("\n✓ Resultado: (%s) ÷ (%s) = %s\n", z1, z2, resultado);
        } catch (ArithmeticException e) {
            System.out.printf("\n❌ Erro: %s\n", e.getMessage());
        }
    }

    private void operacaoConjugado() {
        NumeroComplexo z = lerNumeroComplexo("Número");
        NumeroComplexo conjugado = z.getConjugado();
        printf("\n✓ Conjugado de (%s) = %s\n", z, conjugado);
    }

    private void operacaoPotencia() {
        NumeroComplexo z = lerNumeroComplexo("Número");
        int expoente = obterInteiro("Digite o expoente: ");
        NumeroComplexo resultado = z.potencia(expoente);
        printf("\n✓ Resultado: (%s)^%d = %s\n", z, expoente, resultado);
    }

    private void operacaoRaiz() {
        try {
            NumeroComplexo z = lerNumeroComplexo("Número");
            int indice = obterInteiro("Digite o índice da raiz: ");
            NumeroComplexo resultado = z.raiz(indice);
            printf("\n✓ Resultado: Raiz %d de (%s) = %s\n", indice, z, resultado);
        } catch (IllegalArgumentException e) {
            System.out.printf("\n❌ Erro: %s\n", e.getMessage());
        }
    }

    private void printf(String formato, Object... args) {
        System.out.printf(formato, args);
    }

    public static void main(String[] args) {
        CalculadoraComplexaConsole calculadora = new CalculadoraComplexaConsole();
        calculadora.iniciar();
    }
}

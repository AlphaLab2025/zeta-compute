package com.zetacompute;

import com.zetacompute.models.Expressao;
import com.zetacompute.models.NoConstante;
import com.zetacompute.models.NoOperacao;
import com.zetacompute.models.NoVariavel;
import com.zetacompute.models.NumeroComplexo;
import com.zetacompute.models.parser;

import com.zetacompute.utils.AnsiColor; // <<< IMPORTANTE

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CalculadoraComplexa {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, NumeroComplexo> variaveis = new HashMap<>();

        while (true) {
            exibirMenu();

            System.out.print(AnsiColor.applyColor("Expressão > ", AnsiColor.PROMPT));
            String entrada = scanner.nextLine();

            if (entrada.trim().equalsIgnoreCase("sair")) {
                System.out.println(AnsiColor.applyColor("Encerrando calculadora...", AnsiColor.YELLOW));
                break;
            }

            if (entrada.trim().isEmpty())
                continue;

            try {
                // 1. Parse
                parser parser = new parser(entrada);
                Expressao arvore = parser.parse();

                // 2. Exibir Árvore
                System.out.println(AnsiColor.applyColor("\n[Árvore Gerada - Visual]", AnsiColor.ARROW));
                arvore.exibirArvore();

                // Notação LISP
                System.out.println(AnsiColor.applyColor("\n[Árvore Gerada - Notação LISP]", AnsiColor.ARROW));
                System.out.println(AnsiColor.applyColor(arvore.toLisp(), AnsiColor.CYAN));

                // 3. Variáveis
                variaveis.clear();
                Set<String> varsNaExpressao = arvore.getVariaveis();

                if (!varsNaExpressao.isEmpty()) {
                    System.out.println(AnsiColor.applyColor("\n--- Definição de Variáveis ---", AnsiColor.HIGHLIGHT));

                    for (String varNome : varsNaExpressao) {
                        pedirVariavel(scanner, variaveis, varNome);
                    }
                }

                // 4. Calcular
                NumeroComplexo resultado = arvore.avaliar(variaveis);

                System.out.println(
                        AnsiColor.applyColor("\n>>> RESULTADO FINAL: ", AnsiColor.SUCCESS)
                                + AnsiColor.applyColor(resultado.toString(), AnsiColor.GREEN));

                System.out.println(AnsiColor.applyColor("\nPressione ENTER para continuar...", AnsiColor.CYAN));
                scanner.nextLine();

            } catch (Exception e) {
                System.err.println(
                        AnsiColor.applyColor("\n❌ Erro na expressão: " + e.getMessage(), AnsiColor.ERROR));
                System.out.println(AnsiColor.applyColor("Tente novamente.", AnsiColor.YELLOW));
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out
                .println(AnsiColor.applyColor("\n===================================================", AnsiColor.MENU));
        System.out.println(AnsiColor.applyColor("    CALCULADORA DE NÚMEROS COMPLEXOS (ÁRVORE)", AnsiColor.MENU));
        System.out.println(AnsiColor.applyColor("===================================================", AnsiColor.MENU));
        System.out.println(AnsiColor.applyColor("Operadores: +  -  * /  ^ (potência)", AnsiColor.CYAN));
        System.out.println(AnsiColor.applyColor("Funções:    raiz(exp, grau), conj(exp)", AnsiColor.CYAN));
        System.out.println(AnsiColor.applyColor("Exemplo:    (2 + 3i) * z + raiz(16,2)", AnsiColor.HIGHLIGHT));
        System.out.println(AnsiColor.applyColor("Digite 'sair' para encerrar.", AnsiColor.YELLOW));
        System.out.println(AnsiColor.applyColor("---------------------------------------------------", AnsiColor.MENU));
    }

    private static void pedirVariavel(Scanner sc, Map<String, NumeroComplexo> map, String nome) {
        while (true) {
            try {
                System.out.println(
                        AnsiColor.applyColor("Informe o valor para a variável '" + nome + "':", AnsiColor.HIGHLIGHT));

                System.out.print(AnsiColor.applyColor("   Parte Real: ", AnsiColor.PROMPT));
                double r = Double.parseDouble(sc.nextLine().replace(",", "."));

                System.out.print(AnsiColor.applyColor("   Parte Imaginária: ", AnsiColor.PROMPT));
                double i = Double.parseDouble(sc.nextLine().replace(",", "."));

                map.put(nome, new NumeroComplexo(r, i));
                break;

            } catch (NumberFormatException e) {
                System.out.println(
                        AnsiColor.applyColor("   [Erro] Digite apenas números válidos.", AnsiColor.ERROR));
            }
        }
    }
}

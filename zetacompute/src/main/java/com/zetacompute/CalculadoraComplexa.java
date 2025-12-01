package com.zetacompute;

import com.zetacompute.models.Expressao;
import com.zetacompute.models.NoConstante;
import com.zetacompute.models.NoOperacao;
import com.zetacompute.models.NoVariavel;
import com.zetacompute.models.NumeroComplexo;
import com.zetacompute.models.parser;
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
            
            System.out.print("Expressão > ");
            String entrada = scanner.nextLine();

            if (entrada.trim().equalsIgnoreCase("sair")) {
                System.out.println("Encerrando calculadora...");
                break;
            }

            if (entrada.trim().isEmpty()) continue;

            try {
                // 1. Parse
                parser parser = new parser(entrada);
                Expressao arvore = parser.parse();

                // 2. Exibir Árvore               
                System.out.println("\n[Árvore Gerada - Visual]");
                arvore.exibirArvore();

                // Notação LISP
                System.out.println("\n[Árvore Gerada - Notação LISP]");
                System.out.println(arvore.toLisp());


                variaveis.clear();
                Set<String> varsNaExpressao = arvore.getVariaveis();

                if (!varsNaExpressao.isEmpty()) {
                    System.out.println("\n--- Definição de Variáveis ---");
                    for (String varNome : varsNaExpressao) {
                        pedirVariavel(scanner, variaveis, varNome);
                    }
                }

                // 4. Calcular
                NumeroComplexo resultado = arvore.avaliar(variaveis);
                System.out.println("\n>>> RESULTADO FINAL: " + resultado);
                
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine(); // Pausa para o usuário ver o resultado

            } catch (Exception e) {
                System.err.println("\nErro na expressão: " + e.getMessage());
                System.out.println("Tente novamente.");
            }
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n===================================================");
        System.out.println("    CALCULADORA DE NÚMEROS COMPLEXOS (ÁRVORE)");
        System.out.println("===================================================");
        System.out.println("Operadores: +  -  * /  ^ (potência)");
        System.out.println("Funções:    raiz(exp, grau), conj(exp)");
        System.out.println("Exemplo:    (2 + 3i) * z + raiz(16,2)");
        System.out.println("Digite 'sair' para encerrar.");
        System.out.println("---------------------------------------------------");
    }

    private static void pedirVariavel(Scanner sc, Map<String, NumeroComplexo> map, String nome) {
        while (true) {
            try {
                System.out.println("Informe o valor para a variável '" + nome + "':");
                System.out.print("   Parte Real: ");
                double r = Double.parseDouble(sc.nextLine().replace(",", "."));
                
                System.out.print("   Parte Imaginária: ");
                double i = Double.parseDouble(sc.nextLine().replace(",", "."));
                
                map.put(nome, new NumeroComplexo(r, i));
                break; 
            } catch (NumberFormatException e) {
                System.out.println("   [Erro] Digite apenas números válidos.");
            }
        }
    }
}
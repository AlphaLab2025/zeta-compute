package com.zetacompute.utils;

public class AnsiColor {
    // Retorna à cor padrão
    public static final String RESET = "\u001B[0m";

    // Cores de Texto (Foreground)
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    // Estilos Especiais
    public static final String BOLD = "\u001B[1m";

    // Cores para os elementos da calculadora
    public static final String SUCCESS = GREEN + BOLD; 
    public static final String ERROR = RED + BOLD; 
    public static final String MENU = CYAN + BOLD; // Para o menu principal e títulos
    public static final String HIGHLIGHT = YELLOW; // Para destaque de variáveis ou expressões
    public static final String PROMPT = BLUE; 
    public static final String ARROW = CYAN; // Para a árvore e LISP

    /**
     * Aplica uma cor e reseta a formatação no final da string.
     */
    public static String applyColor(String text, String colorCode) {
        return colorCode + text + RESET;
    }
}
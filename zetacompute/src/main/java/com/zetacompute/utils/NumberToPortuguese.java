package com.zetacompute.utils;

public class NumberToPortuguese {

    private static final String[] UNIDADES = {
        "zero", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove",
        "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"
    };

    private static final String[] DEZENAS = {
        "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"
    };

    private static final String[] CENTENAS = {
        "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"
    };

    /**
     * Converte um número (0..9999) para palavras em português
     * Retorna com a primeira letra em maiúscula (ex: "Três").
     */
    public static String toPortuguese(int n) {
        if (n < 0) return "menos " + capitalize(toPortuguese(-n));
        if (n < 20) return capitalize(UNIDADES[n]);
        if (n < 100) return capitalize(dezenasToString(n));
        if (n < 1000) return capitalize(centenasToString(n));
        if (n < 10000) return capitalize(milharToString(n));
        return String.valueOf(n);
    }

    private static String dezenasToString(int n) {
        if (n < 20) return UNIDADES[n];
        int dez = n / 10;
        int uni = n % 10;
        if (uni == 0) return DEZENAS[dez];
        return DEZENAS[dez] + " e " + UNIDADES[uni];
    }

    private static String centenasToString(int n) {
        if (n == 100) return "cem";
        int cen = n / 100;
        int rem = n % 100;
        String res = CENTENAS[cen];
        if (rem == 0) return res;
        if (rem < 20) return res + " e " + UNIDADES[rem];
        return res + " e " + dezenasToString(rem);
    }

    private static String milharToString(int n) {
        int mil = n / 1000;
        int rem = n % 1000;
        StringBuilder sb = new StringBuilder();
        if (mil == 1) sb.append("mil");
        else sb.append(UNIDADES[mil]).append(" mil");
        if (rem == 0) return sb.toString();
        if (rem < 100) sb.append(" e ").append(dezenasToString(rem));
        else sb.append(" ").append(centenasToString(rem));
        return sb.toString();
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Retorna o ordinal em português na forma feminina (ex: 1 -> "Primeira").
     * Suporta de 1 a 20 explicitamente; para valores maiores retorna o cardinal + "ª" (fallback).
     */
    public static String toPortugueseOrdinalFeminine(int n) {
        switch (n) {
            case 1: return "Primeira";
            case 2: return "Segunda";
            case 3: return "Terceira";
            case 4: return "Quarta";
            case 5: return "Quinta";
            case 6: return "Sexta";
            case 7: return "Sétima";
            case 8: return "Oitava";
            case 9: return "Nona";
            case 10: return "Décima";
            case 11: return "Décima Primeira";
            case 12: return "Décima Segunda";
            case 13: return "Décima Terceira";
            case 14: return "Décima Quarta";
            case 15: return "Décima Quinta";
            case 16: return "Décima Sexta";
            case 17: return "Décima Sétima";
            case 18: return "Décima Oitava";
            case 19: return "Décima Nona";
            case 20: return "Vigésima";
            default:
                // Fallback: cardinal + símbolo ordinal feminino
                String card = toPortuguese(n);
                return card + "ª";
        }
    }
}

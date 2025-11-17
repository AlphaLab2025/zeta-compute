package com.zetacompute.models;

// Importações estáticas do JUnit para asserções
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Importações do JUnit
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NumeroComplexoTest {

    private static final double EPSILON = 1e-9;

    private NumeroComplexo a;
    private NumeroComplexo b;
    private NumeroComplexo zero;
    private NumeroComplexo um;

    @BeforeEach
    void setUp() {
        a = new NumeroComplexo(3, 4); // (3 + 4i)
        b = new NumeroComplexo(1, -2); // (1 - 2i)
        zero = new NumeroComplexo(0, 0); // (0 + 0i)
        um = new NumeroComplexo(1, 0); // (1 + 0i)
    }

    @Test
    @DisplayName("Deve somar dois números complexos")
    void testSomar() {
        // (3 + 4i) + (1 - 2i) = (4 + 2i)
        NumeroComplexo esperado = new NumeroComplexo(4, 2);
        NumeroComplexo resultado = a.somar(b);
        assertEquals(esperado, resultado, "A soma (3+4i) + (1-2i) deve ser (4+2i)");
    }

    @Test
    @DisplayName("Deve subtrair dois números complexos")
    void testSubtrair() {
        // (3 + 4i) - (1 - 2i) = (2 + 6i)
        NumeroComplexo esperado = new NumeroComplexo(2, 6);
        NumeroComplexo resultado = a.subtrair(b);
        assertEquals(esperado, resultado, "A subtração (3+4i) - (1-2i) deve ser (2+6i)");
    }

    @Test
    @DisplayName("Deve multiplicar dois números complexos")
    void testMultiplicar() {
        // (3 + 4i) * (1 - 2i) = (3 - 6i + 4i - 8i^2) = (3 - 2i + 8) = (11 - 2i)
        NumeroComplexo esperado = new NumeroComplexo(11, -2);
        NumeroComplexo resultado = a.multiplicar(b);
        assertEquals(esperado, resultado, "A multiplicação (3+4i) * (1-2i) deve ser (11+2i)");
    }

    @Test
    @DisplayName("Deve dividir dois números complexos")
    void testDividir() {
        // (3 + 4i) / (1 - 2i) = -1 + 2i
        NumeroComplexo esperado = new NumeroComplexo(-1, 2);
        NumeroComplexo resultado = a.dividir(b);

        // Compara usando o epsilon por causa da divisão de doubles
        assertTrue(Math.abs(esperado.getReal() - resultado.getReal()) < EPSILON);
        assertTrue(Math.abs(esperado.getImaginario() - resultado.getImaginario()) < EPSILON);
    }

    @Test
    @DisplayName("Deve lançar ArithmeticException ao dividir por zero")
    void testDividirPorZero() {
        // O teste verifica se a exceção ArithmeticException é lançada.
        assertThrows(ArithmeticException.class, () -> {
            a.dividir(zero);
        }, "Dividir por zero deve lançar uma ArithmeticException");
    }

    @Test
    @DisplayName("Deve calcular o conjugado")
    void testGetConjugado() {
        // Conjugado de (3 + 4i) é (3 - 4i)
        NumeroComplexo esperado = new NumeroComplexo(3, -4);
        assertEquals(esperado, a.getConjugado());

        // Conjugado de (1 - 2i) é (1 + 2i)
        NumeroComplexo esperadoB = new NumeroComplexo(1, 2);
        assertEquals(esperadoB, b.getConjugado());
    }

    @Test
    @DisplayName("Deve calcular potência com expoente positivo, negativo e zero")
    void testPotencia() {
        NumeroComplexo num = new NumeroComplexo(0, 1); // i

        // Teste 1: i^2 = -1
        NumeroComplexo esperadoNegUm = new NumeroComplexo(-1, 0);
        assertEquals(esperadoNegUm, num.potencia(2), "i^2 deve ser -1");

        // Teste 2: (3+4i)^0 = 1
        assertEquals(um, a.potencia(0), "Qualquer número elevado a 0 deve ser 1");

        // Teste 3: (3+4i)^1 = (3+4i)
        assertEquals(a, a.potencia(1), "Qualquer número elevado a 1 deve ser ele mesmo");

        // Teste 4: i^(-1) = -i
        NumeroComplexo esperadoNegI = new NumeroComplexo(0, -1);
        NumeroComplexo resultadoNegI = num.potencia(-1);
        assertTrue(Math.abs(esperadoNegI.getReal() - resultadoNegI.getReal()) < EPSILON);
        assertTrue(Math.abs(esperadoNegI.getImaginario() - resultadoNegI.getImaginario()) < EPSILON);
    }

    @Test
    @DisplayName("Deve calcular a raiz n-ésima principal de um número complexo")
    void testRaiz() {
        NumeroComplexo negQuatro = new NumeroComplexo(-4, 0);

        // Raiz quadrada de (-4 + 0i) = (0 + 2i)
        NumeroComplexo esperado = new NumeroComplexo(0, 2);
        NumeroComplexo resultado = negQuatro.raiz(2);

        assertTrue(Math.abs(esperado.getReal() - resultado.getReal()) < EPSILON);
        assertTrue(Math.abs(esperado.getImaginario() - resultado.getImaginario()) < EPSILON);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para raiz com n <= 0")
    void testRaizInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            a.raiz(0);
        }, "Índice da raiz 0 deve lançar exceção");

        assertThrows(IllegalArgumentException.class, () -> {
            a.raiz(-2);
        }, "Índice da raiz negativo deve lançar exceção");
    }

    @Test
    @DisplayName("Deve verificar igualdade corretamente")
    void testEquals() {
        NumeroComplexo a2 = new NumeroComplexo(3, 4);
        NumeroComplexo c = new NumeroComplexo(3.0000000001, 4.0000000001);

        // Teste 1: Igual a si mesmo
        assertTrue(a.equals(a));
        // Teste 2: Igual a outro objeto com mesmos valores
        assertTrue(a.equals(a2));
        // Teste 3: Desigual
        assertFalse(a.equals(b));
        // Teste 4: Igualdade dentro da tolerância
        assertTrue(a.equals(c), "Números devem ser iguais dentro da tolerância do epsilon");
    }

    @Test
    @DisplayName("Deve gerar o mesmo hashCode para objetos iguais")
    void testHashCode() {
        NumeroComplexo a2 = new NumeroComplexo(3, 4);
        // Objetos iguais
        assertEquals(a.hashCode(), a2.hashCode()); // (3 + 4i)

        // Objetos diferentes
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Deve formatar a string corretamente")
    void testToString() {
        // Teste 1: Número complexo completo (positivo)
        String esperadoA = String.format("%.2f + %.2fi", 3.0, 4.0).replace(",", ".");
        assertEquals(esperadoA, a.toString().replace(",", "."));

        // Teste 2: Número complexo completo (negativo)
        String esperadoB = String.format("%.2f - %.2fi", 1.0, 2.0).replace(",", ".");
        assertEquals(esperadoB, b.toString().replace(",", "."));

        // Teste 3: Apenas real
        NumeroComplexo realPuro = new NumeroComplexo(5, 0);
        String esperadoReal = String.format("%.2f", 5.0).replace(",", ".");
        assertEquals(esperadoReal, realPuro.toString().replace(",", "."));

        // Teste 4: Apenas imaginário
        NumeroComplexo imgPuro = new NumeroComplexo(0, -7);
        String esperadoImg = String.format("%.2fi", -7.0).replace(",", ".");
        assertEquals(esperadoImg, imgPuro.toString().replace(",", "."));
    }

    @Test
    @DisplayName("Getters devem retornar os valores corretos")
    void testGetters() {
        assertEquals(3.0, a.getReal());
        assertEquals(4.0, a.getImaginario());
    }
}
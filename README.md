# 🧮 Calculadora Científica de Números Complexos (Trabalho A3)

Este projeto implementa uma calculadora científica em Java, com interface de linha de comando, capaz de processar expressões matemáticas complexas, incluindo variáveis e operações com números complexos.

O código foi estruturado com base no padrão **Árvore de Expressão (AST)** para representar e executar expressões, garantindo o cumprimento dos **Requisitos 2, 4, 6 e 7** do Trabalho A3.

---

## 👥 Equipe e Identificação (Requisito 11)

| Nome do Integrante                          | Registro Acadêmico (RA)                          |
| ------------------------------------------- | ------------------------------------------------ |
| ARTHUR ANDRADE SILVA                        | 12724119792                                      |
| EDUARDO DE ANDRADE DO BOMFIM JÚNIOR         | 12724142791                                      |
| VALENTIN EDUARDO CARVALHO BISPO DOS SANTOS  | 1272415745                                       |

---

## 🚀 Como Executar o Projeto

O projeto é desenvolvido em Java e pode ser executado em qualquer IDE que suporte projetos Maven.

### **Pré-requisitos**

* Java Development Kit (JDK) versão 21 ou superior.

---

## ⚙️ Funcionalidades e Requisitos Atendidos

A seguir, estão listadas as funcionalidades que cumprem os requisitos do Trabalho A3.

### **1. Aritmética de Números Complexos (Requisito 1)**

A classe `NumeroComplexo` implementa:

* Soma (`somar`)
* Subtração (`subtrair`)
* Multiplicação (`multiplicar`)
* Divisão (`dividir`)
* Conjugado (`getConjugado`)
* Potência inteira (`potencia`), usando **Fórmula de De Moivre**
* Raiz n-ésima principal (`raiz`), usando **Forma Polar**

### **2. Análise e Execução de Expressões (Requisitos 2, 4, 7)**

A função **"8. CALCULAR EXPRESSÃO"** aceita expressões complexas arbitrárias e utiliza o `ExpressaoParser` para construir a AST.

**Operadores suportados:**

* `+`, `-`, `*`, `/`, `**`
* `conj(E)`
* `raiz(n, E)`

**Números:** `3`, `4i`, `2.5-3.1i`, etc.

**Variáveis:** exemplo `Z1`, `X` (solicita valores durante a execução).

### **3. Detecção de Erros e Exceções (Requisito 5)**

Exceções:

| Exceção                           | Descrição                                             |
| --------------------------------- | ----------------------------------------------------- |
| `ArithmeticException`             | Divisão por `0 + 0i` ou cálculo de `0^x` com `x < 0`. |
| `IllegalArgumentException`        | Índice da raiz ≤ 0.                                   |

### **4. Representação da Árvore (Requisito 6)**

A calculadora exibe a expressão em duas formas:

* **Notação LISP** (ex.: `(* (+ 3.00+2.00i 5.00) Z1)`)
* **Árvore ASCII** (com `├──` e `└──`)

### **5. Verificação de Igualdade (Requisito 3)**

A opção **"9. COMPARAR DUAS EXPRESSÕES"** usa `equals()` recursivo para determinar se duas expressões são estruturalmente idênticas.

---

## 📁 Estrutura do Código

```
src/
└── com/
    └── zetacompute/
        ├── models/
        │   ├── Expressao.java
        │   ├── NumeroComplexo.java
        │   ├── NoConstante.java
        │   ├── NoOperacao.java
        │   ├── NoResultado.java
        │   └── NoVariavel.java
        │   └── NoVariavel.java   
        └── CalculadoraComplexa.java
            
```

---

Este documento atende ao **Requisito 11** do Trabalho A3.

package com.zetacompute.models;

import java.util.Map;
import java.util.Objects;

public class NoResultado {
    private final NumeroComplexo valor;

    public NoResultado(NumeroComplexo valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoResultado)) return false;
        NoResultado that = (NoResultado) o;
        return Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
}

package com.github.liebharc.JavaRules;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 214158696;
    private final String type;
    private final long id;
    private int hashCode;

    public Token(String type) {
        this(type, 0);
    }

    public Token(String type, long id) {
        this.type = type;
        this.id = id;
        hashCode = Objects.hash(this.type, this.id);
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Token token = (Token) o;
        return id == token.id && Objects.equals(type, token.type);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, id);
    }
}

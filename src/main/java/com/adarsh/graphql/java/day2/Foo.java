package com.adarsh.graphql.java.day2;

public class Foo {
    private String bar;

    public Foo(String bar) {
        this.bar = bar;
    }

    public String getBar() {
        return bar;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Foo{");
        sb.append("bar='").append(bar).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

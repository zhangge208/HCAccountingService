package com.hardcore.accounting.model.service;

public class Greeting {
    private final Long id;
    private String name;

    public Greeting(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

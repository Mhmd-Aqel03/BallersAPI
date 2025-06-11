package com.ballersApi.ballersApi.security;

import java.security.Principal;

public class WebSocketUserPrincipal implements Principal {
    private String name;

    public WebSocketUserPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

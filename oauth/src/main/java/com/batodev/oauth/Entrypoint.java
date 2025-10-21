package com.batodev.oauth;

import no.nav.security.mock.oauth2.MockOAuth2Server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Entrypoint {
    public static void main(String[] args) throws UnknownHostException {
        MockOAuth2Server server = new MockOAuth2Server();
        server.start(InetAddress.getByName("0.0.0.0"), 9000);
        String issuerId = "default";
        server.wellKnownUrl(issuerId).toString();
    }
}

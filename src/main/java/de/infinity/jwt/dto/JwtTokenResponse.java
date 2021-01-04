package de.infinity.jwt.dto;

public class JwtTokenResponse {
    private String jwt;

    public JwtTokenResponse() {
    }

    public JwtTokenResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}

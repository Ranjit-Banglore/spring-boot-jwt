package de.infinity.jwt.rest_template;

import de.infinity.jwt.dto.AuthenticationRequest;
import de.infinity.jwt.dto.JwtTokenResponse;
import de.infinity.jwt.dto.BankAccountDto;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;


public class RestTemplateTest {

    private static String BASE_URL = "http://localhost:8080/bank";

    private RestTemplate restTemplate = new RestTemplate();

    public String getJwtToken() {
        return restTemplate.postForObject(BASE_URL + "/token", new AuthenticationRequest("ranjit", "password"), JwtTokenResponse.class).getJwt();
    }

    @Test
    public void restTemplateWithBearerToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getJwtToken());
        HttpEntity httpEntity = new HttpEntity(headers);
        var response = restTemplate.exchange(BASE_URL + "/api/1/bankaccounts", HttpMethod.GET, httpEntity, BankAccountDto[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Stream.of(response).forEach(System.out::println);
        }
    }
}

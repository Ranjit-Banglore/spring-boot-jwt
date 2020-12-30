package de.infinity.jwt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Strength set as 16
                String encodedPassword = encoder.encode("password");
                System.out.println("BCryptPasswordEncoder");
                System.out.println(encodedPassword);
                System.out.println("\n");
            }
}

package com.dembaandousmane.gest_priere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.dembaandousmane.gest_priere",

})
public class GestPriereSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestPriereSpringbootApplication.class, args);
    }

}

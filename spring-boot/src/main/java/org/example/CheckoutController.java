package org.example;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    @PostMapping("/checkout")
    public CheckoutResponse checkout() {
        return new CheckoutResponse();
    }

    public record CheckoutResponse() {}

}

package fun.gusmurphy.library.springboothex.adapter.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutHttpAdapter {

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

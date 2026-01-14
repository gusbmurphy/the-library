package fun.gusmurphy.library.springbootadm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import fun.gusmurphy.library.springbootadm.controller.CheckoutController.CheckoutRequest;
import fun.gusmurphy.library.springbootadm.domain.Book;
import fun.gusmurphy.library.springbootadm.repository.UserRepository;
import fun.gusmurphy.library.springbootadm.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class CheckoutControllerTest {

    @Mock BookService bookService;
    @Mock UserRepository userRepository;

    @InjectMocks CheckoutController controller;

    @Test
    void checkoutReturns403WhenUserIsNotRegistered() {
        var request = new CheckoutRequest("some-isbn", "unregistered-user");
        when(userRepository.existsById("unregistered-user")).thenReturn(false);

        var response = controller.checkout(request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User is not registered.", response.getBody());
    }

    @Test
    void checkoutReturns404WhenBookIsUnknown() {
        var request = new CheckoutRequest("unknown-isbn", "registered-user");
        when(userRepository.existsById("registered-user")).thenReturn(true);
        when(bookService.getBookByIsbn("unknown-isbn")).thenReturn(null);

        var response = controller.checkout(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Unknown book.", response.getBody());
    }

    @Test
    void checkoutReturns200WhenSuccessful() {
        var request = new CheckoutRequest("available-isbn", "registered-user");
        when(userRepository.existsById("registered-user")).thenReturn(true);

        var book = new Book();
        book.setIsbn("available-isbn");
        when(bookService.getBookByIsbn("available-isbn")).thenReturn(book);
        when(bookService.checkoutBook("available-isbn", "registered-user")).thenReturn(true);

        var response = controller.checkout(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void checkoutReturns409WhenBookIsAlreadyCheckedOut() {
        var request = new CheckoutRequest("checked-out-isbn", "registered-user");
        when(userRepository.existsById("registered-user")).thenReturn(true);

        var book = new Book();
        book.setIsbn("checked-out-isbn");
        when(bookService.getBookByIsbn("checked-out-isbn")).thenReturn(book);
        when(bookService.checkoutBook("checked-out-isbn", "registered-user")).thenReturn(false);

        var response = controller.checkout(request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Book is currently checked out.", response.getBody());
    }
}

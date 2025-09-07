package org.example.domain;

import java.time.ZonedDateTime;

public record CheckoutRecord(String userId, Book book, ZonedDateTime checkoutTime) {}

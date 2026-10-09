package com.aditya.idempotency;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class IdempotencyApplicationTest { @Test void paymentRequestCarriesExpectedValues(){var r=new PaymentController.PaymentRequest("aditya",499);assertEquals("aditya",r.customer());assertEquals(499,r.amount());} }

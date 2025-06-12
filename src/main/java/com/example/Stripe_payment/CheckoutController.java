package com.example.Stripe_payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

  @Autowired private CheckoutService checkoutService;

  @PostMapping
  public String checkout(@RequestBody CheckoutRequest request) {
    return checkoutService.createCheckoutSession(request.getItems());
  }
}

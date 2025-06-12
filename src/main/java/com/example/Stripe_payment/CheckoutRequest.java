package com.example.Stripe_payment;

import java.util.List;

public class CheckoutRequest {
  private List<CheckoutItemDto> items;

  public List<CheckoutItemDto> getItems() {
    return items;
  }

  public void setItems(List<CheckoutItemDto> items) {
    this.items = items;
  }
}

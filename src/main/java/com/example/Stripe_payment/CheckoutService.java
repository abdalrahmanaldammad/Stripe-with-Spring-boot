package com.example.Stripe_payment;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

  @Value("${strip.secret_key}")
  private String secretKey;

  @PostConstruct
  public void init() {
    Stripe.apiKey = secretKey;
  }

  public String createCheckoutSession(List<CheckoutItemDto> items) {
    List<SessionCreateParams.LineItem> lineItems =
        items.stream()
            .map(
                item ->
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(item.getAmount())
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(item.getName())
                                        .build())
                                .build())
                        .setQuantity(item.getQuantity())
                        .build())
            .collect(Collectors.toList());

    SessionCreateParams params =
        SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:3000/success")
            .setCancelUrl("http://localhost:3000/cancel")
            .addAllLineItem(lineItems)
            .build();

    try {
      Session session = Session.create(params);
      return session.getUrl();
    } catch (Exception e) {
      throw new RuntimeException("Stripe error: " + e.getMessage());
    }
  }
}

package com.example.Stripe_payment;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
public class StripeWebhookController {

  @Value("${strip.webhook_secret}")
  private String endpointSecret;

  public static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

  @PostMapping
  public ResponseEntity<String> handleStripeWebhook(
      @RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {

    Event event;

    try {
      event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
    } catch (SignatureVerificationException e) {
      return ResponseEntity.badRequest().body("Invalid signature");
    }
    log.info("the event type is {}", event.getType());
    if ("checkout.session.completed".equals(event.getType())) {
      Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

      if (session != null) {
        System.out.println("✅ Payment received for order: " + session.getClientReferenceId());
      }
    }

    return ResponseEntity.ok("Webhook received");
  }
}

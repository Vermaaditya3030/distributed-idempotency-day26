package com.aditya.idempotency;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="payment_record", uniqueConstraints=@UniqueConstraint(name="uk_payment_idempotency_key", columnNames="idempotencyKey"))
public class PaymentRecord {
 @Id private UUID id;
 @Column(nullable=false, unique=true) private String idempotencyKey;
 @Column(nullable=false, length=64) private String payloadHash;
 @Column(nullable=false) private String customer;
 private long amount;
 @Column(nullable=false) private String status;
 @Column(nullable=false) private Instant createdAt;
 protected PaymentRecord() {}
 public PaymentRecord(UUID id,String key,String hash,String customer,long amount){this.id=id;this.idempotencyKey=key;this.payloadHash=hash;this.customer=customer;this.amount=amount;this.status="SUCCEEDED";this.createdAt=Instant.now();}
 public UUID getId(){return id;} public String getIdempotencyKey(){return idempotencyKey;} public String getPayloadHash(){return payloadHash;} public String getCustomer(){return customer;} public long getAmount(){return amount;} public String getStatus(){return status;} public Instant getCreatedAt(){return createdAt;}
}

package com.aditya.idempotency;
import org.springframework.http.*; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.bind.annotation.*; import java.nio.charset.StandardCharsets; import java.security.MessageDigest; import java.util.*;
@RestController @RequestMapping("/api/payments") public class PaymentController {
 private final PaymentRepository repository; public PaymentController(PaymentRepository repository){this.repository=repository;}
 @PostMapping @Transactional public ResponseEntity<?> create(@RequestHeader(value="Idempotency-Key",required=false) String key,@RequestBody PaymentRequest request) throws Exception {
  if(key==null||key.isBlank()||key.length()>200)return ResponseEntity.badRequest().body(Map.of("error","A valid Idempotency-Key header is required"));
  if(request.customer()==null||request.customer().isBlank()||request.amount()<=0)return ResponseEntity.badRequest().body(Map.of("error","customer is required and amount must be positive"));
  String canonical=request.customer().trim()+"|"+request.amount(); String hash=HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(canonical.getBytes(StandardCharsets.UTF_8)));
  Optional<PaymentRecord> prior=repository.findByIdempotencyKey(key); if(prior.isPresent())return replayOrConflict(prior.get(),hash);
  PaymentRecord record=new PaymentRecord(UUID.randomUUID(),key,hash,request.customer().trim(),request.amount());
  try {repository.saveAndFlush(record);return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(record,false));}
  catch(org.springframework.dao.DataIntegrityViolationException race){Optional<PaymentRecord> winner=repository.findByIdempotencyKey(key);if(winner.isPresent())return replayOrConflict(winner.get(),hash);throw race;}
 }
 private ResponseEntity<?> replayOrConflict(PaymentRecord p,String hash){if(!p.getPayloadHash().equals(hash))return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","Idempotency-Key was already used with a different request payload"));return ResponseEntity.ok(toResponse(p,true));}
 private Map<String,Object> toResponse(PaymentRecord p,boolean replayed){return Map.of("paymentId",p.getId(),"customer",p.getCustomer(),"amount",p.getAmount(),"status",p.getStatus(),"idempotencyKey",p.getIdempotencyKey(),"replayed",replayed);}
 @GetMapping public List<PaymentRecord> all(){return repository.findAll();}
 public record PaymentRequest(String customer,long amount){}
}

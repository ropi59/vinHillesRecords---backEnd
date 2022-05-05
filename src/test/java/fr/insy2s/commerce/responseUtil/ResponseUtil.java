package fr.insy2s.commerce.responseUtil;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
  static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
    return wrapOrNotFound(maybeResponse, null);
  }

static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
    return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  } 
}

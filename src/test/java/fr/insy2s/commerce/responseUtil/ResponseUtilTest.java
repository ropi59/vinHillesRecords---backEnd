package fr.insy2s.commerce.responseUtil;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class ResponseUtilTest {

  private static final String HEADER_NAME = "X-Test";
  private static final String HEADER_VALUE = "FooBar";

  private Optional<Integer> yes;
  private Optional<Integer> no;
  private HttpHeaders headers;
  
  @BeforeEach
  public void setup() {
    yes = Optional.of(42);
    no = Optional.empty();
    headers = new HttpHeaders();
    headers.add(HEADER_NAME, HEADER_VALUE);
  }

  @Test
  void testYesWithoutHeaders() {
    //TODO remove this file when this code is implemented in another test
    ResponseEntity<Integer> response = ResponseUtil.wrapOrNotFound(yes);
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody()).isEqualTo(42);
    Assertions.assertThat(response.getHeaders()).isEmpty();
  }
}


package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.product.ProductDtoNoId;
import fr.insy2s.commerce.mappers.ProductMapper;
import fr.insy2s.commerce.models.Product;
import fr.insy2s.commerce.repositories.custom.ProductCustomRepository;
import fr.insy2s.commerce.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @MockBean
  private ProductCustomRepository productCustomRepository;

  @Test
  void createValidProductWithoutTag() throws Exception {
    Instant releaseDate = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    Instant disponibilityDate = Instant.now().plusSeconds(1).truncatedTo(ChronoUnit.MILLIS);

    String requestBody = "{\"title\":\"titre 1\"," +
            "\"artist\":\"artist1\"," +
            "\"description\":\"description1\"," +
            "\"price\": 1.11," +
            "\"label\":\"label1\"," +
            "\"format\":\"format1\"," +
            "\"stock\": 11," +
            "\"coverUrl\":\"coverUrl1\"," +
            "\"releaseDate\":\"" + releaseDate + "\"," +
            "\"disponibilityDate\":\"" + disponibilityDate + "\"," +
            "\"tags\": [] }";

    Optional<ProductDtoNoId> productDtoNoId = Optional.ofNullable(ProductDtoNoId.builder()
            .title("titre 1")
            .artist("artist1")
            .description("description1")
            .price(1.11f)
            .label("label1")
            .format("format1")
            .stock(11)
            .coverUrl("coverUrl1")
            .releaseDate(releaseDate)
            .disponibilityDate(disponibilityDate)
            .tags(new ArrayList<>())
            .build());

    Product newProduct = ProductMapper.productDtoToProduct(productDtoNoId);

    when(productService.createProduct(Mockito.any(productDtoNoId.getClass()))).thenReturn(newProduct);

    RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/products")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andExpect(status().isCreated())
            .andReturn();

    String response = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

    JSONAssert.assertEquals("{title: \"titre 1\"," +
            "\"artist\": \"artist1\"," +
            "description: description1," +
            "price: 1.11," +
            "label: label1," +
            "format: format1," +
            "stock: 11," +
            "coverUrl: coverUrl1," +
            "releaseDate:\"" + releaseDate + "\"," +
            "disponibilityDate:\"" + disponibilityDate + "\"," +
            "tags: []" +
            "}", response, false);
  }

  @Test
  void DontCreateProductWithEmptyParam() throws Exception {
    String requestBody = "";

    RequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/products")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON);

    MvcResult mvcResult = mockMvc.perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andReturn();
  }

  @Test
  void DontCreateProductWithParamNull() throws Exception {
    boolean thrown = false;
    try{
      String requestBody = null;
      RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/products")
        .content(requestBody)
        .contentType(MediaType.APPLICATION_JSON);
    } catch (Exception e) {
      thrown = true;
    }
    assertTrue(thrown);
  }

  @Test
  void getAllProduct() {
  }

  @Test
  void getOneProduct() {
  }

  @Test
  void update() {
  }

  @Test
  void getProductsByTagId() {
  }
}
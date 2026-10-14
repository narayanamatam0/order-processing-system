package com.ecommerce.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createsAndRetrievesOrderWithExactDecimalTotal() throws Exception {
        String body = """
                {"customerId":"CUSTOMER-101","currency":"INR","items":[
                  {"productId":"A","quantity":3,"unitPrice":0.10},
                  {"productId":"B","quantity":2,"unitPrice":19.99}
                ]}
                """;
        var result = mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.totalAmount").value(40.28))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.customerId").value("CUSTOMER-101"))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andReturn();
        JsonNode created = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID.fromString(created.get("orderId").asText());
        assertThat(result.getResponse().getHeader("Location"))
                .isEqualTo("/api/orders/" + created.get("orderId").asText());
        mvc.perform(get(result.getResponse().getHeader("Location")))
                .andExpect(status().isOk())
                .andExpect(content().json(result.getResponse().getContentAsString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{}",
            "{\"customerId\":\" \",\"currency\":\"INR\",\"items\":[{\"productId\":\"A\",\"quantity\":1,\"unitPrice\":1}]}",
            "{\"customerId\":\"C\",\"currency\":\"INR\",\"items\":[]}",
            "{\"customerId\":\"C\",\"currency\":\"INR\",\"items\":[null]}",
            "{\"customerId\":\"C\",\"currency\":\"inr\",\"items\":[{\"productId\":\"A\",\"quantity\":1,\"unitPrice\":1}]}",
            "{\"customerId\":\"C\",\"currency\":\"INR\",\"items\":[{\"productId\":\"\",\"quantity\":1,\"unitPrice\":1}]}",
            "{\"customerId\":\"C\",\"currency\":\"INR\",\"items\":[{\"productId\":\"A\",\"quantity\":0,\"unitPrice\":1}]}",
            "{\"customerId\":\"C\",\"currency\":\"INR\",\"items\":[{\"productId\":\"A\",\"quantity\":1,\"unitPrice\":-1}]}",
            "{\"customerId\":\"C\",\"currency\":\"INR\",\"items\":[{\"productId\":\"A\",\"quantity\":1,\"unitPrice\":1.001}]}"
    })
    void rejectsInvalidRequestFields(String body) throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errors").isMap());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "{broken", "{\"customerId\":\"C\",\"currency\":\"INR\",\"items\":[{\"productId\":\"A\",\"quantity\":1.5,\"unitPrice\":1}]}"})
    void rejectsMissingMalformedOrFractionalQuantityBodies(String body) throws Exception {
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(
                        "Request body is missing or contains malformed JSON or invalid field types."));
    }

    @Test
    void returns404ForUnknownOrder() throws Exception {
        mvc.perform(get("/api/orders/" + UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void rejectsMalformedOrderId() throws Exception {
        mvc.perform(get("/api/orders/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}

package com.valensas.exception.integrationTests

import com.valensas.exception.model.MissingParameterModel
import com.valensas.exception.model.WrongFormatParameterModel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class WebParameterExceptionErrorHandlerTest {
    @Autowired lateinit var webClient: WebTestClient

    @Test
    fun `Try to send get request with missing header and check response message`() {
        webClient.get()
            .uri("/web/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
            .expectBody()
            .jsonPath("$.message").isEqualTo("Required request header 'headerNumber' for method parameter type long is not present")
            .returnResult()
    }

    @Test
    fun `Try to send get request with wrong format header and check response message`() {
        webClient.get()
            .uri("/web/${UUID.randomUUID()}")
            .header("headerNumber", "invalid_value")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath(
                "$.message"
            ).isEqualTo(
                "Failed to convert value of type 'java.lang.String' to required type 'long'; For input string: \"invalid_value\""
            )
            .returnResult()
    }

    @Test
    fun `Try to send get request with missing request param and check response message`() {
        webClient.get()
            .uri("/web")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath("$.message").isEqualTo("Required request parameter 'param' for method parameter type UUID is not present")
    }

    @Test
    fun `Try to send get request with wrong format request param and check response message`() {
        webClient.get()
            .uri("/web?param=12")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath(
                "$.message"
            ).isEqualTo("Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: 12")
    }

    @Test
    fun `Try to send get request with wrong format path variable and check response message`() {
        webClient.get()
            .uri("/web/12345")
            .header("headerNumber", "12")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath(
                "$.message"
            ).isEqualTo(
                "Failed to convert value of type 'java.lang.String' to required type 'java.util.UUID'; Invalid UUID string: 12345"
            )
    }

    @Test
    fun `Try to send post request with wrong format body variable and check response message`() {
        webClient.post()
            .uri("/web")
            .bodyValue(WrongFormatParameterModel(12, 13))
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath(
                "$.message"
            ).isEqualTo(
                "JSON parse error: Cannot deserialize value of type `java.util.UUID` from String \"13\": " +
                    "UUID has to be represented by standard 36-char representation"
            )
    }

    @Test
    fun `Try to send post request with missing body variable and check response message`() {
        webClient.post()
            .uri("/web")
            .bodyValue(MissingParameterModel(12))
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath(
                "$.message"
            ).isEqualTo(
                "JSON parse error: Instantiation of [simple type, class com.valensas.exception.model.TestModel] " +
                    "value failed for JSON property id due to missing (therefore NULL) value for creator parameter id " +
                    "which is a non-nullable type"
            )
    }
}

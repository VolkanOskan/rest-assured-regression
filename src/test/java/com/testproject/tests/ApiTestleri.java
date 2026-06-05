package com.testproject.tests;

// REST API testleri için gerekli kütüphaneler
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Yazılım Test Mühendisliği Proje Ödevi
 *
 * Kullanılan API: https://jsonplaceholder.typicode.com
 * Bu API ücretsiz ve herkese açık bir örnek REST servisidir.
 *
 * Yapılan kontroller:
 *  - Status code (HTTP durum kodu)
 *  - Response body (cevap içeriği)
 *  - Response time (yanıt süresi)
 */
class ApiTestleri {

    // Test edeceğimiz servisin adresi
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    // Maksimum kabul edilebilir yanıt süresi: 3 saniye
    private static final long MAX_SURE = 3000L;

    // ---------------------------------------------------------------
    // Tüm testler çalışmadan önce bir kez çalışır
    // RestAssured'a hangi sunucuya bağlanacağını söylüyoruz
    // ---------------------------------------------------------------
    @BeforeAll
    static void kurulum() {
        RestAssured.baseURI = BASE_URL;
    }

    // ===============================================================
    // TEST 1: Tüm postları getir (GET isteği)
    // ===============================================================
    @Test
    @DisplayName("Test 1: GET /posts → 200 döner ve 100 kayıt gelir")
    void tumPostlarıGetir() {

        // given → isteği hazırla
        // when  → isteği gönder
        // then  → cevabı kontrol et
        given()
                .header("Accept", "application/json") // JSON formatında cevap istiyoruz
                .when()
                .get("/posts")                        // GET isteği gönder
                .then()
                .statusCode(200)                      // ✔ HTTP 200 OK gelmeli
                .body("$", hasSize(100))              // ✔ 100 adet kayıt gelmeli
                .body("[0].id", notNullValue())       // ✔ İlk kaydın id'si boş olmamalı
                .body("[0].title", notNullValue());   // ✔ İlk kaydın başlığı boş olmamalı
    }

    // ===============================================================
    // TEST 2: Tek bir post getir (GET isteği)
    // ===============================================================
    @Test
    @DisplayName("Test 2: GET /posts/1 → doğru kayıt gelir")
    void tekPostGetir() {

        given()
                .header("Accept", "application/json")
                .when()
                .get("/posts/1")                      // 1 numaralı postu getir
                .then()
                .statusCode(200)                      // ✔ HTTP 200 OK gelmeli
                .body("id", equalTo(1))               // ✔ Gelen kaydın id'si 1 olmalı
                .body("userId", equalTo(1))           // ✔ userId alanı 1 olmalı
                .body("title", notNullValue())        // ✔ title alanı dolu olmalı
                .body("body", notNullValue());        // ✔ body alanı dolu olmalı
    }

    // ===============================================================
    // TEST 3: Yanıt süresi kontrolü (GET isteği)
    // ===============================================================
    @Test
    @DisplayName("Test 3: GET /posts/1 → 3 saniyeden hızlı cevap verir")
    void yanitSuresiKontrol() {

        // Cevabı bir değişkene alıyoruz ki süreyi ölçebilelim
        Response cevap = given()
                .header("Accept", "application/json")
                .when()
                .get("/posts/1")
                .then()
                .statusCode(200)
                .extract().response();               // Cevabı al

        // Yanıt süresini milisaniye cinsinden ölç
        long sure = cevap.getTime();

        // Süre 3000 ms'den az olmalı
        assertTrue(sure < MAX_SURE,
                "Yanıt süresi çok uzun! Gelen süre: " + sure + "ms");
    }

    // ===============================================================
    // TEST 4: Yeni post oluştur (POST isteği - JSON body ile)
    // ===============================================================
    @Test
    @DisplayName("Test 4: POST /posts → yeni kayıt oluşturulur (201)")
    void yeniPostOlustur() {

        // Göndereceğimiz JSON verisi (request body)
        String requestBody = "{"
                + "\"userId\": 1,"
                + "\"title\": \"Rest Assured Test Gonderisi\","
                + "\"body\": \"Bu gonderi Rest Assured ile otomatik olusturuldu.\""
                + "}";

        given()
                .header("Content-Type", "application/json") // JSON gönderiyoruz
                .header("Accept", "application/json")
                .body(requestBody)                          // Request body'yi ekle
                .when()
                .post("/posts")                             // POST isteği gönder
                .then()
                .statusCode(201)                            // ✔ HTTP 201 Created gelmeli
                .body("id", notNullValue())                 // ✔ Yeni kaydın id'si atanmış olmalı
                .body("userId", equalTo(1))                 // ✔ Gönderdiğimiz userId dönmeli
                .body("title", equalTo(                     // ✔ Gönderdiğimiz title dönmeli
                        "Rest Assured Test Gonderisi"));
    }

    // ===============================================================
    // TEST 5: POST isteği yanıt süresi kontrolü
    // ===============================================================
    @Test
    @DisplayName("Test 5: POST /posts → 3 saniyeden hızlı cevap verir")
    void postYanitSuresiKontrol() {

        String requestBody = "{"
                + "\"userId\": 2,"
                + "\"title\": \"Sure Testi\","
                + "\"body\": \"Bu test yanit suresini olcer.\""
                + "}";

        // Cevabı al
        Response cevap = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract().response();

        // Yanıt süresini ölç
        long sure = cevap.getTime();

        // Süre kontrolü yap
        assertTrue(sure < MAX_SURE,
                "POST yanit suresi cok uzun! Gelen sure: " + sure + "ms");
    }
}

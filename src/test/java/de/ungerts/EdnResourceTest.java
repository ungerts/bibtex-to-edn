package de.ungerts;

import clojure.lang.*;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.WithAssertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class EdnResourceTest implements WithAssertions {

    @Test
    void getEdn() {
        given()
                .when().get("/edn")
                .then()
                .statusCode(200)
                .contentType("text/plain+edn")
                .body(containsString(":authors [\"John Smith\" \"Jane Doe\"]"));
    }

    @Test
    void getEdnStream() {
        String body = given()
                .when().get("/edn/stream")
                .then()
                .statusCode(200)
                .contentType("text/plain+edn")
                .extract().body().asString();
        assertThat(body).contains(":authors [\"John Smith\" \"Jane Doe\"]");
        Object edn = parseEdn(body);
        assertThat(edn).isInstanceOf(PersistentVector.class);
        PersistentVector entries = (PersistentVector) edn;
        assertThat(entries.size()).isEqualTo(3);
        Object javaList = convertToJava(entries);
        assertThat(javaList).isInstanceOf(List.class);
    }

    private static Object convertToJava(@NonNull Object ednObject) {
        switch (ednObject) {
            case PersistentVector vector -> {
                return vector.stream().map(EdnResourceTest::convertToJava).toList();
            }
            case APersistentMap map -> {
                return map.entrySet().stream().map(EdnResourceTest::convertToJava).toList();
            }
            case AMapEntry entry -> {
                return Map.entry(
                        convertToJava(entry.getKey()),
                        convertToJava(entry.getValue())
                );
            }
            case Keyword keyword -> {
                return keyword.getName();
            }
            default -> {
                return ednObject;
            }
        }

    }

    public static Object parseEdn(String ednString) {
        IFn readString = clojure.java.api.Clojure.var("clojure.edn", "read-string");
        return readString.invoke(ednString);
    }
}
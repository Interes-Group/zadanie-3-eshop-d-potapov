package sk.stuba.fei.uim.oop.assignment3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class Assignment3Tests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddProduct() throws Exception {
        addProduct();
    }

    @Test
    void testAddProduct201Response() throws Exception {
        addProduct("name", "description", "unit", 1L, 1.0, status().isCreated());
    }

    @Test
    void testGetAllProduct() throws Exception {
        MvcResult result = mockMvc.perform(get("/product")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        addProduct();
        addProduct();
        mockMvc.perform(get("/product")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            var list = stringToObject(mvcResult, ArrayList.class);
            assert list.size() == 2 + stringToObject(result, ArrayList.class).size();
        });
    }

    @Test
    void testGetProductById() throws Exception {
        TestProductResponse product = addProduct();
        mockMvc.perform(get("/product/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestProductResponse productToControl = stringToObject(mvcResult, TestProductResponse.class);
                    assert Objects.equals(productToControl.getId(), product.getId());
                });
    }

    @Test
    void testGetMissingProductById() throws Exception {
        TestProductResponse product = addProduct();
        mockMvc.perform(get("/product/" + (product.getId() + 1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProduct() throws Exception {
        TestProductResponse product = addProduct();
        TestProductRequest update1 = new TestProductRequest();
        update1.name = "updated name";
        TestProductRequest update2 = new TestProductRequest();
        update2.description = "updated description";
        mockMvc.perform(put("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update1)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestProductResponse response = stringToObject(mvcResult, TestProductResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getDescription(), product.getDescription());
                });
        mockMvc.perform(put("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(update2)))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestProductResponse response = stringToObject(mvcResult, TestProductResponse.class);
                    assert Objects.equals(response.getName(), update1.getName());
                    assert Objects.equals(response.getDescription(), update2.getDescription());
                });
    }

    @Test
    void testUpdateMissingProduct() throws Exception {
        TestProductResponse product = addProduct();
        mockMvc.perform(put("/product/" + (product.getId() + 1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(new TestProductRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct() throws Exception {
        TestProductResponse product = addProduct();
        mockMvc.perform(delete("/product/" + product.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/product/" + product.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMissingProduct() throws Exception {
        TestProductResponse product = addProduct();
        mockMvc.perform(delete("/product/" + (product.getId() + 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetProductAmount() throws Exception {
        TestProductResponse product = addProduct();
        mockMvc.perform(get("/product/" + product.getId() + "/amount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    Amount response = stringToObject(mvcResult, Amount.class);
                    assert Objects.equals(response.getAmount(), product.getAmount());
                });
    }

    @Test
    void testGetMissingProductAmount() throws Exception {
        TestProductResponse product = addProduct();
        mockMvc.perform(get("/product/" + (product.getId() + 1) + "/amount")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testIncrementProductAmount() throws Exception {
        TestProductResponse product = addProduct();
        Amount request = new Amount();
        request.setAmount(10);
        mockMvc.perform(post("/product/" + product.getId() + "/amount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString(request)))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    Amount response = stringToObject(mvcResult, Amount.class);
                    assert Objects.equals(response.getAmount(), product.getAmount() + request.getAmount());
                });
        mockMvc.perform(get("/product/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(mvcResult -> {
                    TestProductResponse response = stringToObject(mvcResult, TestProductResponse.class);
                    assert Objects.equals(response.getAmount(), product.getAmount() + request.getAmount());
                });
    }

    @Test
    void testIncrementMissingProductAmount() throws Exception {
        TestProductResponse product = addProduct();
        Amount request = new Amount();
        request.setAmount(10);
        mockMvc.perform(post("/product/" + (product.getId() + 1) + "/amount")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToString(request)))
                .andExpect(status().isNotFound());
    }

    // ShoppingCart tests ==========================================================================================
    @Test
    void testAddShoppingCart() throws Exception {
        addCart();
    }

    @Test
    void testAddShoppingCart201Response() throws Exception {
        addCart(status().isCreated());
    }

    @Test
    void testGetShoppingCartById() throws Exception {
        TestCartResponse cart = addCart();
        MvcResult mvcResult = mockMvc.perform(get("/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestCartResponse cartResponse = stringToObject(mvcResult, TestCartResponse.class);
        assert Objects.equals(cartResponse.getId(), cart.getId());
    }

    @Test
    void testGetMissingShoppingCartById() throws Exception {
        TestCartResponse cart = addCart();
        mockMvc.perform(get("/cart/" + cart.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteShoppingCartById() throws Exception {
        TestCartResponse cart = addCart();
        mockMvc.perform(delete("/cart/" + cart.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(get("/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMissingShoppingCartById() throws Exception {
        TestCartResponse cart = addCart();
        mockMvc.perform(delete("/cart/" + cart.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void addProductToCart() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        assert cart.getShoppingList().isEmpty();
        TestCartResponse updatedCart = addProductToCart(product, cart, 2L);
        assert updatedCart.getShoppingList().size() == 1;
        assert updatedCart.getShoppingList().get(0).productId == product.getId();
        assert updatedCart.getShoppingList().get(0).amount == 2L;
    }

    @Test
    void addProductToCartRemovesFromStorage() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        addProductToCart(product, cart, 2L);
        mockMvc.perform(get("/product/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestProductResponse productToControl = stringToObject(mvcResult, TestProductResponse.class);
                    assert Objects.equals(productToControl.getAmount(), 3L);
                });
    }

    @Test
    void addProductToCartNotEnoughProduct() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        addProductToCart(product, cart, 10L, status().isBadRequest());
        mockMvc.perform(get("/product/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestProductResponse productToControl = stringToObject(mvcResult, TestProductResponse.class);
                    assert Objects.equals(productToControl.getAmount(), 5L);
                });
        assert cart.getShoppingList().isEmpty();
        mockMvc.perform(get("/cart/" + cart.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestCartResponse cartToControl = stringToObject(mvcResult, TestCartResponse.class);
                    assert cartToControl.getShoppingList().isEmpty();
                });
    }

    @Test
    void addProductToCartMissingProduct() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        addProductToCart(product.getId() + 1, cart.getId(), 5L, status().isNotFound());
    }

    @Test
    void addProductToCartMissingCart() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        addProductToCart(product.getId(), cart.getId() + 1, 5L, status().isNotFound());
    }

    @Test
    void addProductToCartExistingProduct() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(10L);
        addProductToCart(product, cart, 5L);
        TestCartResponse updatedCart = addProductToCart(product, cart, 5L);
        assert updatedCart.getShoppingList().size() == 1;
        assert updatedCart.getShoppingList().get(0).productId == product.getId();
        assert updatedCart.getShoppingList().get(0).amount == 10L;
    }

    @Test
    void testPayForShoppingCart() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L, 10.0);
        TestProductResponse product2 = addProduct(5L, 20.0);
        addProductToCart(product, cart, 4L);
        addProductToCart(product2, cart, 2L);
        mockMvc.perform(get("/cart/" + cart.getId() + "/pay")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(mvcResult -> {
            assert mvcResult.getResponse().getContentAsString().length() > 0;
            double price = Double.parseDouble(mvcResult.getResponse().getContentAsString());
            assert price == 80.0;
        }).andReturn();
        MvcResult mvcResult = mockMvc.perform(get("/cart/" + cart.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        TestCartResponse cartResponse = stringToObject(mvcResult, TestCartResponse.class);
        assert cartResponse.isPayed();
    }

    @Test
    void testPayForMissingCart() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        addProductToCart(product, cart, 4L);
        mockMvc.perform(get("/cart/" + (cart.getId() + 1) + "/pay")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void testPayForCartTwice() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        addProductToCart(product, cart, 4L);
        mockMvc.perform(get("/cart/" + cart.getId() + "/pay")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mockMvc.perform(get("/cart/" + cart.getId() + "/pay")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void addProductToCartPayedCart() throws Exception {
        TestCartResponse cart = addCart();
        TestProductResponse product = addProduct(5L);
        mockMvc.perform(get("/cart/" + cart.getId() + "/pay")
                .accept(MediaType.TEXT_PLAIN)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        addProductToCart(product, cart, 4L, status().isBadRequest());
        mockMvc.perform(get("/product/" + product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestProductResponse productToControl = stringToObject(mvcResult, TestProductResponse.class);
                    assert Objects.equals(productToControl.getAmount(), 5L);
                });
        mockMvc.perform(get("/cart/" + cart.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(mvcResult -> {
                    TestCartResponse cartToControl = stringToObject(mvcResult, TestCartResponse.class);
                    assert cartToControl.getShoppingList().isEmpty();
                });
    }


    TestProductResponse addProduct() throws Exception {
        return addProduct("name", "description", "unit", 1L, 10.0, status().is2xxSuccessful());
    }

    TestProductResponse addProduct(Long amount) throws Exception {
        return addProduct("name", "description", "unit", amount, 10L, status().is2xxSuccessful());
    }

    TestProductResponse addProduct(Long amount, double price) throws Exception {
        return addProduct("name", "description", "unit", amount, price, status().is2xxSuccessful());
    }

    TestProductResponse addProduct(String name, String description, String unit, Long amount, double price) throws Exception {
        return addProduct(name, description, unit, amount, price, status().is2xxSuccessful());
    }

    TestProductResponse addProduct(String name, String description, String unit, Long amount, double price, ResultMatcher statusMatcher) throws Exception {
        TestProductRequest product = new TestProductRequest();
        product.setName(name);
        product.setDescription(description);
        product.setUnit(unit);
        product.setAmount(amount);
        product.setPrice(price);
        MvcResult mvcResult = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(product))
                ).andExpect(statusMatcher)
                .andDo(mvcResult1 -> {
                    TestProductResponse productToControl = stringToObject(mvcResult1, TestProductResponse.class);
                    assert Objects.equals(product.getName(), productToControl.getName());
                    assert Objects.equals(product.getDescription(), productToControl.getDescription());
                    assert Objects.equals(product.getUnit(), productToControl.getUnit());
                    assert Objects.equals(product.getAmount(), productToControl.getAmount());
                })
                .andReturn();
        return stringToObject(mvcResult, TestProductResponse.class);
    }

    TestCartResponse addCart() throws Exception {
        return addCart(status().is2xxSuccessful());
    }

    TestCartResponse addCart(ResultMatcher statusMatcher) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/cart")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(statusMatcher)
                .andReturn();
        return stringToObject(mvcResult, TestCartResponse.class);
    }

    TestCartResponse addProductToCart(TestProductResponse product, TestCartResponse cart, long amount) throws Exception {
        return addProductToCart(product, cart, amount, status().isOk());
    }

    TestCartResponse addProductToCart(TestProductResponse product, TestCartResponse cart, long amount, ResultMatcher statusMatcher) throws Exception {
        return addProductToCart(product.getId(), cart.getId(), amount, statusMatcher);
    }

    TestCartResponse addProductToCart(long productId, long cartId, long amount, ResultMatcher statusMatcher) throws Exception {
        TestCartEntry cartEntry = new TestCartEntry(productId, amount);
        MvcResult mvcResult = mockMvc.perform(post("/cart/" + cartId + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectToString(cartEntry)))
                .andExpect(statusMatcher)
                .andReturn();
        if (mvcResult.getResponse().getStatus() == HttpStatus.OK.value()) {
            return stringToObject(mvcResult, TestCartResponse.class);
        }
        return null;
    }

    static String objectToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <K> K stringToObject(MvcResult object, Class<K> objectClass) throws UnsupportedEncodingException, JsonProcessingException {
        return new ObjectMapper().readValue(object.getResponse().getContentAsString(), objectClass);
    }

    @Getter
    @Setter
    private static class Amount {
        private long amount;
    }

    @Getter
    @Setter
    private static class TestProductRequest extends Amount {
        private String name;
        private String description;
        private String unit;
        private double price;
    }

    @Getter
    @Setter
    private static class TestProductResponse extends TestProductRequest {
        private long id;
    }

    @Getter
    @Setter
    private static class TestCartResponse {
        private long id;
        private List<TestCartEntry> shoppingList;
        private boolean payed;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TestCartEntry {
        private Long productId;
        private Long amount;
    }
}

package com.bookstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Checkout request payload")
public class CheckoutRequestDto {

    @Schema(
            description = "Cart ID to checkout",
            example = "12",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long cartId;

    @NotNull
    private AddressRequestDto shippingAddress;

    @Schema(
            description = "Payment method",
            example = "MOCK_CARD",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private String paymentMethod;

    @Schema(
            description = """
        Optional user ID.
        - Used ONLY by ADMIN to checkout on behalf of a user
        - Must NOT be provided by CUSTOMER
        """,
            example = "3",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Long userId;

    public CheckoutRequestDto() {}

    public CheckoutRequestDto(Long cartId, AddressRequestDto shippingAddress, String paymentMethod, Long userId) {
        this.cartId = cartId;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.userId = userId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public AddressRequestDto getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressRequestDto shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

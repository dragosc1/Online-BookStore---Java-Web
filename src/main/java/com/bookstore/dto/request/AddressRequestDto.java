package com.bookstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Shipping address payload")
public class AddressRequestDto {

    @NotBlank
    @Schema(description = "Street address", example = "123 Main St", required = true)
    private String street;

    @NotBlank
    @Schema(description = "City name", example = "New York", required = true)
    private String city;

    @NotBlank
    @Schema(description = "Postal/ZIP code", example = "10001", required = true)
    private String postalCode;

    @NotBlank
    @Schema(description = "State or province", example = "NY", required = true)
    private String state;

    @NotBlank
    @Schema(description = "Country", example = "USA", required = true)
    private String country;

    public AddressRequestDto() {
    }

    public AddressRequestDto(String street, String city, String postalCode, String state, String country) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.state = state;
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

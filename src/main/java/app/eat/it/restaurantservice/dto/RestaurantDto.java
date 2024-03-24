package app.eat.it.restaurantservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record RestaurantDto(@NotEmpty String name,
                            @NotEmpty String address) {
}

package app.eat.it.restaurantservice.unit;

import app.eat.it.restaurantservice.dto.RestaurantDto;
import app.eat.it.restaurantservice.exception.EntityNotFoundException;
import app.eat.it.restaurantservice.model.Restaurant;
import app.eat.it.restaurantservice.repository.RestaurantRepository;
import app.eat.it.restaurantservice.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    private final RestaurantRepository repository = Mockito.mock(RestaurantRepository.class);
    private final RestaurantService restaurantService = new RestaurantService(repository);

    @Test
    void getRestaurants_should_return_restaurantList() {
        var restaurantList = List.of(prepareSampleRestaurant(randomId()));
        var restaurantPage = new PageImpl<>(restaurantList);

        when(repository.findAll(any(Pageable.class))).thenReturn(restaurantPage);

        var retrievedRestaurantList = restaurantService.getRestaurants(1, 10, "name", "asc");

        verify(repository, times(1)).findAll(any(Pageable.class));
        assertThat(retrievedRestaurantList).usingRecursiveAssertion().isEqualTo(restaurantList);
    }

    @Test
    void getRestaurant_should_return_restaurant_if_id_exists() {
        var id = randomId();
        var restaurant = prepareSampleRestaurant(id);

        when(repository.findById(id)).thenReturn(Optional.of(restaurant));

        var retrievedRestaurant = restaurantService.getRestaurant(id);

        verify(repository, times(1)).findById(id);
        assertEquals(retrievedRestaurant, restaurant);
    }

    @Test
    void getRestaurant_should_throw_exception_if_id_not_exists() {
        var id = randomId();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> restaurantService.getRestaurant(id));
        verify(repository, times(1)).findById(id);
    }

    @Test
    void createRestaurant_should_return_restaurant() {
        var id = randomId();
        var restaurant = prepareSampleRestaurant(null);
        var restaurantWithId = prepareSampleRestaurant(id);

        when(repository.save(restaurant)).thenReturn(restaurantWithId);

        restaurantService.createRestaurant(restaurant);

        verify(repository, times(1)).save(restaurant);
        assertEquals(id, restaurantWithId.getId());
    }

    @Test
    void updateRestaurant() {
        var id = randomId();
        var restaurantDto = new RestaurantDto("Blue whale", "Paris");
        var restaurant = prepareSampleRestaurant(id);

        when(repository.findById(id)).thenReturn(Optional.of(restaurant));
        when(repository.save(any(Restaurant.class))).thenReturn(restaurant);

        restaurantService.updateRestaurant(id, restaurantDto);

        verify(repository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void deleteRestaurant() {
        var id = randomId();
        var restaurant = prepareSampleRestaurant(id);

        when(repository.findById(id)).thenReturn(Optional.of(restaurant));

        restaurantService.deleteRestaurant(id);

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).delete(restaurant);
    }

    private Restaurant prepareSampleRestaurant(Long id) {
        return new Restaurant(id, "Blue whale", "Paris");
    }

    private Long randomId() {
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}
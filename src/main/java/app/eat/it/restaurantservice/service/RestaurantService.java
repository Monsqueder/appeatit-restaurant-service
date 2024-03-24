package app.eat.it.restaurantservice.service;

import app.eat.it.restaurantservice.dto.RestaurantDto;
import app.eat.it.restaurantservice.exception.EntityNotFoundException;
import app.eat.it.restaurantservice.model.Restaurant;
import app.eat.it.restaurantservice.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;
    private final List<String> allowedSortParameters = List.of("name", "address");

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> getRestaurants(Integer pageNumber, Integer pageSize, String sortParameter, String sortType) {
        pageNumber--;
        Pageable pageable;
        if (validateSortParameter(sortParameter)) {
            Sort sort;
            if ("asc".equalsIgnoreCase(sortType)) {
                sort = Sort.by(sortParameter).ascending();
            } else {
                sort = Sort.by(sortParameter).descending();
            }
            pageable = PageRequest.of(pageNumber, pageSize, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        Page<Restaurant> restaurantPage = repository.findAll(pageable);
        return restaurantPage.toList();
    }

    public Restaurant getRestaurant(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Restaurant.class, id));
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public void updateRestaurant(Long id, RestaurantDto restaurantDto) {
        Restaurant restaurant = getRestaurant(id);
        restaurant.setName(restaurantDto.name());
        restaurant.setAddress(restaurantDto.address());
        repository.save(restaurant);
    }

    public void deleteRestaurant(Long id) {
        Restaurant restaurant = getRestaurant(id);
        repository.delete(restaurant);
    }

    private boolean validateSortParameter(String sortParameter) {
        return isNotBlank(sortParameter) && allowedSortParameters.contains(sortParameter);
    }

}

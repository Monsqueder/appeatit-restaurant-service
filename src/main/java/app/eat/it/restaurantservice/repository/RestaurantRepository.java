package app.eat.it.restaurantservice.repository;

import app.eat.it.restaurantservice.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Page<Restaurant> findAll(Pageable pageable);

}

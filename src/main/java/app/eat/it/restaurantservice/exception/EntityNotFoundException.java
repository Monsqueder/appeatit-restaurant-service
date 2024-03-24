package app.eat.it.restaurantservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, Long id) {
        super(clazz.getName() + "with id: " + id + " is not found!");
    }
}

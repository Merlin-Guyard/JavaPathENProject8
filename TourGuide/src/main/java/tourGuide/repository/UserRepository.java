package tourGuide.repository;

import org.springframework.data.repository.CrudRepository;
import tourGuide.user.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}

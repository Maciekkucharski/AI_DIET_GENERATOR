package pjait.dgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjait.dgen.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}

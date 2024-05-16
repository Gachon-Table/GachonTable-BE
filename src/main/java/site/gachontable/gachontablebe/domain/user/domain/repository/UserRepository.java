package site.gachontable.gachontablebe.domain.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.user.domain.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}

package site.gachontable.domain.member.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.domain.member.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUserTel(String tel);
}

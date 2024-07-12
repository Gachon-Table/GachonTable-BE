package site.gachontable.gachontablebe.domain.menu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}

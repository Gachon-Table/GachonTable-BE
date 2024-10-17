package site.gachontable.gachontablebe.domain.menu.domain.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Cacheable(key = "#pub.pubName", value = "menuCache", cacheManager = "menuCacheManager")
    List<Menu> findAllByPub(Pub pub);

    void deleteAllByPub(Pub pub);
}

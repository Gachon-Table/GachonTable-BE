package site.gachontable.gachontablebe.domain.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;

import java.util.List;
import java.util.Optional;

public interface PubRepository extends JpaRepository<Pub, Integer> {
    Optional<Pub> findByPubId(Integer pubId);

    @Query("SELECT DISTINCT p FROM pub p WHERE p.pubName LIKE %:keyword% OR p.oneLiner LIKE %:keyword%")
    List<Pub> findAllByPubNameOrOneLinerContaining(@Param("keyword") String keyword);
}

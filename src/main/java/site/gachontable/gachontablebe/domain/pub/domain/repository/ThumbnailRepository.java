package site.gachontable.gachontablebe.domain.pub.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.Thumbnail;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {

    @Query("SELECT t.url FROM thumbnail t WHERE t.pub = :pub")
    List<String> findUrlsByPub(Pub pub);

    List<Thumbnail> findAllByPub(Pub pub);
}

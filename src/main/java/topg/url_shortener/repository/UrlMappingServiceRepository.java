package topg.url_shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import topg.url_shortener.models.UrlMapping;
import topg.url_shortener.models.User;

import java.util.List;

@Repository
public interface UrlMappingServiceRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(User user);


}

package topg.url_shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import topg.url_shortener.models.ClickedEvent;
import topg.url_shortener.models.UrlMapping;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickedEvent, Long> {

    List<ClickedEvent> findByUrlMappingAndClickDateBetween(UrlMapping urlMapping, LocalDateTime start, LocalDateTime end );
    List<ClickedEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMapping, LocalDateTime start, LocalDateTime end );
}

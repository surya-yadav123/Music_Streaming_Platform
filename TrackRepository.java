package in.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.main.entities.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
}

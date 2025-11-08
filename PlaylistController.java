package in.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.main.entities.Playlist;
import in.main.entities.User;
import in.main.entities.Track;
import in.main.repository.PlaylistRepository;
import in.main.repository.UserRepository;
import in.main.repository.TrackRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TrackRepository trackRepo;

    // ✅ Create playlist with JSON body
    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(@RequestBody Playlist playlist) {
        if (playlist.getUser() == null || playlist.getUser().getEmail() == null) {
            return ResponseEntity.badRequest().body("User email missing");
        }

        Optional<User> userOpt = userRepo.findByEmail(playlist.getUser().getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        playlist.setUser(userOpt.get());
        playlistRepo.save(playlist);
        return ResponseEntity.ok("Playlist created successfully");
    }

    // ✅ Add a track to playlist
    @PostMapping("/{playlistId}/addTrack/{trackId}")
    public ResponseEntity<?> addTrackToPlaylist(@PathVariable("playlistId") Long playlistId, @PathVariable("trackId") Long trackId) {
        Optional<Playlist> p = playlistRepo.findById(playlistId);
        Optional<Track> t = trackRepo.findById(trackId);

        if (p.isEmpty() || t.isEmpty()) return ResponseEntity.badRequest().body("Playlist or track not found");

        Playlist playlist = p.get();
        playlist.getTracks().add(t.get());
        playlistRepo.save(playlist);

        return ResponseEntity.ok("Track added successfully");
    }
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserPlaylists(@PathVariable("email") String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");

        List<Playlist> playlists = playlistRepo.findByUser(userOpt.get());
        return ResponseEntity.ok(playlists);
    }
    @DeleteMapping("/{playlistId}/removeTrack/{trackId}")
    public ResponseEntity<?> removeTrackFromPlaylist(@PathVariable("playlistId") Long playlistId, @PathVariable("trackId") Long trackId) {
        Optional<Playlist> p = playlistRepo.findById(playlistId);
        Optional<Track> t = trackRepo.findById(trackId);

        if (p.isEmpty() || t.isEmpty()) return ResponseEntity.badRequest().body("Playlist or track not found");

        Playlist playlist = p.get();
        boolean removed = playlist.getTracks().remove(t.get());

        if (!removed)
            return ResponseEntity.badRequest().body("Track not found in playlist");

        playlistRepo.save(playlist);
        return ResponseEntity.ok("Track removed successfully");
    }
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long playlistId) {
        Optional<Playlist> p = playlistRepo.findById(playlistId);
        if (p.isEmpty()) return ResponseEntity.badRequest().body("Playlist not found");

        playlistRepo.deleteById(playlistId);
        return ResponseEntity.ok("Playlist deleted successfully");
    }
}

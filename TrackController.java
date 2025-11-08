package in.main.controller;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    // ✅ Folder where your songs are stored
    private static final String MUSIC_FOLDER = "C:/MusicUploads";

    @GetMapping
    public List<TrackDTO> getAllTracks() {
        File folder = new File(MUSIC_FOLDER);

        // Return empty list if folder doesn’t exist
        if (!folder.exists() || !folder.isDirectory()) {
            return List.of();
        }

        // ✅ Filter only .mp3 and .wav files
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".mp3") || name.toLowerCase().endsWith(".wav")
        );

        if (files == null) return List.of();

        // ✅ Convert files into DTOs
        return Arrays.stream(files)
                .map(file -> new TrackDTO(
                        file.getName().replaceFirst("[.][^.]+$", ""), // Title without extension
                        "Unknown Artist",
                        "/music/" + file.getName() // public URL
                ))
                .collect(Collectors.toList());
    }

    // ✅ DTO class
    public static class TrackDTO {
        private String title;
        private String artist;
        private String url;

        public TrackDTO(String title, String artist, String url) {
            this.title = title;
            this.artist = artist;
            this.url = url;
        }

        public String getTitle() { return title; }
        public String getArtist() { return artist; }
        public String getUrl() { return url; }
    }
}

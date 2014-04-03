package ua.edu.sumdu.model;

import ua.edu.sumdu.domain.Track;

import java.util.List;

/**
 * User: flash
 * Date: 28.01.12
 */
public class TrackListResponse implements SimpleResponse{

    List<Track> tracks;

    public TrackListResponse() {
    }

    public TrackListResponse(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean isSuccess() {
        return tracks != null;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}

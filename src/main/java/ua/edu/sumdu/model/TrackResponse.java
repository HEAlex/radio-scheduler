package ua.edu.sumdu.model;

import ua.edu.sumdu.domain.Track;

public class TrackResponse implements SimpleResponse {

    private Track track;

    public TrackResponse() {
    }

    public TrackResponse(Track track) {
        this.track = track;
    }

    @Override
    public boolean isSuccess() {
        return track != null;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}

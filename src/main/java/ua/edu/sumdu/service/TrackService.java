package ua.edu.sumdu.service;

import ua.edu.sumdu.domain.Direction;
import ua.edu.sumdu.domain.Track;

import java.util.Date;
import java.util.List;

public interface TrackService {

    void addTrack(Track track);

    Track getTrack(Integer id);

    void updateTrack(Track track);

    void deleteTrack(Integer id);

    void moveTrack(Integer trackId, Direction direction);
    
    List<Track> getWeekTracks(Date date);
    
    List<List<List<Track>>> getTrackCalendar(Date startDate);

    void deleteTracksByFile(int file);
}

package ua.edu.sumdu.dao;

import ua.edu.sumdu.domain.Direction;
import ua.edu.sumdu.domain.Track;

import java.util.Date;
import java.util.List;

public interface TrackDao {

    public void addTrack(Track track);

    public Track getTrack(Integer id);
    
    public void updateTrack(Track track);

    public void deleteTrack(Integer id);

    public List<Track> getWeekTracks(Date date);

    public void moveTrack(Track track, Direction direction);

    public String[] getTracks(Date date, int breakOfDay);
    
    public void deleteTracksByFile(int trackId);
}

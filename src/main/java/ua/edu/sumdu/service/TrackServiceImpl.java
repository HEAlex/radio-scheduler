package ua.edu.sumdu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.sumdu.dao.TrackDao;
import ua.edu.sumdu.domain.Direction;
import ua.edu.sumdu.domain.Track;
import ua.edu.sumdu.util.DateUtils;

import java.util.*;

@Service
public class TrackServiceImpl implements TrackService {

    @Value("${ftpserver.user.admin.homedirectory}")
    String trackPath;
    
    @Autowired
    TrackDao trackDao;

    @Transactional
    public void addTrack(Track track) {
        trackDao.addTrack(track);
    }

    @Transactional
    public Track getTrack(Integer id) {
        return trackDao.getTrack(id);
    }

    @Transactional
    public void updateTrack(Track track) {
        trackDao.updateTrack(track);
    }

    @Transactional
    public void deleteTrack(Integer id) {
        trackDao.deleteTrack(id);
    }

    @Transactional
    public void moveTrack(Integer trackId, Direction direction) {
        Track track = trackDao.getTrack(trackId);
        trackDao.moveTrack(track, direction);

    }

    @Override
    public List<Track> getWeekTracks(Date date) {
        return trackDao.getWeekTracks(DateUtils.clearDate(new Date()));
    }

    @Transactional
    public List<List<List<Track>>> getTrackCalendar(Date startDate) {
        ArrayList<List<List<Track>>> cal = new ArrayList<List<List<Track>>>(8);
        for (int i = 0; i <= 7; ++i) {
            ArrayList<List<Track>> day = new ArrayList<List<Track>>(7);
            for (int j = 0; j <= 8; ++j) {
                day.add(new LinkedList<Track>());
            }
            cal.add(day);
        }
        for (Track track : trackDao.getWeekTracks(startDate)) {
            cal.get(DateUtils.getDayDifference(startDate, track.getDate())).get(track.getTimePart()).add(track);
        }
        return cal;
    }

    @Transactional
    public void deleteTracksByFile(int file) {
        trackDao.deleteTracksByFile(file);
    }


}

package ua.edu.sumdu.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.domain.Direction;
import ua.edu.sumdu.domain.Mp3File;
import ua.edu.sumdu.domain.Track;
import ua.edu.sumdu.util.DateUtils;

import java.util.Date;
import java.util.List;

@Repository
public class TrackDaoImpl implements TrackDao {

    private static final String MOVE_DOWN_QWERY =
            "UPDATE ua.edu.sumdu.domain.Track AS t SET t.position = t.position + 1 " +
            "WHERE t.position < :cur_pos AND t.position >= :new_pos " +
            "AND t.date = :day AND timePart = :timePart";

    private static final String MOVE_UP_QWERY =
            "UPDATE ua.edu.sumdu.domain.Track AS t SET t.position = t.position + 1 " +
            "WHERE t.position <= :new_pos and t.position > :cur_pos " +
            "AND t.date = :day AND timePart = :timePart";

    @Autowired
    Mp3FileDao mp3FileDao;

    @Autowired
    private SessionFactory sessionFactory;

    public void addTrack(Track track) {
        sessionFactory.getCurrentSession().save(track);
    }

    public Track getTrack(Integer id) {
        return (Track) sessionFactory.getCurrentSession().get(Track.class, id);
    }

    public void updateTrack(Track track) {
        sessionFactory.getCurrentSession().update(track);
    }

    public void deleteTrack(Integer id) {
        Track track = getTrack(id);
        sessionFactory.getCurrentSession().createSQLQuery("UPDATE Tracks SET position = position - 1 " +
                "WHERE position > :pos").setInteger("pos", track.getPosition()).executeUpdate();
        sessionFactory.getCurrentSession().delete(track);
    }

    @SuppressWarnings("unchecked")
    public List<Track> getWeekTracks(Date date) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Track.class)
                .add(Restrictions.between("date", DateUtils.getWeekStartDate(date), DateUtils.getWeekEndDate(date)))
                .addOrder(Order.asc("date"))
                .addOrder(Order.asc("timePart"))
                .addOrder(Order.asc("position"))
                .list();
    }

    public void moveTrack(Track track, Direction direction) {
        Integer oldPosition = track.getPosition();
        Integer newPosition = direction.equals(Direction.DOWN) ? oldPosition + 1 : oldPosition - 1;

        Track neighbor = (Track) sessionFactory.getCurrentSession().createCriteria(Track.class)
                .add(direction.equals(Direction.DOWN)
                        ? Restrictions.ge("position", newPosition)
                        : Restrictions.le("position", newPosition))
                .add(Restrictions.eq("date", track.getDate()))
                .add(Restrictions.eq("timePart", track.getTimePart()))
                .setMaxResults(1)
                .addOrder(direction.equals(Direction.DOWN)
                        ? Order.asc("position")
                        : Order.desc("position"))
                .uniqueResult();
        if (neighbor != null) {
            neighbor.setPosition(oldPosition);
            sessionFactory.getCurrentSession().update(neighbor);
        }
        track.setPosition(newPosition);
        sessionFactory.getCurrentSession().update(track);
    }

    public String[] getTracks(Date date, int breakOfDay) {
        Session session = sessionFactory.openSession();
        //TODO: сделать возвращаемым типом список фалов.
        Query q = session.createSQLQuery(
                "SELECT filename FROM Tracks t " +
                        "JOIN Files f ON t.fileId = f.id " +
                        "WHERE date=:date AND timePart=:break ORDER BY position ASC"
        );
        q.setDate("date", date);
        q.setInteger("break", breakOfDay);
        List list = q.list();
        session.close();
        return (String[]) list.toArray(new String[0]);
    }

    public void deleteTracksByFile(int id) {
        Mp3File file = mp3FileDao.getMp3File(id);
        mp3FileDao.deleteMp3File(file);
        sessionFactory.getCurrentSession()
                .createSQLQuery("DELETE FROM Tracks WHERE fileId = :fileId")
                .setInteger("fileId", id).executeUpdate();
        //TODO: update positions
    }
}

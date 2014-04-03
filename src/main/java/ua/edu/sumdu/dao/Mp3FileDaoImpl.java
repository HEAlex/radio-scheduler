package ua.edu.sumdu.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.stereotype.Repository;
import ua.edu.sumdu.domain.Mp3File;
import ua.edu.sumdu.util.Mp3FileUtils;

import java.io.File;
import java.util.List;

@Repository
public class Mp3FileDaoImpl implements Mp3FileDao {

    @Value("${ftpserver.user.admin.homedirectory}")
    String trackPath;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Mp3File getMp3File(int id) {
        return (Mp3File) sessionFactory.getCurrentSession().get(Mp3File.class, id);
    }

    @Override
    public Mp3File getMp3FileByFilename(String path) {
        List<Mp3File> files = sessionFactory.getCurrentSession()
                .createCriteria(Mp3File.class)
                .add(Restrictions.eq("filename", path))
                .list();
        if (files.size()==1){
            return files.get(0);
        }
        if (files.size()==0){
            return null;
        }
        throw new DataIntegrityViolationException("Too many files found. Expected 1. Seems file structure is broken.");
    }

    @Override
    public void createMp3File(Mp3File file) {
        sessionFactory.getCurrentSession().save(file);
    }

    @Override
    public void updateMp3File(Mp3File file) {
        sessionFactory.getCurrentSession().update(file);
    }

    @Override
    public void deleteMp3File(Mp3File file) {
        File realFile = new File(trackPath + file.getFilename());
        if (realFile.exists()) {
            realFile.delete();
        }
        sessionFactory.getCurrentSession().delete(file);
    }

    @Override
    public List<Mp3File> getAllMp3Files() {
        return getAllMp3Files(false);
    }

    /*
    what is it for?
     */
    @Override
    public List<Mp3File> getAllMp3Files(boolean force) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            /* ignore */
        }
        boolean closeOnEnd = false;
        if (session == null) {
            session = sessionFactory.openSession();
            closeOnEnd = true;
        }
        List<Mp3File> res = session.createCriteria(Mp3File.class).list();
        if (closeOnEnd) {
            session.close();
        }
        return res;
    }

    @Override
    public void forgotDeletedFiles(List<String> list) {
        List<Mp3File> deleted = sessionFactory.getCurrentSession()
            .createCriteria(Mp3File.class)
            .add(Restrictions.not(
                    Restrictions.in("filename", list)
            ))
            .list();
        for (Mp3File file : deleted) {
            sessionFactory.getCurrentSession().delete(file);
        }
    }
}

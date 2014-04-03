package ua.edu.sumdu.dao;

import ua.edu.sumdu.domain.Mp3File;

import java.util.List;

public interface Mp3FileDao {

    public Mp3File getMp3File(int id);

    public Mp3File getMp3FileByFilename(String path);

    void createMp3File(Mp3File file);

    void updateMp3File(Mp3File file);

    void deleteMp3File(Mp3File file);

    List<Mp3File> getAllMp3Files();

    public List<Mp3File> getAllMp3Files(boolean force);

    void forgotDeletedFiles(List<String> list);
}

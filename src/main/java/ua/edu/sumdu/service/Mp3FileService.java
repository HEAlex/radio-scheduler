package ua.edu.sumdu.service;

import org.springframework.transaction.annotation.Transactional;
import ua.edu.sumdu.domain.FileTreeNode;
import ua.edu.sumdu.domain.Mp3File;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Mp3FileService {
    public Mp3File getMp3File(int id);

    public void createMp3File(Mp3File file);

    public void updateMp3File(Mp3File file);

    public void deleteMp3File(int id);

    public void deleteMp3File(String filename);

    public List<Mp3File> getAllMp3Files();

    public void createMp3File(String filename, String filepath) throws UnsupportedAudioFileException, IOException;
   
    public FileTreeNode getFileTree();

    void refreshFileTree();

    @Transactional
    void renameMp3File(String oldFilename, String newFilename);

    List<String> resyncFiles();

    void syncFile(File file, String cutSuffix);

    @Transactional
    void updateDatabase();
}

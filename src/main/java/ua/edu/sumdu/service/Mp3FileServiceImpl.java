package ua.edu.sumdu.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.sumdu.dao.Mp3FileDao;
import ua.edu.sumdu.domain.FileTreeNode;
import ua.edu.sumdu.domain.Mp3File;
import ua.edu.sumdu.util.Mp3FileUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class Mp3FileServiceImpl implements Mp3FileService, InitializingBean {

    @Autowired
    Mp3FileDao mp3FileDao;

    FileTreeNode root;

    @Value("${ftpserver.user.admin.homedirectory}")
    String trackPath;

    List<String> files;

    @Override
    public List<String> resyncFiles() {
        files = new LinkedList<String>();
        File tracksFolder = new File(trackPath);
        syncFile(tracksFolder, tracksFolder.getPath());
        System.out.println(files.toString());
        return files;
    }

    @Transactional
    @Override
    public void updateDatabase() {
        List<String> list = resyncFiles();
        mp3FileDao.forgotDeletedFiles(list);
        for (String file : list) {
            Mp3File mp3File = mp3FileDao.getMp3FileByFilename(file);
            if (mp3File == null) {
                try{
                    mp3File = new Mp3File();
                    mp3File.setFilename(file);
                    mp3File.setDuration(Mp3FileUtils.getDuration(new File(trackPath + file)));
                    mp3FileDao.createMp3File(mp3File);
                } catch (Exception e) {}
            }
        }
        refreshFileTree();
    }

    @Override
    public void syncFile(File file, String cutSuffix) {
        if (file.isDirectory()) {
            for(File f : file.listFiles()){
                syncFile(f, cutSuffix);
            }
        } else {
            files.add(file.getPath().replace(cutSuffix,"").replace("/", File.separator));
        }
    }

    @Transactional
    @Override
    public Mp3File getMp3File(int id) {
        return mp3FileDao.getMp3File(id);
    }

    @Transactional
    @Override
    public void createMp3File(Mp3File file) {
        mp3FileDao.createMp3File(file);
    }

    @Transactional
    @Override
    public void createMp3File(String filename, String filepath) throws UnsupportedAudioFileException, IOException {
        Mp3File newMp3File = new Mp3File();
        filepath = filepath.endsWith("/") ? filepath : filepath + "/";
        newMp3File.setFilename(filepath + filename);
        newMp3File.setDuration(Mp3FileUtils.getDuration(new File(trackPath + filepath + filename)));
        mp3FileDao.createMp3File(newMp3File);
        root.add(newMp3File);
    }

    @Transactional
    @Override
    public void updateMp3File(Mp3File file) {
        mp3FileDao.updateMp3File(file);
    }

    @Transactional
    @Override
    public void deleteMp3File(int id) {
        Mp3File file = mp3FileDao.getMp3File(id);
        mp3FileDao.deleteMp3File(file);
        root.deleteFile(file);
    }

    @Transactional
    @Override
    public void deleteMp3File(String filename) {
        Mp3File file = mp3FileDao.getMp3FileByFilename(filename);
        mp3FileDao.deleteMp3File(file);
        refreshFileTree();
    }

    @Transactional
    @Override
    public void renameMp3File(String oldFilename, String newFilename) {
        Mp3File file = mp3FileDao.getMp3FileByFilename(oldFilename);
        file.setFilename(newFilename);
        mp3FileDao.updateMp3File(file);
        refreshFileTree();
    }

    @Transactional
    @Override
    public List<Mp3File> getAllMp3Files() {
        return mp3FileDao.getAllMp3Files();
    }

    @Transactional
    @Override
    public FileTreeNode getFileTree() {
        return root;
    }

    @Transactional
    @Override
    public void refreshFileTree(){
        root = new FileTreeNode("/");
        root.addAll(mp3FileDao.getAllMp3Files(true));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        refreshFileTree();
    }
}

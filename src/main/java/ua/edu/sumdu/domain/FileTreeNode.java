package ua.edu.sumdu.domain;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import ua.edu.sumdu.util.FileTreeNodeSerializer;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@JsonSerialize(using = FileTreeNodeSerializer.class)
public class FileTreeNode {

    Map<String, FileTreeNode> nodes = new ConcurrentHashMap<String, FileTreeNode>();

    List<Mp3File> children = new LinkedList<Mp3File>();

    String basePath;

    public FileTreeNode(String basePath) {
        this.basePath = basePath;
    }

    private void add(String folder, Mp3File file) {
        int pos = file.getFilename().indexOf(File.separator, folder.length() + 1);
        if (pos > 0) {
            String currentFolder = file.getFilename().substring(folder.length(), pos);
            if (!nodes.containsKey(currentFolder)) {
                nodes.put(currentFolder, new FileTreeNode(currentFolder));
            }
            nodes.get(currentFolder).add(file.getFilename().substring(0, pos + 1), file);
        } else {
            if (children == null) {
                children = new LinkedList<Mp3File>();
            }
            children.add(file);
        }
    }

    public void add(Mp3File file) {
        this.add(basePath, file);
    }

    public void addAll(Collection<Mp3File> newFiles) {
        for (Mp3File f : newFiles) {
            add(f);
        }
    }

    private void deleteFile(String folder, Mp3File file) {
        int pos = file.getFilename().indexOf("/", folder.length() + 1);
        if (pos > 0) {
            String currentFolder = file.getFilename().substring(folder.length(), pos);
            if (nodes.containsKey(currentFolder)) {
                nodes.get(currentFolder).deleteFile(file.getFilename().substring(0, pos + 1), file);
                if (nodes.get(currentFolder).getChildren().isEmpty() && nodes.get(currentFolder).getNodes().isEmpty()) {
                    nodes.remove(currentFolder);
                }
                return;
            } else {
                throw new NullPointerException("File delete fail.");
            }
        } else {
            if (children.contains(file)){
                children.remove(file);
                return;
            }
        }

        throw new NullPointerException("File delete fail.");

    }
    
    public void deleteFile(Mp3File file){
        deleteFile(basePath, file);
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<Mp3File> getChildren() {
        return children;
    }

    public void setChildren(List<Mp3File> children) {
        this.children = children;
    }

    public Map<String, FileTreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, FileTreeNode> nodes) {
        this.nodes = nodes;
    }

}

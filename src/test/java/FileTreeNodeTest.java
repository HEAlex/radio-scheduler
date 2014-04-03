import org.junit.Test;
import ua.edu.sumdu.domain.FileTreeNode;
import ua.edu.sumdu.domain.Mp3File;
import static org.junit.Assert.*;

public class FileTreeNodeTest{

    static Mp3File file1, file2, file3;
    static {
        file1 = new Mp3File();
        file1.setId(1);
        file1.setFilename("/filetest1.mp3");

        file2 = new Mp3File();
        file2.setId(2);
        file2.setFilename("/test1/filetest2.mp3");

        file3 = new Mp3File();
        file3.setId(3);
        file3.setFilename("/test1/test2/filetest3.mp3");
    }

    private static FileTreeNode initSimpleFileTreeNode(){
        FileTreeNode root = new FileTreeNode("/");
        root.add(file1);
        root.add(file2);
        root.add(file3);

        return root;
    }

    @Test
    public void insertTest(){
        FileTreeNode root = initSimpleFileTreeNode();

        assertTrue(root.getChildren().contains(file1));
        assertTrue(root.getNodes().size() > 0);
        assertTrue(root.getNodes().containsKey("test1"));
        assertTrue(root.getNodes().get("test1").getChildren().contains(file2));
        assertTrue(root.getNodes().get("test1").getNodes().containsKey("test2"));
        assertTrue(root.getNodes().get("test1").getNodes().get("test2").getChildren().contains(file3));
    }

    @Test
    public void deleteTest(){
        FileTreeNode root = initSimpleFileTreeNode();
        root.deleteFile(file2);
        assertFalse(root.getNodes().get("test1").getChildren().contains(file2));
        root.deleteFile(file3);
        assertFalse(root.getNodes().containsKey("test1"));
    }

}

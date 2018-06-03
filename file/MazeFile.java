/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import domain.Block;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yerlin Leal
 */
public class MazeFile {

    private String path;

    public MazeFile() {
        this.path = "maze.dat";
    }

    public void addMaze(Block[][] newMaze) throws IOException, ClassNotFoundException {
        File file = new File(this.path);
        List<Block[][]> mazeList = new ArrayList<>();
        if (file.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            Object aux = objectInputStream.readObject();
            mazeList = (List<Block[][]>) aux;
            objectInputStream.close();
        }
        mazeList.add(newMaze);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
        objectOutputStream.writeUnshared(mazeList);
        objectOutputStream.close();
    }

    public List<Block[][]> getAllMaze() throws IOException, ClassNotFoundException {
        File myFile = new File(this.path);
        List<Block[][]> mazeList = new ArrayList<>();
        if (myFile.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(myFile));
            Object aux = objectInputStream.readObject();
            mazeList = (List<Block[][]>) aux;
            objectInputStream.close();
        }
        return mazeList;
    }

    public Block[][] getMaze(int difficulty) throws IOException, ClassNotFoundException {
        List<Block[][]> list = getAllMaze();
        return list.get(difficulty-1);
    }

} // end class



import com.mpatric.mp3agic.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.LinkedList;

public class mp3Sorter {
    public String mp3directory, sortedDirectory, textFileDirectory;
    public boolean madeTextFile;
    public FileWriter writer;
    public LinkedList<String> textFileLines;

    public mp3Sorter(String mp3directory) {
        this.mp3directory = mp3directory;
        this.sortedDirectory = this.mp3directory + "//sorted";
        this.textFileDirectory = this.mp3directory + "//ArtistAlbumYear.txt";
        this.madeTextFile = false;
        this.textFileLines = new LinkedList<>();
    }

    /**
     * crates folder called sorted for bands -> albums -> songs
     * @return whether folder made
     */
    public boolean makeSortedFolder(){
        File f1 = new File(this.sortedDirectory);
        return f1.mkdir();
    }

    /**
     * set field for whether successfully made Artist/Album (Year) text file
     * @throws IOException .
     */
    public void createTextFile() throws IOException {
        //make text file
        File textFile = new File (this.textFileDirectory);
        boolean madeTextFile = textFile.createNewFile();

        if(madeTextFile) {
            System.out.println("Successfully created ArtistAlbumYear.txt file! :)");
            this.writer = new FileWriter(this.textFileDirectory);
        }
        else {
            System.out.println("Failed to create ArtistAlbumYear.txt file! :(");
        }

        System.out.println();
        this.madeTextFile = madeTextFile;
    }

    /**
     * @param mp3 file
     * @return String array in format [song, album, band]
     */
    public String[] getSongInfo(Mp3File mp3){
        String band = "";
        String album = "";
        String song = "";
        String year = "";

        String[] SongAlbumBandYear = new String[4];

        if (mp3.hasId3v2Tag()) {
            ID3v2 tag = mp3.getId3v2Tag();
            band = tag.getArtist();
            album = tag.getAlbum();
            song = tag.getTitle();
            year = tag.getYear();
        }
        if (mp3.hasId3v1Tag()) {
            ID3v1 tag = mp3.getId3v1Tag();
            band = tag.getArtist();
            album = tag.getAlbum();
            song = tag.getTitle();
            year = tag.getYear();
        }

        SongAlbumBandYear[0] = song;
        SongAlbumBandYear[1] = album;
        SongAlbumBandYear[2] = band;
        SongAlbumBandYear[3] = year;

        return SongAlbumBandYear;
    }

    /**
     * @param band name
     * @return true if band does not have folder, false if band has folder
     */
    public boolean noBandFolder(String band)  {
        //open folder with bands folders
        File bandsFolder = new File(this.sortedDirectory);
        //array of band folders
        File[] bands = bandsFolder.listFiles();

        if(bands == null || bands.length == 0){
            return true;
        }
        else{
            for(File bandFolder : bands){
                if(bandFolder.getName().equals(band)){
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * @param album name
     * @param band name
     * @return whether album has folder already
     */
    public boolean hasAlbumFolder(String album, String band) {
        //if no folder for this band, no album folder
        if(this.noBandFolder(band)){
            return false;
        }

        //else, look in band folder for album
        String bandDirectory = this.sortedDirectory + "//" + band;

        File bandAlbums = new File(bandDirectory);
        File[] albumsFolder = bandAlbums.listFiles();

        if(albumsFolder == null || albumsFolder.length == 0){
            return false;
        }
        else{
            for(File albumFolder : albumsFolder){
                if(albumFolder.getName().equals(album)){
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * moves mp3 file to correct folder
     * prints info for mp3 (for debugging/testing)
     * @param mp3   Mp3File
     * @param filepath   Path to mp3
     * @param filename    string name of mp3 file
     * @throws IOException input/output error
     */
    public void addSong(Mp3File mp3, Path filepath, String filename) throws IOException{
        String[] SongAlbumBandYear = this.getSongInfo(mp3);
        String song = SongAlbumBandYear[0];
        String album = SongAlbumBandYear[1];
        String band = SongAlbumBandYear[2];
        String year = SongAlbumBandYear[3];

        String bandDirectory = this.sortedDirectory + "//" + band;
        String albumDirectory = bandDirectory + "//" + album;
        String songDirectory = albumDirectory + "//" + filename;
        Path songPath = Paths.get(songDirectory);

        boolean songHasBandFolder = true;

        //make band folder if no band folder
        if(this.noBandFolder(band)) {
            File f1 = new File(bandDirectory);
            songHasBandFolder = f1.mkdir();
        }

        //once we've made sure album has band folder...

        if(songHasBandFolder) {
            //if song has album folder, move mp3 to album folder
            if (this.hasAlbumFolder(album, band)) {
                Files.move(filepath, songPath, StandardCopyOption.REPLACE_EXISTING);
            }
            else {
                //if no album folder, make album folder
                File f1 = new File(albumDirectory);
                boolean madeAlbumFolder = f1.mkdir();

                //if album folder made, move song to album folder
                if (madeAlbumFolder) {
                    Files.move(filepath, songPath, StandardCopyOption.REPLACE_EXISTING);

                    //add text file line for this album
                    this.textFileLines.add(band + "/" + album + " (" + year + ")\n");

                    /*  PRINT DIRECTLY (NOT IN ALPHABETICAL ORDER)
                    if(this.madeTextFile){
                        //print band/album (year) info each time you make a new album folder
                        this.writer.write(band + "/" + album + " (" + year + ")\n");
                    } */
                }
            }
        }

        //prints song info to terminal
        String s1 = String.format("%-" + 65 + "s", "| " + song);
        String s2 = String.format("%-" + 65 + "s", "| " + album);
        String s3 = String.format("%-" + 65 + "s", "| " + band);
        System.out.println(s1 + s2 + s3);
        System.out.println();
        //System.out.println("----------------------------------------------------------------------------------------------------" +
        //                "----------------------------------------------------------------------------------------------------" +
        //                "----------------------------------------------------------------------------------------------------" +
        //                "-----");
    }

    /**
     * prints each line in the text file in alphabetical order
     */
    public void printLines() throws IOException {
        //sort by alphabetical order
        Collections.sort(this.textFileLines);

        //print each line
        if(this.madeTextFile){
            for(String line: this.textFileLines) {
                this.writer.write(line);
            }
        }

        this.writer.close();
    }
}

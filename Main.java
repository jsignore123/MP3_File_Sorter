import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException, com.mpatric.mp3agic.InvalidDataException, UnsupportedTagException {
        //first argument is directory to mp3 Files folder
        String directory = args[0];
        //String directory = "C://Users//sirow//Java Projects//mp3 sorter_intelliJ//start_updated";


        //open mp3s folder and make mp3 array
        File dir = new File(directory);
        File[] files = dir.listFiles();


        //mp3 sorting class object
        mp3Sorter sorter = new mp3Sorter(directory);
        //make sorted folder and text file
        boolean sortedFolderMade = sorter.makeSortedFolder();
        sorter.createTextFile();


        //PRINTING STUFF:
        //title for printing each mp3's info
        System.out.println("| SONG                                                           " +
                "| ALBUM                                                          " +
                "| BAND");
        System.out.println("----------------------------------------------------------------------------------------------------" +
                "----------------------------------------------");
        System.out.println();
        // 100 spaces/lines in string:
        // "                                                                                                    "
        // "----------------------------------------------------------------------------------------------------"


        if(sortedFolderMade) {
            //SORT EACH MP3
            assert files != null;
            for (File file : files) {
                Path filepath = file.toPath();
                String filename = file.getName();

                //get file type
                String filetype = "";
                int i = filename.lastIndexOf('.');
                if (i >= 0) { filetype = filename.substring(i+1); }

                //if mp3 then add
                if(filetype.equals("mp3")) {
                    Mp3File mp3 = new Mp3File(file);
                    sorter.addSong(mp3, filepath, filename);
                }
                else{ //if not mp3, check if folder
                    File[] filesInFolder = file.listFiles();

                    //if non-mp3 is directory, sort the mp3s
                    if(filesInFolder != null){
                        for(File fileInFolder : filesInFolder){
                            Path filepath2 = fileInFolder.toPath();
                            String filename2 = fileInFolder.getName();

                            //get file type
                            String filetype2 = "";
                            int i2 = filename2.lastIndexOf('.');
                            if (i2 >= 0) { filetype2 = filename2.substring(i2+1); }

                            //add song if mp3 same as before
                            if(filetype2.equals("mp3")) {
                                Mp3File mp3 = new Mp3File(fileInFolder);
                                sorter.addSong(mp3, filepath2, filename2);
                            }
                        }
                    }
                }
            }
        }

        //print each album info line to text file
        sorter.printLines();
    }
}

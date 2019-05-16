import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class MainClass {

    public static void main (String[] args) throws IOException {

        String path, fileName;

        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(coreCount);

        PropertyFileWriting obj = new PropertyFileWriting();
        Properties write = obj.setProperties();
        path = write.getProperty("path");
        fileName = write.getProperty("txtFile");
        System.out.println("Path: " + path);
        System.out.println("FileName: " + fileName);

        Path path1 = Paths.get("C:\\Users\\user\\Desktop\\URL.txt"); //change your path
        List<String> line = null;
        line = Files.readAllLines(path1);

        //save valid link
        File file = new File("C:\\Users\\user\\Desktop\\validURL.txt");

        // If file doesn't exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        System.out.println("**************************** DISPLAY VALID LINKS ****************************");
        new FileWriter("myLogFile.log", false); //overwrites file
        for (int a = 0; a < line.size(); a++) {
            CheckUrls link1 = new CheckUrls(line.get(a));
            Future<String> future = service.submit(link1);
            try {
                if (future.get() != null) {
                    //validLink = future.get();
                    Thread thread = new Thread();
                    service.execute(thread);
                    bw.write(line.get(a));
                    bw.newLine();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        bw.close();

        CheckValidTable cvt = new CheckValidTable();
        cvt.run();

        PropFile pF = new PropFile();
        pF.run();

        display dp =new display();
        dp.run();

        service.shutdown();
        try {
            if (!service.awaitTermination(60, TimeUnit.SECONDS)) { //wait for existing tasks to terminate
                service.shutdownNow(); //cancel currently executing task
                if (!service.awaitTermination(60, TimeUnit.SECONDS)) { //wait for tasks to respond to being cancelled
                    System.err.println("Service didn't terminate!");
                }
            }
        } catch (InterruptedException e) {
            service.shutdownNow(); //re-cancel if current thread also interrupted
            Thread.currentThread().interrupt(); //preserve interrupt status
        }

    }
}
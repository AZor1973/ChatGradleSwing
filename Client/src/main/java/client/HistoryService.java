package client;

import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryService {

    private static final String FILENAME = "Client/history/history_%s.txt";
    private final String login;
    private PrintWriter printWriter;
    private File fileHistory;

    public HistoryService(String login){
        this.login = login;
    }

    public void keepHistory(){
        String path = String.format(FILENAME, login);
        fileHistory = new File(path);
        if (!fileHistory.exists()){
            try {
                fileHistory.createNewFile();
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла");
                e.printStackTrace();
            }
        }
        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileHistory, true)));
        } catch (IOException e) {
            System.out.println("Ошибка при записи истории сообщений");
            e.printStackTrace();
        }
    }

    public void keepText(String text){
        printWriter.print(text);
        printWriter.flush();
    }

    public void close(){
        if (printWriter != null){
            System.out.println("Writer closed");
            printWriter.close();
        }
    }

    public String loadLastRows2(int rowsNumber) {
        List<String> result = new ArrayList<>(rowsNumber);
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(fileHistory, 4096, StandardCharsets.UTF_8)) {
            for (int i = 0; i < rowsNumber; i++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.reverse(result);
        return String.join(System.lineSeparator(), result);
    }
}


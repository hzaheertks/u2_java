import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HeartRateStressMonitor {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("Heart rate (BPM) vs. time of day (hrs).csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

//time=line.substring(0,2)
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HeartRateStressMonitor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner (System. in) ;

        System.out.println("Welcome to the heart rate stress monitor!");
        System.out.println("What is your name? ");
        String name = scanner.nextLine();

        System.out.println("Would you like to begin (1) or quit (2) the program, " + name +"?");
        int decision = scanner.nextInt();


        if (decision == 1) {
            System.out.println("\nMany students’ mental health and stress levels are translated in variability and spikes in their heart rate, \nit is important that we identify these spikes so we know when to manage stress! \nLet's begin, " + name );
            System.out.println("------------------------------------------");
            System.out.println("Begin by entering your average BPM below, every 2 hours for one day: "); //in this program, user input will really come from the CSV file, this line is just to mimic what would have been said to user
            System.out.println("------------------------------------------"); //marybe try turning into a method for efficiency?

            String filename = "Heart rate (BPM) vs. time of day (hrs).csv";
            ArrayList<Integer> readings = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (line.toLowerCase().contains("heart")) continue;
                    if (line.toLowerCase().contains("time")) continue;
                    int heartRate = Integer.parseInt(parts[1].trim());
                    readings.add(heartRate);
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        else if (decision == 2) {
            System.out.println("Bye bye!");
        }

        else{
            System.out.println("Invalid input! Please restart!");
        }

        int[] hrArray = new int[readings.size()];
        for (int i = 0; i < readings.size(); i++) {
            hrArray[i] = readings.get(i);
        }

        System.out.println("Heart rate classificaitons: ");
        


    }

//time=line.substring(0,2(
    //.indexOf string method

}

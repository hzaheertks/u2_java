import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JFrame;



public class HeartRateStressMonitor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner (System. in) ;

        //Welcoming user + user input
        System.out.println("Welcome to the heart rate stress monitor!");
        System.out.println("What is your name? ");
        String name = scanner.nextLine();

        System.out.println("Would you like to begin (1) or quit (2) the program, " + name +"?");
        int decision = scanner.nextInt();
        scanner.nextLine();

        //Lists to store data of CSV file
        ArrayList<String> times = new ArrayList<>();
        ArrayList<Integer> readings = new ArrayList<>();

        if (decision == 1) {
            System.out.println("\nMany students’ mental health and stress levels are translated in variability and spikes in their heart rate, \nit is important that we identify these spikes so we know when to manage stress! \nLet's begin, " + name );
            printDashes(80);
            System.out.println("Begin by entering your average bpm below, every 2 hours for one day: "); //in this program, user input will really come from the CSV file, this line is just to mimic what would have been said to user
            printDashes(80);

            String filename = "Heart rate (BPM) vs. time of day (hrs).csv";

            //Reading CSV file
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    if (line.trim().isEmpty() || line.startsWith("Time")) continue;
                    String[] parts = line.split(",");
                    times.add(parts[0].trim());
                    readings.add(Integer.parseInt(parts[1].trim()));

                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }

            //Converting ArrayLists to ararys, required for library
            int[] hrArray = new int[readings.size()];
            for (int i = 0; i < readings.size(); i++) {
                hrArray[i] = readings.get(i);
            }

            String[] timeArray = new String[times.size()];
            for (int i = 0; i < times.size(); i++) {
                timeArray[i] = times.get(i);
            }

            //Detecting spikes and classificatiosn
            ArrayList<Integer> spikes = new ArrayList<>();
            String[] classification = new String[readings.size()];

            printDashes(30);
            System.out.println("\nHeart rate classificaitons: ");
            for (int i = 0; i < readings.size(); i++) {
                classification[i] = classifyReading(hrArray[i]); //calling method classifyReading
                if (i > 0 && Math.abs(hrArray[i] - hrArray[i - 1]) > 15) {
                    spikes.add(i);
                }
                System.out.println("Reading:  " + hrArray[i] + " bpm \n   Classification: " + classification[i]);
                if (spikes.contains(i)) System.out.print(" (Spike!)");
                System.out.println();
            }

            //Caclulating avg heart rate + display
            double average = averageReading(hrArray);
            long roundedAverage = Math.round(average);
            printDashes(30);
            System.out.println("Average heart rate of day: " +roundedAverage + " bpm");
            printDashes(30);

            //Summarizing time periods averages
            summarizeByPeriod(timeArray, hrArray);

            //Displaying overall classificaiton output
            String overall = overallClassification(classification);
            System.out.println("Overall State: " + overall);

            //Displaying graph
        graph(timeArray, hrArray, name);
        }
        else if (decision == 2) {
            System.out.println("Bye bye!");
        }

        else{
            System.out.println("Invalid input! Please restart!");
        }

    }

    //Printing dash line method for formatting
    public static void printDashes(int length) {
        for (int i = 0; i < length; i++) {
            System.out.print("-");
        }
        System.out.println();
    }


    //Calculating avg heart rate method
    public static double averageReading(int[] array){
        int total = 0;
        for(int i = 0; i < array.length; i++) {
            total += array[i];
        }
    return (double) total / array.length;

}
//Classifying invididual heart rates method
    public static String classifyReading(int heartrate){
        if (heartrate <= 75) {
            return "Calm";
        }
        else if (heartrate <= 85) {
            return "Moderate";
        }
        else {
            return "Stressed!";
        }
    }

    //Summarize heart rate during each time period
    public static void summarizeByPeriod(String[] times, int[] hrArray) {
        int morningTotal = 0, morningCount = 0;
        int afternoonTotal = 0, afternoonCount = 0;
        int eveningTotal = 0, eveningCount = 0;

        for (int i = 0; i < hrArray.length; i++) {
            int hour = Integer.parseInt(times[i].split(":")[0]);
            if (hour >= 6 && hour < 12) { morningTotal += hrArray[i]; morningCount++; }
            else if (hour >= 12 && hour < 18) { afternoonTotal += hrArray[i]; afternoonCount++; }
            else { eveningTotal += hrArray[i]; eveningCount++; }
        }

        System.out.println("\nAverage Heart Rate by Period:");
        if (morningCount > 0) System.out.println("Morning: " + (morningTotal / morningCount) + " bpm");
        if (afternoonCount > 0) System.out.println("Afternoon: " + (afternoonTotal / afternoonCount) + " bpm");
        if (eveningCount > 0) System.out.println("Evening: " + (eveningTotal / eveningCount) + " bpm");
    }

    //Determining overall classsification of heart rate, overall state
    public static String overallClassification(String[] array) {

        int calm = 0, moderate = 0, stressed = 0;
        for (int i = 0; i < array.length; i++) {
            String classify = array[i];
            if (classify.equals("Calm")) calm++;
            else if (classify.equals("Moderate")) moderate++;
            else if (classify.equals("Stressed!")) stressed++;
        }
        if (stressed >= calm && stressed >= moderate) return "Stressed!";
        if (moderate >= calm && moderate >= stressed) return "Moderate";
        return "Calm";

    }

    //Graphing heart rate with JFreeChart library;
    public static void graph(String[] times, int[] hrArray, String name) {
        try {
            // Wait 2 seconds before showing graph
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < hrArray.length; i++) {
            dataset.addValue(hrArray[i], "Heart Rate", times[i]);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Heart Rate Throughout the Day - " + name,
                "Time of Day",
                "Heart Rate (BPM)",
                dataset);

        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame("Heart Rate Graph");
        frame.setContentPane(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

}
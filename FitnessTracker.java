import java.util.*;
import java.io.*;
import java.nio.file.*;

abstract class Fitness {
    abstract double calories(int minutes);
}

class Workout extends Fitness {
    private String activity;
    private int minutes;

    Workout(String activity, int minutes) {
        this.activity = activity;
        this.minutes = minutes;
    }
    public String getActivity() {
        return activity;
    }
    public int getMinutes() {
        return minutes;
    }
    @Override
    double calories(int minutes) {
        return minutes * 5.0;
    }
    public void display() {
        System.out.println(activity + " | " + minutes
                + " minutes | " + calories(minutes) + " calories");
    }
    public String toString() {
        return activity + "," + minutes;
    }
}

public class FitnessTracker {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Workout> history = new ArrayList<>();
    static final String FILE = "workouts.txt";

    static double bmi(double weight, double height) {
        height = height / 100;
        return weight / (height * height);
    }

    static int count(int n) {
        if (n == 0) return 0;
        return 1 + count(n - 1);
    }

    static void save() {
        ArrayList<String> lines = new ArrayList<>();
        for (Workout w : history)
            lines.add(w.toString());

        try {
            Files.write(Path.of(FILE), lines);
        } catch (IOException e) {
            System.out.println("Error saving history.");
        }
    }

    static void load() {
        try {
            if (Files.exists(Path.of(FILE))) {
                for (String line : Files.readAllLines(Path.of(FILE))) {
                    String[] data = line.split(",");
                    if (data.length == 2)
                        history.add(new Workout(data[0],
                                Integer.parseInt(data[1])));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading history.");
        }
    }

    static void addWorkout() {
        System.out.print("Enter exercise name: ");
        String activity = sc.nextLine();

        System.out.print("Enter duration in minutes: ");
        int minutes = sc.nextInt();
        sc.nextLine();

        if (minutes > 0) {
            history.add(new Workout(activity, minutes));
            save();
            System.out.println("Workout saved!");
        } else {
            System.out.println("Duration must be positive.");
        }
    }

    static void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No workout history.");
            return;
        }

        for (Workout w : history)
            w.display();
    }

    public static void main(String[] args) {
        System.out.println("=== FITNESS TRACKER ===");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your age: ");
        int age = sc.nextInt();

        System.out.print("Enter your height in cm: ");
        double height = sc.nextDouble();

        System.out.print("Enter your weight in kg: ");
        double weight = sc.nextDouble();
        sc.nextLine();

        if (age <= 0 || height <= 0 || weight <= 0) {
            System.out.println("Enter valid personal details.");
            sc.close();
            return;
        }

        load();
        int choice;

        do {
            System.out.println("\n1. Add workout");
            System.out.println("2. View history");
            System.out.println("3. Calculate BMI");
            System.out.println("4. Compare activity");
            System.out.println("5. Show fitness message");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("How many exercises? ");
                    int n = sc.nextInt();
                    sc.nextLine();

                    for (int i = 0; i < n; i++) {
                        System.out.println("Exercise " + (i + 1));
                        addWorkout();
                    }
                    break;

                case 2:
                    showHistory();
                    System.out.println("Total workouts: "
                            + count(history.size()));
                    break;

                case 3:
                    System.out.printf("Your BMI: %.2f%n",
                            bmi(weight, height));
                    break;

                case 4:
                    System.out.print("Enter activity to compare with Running: ");
                    String activity = sc.nextLine();

                    if (activity.equalsIgnoreCase("Running"))
                        System.out.println("Activity matches Running.");
                    else
                        System.out.println("Different activity.");
                    break;

                case 5:
                    StringBuilder report = new StringBuilder("Keep going, ");
                    report.append(name).append("!");

                    StringBuffer message = new StringBuffer(report);
                    System.out.println(message);
                    break;

                case 0:
                    save();
                    System.out.println("Goodbye, " + name + "!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }
}

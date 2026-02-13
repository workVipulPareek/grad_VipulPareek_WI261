package BikeRace;

import java.util.*;
import java.util.concurrent.*;
import java.time.*;

class RaceResult {
    String name;
    LocalDateTime startTime;
    LocalDateTime endTime;
    long timeTakenMillis;

    public RaceResult(String name, LocalDateTime startTime,
                      LocalDateTime endTime, long timeTakenMillis) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeTakenMillis = timeTakenMillis;
    }
}

class Biker implements Callable<RaceResult> {

    private String name;
    private double totalDistance; // in meters

    public Biker(String name, double totalDistance) {
        this.name = name;
        this.totalDistance = totalDistance;
    }

    @Override
    public RaceResult call() throws Exception {

        double covered = 0;
        Random random = new Random();

        LocalDateTime start = LocalDateTime.now();
        long startMillis = System.currentTimeMillis();

        while (covered < totalDistance) {

            // Dynamic speed between 20 km/h to 60 km/h
            double speed = 20 + random.nextInt(40);

            // Convert speed to meters per second
            double speedMps = speed * 1000 / 3600;

            covered += speedMps;

            if ((int)covered % 100 < speedMps) {
                System.out.println(name + " covered " + (int)covered + " meters");
            }

            Thread.sleep(500); // simulate movement delay
        }

        long endMillis = System.currentTimeMillis();
        LocalDateTime end = LocalDateTime.now();

        return new RaceResult(name, start, end, (endMillis - startMillis));
    }
}

public class BikeRaceGame {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        List<String> bikerNames = new ArrayList<>();

        System.out.println("Enter names of 10 bikers:");

        for (int i = 1; i <= 10; i++) {
            System.out.print("Biker " + i + ": ");
            bikerNames.add(sc.nextLine());
        }

        System.out.print("Enter race distance in KM: ");
        double distanceKm = sc.nextDouble();
        double distanceMeters = distanceKm * 1000;

        // Countdown
        System.out.println("\nGet Ready!!!");
        for (int i = 10; i >= 1; i--) {
            System.out.print(i + " ");
            Thread.sleep(500);
        }
        System.out.println("\nLets Playyyy\n");

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<RaceResult>> futures = new ArrayList<>();

        for (String name : bikerNames) {
            futures.add(executor.submit(new Biker(name, distanceMeters)));
        }

        List<RaceResult> results = new ArrayList<>();

        for (Future<RaceResult> future : futures) {
            results.add(future.get());
        }

        executor.shutdown();

        results.sort(Comparator.comparingLong(r -> r.timeTakenMillis));

        System.out.println("\n========= FINAL DASHBOARD =========");

        int rank = 1;
        for (RaceResult r : results) {
            System.out.println("Rank " + rank++);
            System.out.println("Name      : " + r.name);
            System.out.println("Start     : " + r.startTime);
            System.out.println("End       : " + r.endTime);
            System.out.println("Time Taken: " + r.timeTakenMillis / 1000.0 + " seconds");
            System.out.println("-----------------------------------");
        }

        sc.close();
    }
}

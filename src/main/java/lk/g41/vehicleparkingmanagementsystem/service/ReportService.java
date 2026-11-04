
package lk.g41.vehicleparkingmanagementsystem.service;

import lk.g41.vehicleparkingmanagementsystem.model.ParkingRecord;
import lk.g41.vehicleparkingmanagementsystem.model.ParkingReport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final EntryExitService entries;

    public ReportService(EntryExitService e) {
        entries = e;
    }

    public List<ParkingReport> all() {
        List<ParkingReport> a = new ArrayList<>();

        for (ParkingRecord r : entries.all()) {
            a.add(
                    new ParkingReport(
                            r.getVehicleNumber(),
                            r.getEntryTime(),
                            r.getExitTime(),
                            r.getStatus(),
                            entries.durationHours(r)
                    )
            );
        }

        return a;
    }

    // Sort reports by vehicle number using Quick Sort
    public void sortVehicle(List<ParkingReport> a) {
        if (a.size() > 1) {
            quickVehicle(a, 0, a.size() - 1);
        }
    }

    private void quickVehicle(List<ParkingReport> a, int lo, int hi) {
        if (lo < hi) {
            int p = partVehicle(a, lo, hi);

            quickVehicle(a, lo, p - 1);
            quickVehicle(a, p + 1, hi);
        }
    }

    private int partVehicle(List<ParkingReport> a, int lo, int hi) {
        String pivot = a.get(hi).getVehicleNumber();
        int i = lo - 1;

        for (int j = lo; j < hi; j++) {
            if (a.get(j).getVehicleNumber()
                    .compareToIgnoreCase(pivot) <= 0) {
                i++;
                swap(a, i, j);
            }
        }

        swap(a, i + 1, hi);

        return i + 1;
    }

    // Sort reports by parking duration using Quick Sort
    public void sortDuration(List<ParkingReport> a) {
        if (a.size() > 1) {
            quickDuration(a, 0, a.size() - 1);
        }
    }

    private void quickDuration(List<ParkingReport> a, int lo, int hi) {
        if (lo < hi) {
            int p = partDuration(a, lo, hi);

            quickDuration(a, lo, p - 1);
            quickDuration(a, p + 1, hi);
        }
    }

    private int partDuration(List<ParkingReport> a, int lo, int hi) {
        double pivot = a.get(hi).getDurationHours();
        int i = lo - 1;

        for (int j = lo; j < hi; j++) {
            if (a.get(j).getDurationHours() <= pivot) {
                i++;
                swap(a, i, j);
            }
        }

        swap(a, i + 1, hi);

        return i + 1;
    }

    // Swap two reports
    private void swap(List<ParkingReport> a, int i, int j) {
        ParkingReport t = a.get(i);

        a.set(i, a.get(j));
        a.set(j, t);
    }

    // Count currently parked vehicles
    public long parked() {
        return entries.all()
                .stream()
                .filter(r -> "Parked".equalsIgnoreCase(r.getStatus()))
                .count();
    }

    // Count exited vehicles
    public long exited() {
        return entries.all()
                .stream()
                .filter(r -> "Exited".equalsIgnoreCase(r.getStatus()))
                .count();
    }
}

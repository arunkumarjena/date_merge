package assignment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DateMerger {
    public static final Comparator<DateRange> DATE_COMPARATOR = (o1,o2) -> {
        if (o1.getStartDate() != null && o2.getStartDate() != null) {
            if (o1.getStartDate().isBefore(o2.getStartDate())) {
                return -1;  // return -> -1 (less than)
            }
            else {
                return o1.getStartDate().isAfter(o2.getStartDate()) ? 1 : 0;
            }
        }
        else if (o1.getStartDate() != null && o2.getStartDate() == null) {
            return -1;
        }
        else if (o1.getStartDate() == null && o2.getStartDate() != null) {
            return 1; // return -> 1 (greater)
        }
        else {
            return 0; // return -> 0 (equal)
        }
    };
    public static void main(String[] args) {
        List<DateRange> dateRanges = new ArrayList<>();
        dateRanges.add(new DateRange(LocalDate.of(2014, 1, 1), LocalDate.of(2014, 1, 30)));
        dateRanges.add(new DateRange(LocalDate.of(2014, 1, 15), LocalDate.of(2014, 2, 15)));
        dateRanges.add(new DateRange(LocalDate.of(2014, 3, 10), LocalDate.of(2014, 4, 15)));
        dateRanges.add(new DateRange(LocalDate.of(2014, 4, 10), LocalDate.of(2014, 5, 15)));

        // to print input hard coded dates
        for(DateRange dateRange : dateRanges){
            System.out.println(dateRange.getStartDate() + " - " + dateRange.getEndDate());
        }

        // here all the merged dates already sorted store here
        List<DateRange> mergedDateRange = mergeDates(dateRanges);

        System.out.println("Output: ");
        for(DateRange dateRange : mergedDateRange){
            System.out.println(dateRange.getStartDate() + " - " + dateRange.getEndDate());
        }
    }

    private static List<DateRange> mergeDates(List<DateRange> dateRanges) {
        List<DateRange> mergedDateRange = new ArrayList<>();
        dateRanges.sort(DATE_COMPARATOR);

        mergedDateRange.add(dateRanges.get(0));
        for (int i = 1; i < dateRanges.size(); i++) {
            DateRange current = dateRanges.get(i);
            List<DateRange> toBeAdded = new ArrayList<>();
            boolean rangeMerged = false;
            for (DateRange mergedRange : mergedDateRange) {
                DateRange merged = checkOverlap(mergedRange, current);
                if (merged == null) {
                    toBeAdded.add(current);
                }
                else {
                    mergedRange.setEndDate(merged.getEndDate());
                    mergedRange.setStartDate(merged.getStartDate());
                    rangeMerged = true;
                    break;
                }
            }
            if (!rangeMerged) {
                mergedDateRange.addAll(toBeAdded);
            }
            toBeAdded.clear();
        }
        List<DateRange> mergedDateRangeList = new ArrayList<>(mergedDateRange);
        // sort takes two parameters (o1,o2) and compare it whether 1,0 or -1
        mergedDateRangeList.sort(DATE_COMPARATOR);
        return mergedDateRangeList;
    }

    private static DateRange checkOverlap(DateRange mergedDate, DateRange currentDate) {
        if (mergedDate.getStartDate().isAfter(currentDate.getEndDate()) || mergedDate.getEndDate().isBefore(currentDate.getStartDate())) {
            return null;
        }
        else {
            return new DateRange(
                    mergedDate.getStartDate().isBefore(currentDate.getStartDate()) ? mergedDate.getStartDate() : currentDate.getStartDate(),
                    mergedDate.getEndDate().isAfter(currentDate.getEndDate()) ? mergedDate.getEndDate() : currentDate.getEndDate());
        }
    }

}

package edu.harvard.integration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Year;
import java.util.Calendar;

/**
 * A generic Utilities class for {@link Integrator}.
 */
public final class Utils {
    /**
     * Checks if the given string is a valid integer.
     * @param input The string to check.
     * @return True if the input is a valid integer, false otherwise.
     */
    public static boolean legalInt(@Nullable String input) {
        if (input == null) {
            return false;
        }
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Nullable
    public static Calendar getDate(@Nonnull String sDate) {
        String[] pieces = sDate.split("/");
        if ((pieces.length == 2 || pieces.length == 3) && legalInt(pieces[0]) && legalInt(pieces[1])) {
            int year;
            int month = Integer.parseInt(pieces[0]);
            int day = Integer.parseInt(pieces[1]);
            if (pieces.length == 2) {
                //If the year is not included fallback to this year
                year = Year.now().getValue();
            } else if (legalInt(pieces[2])) {
                year = Integer.parseInt(pieces[2]);
            } else {
                return null;
            }
            month--;
            if (month >= 0 && day >= 0 && month < 12 && day < 32) {
                return new Calendar.Builder().setDate(year, month, day).build();
            }
        }
        return null;
    }

    @Nonnull
    public static String formattedDate(@Nonnull Calendar c, boolean showYear) {
        if (showYear) {
            return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE) + "/" + c.get(Calendar.YEAR);
        }
        return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DATE);
    }
}
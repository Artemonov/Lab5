import java.util.List;
import java.util.Scanner;

public interface Service {
    void averageAmountOfDaySpamForPeriod(Scanner sc, List<EmailAccount> emailAccounts);

    void daysWithSpamMessagesPercentLessThanSpecificPercent(Scanner sc, List<EmailAccount> emailAccounts);
}


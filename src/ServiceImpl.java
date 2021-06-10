import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ServiceImpl implements Service {
    @Override
    public void averageAmountOfDaySpamForPeriod(Scanner sc, List<EmailAccount> emailAccounts) {
        System.out.println("Введіть дату початку (дд.ММ.рррр)");
        LocalDate from = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        System.out.println("Введіть дату закінчення (дд.ММ.рррр)");
        LocalDate till = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        double average = emailAccounts.stream()
                .filter(wd -> wd.getSpam().getDate().isAfter(from) && wd.getSpam().getDate().isBefore(till))
                .map(EmailAccount::getSpam)
                .mapToLong(Spam::getSpamMessageNumber)
                .average()
                .getAsDouble();

        System.out.printf("Середня кількість хостів в день за період %s-%s: %.2f повідомлень%n", from.toString(), till.toString(), average);

        System.out.println();
    }

    @Override
    public void daysWithSpamMessagesPercentLessThanSpecificPercent(Scanner sc, List<EmailAccount> emailAccounts) {
        System.out.println("Введіть відсоток від 1 до 100");
        int percent = Integer.parseInt(sc.nextLine());
        long count = emailAccounts
                .stream()
                .filter(emailAccount -> (((double) emailAccount.getSpam().getSpamMessageNumber() / (double) emailAccount.getSpam().getTotalMessageNumber()) * 100) < percent)
                .count();


        System.out.println("Кількість днів, коли відсоток спам повідомлень був менший за задане значення:\n" + count);

        System.out.println();
    }
}

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        Path path = Paths.get("src/files/database.txt");
        Pattern emailPattern = Pattern.compile("(?<band>EmailAccount:) (?<email>[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+ )(?<pib>- [\\w\\s]+)", Pattern.CASE_INSENSITIVE);
        Pattern spamPattern = Pattern.compile("(?<spam>Spam:) (?<date>\\d{2}\\.\\d{2}\\.\\d{4}), (?<spams>\\d*\\sspam messages), (?<total>\\d*\\stotal messages)", Pattern.CASE_INSENSITIVE);
        List<EmailAccount> emailAccounts = new ArrayList<>();
        readFromFile(path, spamPattern, emailPattern, emailAccounts);
        Service service = new ServiceImpl();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Виберіть дію:\n1 - Середня кількість спаму в день за період; \n2 - Кількість днів, коли відсоток спам повідомлень був менший за задане значення; \n3 - Показати всі записи; \nq - Вихід; \ne - Редагування");
            switch (sc.nextLine()) {
                case "1": {
                    service.averageAmountOfDaySpamForPeriod(sc, emailAccounts);
                    break;
                }
                case "2": {
                    service.daysWithSpamMessagesPercentLessThanSpecificPercent(sc, emailAccounts);
                    break;
                }
                case "3": {
                    readFromFile(path, spamPattern, emailPattern, emailAccounts);
                    break;
                }
                case "q": {
                    return;
                }
                case "e": {
                    startCRUD(sc, emailAccounts, path);
                    break;
                }
                default: {
                    throw new IncorrectChoiceException("Неправильна дія! ");
                }
            }
        }
    }

    private static void startCRUD(Scanner sc, List<EmailAccount> emailAccounts, Path path) {
        System.out.println("Виберіть дію:\n1 - Видалити запис; \n2 - Редагувати запис; \n3 - Додати новий запис; \nq - Вихід;");
        switch (sc.nextLine()) {
            case "1": {
                delete(sc, emailAccounts, path);
                System.out.println("Успішно видалено!");
                break;
            }
            case "2": {
                edit(sc, emailAccounts, path);
                break;
            }
            case "3": {
                save(sc, path);
                break;
            }
            default: {
                break;
            }
        }
    }

    private static void showAllWithNum(List<EmailAccount> emailAccounts) {
        for (int i = 0; i < emailAccounts.size(); i++) {
            System.out.println((i + 1) + " " + emailAccounts.get(i).toString());
        }
    }

    private static void delete(Scanner sc, List<EmailAccount> emailAccounts, Path path) {
        showAllWithNum(emailAccounts);
        System.out.println("Виберіть номер запису для видалення: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        emailAccounts.remove(index);
        save(emailAccounts, path);
    }

    private static void edit(Scanner sc, List<EmailAccount> emailAccounts, Path path) {
        showAllWithNum(emailAccounts);
        System.out.println("Виберіть номер запису для редагування: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        EmailAccount emailAccount = emailAccounts.get(index);
        System.out.println(emailAccount);
        System.out.println("1 - Змінити кількість спам-повідомлень; \n2 - Змінити загальну кількість повідомлень; \n3 - Змінити поштову адресу; \n4 - Змінити ПІБ власника пошти\n");
        switch (sc.nextLine()) {
            case "1": {
                System.out.println("Введіть нову кількість спам-повідомлен");
                emailAccount.getSpam().setSpamMessageNumber(Integer.parseInt(sc.nextLine()));
                break;
            }
            case "2": {
                System.out.println("Введіть нову загальну кількість повідомлень");
                emailAccount.getSpam().setTotalMessageNumber(Integer.parseInt(sc.nextLine()));
                break;
            }
            case "3": {
                System.out.println("Введіть нову поштову адресу");
                emailAccount.setEmail(sc.nextLine());
                break;
            }
            case "4": {
                System.out.println("Введіть нові ПІБ власника пошти");
                emailAccount.setPib(sc.nextLine());
                break;
            }
        }
        emailAccounts.remove(index);
        emailAccounts.add(index, emailAccount);
        save(emailAccounts, path);
        System.out.println("Зміни збережено!");
        System.out.println();
    }

    private static String mapToFileFormat(EmailAccount wd) {
        StringBuilder sb = new StringBuilder();
        sb.append("EmailAccount: ")
                .append(wd.getEmail())
                .append(" - ")
                .append(wd.getPib())
                .append("\r\n");
        sb.append("Spam: ")
                .append(wd.getSpam().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .append(", ")
                .append(wd.getSpam().getSpamMessageNumber()).append(" spam messages, ")
                .append(wd.getSpam().getTotalMessageNumber()).append(" total messages")
                .append("\r\n");
        sb.append("-------------------------------------------------------");

        return sb.toString();
    }

    private static void save(List<EmailAccount> emailAccounts, Path path) {
        try {
            List<String> emailsResult = emailAccounts.stream()
                    .map(Main::mapToFileFormat)
                    .collect(Collectors.toList());
            Files.write(path, emailsResult, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save(Scanner sc, Path path) {
        System.out.println("Електронна пошта ");
        String email = sc.nextLine();
        System.out.println("ПІБ власника пошти: ");
        String pib = sc.nextLine();
        System.out.println("Дата спаму (dd.MM.yyyy): ");
        LocalDate date = LocalDate.parse(sc.nextLine().trim(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        System.out.println("Кількість спам-повідомлень: ");
        int spamMessages = Integer.parseInt(sc.nextLine());
        System.out.println("Загальна кількість повідомлень: ");
        int totalMessages = Integer.parseInt(sc.nextLine());
        EmailAccount emailAccount = new EmailAccount(email, pib);
        emailAccount.setSpam(new Spam(date, spamMessages, totalMessages));
        System.out.println(emailAccount);
        try {
            Files.write(path, ("\r\n" + mapToFileFormat(emailAccount)).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            System.out.println("Збережено!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void readFromFile(Path path, Pattern spamPattern, Pattern emailPattern, List<EmailAccount> emailAccounts) throws IOException {
        List<String> lines = Files.readAllLines(path);
        Matcher matcher;
        LocalDate date = LocalDate.MIN;
        long spamMessages = 0;
        long totalMessages = 0;
        String email = "";
        String pib = "";
        for (String line : lines) {
            if (line.startsWith("Spam")) {
                date = LocalDate.MIN;
                spamMessages = 0;
                totalMessages = 0;
                matcher = spamPattern.matcher(line);
                if (matcher.find()) {
                    date = LocalDate.parse(matcher.group("date"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    spamMessages = Long.parseLong(matcher.group("spams").split("\\s")[0]);
                    totalMessages = Long.parseLong(matcher.group("total").split("\\s")[0]);
                }

            } else if (line.startsWith("EmailAccount")) {
                email = "";
                pib = "";
                matcher = emailPattern.matcher(line);
                if (matcher.find()) {
                    email = matcher.group("email").trim();
                    pib = matcher.group("pib").split("- ")[1];
                }
            } else if (line.startsWith("-")) {
                EmailAccount emailAccount = new EmailAccount(email, pib);
                Spam spam = new Spam(date, spamMessages, totalMessages);
                emailAccount.setSpam(spam);
                emailAccounts.add(emailAccount);

                System.out.println(emailAccount);
            }
        }
    }
}

import java.time.LocalDate;
import java.util.Objects;

public class Spam {

    private LocalDate date;
    private long spamMessageNumber;
    private long totalMessageNumber;

    public Spam(LocalDate date, long spamMessageNumber, long totalMessageNumber) {
        this.date = date;
        this.spamMessageNumber = spamMessageNumber;
        this.totalMessageNumber = totalMessageNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getTotalMessageNumber() {
        return totalMessageNumber;
    }

    public void setTotalMessageNumber(long totalMessageNumber) {
        this.totalMessageNumber = totalMessageNumber;
    }

    public long getSpamMessageNumber() {
        return spamMessageNumber;
    }

    public void setSpamMessageNumber(long spamMessageNumber) {
        this.spamMessageNumber = spamMessageNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spam spam = (Spam) o;
        return totalMessageNumber == spam.totalMessageNumber && spamMessageNumber == spam.spamMessageNumber && Objects.equals(date, spam.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, totalMessageNumber, spamMessageNumber);
    }

    @Override
    public String toString() {
        return "Spam{" +
                "date=" + date +
                ", totalMessageNumber=" + totalMessageNumber +
                ", spamMessageNumber='" + spamMessageNumber + '\'' +
                '}';
    }
}

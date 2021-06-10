import java.util.Objects;

public class EmailAccount {
    private String email;
    private String pib;
    private Spam spam;

    public EmailAccount(String email, String pib) {
        this.email = email;
        this.pib = pib;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public Spam getSpam() {
        return spam;
    }

    public void setSpam(Spam spam) {
        this.spam = spam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAccount that = (EmailAccount) o;
        return Objects.equals(email, that.email) && Objects.equals(pib, that.pib) && Objects.equals(spam, that.spam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, pib, spam);
    }

    @Override
    public String toString() {
        return "EmailAccount{" +
                "email='" + email + '\'' +
                ", pib='" + pib + '\'' +
                ", spam=" + spam +
                '}';
    }
}

package fun.gusmurphy.library.springbootadm.domain;

public class OverdueNotification {

    private String bookIsbn;
    private String userId;
    private String lateAsOf;

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLateAsOf() {
        return lateAsOf;
    }

    public void setLateAsOf(String lateAsOf) {
        this.lateAsOf = lateAsOf;
    }
}

package booking.details;

public class BookingDetailsBuilder {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    public BookingDetailsBuilder setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public BookingDetailsBuilder setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public BookingDetailsBuilder setTotalprice(int totalprice) {
        this.totalprice = totalprice;
        return this;
    }

    public BookingDetailsBuilder setDepositpaid(boolean depositpaid) {
        this.depositpaid = depositpaid;
        return this;
    }

    public BookingDetailsBuilder setBookingdates(BookingDates bookingdates) {
        this.bookingdates = bookingdates;
        return this;
    }

    public BookingDetailsBuilder setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
        return this;
    }

    public BookingDetails build() {
        return new BookingDetails(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
    }
}
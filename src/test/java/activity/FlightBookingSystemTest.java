package activity;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FlightBookingSystemTest {
    @Test
    public void testBookingWithEnoughSeatsAndNoPoints() {
        FlightBookingSystem system = new FlightBookingSystem();
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime departureTime = bookingTime.plusDays(1);

        FlightBookingSystem.BookingResult result = system.bookFlight(
                3,
                bookingTime,
                10,
                300.0,
                200,
                false,
                departureTime,
                0
        );

        assertTrue(result.confirmation);
        assertEquals(300.0 * 3 * 1.6, result.totalPrice);
        assertEquals(0.0, result.refundAmount);
        assertFalse(result.pointsUsed);
    }

    @Test
    public void testBookingWithoutEnoughSeats() {
        FlightBookingSystem system = new FlightBookingSystem();
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime departureTime = bookingTime.plusDays(1);

        FlightBookingSystem.BookingResult result = system.bookFlight(
                3,
                bookingTime,
                2,
                300.0,
                200,
                false,
                departureTime,
                10
        );

        assertFalse(result.confirmation);
        assertEquals(0.0, result.totalPrice);
        assertEquals(0.0, result.refundAmount);
        assertFalse(result.pointsUsed);
    }

    @Test
    public void testBookingWithCancellation() {
        FlightBookingSystem system = new FlightBookingSystem();
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime departureTime = bookingTime.plusDays(1);

        FlightBookingSystem.BookingResult result = system.bookFlight(
                1,
                bookingTime,
                10,
                300.0,
                200,
                true,
                departureTime,
                10
        );

        assertFalse(result.confirmation);
        assertEquals(0.0, result.totalPrice);
        assertEquals((300 * 1.6 - 0.1) * 0.5, result.refundAmount);
        assertFalse(result.pointsUsed);
    }

    @Test
    public void testBookingWithRewardPoints() {
        FlightBookingSystem system = new FlightBookingSystem();
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime departureTime = bookingTime.plusDays(1);

        FlightBookingSystem.BookingResult result = system.bookFlight(
                1,
                bookingTime,
                5,
                300.0,
                200,
                false,
                departureTime,
                10
        );

        assertTrue(result.confirmation);
        // Assuming points reduce the total price, adjust as per the actual calculation
        assertEquals(300 * 1.6 -0.1, result.totalPrice);
        assertEquals(0.0, result.refundAmount);
        assertTrue(result.pointsUsed);
    }

    @Test
    public void testBookingOnDepartureDay() {
        FlightBookingSystem system = new FlightBookingSystem();
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime departureTime = bookingTime;

        FlightBookingSystem.BookingResult result = system.bookFlight(
                1,
                bookingTime,
                10,
                300.0,
                200,
                false,
                departureTime,
                0
        );

        assertTrue(result.confirmation);
        assertEquals(580, result.totalPrice);
        assertEquals(0.0, result.refundAmount);
        assertFalse(result.pointsUsed);
    }

    @Test
    public void testGroupBookingDiscount() {
        FlightBookingSystem bookingSystem = new FlightBookingSystem();
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime departureTime = bookingTime.plusDays(3);

        FlightBookingSystem.BookingResult result = bookingSystem.bookFlight(
                5,
                bookingTime,
                10,
                100.0,
                50,
                false,
                departureTime,
                0
        );

        assertTrue(result.confirmation);
        assertEquals(190, result.totalPrice, 0.01);
        assertFalse(result.pointsUsed);
    }

    @Test
    public void testBookingWithRewardPointsRedemption() {
        FlightBookingSystem bookingSystem = new FlightBookingSystem();
        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime departureTime = bookingTime.plusDays(3);

        FlightBookingSystem.BookingResult result = bookingSystem.bookFlight(
                1,
                bookingTime,
                10,
                100.0,
                50,
                true,
                departureTime,
                5000
        );

        assertFalse(result.confirmation);
        double expectedPrice = -10;
        assertEquals(0, result.totalPrice, 0.01);
        assertFalse(result.pointsUsed);
    }
}

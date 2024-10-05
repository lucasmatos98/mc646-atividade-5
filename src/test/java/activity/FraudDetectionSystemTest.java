package activity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FraudDetectionSystemTest {

    @Test
    public void testHighTransactionAmount() {
        System.out.println("Running testHighTransactionAmount");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(15000, LocalDateTime.now(), "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), Collections.emptyList());

        assertTrue(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertTrue(result.verificationRequired);
        assertEquals(50, result.riskScore);
    }

    @Test
    public void testExcessiveTransactionsInLastHour() {
        System.out.println("Running testExcessiveTransactionsInLastHour");
        FraudDetectionSystem system = new FraudDetectionSystem();
        LocalDateTime now = LocalDateTime.now();
        List<FraudDetectionSystem.Transaction> previousTransactions = Arrays.asList(
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(10), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(20), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(30), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(40), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(50), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(55), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(56), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(57), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(58), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(59), "NYC"),
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(60), "NYC")
        );
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, now, "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());

        assertFalse(result.isFraudulent);
        assertTrue(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(30, result.riskScore);
    }

    @Test
    public void testLocationChangeWithinShortTimeFrame() {
        System.out.println("Running testLocationChangeWithinShortTimeFrame");
        FraudDetectionSystem system = new FraudDetectionSystem();
        LocalDateTime now = LocalDateTime.now();
        List<FraudDetectionSystem.Transaction> previousTransactions = Collections.singletonList(
            new FraudDetectionSystem.Transaction(100, now.minusMinutes(20), "NYC")
        );
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, now, "LA");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, previousTransactions, Collections.emptyList());

        assertTrue(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertTrue(result.verificationRequired);
        assertEquals(20, result.riskScore);
    }

    @Test
    public void testBlacklistedLocation() {
        System.out.println("Running testBlacklistedLocation");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, LocalDateTime.now(), "BlacklistedLocation");
        List<String> blacklistedLocations = Collections.singletonList("BlacklistedLocation");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), blacklistedLocations);

        assertFalse(result.isFraudulent);
        assertTrue(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(100, result.riskScore);
    }

    @Test
    public void testNormalTransaction() {
        System.out.println("Running testNormalTransaction");
        FraudDetectionSystem system = new FraudDetectionSystem();
        FraudDetectionSystem.Transaction transaction = new FraudDetectionSystem.Transaction(100, LocalDateTime.now(), "NYC");
        FraudDetectionSystem.FraudCheckResult result = system.checkForFraud(transaction, Collections.emptyList(), Collections.emptyList());

        assertFalse(result.isFraudulent);
        assertFalse(result.isBlocked);
        assertFalse(result.verificationRequired);
        assertEquals(0, result.riskScore);
    }
}
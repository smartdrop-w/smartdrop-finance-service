package com.smart.drop.finance.bdd.steps;

import com.smart.drop.finance.application.services.PaymentService;
import com.smart.drop.finance.application.services.WalletService;
import com.smart.drop.finance.domain.model.aggregates.Subscription;
import com.smart.drop.finance.domain.model.entities.SubscriptionPlan;
import com.smart.drop.finance.domain.model.entities.Wallet;
import com.smart.drop.finance.domain.model.entities.WalletTransaction;
import com.smart.drop.finance.domain.model.repositories.FinanceRepository;
import com.smart.drop.finance.domain.model.repositories.SubscriptionRepository;
import com.smart.drop.planning.application.services.AssignmentService;
import com.smart.drop.planning.application.services.RouteService;
import com.smart.drop.planning.domain.model.entities.Carrier;
import com.smart.drop.planning.domain.model.entities.Route;
import com.smart.drop.planning.domain.model.repositories.CarrierRepository;
import com.smart.drop.planning.domain.model.repositories.RouteRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FinanceBddSteps {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private FinanceRepository financeRepository;

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private RouteRepository routeRepository;

    private Integer userId = 4001;
    private SubscriptionPlan currentPlan;
    private Subscription currentSubscription;
    private Wallet currentWallet;
    private Route currentRoute;
    private Carrier currentCarrier;
    private Exception lastException;

    @Given("the SmartDrop finance service is operational")
    public void theSmartDropFinanceServiceIsOperational() {
        assertThat(paymentService).isNotNull();
        assertThat(walletService).isNotNull();
    }

    @Given("an active subscription plan exists titled {string} with price {double} USD and duration {int} days")
    public void anActiveSubscriptionPlanExists(String name, double price, int duration) {
        List<SubscriptionPlan> existing = subscriptionRepository.findAllPlans();
        currentPlan = existing.stream()
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> paymentService.createPlan(name, "Plan BDD testing", BigDecimal.valueOf(price), duration));
    }

    @Given("the subscriber has user ID {int}")
    public void theSubscriberHasUserId(int id) {
        this.userId = id;
    }

    @Given("the user has ID {int}")
    public void theUserHasId(int id) {
        this.userId = id;
    }

    @When("the subscriber creates a subscription for {string} with auto-renew enabled")
    public void theSubscriberCreatesASubscriptionWithAutoRenewEnabled(String planName) {
        currentSubscription = paymentService.createSubscription(userId, currentPlan.planId(), true);
    }

    @Then("the subscription should be created with status {string}")
    public void theSubscriptionShouldBeCreatedWithStatus(String expectedStatus) {
        assertThat(currentSubscription).isNotNull();
        assertThat(currentSubscription.status()).isEqualTo(expectedStatus);
    }

    @And("the subscription auto-renew setting should be true")
    public void theSubscriptionAutoRenewSettingShouldBeTrue() {
        assertThat(currentSubscription.autoRenew()).isTrue();
    }

    @Given("an existing subscription with auto-renew enabled")
    public void anExistingSubscriptionWithAutoRenewEnabled() {
        if (currentPlan == null) {
            anActiveSubscriptionPlanExists("Plan Cervecero Pro", 149.99, 30);
        }
        currentSubscription = paymentService.createSubscription(userId, currentPlan.planId(), true);
    }

    @When("the subscriber toggles auto-renew to false")
    public void theSubscriberTogglesAutoRenewToFalse() {
        currentSubscription = paymentService.toggleAutoRenew(currentSubscription.subscriptionId(), false);
    }

    @Then("the subscription auto-renew setting should be false")
    public void theSubscriptionAutoRenewSettingShouldBeFalse() {
        assertThat(currentSubscription.autoRenew()).isFalse();
    }

    @When("the administrator updates the subscription status to {string}")
    public void theAdministratorUpdatesTheSubscriptionStatusTo(String status) {
        currentSubscription = paymentService.updateSubscriptionStatus(currentSubscription.subscriptionId(), status);
    }

    @Then("the subscription status should be {string}")
    public void theSubscriptionStatusShouldBe(String expectedStatus) {
        assertThat(currentSubscription.status()).isEqualTo(expectedStatus);
    }

    @When("the user recharges the wallet with {double} USD for {string}")
    public void theUserRechargesTheWalletWithUSDFor(double amount, String concept) {
        currentWallet = walletService.addFunds(userId, BigDecimal.valueOf(amount), concept);
    }

    @Then("the wallet balance should be at least {double} USD")
    public void theWalletBalanceShouldBeAtLeastUSD(double expectedMinimum) {
        assertThat(currentWallet).isNotNull();
        assertThat(currentWallet.balance()).isGreaterThanOrEqualTo(BigDecimal.valueOf(expectedMinimum));
    }

    @And("the transaction history should contain an entry with positive amount {double} USD")
    public void theTransactionHistoryShouldContainAnEntryWithPositiveAmount(double expectedAmount) {
        List<WalletTransaction> txs = walletService.getTransactions(userId);
        assertThat(txs).anyMatch(tx -> tx.amount().compareTo(BigDecimal.valueOf(expectedAmount)) == 0);
    }

    @Given("the user wallet has an available balance of {double} USD")
    public void theUserWalletHasAnAvailableBalanceOfUSD(double targetBalance) {
        Wallet wallet = walletService.getOrCreateWallet(userId);
        Wallet updated = new Wallet(wallet.walletId(), userId, BigDecimal.valueOf(targetBalance));
        financeRepository.saveWallet(updated);
        currentWallet = updated;
    }

    @When("the user withdraws {double} USD from the wallet for {string}")
    public void theUserWithdrawsUSDFromTheWalletFor(double amount, String concept) {
        currentWallet = walletService.withdrawFunds(userId, BigDecimal.valueOf(amount), concept);
    }

    @Then("the wallet balance should be {double} USD")
    public void theWalletBalanceShouldBeUSD(double expectedBalance) {
        assertThat(currentWallet.balance()).isEqualByComparingTo(BigDecimal.valueOf(expectedBalance));
    }

    @When("the user attempts to withdraw {double} USD from the wallet")
    public void theUserAttemptsToWithdrawUSDFromTheWallet(double amount) {
        try {
            lastException = null;
            currentWallet = walletService.withdrawFunds(userId, BigDecimal.valueOf(amount), "Overdraft attempt");
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("the transaction should fail with an insufficient funds error")
    public void theTransactionShouldFailWithAnInsufficientFundsError() {
        assertThat(lastException).isNotNull();
        assertThat(lastException).isInstanceOf(IllegalArgumentException.class);
        assertThat(lastException.getMessage()).containsIgnoringCase("insufficient funds");
    }

    @Given("the SmartDrop logistics planning service is operational")
    public void theSmartDropLogisticsPlanningServiceIsOperational() {
        assertThat(routeService).isNotNull();
        assertThat(assignmentService).isNotNull();
    }

    @Given("an active carrier exists named {string} with contact phone {string}")
    public void anActiveCarrierExists(String name, String phone) {
        currentCarrier = carrierRepository.findAll().stream()
                .filter(c -> c.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> assignmentService.createCarrier(name, phone));
    }

    @When("the dispatcher schedules a route from {string} to {string}")
    public void theDispatcherSchedulesARouteFromTo(String origin, String destination) {
        currentRoute = routeService.calculateAndCreateRoute(origin, destination, null);
    }

    @Then("the route should be created with estimated distance and time")
    public void theRouteShouldBeCreatedWithEstimatedDistanceAndTime() {
        assertThat(currentRoute).isNotNull();
        assertThat(currentRoute.routeId()).isNotNull();
        assertThat(currentRoute.distanceKm()).isNotNull();
    }

    @And("the initial route status should be {string}")
    public void theInitialRouteStatusShouldBe(String expectedStatus) {
        assertThat(currentRoute.status()).isEqualTo(expectedStatus);
    }

    @Given("a scheduled route from {string} to {string}")
    public void aScheduledRouteFromTo(String origin, String destination) {
        currentRoute = routeService.calculateAndCreateRoute(origin, destination, null);
    }

    @When("the dispatcher assigns carrier {string} to the route")
    public void theDispatcherAssignsCarrierToTheRoute(String carrierName) {
        if (currentCarrier == null || !currentCarrier.name().equalsIgnoreCase(carrierName)) {
            anActiveCarrierExists(carrierName, "+51998877665");
        }
        currentRoute = assignmentService.assignCarrierToRoute(currentRoute.routeId(), currentCarrier.carrierId());
    }

    @Then("the route should be linked with the carrier ID")
    public void theRouteShouldBeLinkedWithTheCarrierId() {
        assertThat(currentRoute.assignedCarrierId()).isEqualTo(currentCarrier.carrierId());
    }

    @When("the carrier updates the route status to {string}")
    public void theCarrierUpdatesTheRouteStatusTo(String status) {
        currentRoute = routeService.updateRouteStatus(currentRoute.routeId(), status);
    }

    @Then("the route status should become {string}")
    public void theRouteStatusShouldBecome(String expectedStatus) {
        assertThat(currentRoute.status()).isEqualTo(expectedStatus);
    }
}

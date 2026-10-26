package com.smart.drop.finance.application.services;

import com.smart.drop.finance.domain.model.aggregates.Subscription;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.entities.Payment;
import com.smart.drop.finance.domain.model.entities.SubscriptionPlan;
import com.smart.drop.finance.domain.model.entities.Wallet;
import com.smart.drop.finance.domain.model.ports.PaymentGatewayPort;
import com.smart.drop.finance.domain.model.repositories.FinanceRepository;
import com.smart.drop.finance.domain.model.repositories.SubscriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas BDD y Unitarias - Microservicio Finance: Billetera Digital y Suscripciones SaaS")
class FinanceAndSubscriptionBddTest {

    @Mock
    private FinanceRepository financeRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private PaymentGatewayPort paymentGatewayPort;

    @InjectMocks
    private WalletService walletService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("US15 Escenario 1: Recarga exitosa de saldo en la billetera digital del usuario")
    void shouldAddFundsToWalletSuccessfully() {
        // Given
        Wallet existingWallet = new Wallet(201, 1001, new BigDecimal("50.00"));
        when(financeRepository.findWalletByUserId(1001)).thenReturn(Optional.of(existingWallet));
        when(financeRepository.saveWallet(any(Wallet.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Wallet updated = walletService.addFunds(1001, new BigDecimal("150.00"), "Recarga Tarjeta Visa");

        // Then
        assertThat(updated.balance()).isEqualByComparingTo("200.00");
        verify(financeRepository, times(1)).saveTransaction(any());
    }

    @Test
    @DisplayName("US15 Escenario 2: Rechazo de retiro cuando el saldo de la billetera es insuficiente")
    void shouldRejectWithdrawalWithInsufficientFunds() {
        // Given
        Wallet existingWallet = new Wallet(201, 1001, new BigDecimal("30.00"));
        when(financeRepository.findWalletByUserId(1001)).thenReturn(Optional.of(existingWallet));

        // When & Then
        assertThatThrownBy(() -> walletService.withdrawFunds(1001, new BigDecimal("100.00"), "Pago Suscripcion"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient funds in wallet");
    }

    @Test
    @DisplayName("US15 Escenario 3: Creacion de suscripcion SaaS asociada a un plan empresarial")
    void shouldCreateSubscriptionForValidPlan() {
        // Given
        SubscriptionPlan breweryPlan = SubscriptionPlan.create("Brewery Pro", "Monitoreo cervecero", new BigDecimal("49.90"), 30);
        when(subscriptionRepository.findPlanById(10)).thenReturn(Optional.of(breweryPlan));
        when(subscriptionRepository.saveSubscription(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Subscription sub = paymentService.createSubscription(1001, 10, true);

        // Then
        assertThat(sub.status()).isEqualTo("PENDING");
        assertThat(sub.autoRenew()).isTrue();
        assertThat(sub.plan().name()).isEqualTo("Brewery Pro");
    }

    @Test
    @DisplayName("US15 Escenario 4: Procesamiento de pago exitoso, emision de factura y activacion de suscripcion")
    void shouldProcessPaymentAndActivateSubscription() {
        // Given
        Subscription pendingSub = Subscription.create(1001, 10, 30, true);
        when(subscriptionRepository.findSubscriptionById(801)).thenReturn(Optional.of(pendingSub));
        when(paymentGatewayPort.charge(any())).thenReturn(
                new PaymentGatewayPort.PaymentGatewayResponse(true, "TX-998877", "Approved")
        );
        when(subscriptionRepository.savePayment(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));
        when(subscriptionRepository.saveInvoice(any(Invoice.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Payment payment = paymentService.processPayment(801, new BigDecimal("49.90"), "USD", "CARD");

        // Then
        assertThat(payment.transactionId()).isEqualTo("TX-998877");
        verify(subscriptionRepository, times(1)).saveInvoice(any(Invoice.class));
        verify(subscriptionRepository, times(1)).updateSubscription(argThat(s -> "ACTIVE".equals(s.status())));
    }
}

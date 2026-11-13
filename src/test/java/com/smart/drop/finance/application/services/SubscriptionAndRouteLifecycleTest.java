package com.smart.drop.finance.application.services;

import com.smart.drop.finance.domain.model.aggregates.Subscription;
import com.smart.drop.finance.domain.model.entities.Invoice;
import com.smart.drop.finance.domain.model.ports.PaymentGatewayPort;
import com.smart.drop.finance.domain.model.repositories.SubscriptionRepository;
import com.smart.drop.planning.application.services.AssignmentService;
import com.smart.drop.planning.application.services.RouteService;
import com.smart.drop.planning.domain.model.entities.Carrier;
import com.smart.drop.planning.domain.model.entities.Route;
import com.smart.drop.planning.domain.model.ports.MapsServicePort;
import com.smart.drop.planning.domain.model.repositories.CarrierRepository;
import com.smart.drop.planning.domain.model.repositories.RouteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for Subscription Auto-Renew, Invoicing, and Route Planning Lifecycle")
class SubscriptionAndRouteLifecycleTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private PaymentGatewayPort paymentGatewayPort;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @Mock
    private MapsServicePort mapsServicePort;

    @InjectMocks
    private PaymentService paymentService;

    @InjectMocks
    private RouteService routeService;

    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    @DisplayName("toggleAutoRenew should flip boolean autoRenew when target is null")
    void toggleAutoRenew_ShouldFlipBooleanWhenTargetNull() {
        Subscription initial = new Subscription(
                10,
                42,
                1,
                "ACTIVE",
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                true,
                null,
                List.of(),
                List.of()
        );

        when(subscriptionRepository.findSubscriptionById(10)).thenReturn(Optional.of(initial));
        when(subscriptionRepository.updateSubscription(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        Subscription toggled = paymentService.toggleAutoRenew(10, null);

        assertThat(toggled.autoRenew()).isFalse();
        verify(subscriptionRepository, times(1)).updateSubscription(any(Subscription.class));
    }

    @Test
    @DisplayName("toggleAutoRenew should explicitly set requested autoRenew value")
    void toggleAutoRenew_ShouldSetExplicitValue() {
        Subscription initial = new Subscription(
                11,
                42,
                1,
                "ACTIVE",
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                false,
                null,
                List.of(),
                List.of()
        );

        when(subscriptionRepository.findSubscriptionById(11)).thenReturn(Optional.of(initial));
        when(subscriptionRepository.updateSubscription(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

        Subscription updated = paymentService.toggleAutoRenew(11, true);

        assertThat(updated.autoRenew()).isTrue();
    }

    @Test
    @DisplayName("getInvoiceById should return invoice from repository")
    void getInvoiceById_ShouldReturnInvoice() {
        Invoice invoice = new Invoice(
                5001,
                10,
                LocalDateTime.now(),
                new BigDecimal("99.90"),
                "USD"
        );

        when(subscriptionRepository.findInvoiceById(5001)).thenReturn(Optional.of(invoice));

        Optional<Invoice> result = paymentService.getInvoiceById(5001);

        assertThat(result).isPresent();
        assertThat(result.get().invoiceId()).isEqualTo(5001);
        assertThat(result.get().total()).isEqualByComparingTo("99.90");
    }

    @Test
    @DisplayName("updateRouteStatus should update status on route")
    void updateRouteStatus_ShouldUpdateStatus() {
        Route existing = new Route(
                101,
                "Point A",
                "Point B",
                new BigDecimal("12.50"),
                new BigDecimal("25.00"),
                5,
                "SCHEDULED",
                LocalDateTime.now()
        );

        when(routeRepository.findById(101)).thenReturn(Optional.of(existing));
        when(routeRepository.update(any(Route.class))).thenAnswer(inv -> inv.getArgument(0));

        Route updated = routeService.updateRouteStatus(101, "IN_TRANSIT");

        assertThat(updated.status()).isEqualTo("IN_TRANSIT");
        assertThat(updated.routeId()).isEqualTo(101);
        verify(routeRepository, times(1)).update(any(Route.class));
    }

    @Test
    @DisplayName("updateCarrierStatus should update status on carrier")
    void updateCarrierStatus_ShouldUpdateStatus() {
        Carrier existing = new Carrier(
                5,
                "TransAqua Fleet",
                "+51999888777",
                "ACTIVE"
        );

        when(carrierRepository.findById(5)).thenReturn(Optional.of(existing));
        when(carrierRepository.save(any(Carrier.class))).thenAnswer(inv -> inv.getArgument(0));

        Carrier updated = assignmentService.updateCarrierStatus(5, "BUSY");

        assertThat(updated.status()).isEqualTo("BUSY");
        assertThat(updated.carrierId()).isEqualTo(5);
        verify(carrierRepository, times(1)).save(any(Carrier.class));
    }
}

package com.smart.drop.planning.interfaces.rest;

import com.smart.drop.planning.application.services.AssignmentService;
import com.smart.drop.planning.application.services.RouteService;
import com.smart.drop.planning.domain.model.entities.Carrier;
import com.smart.drop.planning.domain.model.entities.Route;
import com.smart.drop.planning.interfaces.rest.dto.AssignCarrierRequest;
import com.smart.drop.planning.interfaces.rest.dto.CarrierResponse;
import com.smart.drop.planning.interfaces.rest.dto.CreateCarrierRequest;
import com.smart.drop.planning.interfaces.rest.dto.CreateRouteRequest;
import com.smart.drop.planning.interfaces.rest.dto.RouteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/planning/routes")
public class RouteController {

    private final RouteService routeService;
    private final AssignmentService assignmentService;

    public RouteController(RouteService routeService, AssignmentService assignmentService) {
        this.routeService = routeService;
        this.assignmentService = assignmentService;
    }

    @PostMapping("/carriers")
    public ResponseEntity<CarrierResponse> createCarrier(@RequestBody CreateCarrierRequest request) {
        Carrier carrier = assignmentService.createCarrier(request.name(), request.contactPhone());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCarrierResponse(carrier));
    }

    @GetMapping("/carriers")
    public ResponseEntity<List<CarrierResponse>> getCarriers() {
        List<CarrierResponse> data = assignmentService.getAllCarriers().stream().map(this::toCarrierResponse).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/carriers/{carrierId}")
    public ResponseEntity<CarrierResponse> getCarrierById(@PathVariable Integer carrierId) {
        return assignmentService.getCarrierById(carrierId)
                .map(carrier -> ResponseEntity.ok(toCarrierResponse(carrier)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/routes")
    public ResponseEntity<RouteResponse> createRoute(@RequestBody CreateRouteRequest request) {
        Route route = routeService.calculateAndCreateRoute(
                request.origin(),
                request.destination(),
                request.assignedCarrierId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toRouteResponse(route));
    }

    @GetMapping("/routes")
    public ResponseEntity<List<RouteResponse>> getRoutes() {
        List<RouteResponse> data = routeService.getAll().stream().map(this::toRouteResponse).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/routes/{routeId}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable Integer routeId) {
        return routeService.getById(routeId)
                .map(route -> ResponseEntity.ok(toRouteResponse(route)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/routes/carrier/{carrierId}")
    public ResponseEntity<List<RouteResponse>> getRoutesByCarrier(@PathVariable Integer carrierId) {
        List<RouteResponse> data = routeService.getByCarrier(carrierId).stream().map(this::toRouteResponse).toList();
        return ResponseEntity.ok(data);
    }

    @PutMapping("/routes/{routeId}/assign")
    public ResponseEntity<RouteResponse> assignCarrier(@PathVariable Integer routeId,
                                                       @RequestBody AssignCarrierRequest request) {
        Route updated = assignmentService.assignCarrierToRoute(routeId, request.carrierId());
        return ResponseEntity.ok(toRouteResponse(updated));
    }

    private CarrierResponse toCarrierResponse(Carrier carrier) {
        return new CarrierResponse(carrier.carrierId(), carrier.name(), carrier.contactPhone(), carrier.status());
    }

    private RouteResponse toRouteResponse(Route route) {
        return new RouteResponse(
                route.routeId(),
                route.origin(),
                route.destination(),
                route.distanceKm(),
                route.estimatedTime(),
                route.assignedCarrierId(),
                route.createdAt()
        );
    }
}


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
import com.smart.drop.planning.interfaces.rest.dto.UpdateCarrierStatusRequest;
import com.smart.drop.planning.interfaces.rest.dto.UpdateRouteStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Planning & Routes", description = "Water delivery routing, dispatch tracking, and carrier fleet assignment operations")
public class RouteController {

    private final RouteService routeService;
    private final AssignmentService assignmentService;

    public RouteController(RouteService routeService, AssignmentService assignmentService) {
        this.routeService = routeService;
        this.assignmentService = assignmentService;
    }

    @PostMapping("/carriers")
    @Operation(summary = "Register logistics carrier", description = "Registers a new water distribution carrier in the logistics fleet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrier registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid carrier data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CarrierResponse> createCarrier(@RequestBody CreateCarrierRequest request) {
        Carrier carrier = assignmentService.createCarrier(request.name(), request.contactPhone());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCarrierResponse(carrier));
    }

    @GetMapping("/carriers")
    @Operation(summary = "List all carriers", description = "Retrieves all registered logistics carriers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carriers retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<CarrierResponse>> getCarriers() {
        List<CarrierResponse> data = assignmentService.getAllCarriers().stream().map(this::toCarrierResponse).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/carriers/{carrierId}")
    @Operation(summary = "Get carrier by ID", description = "Retrieves details of a specific logistics carrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrier found"),
            @ApiResponse(responseCode = "404", description = "Carrier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CarrierResponse> getCarrierById(@PathVariable Integer carrierId) {
        return assignmentService.getCarrierById(carrierId)
                .map(carrier -> ResponseEntity.ok(toCarrierResponse(carrier)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/carriers/{carrierId}/status")
    @Operation(summary = "Update carrier operational status", description = "Updates carrier status (e.g., ACTIVE, INACTIVE, BUSY)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrier status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Carrier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CarrierResponse> updateCarrierStatus(
            @PathVariable Integer carrierId,
            @RequestBody UpdateCarrierStatusRequest request
    ) {
        Carrier updated = assignmentService.updateCarrierStatus(carrierId, request.status());
        return ResponseEntity.ok(toCarrierResponse(updated));
    }

    @PostMapping("/routes")
    @Operation(summary = "Create distribution route", description = "Calculates distance and estimated duration, creating a new delivery route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Route created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid route coordinates/addresses"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RouteResponse> createRoute(@RequestBody CreateRouteRequest request) {
        Route route = routeService.calculateAndCreateRoute(
                request.origin(),
                request.destination(),
                request.assignedCarrierId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toRouteResponse(route));
    }

    @GetMapping("/routes")
    @Operation(summary = "List all distribution routes", description = "Retrieves all planned and active distribution routes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<RouteResponse>> getRoutes() {
        List<RouteResponse> data = routeService.getAll().stream().map(this::toRouteResponse).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/routes/{routeId}")
    @Operation(summary = "Get route by ID", description = "Retrieves details of a specific water delivery route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route found"),
            @ApiResponse(responseCode = "404", description = "Route not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable Integer routeId) {
        return routeService.getById(routeId)
                .map(route -> ResponseEntity.ok(toRouteResponse(route)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/routes/carrier/{carrierId}")
    @Operation(summary = "Get routes by carrier ID", description = "Retrieves all routes assigned to a specific carrier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<RouteResponse>> getRoutesByCarrier(@PathVariable Integer carrierId) {
        List<RouteResponse> data = routeService.getByCarrier(carrierId).stream().map(this::toRouteResponse).toList();
        return ResponseEntity.ok(data);
    }

    @PutMapping("/routes/{routeId}/assign")
    @Operation(summary = "Assign carrier to route", description = "Assigns an available logistics carrier to an existing route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrier assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Route or carrier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RouteResponse> assignCarrier(@PathVariable Integer routeId,
                                                        @RequestBody AssignCarrierRequest request) {
        Route updated = assignmentService.assignCarrierToRoute(routeId, request.carrierId());
        return ResponseEntity.ok(toRouteResponse(updated));
    }

    @PutMapping("/routes/{routeId}/status")
    @Operation(summary = "Update route execution status", description = "Updates the delivery route status (e.g., SCHEDULED, IN_TRANSIT, COMPLETED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<RouteResponse> updateRouteStatus(
            @PathVariable Integer routeId,
            @RequestBody UpdateRouteStatusRequest request
    ) {
        Route updated = routeService.updateRouteStatus(routeId, request.status());
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
                route.status(),
                route.createdAt()
        );
    }
}

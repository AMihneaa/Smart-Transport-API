package com.tpi.demo.web;

import com.tpi.demo.models.Point.StopPoint;
import com.tpi.demo.models.Route.LegDto;
import com.tpi.demo.models.Route.Route;
import com.tpi.demo.models.Route.RouteOptionDto;
import com.tpi.demo.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    private static final Logger log = LoggerFactory.getLogger(RouteController.class);

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/options")
    public ResponseEntity<List<RouteOptionDto>> getRouteOptions(
            @RequestParam String departure,
            @RequestParam String arrival
    ) {
        List<List<Route>> options;

        try {
            options = routeService.findRouteOptions(departure, arrival);

        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("No routes found")) {
                log.info("No route options found for {} -> {}. Returning empty list.", departure, arrival);
                return ResponseEntity.ok(Collections.emptyList());
            }

            throw e;
        }

        if (options == null || options.isEmpty()) {
            log.info("RouteService returned empty options for {} -> {}. Returning empty list.", departure, arrival);
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<RouteOptionDto> dto = options.stream()
                .map(routes -> new RouteOptionDto(
                        routes.stream()
                                .map(route -> new LegDto(
                                        route.getId(),
                                        route.getTransportId(),
                                        route.getTransportType(),
                                        route.getStops().stream()
                                                .map(StopPoint::getLocation)
                                                .toList(),
                                        route.getAvailableSeats()
                                ))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(dto);
    }
}

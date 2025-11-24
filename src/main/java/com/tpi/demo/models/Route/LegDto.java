package com.tpi.demo.models.Route;

import com.tpi.demo.models.Enums.TransportType;
import java.util.List;

public record LegDto(
        String routeId,
        String transportId,
        TransportType transportType,
        List<String> stops,
        int availableSeats
) {}
package com.tpi.demo.models.Route;

import java.util.List;

public record RouteOptionDto(
        List<LegDto> legs
) {}

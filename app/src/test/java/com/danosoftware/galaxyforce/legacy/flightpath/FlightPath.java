package com.danosoftware.galaxyforce.legacy.flightpath;

import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;

import java.util.List;

public interface FlightPath {

    /* returns an array of points to append to existing path */
    List<Point> addPath();

    PathDTO createDTO();

}

package com.danosoftware.galaxyforce.flightpath.paths;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.dto.BezierPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.CircularPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.LinearPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PausePathDTO;
import com.danosoftware.galaxyforce.flightpath.generators.BezierCurveGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.CircularGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.LinearGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.PathGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.PauseGenerator;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;
import com.danosoftware.galaxyforce.flightpath.utilities.PathLoader;

import java.util.ArrayList;
import java.util.List;

public final class PathFactory {

    private final PathLoader loader;

    public PathFactory(PathLoader loader) {
        this.loader = loader;
    }

    public List<Point> createPath(Path path, PointTranslatorChain translators) {
        List<Point> pathPoints = new ArrayList<>();

        // load path data from file
        PathListDTO pathData = loader.loadPaths(path.getPathFile());

        for (PathDTO pathDTO : pathData.getPathList()) {
            final PathGenerator generator;
            switch (pathDTO.getType()) {
                case BEZIER:
                    BezierPathDTO bezierData = (BezierPathDTO) pathDTO;
                    generator = new BezierCurveGenerator(bezierData, translators);
                    break;
                case LINEAR:
                    LinearPathDTO linearData = (LinearPathDTO) pathDTO;
                    generator = new LinearGenerator(linearData, translators);
                    break;
                case PAUSE:
                    PausePathDTO pauseData = (PausePathDTO) pathDTO;
                    generator = new PauseGenerator(pauseData, translators);
                    break;
                case CIRCULAR:
                    CircularPathDTO circularData = (CircularPathDTO) pathDTO;
                    generator = new CircularGenerator(circularData, translators);
                    break;
                default:
                    throw new GalaxyForceException("Unknown path type: " + pathDTO.getType().name());
            }
            pathPoints.addAll(generator.path());
        }
        return pathPoints;
    }
}

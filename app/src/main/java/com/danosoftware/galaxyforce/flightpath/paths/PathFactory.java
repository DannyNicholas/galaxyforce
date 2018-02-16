package com.danosoftware.galaxyforce.flightpath.paths;

import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.flightpath.dto.BezierPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.LinearPathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PausePathDTO;
import com.danosoftware.galaxyforce.flightpath.generators.BezierCurveGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.LinearGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.PathGenerator;
import com.danosoftware.galaxyforce.flightpath.generators.PauseGenerator;
import com.danosoftware.galaxyforce.flightpath.translators.PointTranslatorChain;

import java.util.ArrayList;
import java.util.List;

import static com.danosoftware.galaxyforce.flightpath.utilities.PathLoader.loadPaths;

public final class PathFactory
{

    private PathFactory() {
    }

    public static List<Point> createPath(Path path, PointTranslatorChain translators)
    {
        List<Point> alienPath = new ArrayList<>();

        // load path data from file
        PathListDTO pathData = loadPaths(path.getPathFile());

        for (PathDTO pathDTO: pathData.getPathList()) {
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
                default:
                    throw new GalaxyForceException("Unknown path type: "+ pathDTO.getType().name());
            }
            alienPath.addAll(generator.path());
        }
        return alienPath;
    }
}

package com.danosoftware.galaxyforce.flightpath.legacy;

import android.support.annotation.NonNull;

import com.danosoftware.galaxyforce.flightpath.dto.PathDTO;
import com.danosoftware.galaxyforce.flightpath.dto.PathListDTO;
import com.danosoftware.galaxyforce.legacy.flightpath.FlightPath;
import com.danosoftware.galaxyforce.legacy.flightpath.Path;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that can create JSON files from legacy paths.
 * This allows the old style paths to be converted to JSON
 * paths and used as new style paths.
 */
public class CreatePathJsonFiles {

    final static Logger logger = LoggerFactory.getLogger(CreatePathJsonFiles.class);

    /**
     * Create JSON files holding path data for all paths.
     */
    @Test
    @Ignore
    public void createJsonFiles() {

        for (Path path : Path.values()) {

            logger.info("Converting " + path.name());

            List<PathDTO> pathDTOs = new ArrayList<>();
            for (FlightPath flightPath : path.getPathList()) {
                pathDTOs.add(flightPath.createDTO());
            }
            PathListDTO list = new PathListDTO((pathDTOs));
            savePaths(list, path.name() + ".json");
        }
        logger.info("All Finished.");
    }

    /**
     * Save a JSON file for the supplied path DTO
     *
     * @param listDTO  list of path DTOs
     * @param fileName name of JSON file to export to
     */
    private void savePaths(PathListDTO listDTO, String fileName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = getFile(fileName);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, listDTO);
            logger.info("Saved JSON path '{}' to {}", fileName, file);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading JSON path: " + fileName, e);
        }
    }

    /**
     * Return a file for the supplied file name.
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    @NonNull
    private static File getFile(String fileName) throws IOException {
        java.nio.file.Path directory = Paths.get(System.getProperty("user.home"), "development", "galaxyforce-exported-paths");
        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
        java.nio.file.Path filePath = Paths.get(directory.toString(), fileName);
        return new File(filePath.toUri());
    }
}

package com.danosoftware.galaxyforce.services.file;

import java.io.IOException;
import java.io.InputStream;

public interface FileIO {
    InputStream readAsset(String fileName) throws IOException;
}

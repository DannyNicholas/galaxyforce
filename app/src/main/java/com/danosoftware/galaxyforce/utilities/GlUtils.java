/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.danosoftware.galaxyforce.utilities;

import android.opengl.GLES20;
import android.util.Log;

/**
 * A handful of utility functions.
 */
public class GlUtils {

  private static final String TAG = "GlUtils";

  /**
   * Loads a shader from a string and compiles it.
   *
   * @param type       GL shader type, e.g. GLES20.GL_VERTEX_SHADER.
   * @param shaderCode Shader source code.
   * @return Handle to shader.
   */
  public static int loadShader(int type, String shaderCode) {
    int shaderHandle = GLES20.glCreateShader(type);

    GLES20.glShaderSource(shaderHandle, shaderCode);
    GLES20.glCompileShader(shaderHandle);

    // Check for failure.
    int[] compileStatus = new int[1];
    GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
    if (compileStatus[0] != GLES20.GL_TRUE) {
      // Extract the detailed failure message.
      String msg = GLES20.glGetShaderInfoLog(shaderHandle);
      GLES20.glDeleteProgram(shaderHandle);
      Log.e(TAG, "glCompileShader: " + msg);
      throw new RuntimeException("glCompileShader failed");
    }

    return shaderHandle;
  }

  /**
   * Creates a program, given source code for vertex and fragment shaders.
   *
   * @param vertexShaderCode   Source code for vertex shader.
   * @param fragmentShaderCode Source code for fragment shader.
   * @return Handle to program.
   */
  public static ShaderProgram createProgram(String vertexShaderCode, String fragmentShaderCode) {
    // Load the shaders.
    int vertexShader =
        GlUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
    int fragmentShader =
        GlUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

    // Build the program.
    int programHandle = GLES20.glCreateProgram();
    GLES20.glAttachShader(programHandle, vertexShader);
    GLES20.glAttachShader(programHandle, fragmentShader);
    GLES20.glLinkProgram(programHandle);

    // Check for failure.
    int[] linkStatus = new int[1];
    GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
    if (linkStatus[0] != GLES20.GL_TRUE) {
      // Extract the detailed failure message.
      String msg = GLES20.glGetProgramInfoLog(programHandle);
      GLES20.glDeleteProgram(programHandle);
      Log.e(TAG, "glLinkProgram: " + msg);
      throw new RuntimeException("glLinkProgram failed");
    }

    return ShaderProgram
        .builder()
        .programHandle(programHandle)
        .vertexShader(vertexShader)
        .fragmentShader(fragmentShader)
        .build();
  }

  /**
   * * Dispose of the GL program and shaders.
   */
  public static void dispose(ShaderProgram shaderProgram) {
    GLES20.glDetachShader(shaderProgram.getProgramHandle(), shaderProgram.getVertexShader());
    GLES20.glDetachShader(shaderProgram.getProgramHandle(), shaderProgram.getFragmentShader());

    GLES20.glDeleteShader(shaderProgram.getVertexShader());
    GLES20.glDeleteShader(shaderProgram.getFragmentShader());

    GLES20.glDeleteProgram(shaderProgram.getProgramHandle());
  }

  /**
   * Utility method for checking for OpenGL errors.  Use like this:
   *
   * <pre>
   * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
   * MyGLRenderer.checkGlError("glGetUniformLocation");
   * </pre>
   * <p>
   * If an error was detected, this will throw an exception.
   * <p>
   * NOTE: ProGuard is configured to remove any calls to this method from release build.
   *
   * @param msg string to display in the error message (usually the name of the last GL operation)
   */
  public static void checkGlError(String msg) {
    int error, lastError = GLES20.GL_NO_ERROR;

    while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
      Log.e(TAG, msg + ": glError " + error);
      lastError = error;
    }
    if (lastError != GLES20.GL_NO_ERROR) {
      throw new RuntimeException(msg + ": glError " + lastError);
    }
  }
}

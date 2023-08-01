package com.danosoftware.galaxyforce.utilities;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class ShaderProgram {

  private final int programHandle;
  private final int vertexShader;
  private final int fragmentShader;
}

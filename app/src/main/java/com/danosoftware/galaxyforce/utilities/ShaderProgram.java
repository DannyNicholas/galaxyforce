package com.danosoftware.galaxyforce.utilities;

public final class ShaderProgram {

  private final int programHandle;
  private final int vertexShader;
  private final int fragmentShader;

  ShaderProgram(int programHandle, int vertexShader, int fragmentShader) {
    this.programHandle = programHandle;
    this.vertexShader = vertexShader;
    this.fragmentShader = fragmentShader;
  }

  public static ShaderProgramBuilder builder() {
    return new ShaderProgramBuilder();
  }

  public int getProgramHandle() {
    return this.programHandle;
  }

  public int getVertexShader() {
    return this.vertexShader;
  }

  public int getFragmentShader() {
    return this.fragmentShader;
  }

  public static class ShaderProgramBuilder {
    private int programHandle;
    private int vertexShader;
    private int fragmentShader;

    ShaderProgramBuilder() {
    }

    public ShaderProgram.ShaderProgramBuilder programHandle(int programHandle) {
      this.programHandle = programHandle;
      return this;
    }

    public ShaderProgram.ShaderProgramBuilder vertexShader(int vertexShader) {
      this.vertexShader = vertexShader;
      return this;
    }

    public ShaderProgram.ShaderProgramBuilder fragmentShader(int fragmentShader) {
      this.fragmentShader = fragmentShader;
      return this;
    }

    public ShaderProgram build() {
      return new ShaderProgram(programHandle, vertexShader, fragmentShader);
    }
  }
}

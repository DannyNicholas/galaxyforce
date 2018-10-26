package com.danosoftware.galaxyforce.controller.interfaces;

/**
 * Base Controller Model interface for tilt implementations (e.g.
 * accelerometer).
 */
public interface TiltBaseControllerModel extends BaseControllerModel
{

    /**
     * Sets the x and y weighting of the model.
     * 
     * @param weightingX
     *            x weighting
     * @param weightingY
     *            y weighting
     */
    void setWeighting(float weightingX, float weightingY);

}

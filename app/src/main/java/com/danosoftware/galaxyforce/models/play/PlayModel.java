package com.danosoftware.galaxyforce.models.play;

import com.danosoftware.galaxyforce.models.Model;
import com.danosoftware.galaxyforce.text.Text;

public interface PlayModel extends Model {
    /**
     * Called by any flashing text implementations when their state changes. The
     * supplied text should be displayed if the flashState is true. Otherwise
     * the supplied text should be hidden if the flashState is false.
     *
     * @param text       - text instance
     * @param flashState - show or hide text
     */
    void flashText(Text text, boolean flashState);

}

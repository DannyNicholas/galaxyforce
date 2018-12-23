package com.danosoftware.galaxyforce.models.screens.options;

import com.danosoftware.galaxyforce.models.screens.Model;
import com.danosoftware.galaxyforce.options.Option;

public interface OptionsModel extends Model {

    /**
     * Called when option selected. Implementation will need to work out which
     * option type has been selected and take appropriate action.
     */
    void optionSelected(Option optionSelected);
}

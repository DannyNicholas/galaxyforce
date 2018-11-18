package com.danosoftware.galaxyforce.interfaces;

import com.danosoftware.galaxyforce.options.Option;

public interface OptionsModel extends Model {

    /**
     * Called when option selected. Implementation will need to work out which
     * option type has been selected and take appropriate action.
     */
    void optionSelected(Option optionSelected);
}

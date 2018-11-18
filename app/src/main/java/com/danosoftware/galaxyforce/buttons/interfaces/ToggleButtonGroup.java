package com.danosoftware.galaxyforce.buttons.interfaces;

import com.danosoftware.galaxyforce.options.Option;

public interface ToggleButtonGroup {

    /**
     * Allows new buttons to be added to the option group
     *
     * @param button     - new button being added
     * @param optionType - option type associated with button
     */
    void addOption(Button button, Option optionType);

    /**
     * Allows buttons to call back toggle group when selected so the group knows
     * which button is active and hence which buttons are inactive.
     *
     * @param buttonSelected - button that has just been selected
     * @param optionType     - option type associated with button
     */
    void optionSelected(Button buttonSelected, Option optionType);

    /**
     * Returns the currently selected option within the group
     *
     * @return selected option
     */
    Option getSelectedOption();

}

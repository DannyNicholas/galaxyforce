package com.danosoftware.galaxyforce.buttons.impl;

import com.danosoftware.galaxyforce.buttons.interfaces.Button;
import com.danosoftware.galaxyforce.buttons.interfaces.ToggleButtonGroup;
import com.danosoftware.galaxyforce.interfaces.OptionsModel;
import com.danosoftware.galaxyforce.options.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a level selector. Level selector has a border, level number and
 * position.
 */
public class ToggleOption implements ToggleButtonGroup
{
    // reference to list of buttons
    private List<Button> options = null;

    // reference to model
    OptionsModel model = null;

    // reference to the currently selected option
    Option currentOption = null;

    public ToggleOption(OptionsModel model, Option currentOption)
    {
        this.model = model;
        this.currentOption = currentOption;
        this.options = new ArrayList<Button>();
    }

    // allows buttons to be added to option group.
    @Override
    public void addOption(Button button, Option optionType)
    {
        options.add(button);

        // selects button if it is currently selected option
        if (optionType == currentOption)
        {
            button.buttonDown();
        }
        else
        {
            button.buttonReleased();
        }
    }

    // allows buttons to call back when selected
    @Override
    public void optionSelected(Button buttonSelected, Option optionType)
    {

        // set current option to new selection
        currentOption = optionType;

        // show down button for button selected.
        // show up button for all other buttons.
        for (Button anOption : options)
        {
            if (anOption == buttonSelected)
            {
                anOption.buttonDown();
            }
            else
            {
                anOption.buttonReleased();
            }
        }

        // pass new controller to model
        model.optionSelected(optionType);
    }

    @Override
    public Option getSelectedOption()
    {
        return currentOption;
    }
}

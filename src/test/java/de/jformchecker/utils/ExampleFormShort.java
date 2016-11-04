package de.jformchecker.utils;

import de.jformchecker.FormCheckerForm;
import de.jformchecker.criteria.Criteria;
import de.jformchecker.elements.CheckboxInput;
import de.jformchecker.elements.NumberInput;
import de.jformchecker.elements.TextInput;

public class ExampleFormShort extends FormCheckerForm {

  public void init() {
    add(TextInput.build("firstname").setDescription("Your Firstname").setPreSetValue("Peter")
        .setRequired());

    add(TextInput.build("lastname").setPlaceholerText("Mustermann").setDescription("Your Lastname")
        .setHelpText("This is an example Helptext for describing this lastname field")
        .setPreSetValue("p") // this
        .setCriterias(Criteria.accept("Pan", "Mustermann")));

    add(CheckboxInput.build("playsPiano").setDescription("Plays Piano"));

    add(NumberInput.build("age").setDescription("Your Age"));

  }
}

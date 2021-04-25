package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Marker extends Component {
    public String name;
    public void set(String name) {
        this.name = name;
    }
}
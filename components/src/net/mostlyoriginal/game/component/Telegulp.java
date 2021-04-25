package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Telegulp extends Component {
    public String destination;
    public String sfx;
    public float roarCooldown = 0;
    public void set( String destination ){
        this.destination=destination;
    }
}

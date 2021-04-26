package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.api.EBag;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.FollowSystem;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.map.EntitySpawnerSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.map.MapSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class CurableSystem extends FluidIteratingSystem {

    public CurableSystem() {
        super(Aspect.all(Pos.class, Curable.class));
    }

    MyAnimRenderSystem myAnimRenderSystem;
    ParticleSystem particleSystem;
    MapSystem mapSystem;

    @Override
    protected void process(E e) {

        E player = entityWithTag("player");

        if ( !player.hasDead() &&  overlaps(player, e) && !player.hasShrunk()) {

            if ( e.curableUnlock() != null ) {
                EBag doors = allEntitiesWith(Door.class);
                boolean doorRemoved=false;
                for (E door : doors) {
                    if ( door != null && door.doorName().equals(e.curableUnlock())) {
                        door.doorKeys(door.doorKeys() - 1);
                        if (door.doorKeys() <= 0) {
                            mapSystem.removeAt(door.posX()+16, door.posY()+16);
                            door.removeDoor();
                            doorRemoved=true;
                        }
                    }
                }
                if ( doorRemoved ) {
                    E.E().playSound("mucus_door_opens");
                }
            }

            E.E().playSound("spray");

            particleSystem.cureSpray(e.posX()+e.boundsCx(), e.posY() + e.boundsCy(),40);
            myAnimRenderSystem.forceAnim(player,"doctor-big-attack");
            e.anim(e.curableCuredAnim());
            e
                    .removeCurable()
                    .removeSpout();
        }

    }

}

package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import net.mostlyoriginal.game.component.Eaten;
import net.mostlyoriginal.game.component.MonsterAnim;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;

/**
 * Going overboard with the systems now but i'm out of tiiiime!
 * @author Daan van Yperen
 */
public class MonsterAnimSystem extends FluidIteratingSystem {

    public MonsterAnimSystem() {
        super(Aspect.all(MonsterAnim.class));
    }

    @Override
    protected void process(E e) {
        if ( e.hasEaten() ) {
            e.anim(e.monsterAnimEaten());
        } else  if ( e.hasSwallowed() ) {
            e.anim(e.monsterAnimSwallowed());
        } else e.anim(e.monsterAnimIdle());
    }
}

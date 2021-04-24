package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.system.physics.SocketSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.PlayerControlled;
import net.mostlyoriginal.game.component.Socket;
import net.mostlyoriginal.game.component.map.WallSensor;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.detection.DialogSystem;
import net.mostlyoriginal.game.system.detection.ParticleSystem;
import net.mostlyoriginal.game.system.map.MapCollisionSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
public class PlayerControlSystem extends FluidIteratingSystem {
    private static final float RUN_SLOW_PACE_FACTOR = 500;
    private static final float RUN_FAST_PACE_FACTOR = 1000;


    private static final int TOP_SPEED = 350;
    private float SPEED_UP = 1200;
    private float BREAKING = SPEED_UP*2f;

    private float JUMP_FACTOR = 24000*2;
    private SocketSystem socketSystem;
    private FollowSystem followSystem;
    private MyAnimRenderSystem animSystem;
    private GameScreenAssetSystem assetSystem;
    private DialogSystem dialogSystem;
    private ParticleSystem particleSystem;
    private Controller controller;

    public PlayerControlSystem() {
        super(Aspect.all(PlayerControlled.class, Physics.class, WallSensor.class, Anim.class));
    }

    @Override
    protected void begin() {
        super.begin();
        controller = Controllers.getCurrent();
    }

    @Override
    protected void process(E e) {


        String playerAnimPrefix = "doctor-";

        // use carry animation group.
        if (e.hasCarries() && e.carriesEntityId() != 0) {
            E carried = E.E(e.carriesEntityId());
            carried.invisible();
            playerAnimPrefix = carried.typeType().equals("battery2") ? "player-red-battery-" : "player-green-battery-";

        }

        {
            E socket = firstTouchingEntityMatching(e, Aspect.all(Socket.class));
            E carried = e.hasCarries() ? E.E(e.carriesEntityId()) : null;

            // battery to put.
            if (socket != null
                    && carried != null
                    && socket.typeType() != null
                    && carried.typeType().equals(socket.typeType())
                    && socket.socketEntityId() == 0) {
                dialogSystem.playerSay(DialogSystem.Dialog.E, 0f, 1f);
            }

            // battery to get.
            if (socket != null && carried == null && socket.socketEntityId() != 0) {
                dialogSystem.playerSay(DialogSystem.Dialog.E, 0f, 1f);
            }
        }

        e.animId(playerAnimPrefix + "idle");
        e.angleRotation(0);
        e.physicsVr(0);



        float dx = 0;
        float dy = 0;

        if (leftPressed() && !e.hasDead()) {
            dx = e.physicsVx() > 0 ? -BREAKING : -SPEED_UP;
            e.animFlippedX(true);
        }
        if (rightPressed(e)) {
            dx = e.physicsVx() < 0 ? BREAKING : SPEED_UP;
            e.animFlippedX(false);
        }

        if ( dx != 0 ) {
                e.physicsVx(e.physicsVx() + (dx * world.delta));
        } else {
            if ( e.physicsVx() > 0 ) {
                e.physicsVx(e.physicsVx() - (BREAKING * world.delta));
                if ( e.physicsVx() <= 0 ) e.physicsVx(0);
            }
            if ( e.physicsVx() < 0 ) {
                e.physicsVx(e.physicsVx() + (BREAKING * world.delta));
                if ( e.physicsVx() >= 0 ) e.physicsVx(0);
            }
        }

        boolean onFloor = e.wallSensorOnFloor() || e.wallSensorOnPlatform() || e.wallSensorNearFloor();
        if (jumpPressed()) {
            if ( onFloor && !e.hasDead()){
                e.physicsVy(JUMP_FACTOR * 0.016f);
                for (int i = 0; i < 4; i++) {
                    particleSystem.dust(e.posX()+e.boundsCx(), e.posY(), 90+20);
                    particleSystem.dust(e.posX()+e.boundsCx(), e.posY(), 90-20);
                }
            }
        }

        if (shrinkPressed()) {
            if ( !e.hasShrunk()) {
                e.posX(e.posX()+16);
                particleSystem.smoke(e.posX()+8, e.posY()+12, 40);
            }
            e.shrunk(true);

        } else {
            if ( e.hasShrunk()) {
                e.posX(e.posX()-16);
                particleSystem.smoke(e.posX()+32, e.posY()+48, 40);
                particleSystem.smoke(e.posX()+32, e.posY()+16, 40);
            }
            e.shrunk(false);
        }

        if (!G.PRODUCTION) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) MapCollisionSystem.DEBUG = !MapCollisionSystem.DEBUG;
        }
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || Gdx.input.isKeyJustPressed(Input.Keys.X)) {
//            if (e.hasCarries()) {
//                E socket = firstTouchingEntityMatching(e, Aspect.all(Socket.class));
//                if (socket != null) {
//                    socketCarried(e, socket);
//                } else {
//                    callRobot(e);
//                    animSystem.forceAnim(e, playerAnimPrefix + "whistles");
//                    //dropCarried(e);
//                }
//            } else {
//                E pickup = firstTouchingEntityMatching(e, Aspect.all(Pickup.class));
//                if (pickup != null) {
//                    carryItem(e, pickup);
//                } else if (onFloor) {
//                    callRobot(e);
//                    animSystem.forceAnim(e, playerAnimPrefix + "whistles");
//                }
//            }
//        }

        if (e.isRunning()) {
            if (dx != 0) {
                e.animId(playerAnimPrefix + "run");
                e.removePriorityAnim();
            }
        } else {
            if (dx != 0) {
                e.animId(playerAnimPrefix + "walk");
                e.removePriorityAnim();
            }
        }

        e.physicsVx(MathUtils.clamp(e.physicsVx(),-TOP_SPEED, TOP_SPEED));


        if (e.hasCarries() && e.carriesEntityId() != 0) {
            e.carriesAnchorX(e.animFlippedX() ? 4 : -4);
        }

        if (Math.abs(e.physicsVy()) > 0.05f) {
            if (e.physicsVy() > 0) {
                e.animId(playerAnimPrefix + "jump");
                if (!e.isJumping()) {
                    e.animLoop(false);
                    e.animAge(0);
                }
                e.jumping();
            } else {
                e.animId(playerAnimPrefix + "fall");
                if (!e.isFalling()) {
                    e.animLoop(false);
                    e.animAge(0);
                }
                e.falling();
            }
        } else {
            if (e.isFlying() || e.isJumping()) {
                e.removeFlying().removeJumping();
            }
            e.animLoop(true);
        }
    }



    private boolean shrinkPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.S) || (controller != null && controller.getButton(controller.getMapping().buttonDpadDown));
    }

    private boolean jumpPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.SPACE) || (controller != null && controller.getButton(controller.getMapping().buttonA));
    }

    private boolean rightPressed(E e) {
        return Gdx.input.isKeyPressed(Input.Keys.D) && !e.hasDead() || (controller != null && controller.getButton(controller.getMapping().buttonDpadRight));
    }

    private boolean leftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.A) || (controller != null && controller.getButton(controller.getMapping().buttonDpadLeft));
    }

    private void socketCarried(E e, E socket) {
        if (e.hasCarries() && socket.socketEntityId() == 0) {
            E battery = E.E(e.getCarries().entityId);
            if (battery.typeType().equals(socket.typeType())) {
                socketSystem.socket(battery, socket);
                e.removeCarries();
            }
        }
    }

    private void dropCarried(E e) {
        if (e.hasCarries()) {
            E.E(e.getCarries().entityId).gravity();
            e.removeCarries();
        }
    }

    private void carryItem(E e, E pickup) {
        if (pickup.hasSocketedInside()) {
            socketSystem.unsocket(pickup);
        }
        e.carriesEntityId(pickup.id());
        e.carriesAnchorY((int) e.boundsMaxy() - 4);
        pickup.removeGravity();
    }

    private void callRobot(E e) {
        followSystem.createMarker(e);
        E.E().playSound("voice1");
    }
}

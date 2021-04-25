package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.system.physics.SocketSystem;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Spout;
import net.mostlyoriginal.game.system.detection.SpoutSystem;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class EntitySpawnerSystem extends BaseSystem {

    private SocketSystem socketSystem;
    private PowerSystem powerSystem;
    private SpoutSystem spoutSystem;

    @Override
    protected void processSystem() {
    }


    public boolean spawnEntity(float x, float y, MapProperties properties, MapProperties extendedProps) {

        final String entity = (String) properties.get("entity");

        switch (entity) {
            case "player":
                assemblePlayer(x, y);
                break;
            case "wormslug":
                assembleWormSlug(x, y, (String) extendedProps.get("to"), (String) extendedProps.get("sfx"));
                break;
            case "anus":
                assembleAnus(x, y, (String) extendedProps.get("to"), (String) extendedProps.get("sfx"));
                break;
            case "nodule_a":
                assembleNodule(x-2, y-32, "nodule-wall-left-infected","nodule-wall-left-healed", 64, 64, extendedProps != null ? (String) extendedProps.get("unlock") : null);
                break;
            case "nodule_c":
                assembleNodule(x-30, y-32, "nodule-wall-right-infected","nodule-wall-right-healed", 64, 64, extendedProps != null ? (String) extendedProps.get("unlock") : null);
                break;
            case "nodule_b":
                assembleNodule(x, y-2, "nodule-floor-infected","nodule-floor-healed", 64, 64, extendedProps != null ? (String) extendedProps.get("unlock") : null);
                break;
            case "marker":
                assembleMarker(x,y, (String) extendedProps.get("name"));
                break;
            case "lock":
                assembleDoor(x,y, (String) extendedProps.get("name"), (Integer) extendedProps.get("keys"));
                break;
            case "glorb":
                assembleGlorb(x, y);
                break;
            case "exit":
                assembleExit(x, y);
                break;
            case "trigger":
                assembleTrigger(x, y, (String) properties.get("trigger"), (String) properties.get("parameter"));
                break;
            case "robot":
                E robot = assembleRobot(x, y);
                if (properties.containsKey("slumbering")) {
                    robot.slumbering();
                } else robot.chargeCharge(3);
                break;
            case "battery":
                assembleBattery(x, y, "battery");
                break;
            case "battery2":
                assembleBattery(x, y, "battery2");
                break;
            case "gremlin":
                spoutSystem.spawnGremlin(0,x,y);
                return true;
            case "birds":
                for (int i = 0, s = MathUtils.random(1, 3); i <= s; i++) {
                    assembleBird(x + MathUtils.random(G.CELL_SIZE), y + MathUtils.random(G.CELL_SIZE));
                }
                return true;
            case "spout":
                assembleSpout(x, y, (Integer) properties.get("angle"), "DRIP");
                return false;
            case "spawner":
                assembleSpout(x, y, (Integer) properties.get("angle"), (String) properties.get("spawns"))
                        .spoutSprayInterval(0.5f).spoutSprayDuration(1);
                return false;
            case "socket":
                assembleBatterySlot(x, y, (Boolean) properties.get("powered"), (String) properties.get("accept"));
                break;
            case "sandsprinkler":
                assembleSandSprinkler(x,y-1);
                return false;
            default:
                return false;
            //throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
        return true;
    }

    private void assembleMarker(float x, float y, String name) {
        E.E().pos(x,y).marker(name).tag(name);
    }

    private void assembleDoor(float x, float y, String name, int keys) {
        E.E().pos(x,y).doorKeys(keys).doorName(name);
    }

    private void assembleNodule(float x, float y, String infectedAnim, String curedAnim, int w, int h, String unlock) {
        E().anim(infectedAnim)
                .pos(x, y)
                .render(G.LAYER_GREMLIN)
                .gravity(0,-20)
                .bounds(0, 0, w, h)
                .curableCuredAnim(curedAnim)
                .curableUnlock(unlock)
                .wallSensor()
                .spoutAngle(-90).spoutType(Spout.Type.DRIP).spoutSprayDuration(0.5f).spoutCooldown(1.1f)
                .teamTeam(G.TEAM_MONSTERS)

                .originX(0.5F);
    }

    private void assembleSandSprinkler(float x, float y) {
        E.E().pos(x,y).bounds(0,0,16,1).sandSprinkler();
    }

    private int birdIndex = 0;

    private void assembleBird(float x, float y) {
        String birdType = "bird-1";
        E().pos(x, y)
                .bounds(0, 0, 2, 2)
                .anim()
                .renderLayer(G.LAYER_BIRDS + birdIndex++)
                .animFlippedX(MathUtils.randomBoolean())
                .birdBrain()
                .birdBrainAnimIdle(birdType + "-idle")
                .birdBrainAnimFlying(birdType + "-flying")
                .gravityY(-0.2f)
                .physics()
                .teamTeam(2)
                .wallSensor();

    }

    private E assembleSpout(float x, float y, Integer angle, String spawns) {
        return E().pos(x, y).bounds(0, 16, 32, 32).spoutAngle(angle).spoutType(Spout.Type.valueOf(spawns)).spoutSprayDuration(0.5f).spoutCooldown(1.1f);
    }

    private void assembleTrigger(float x, float y, String trigger, String parameter) {
        boolean tallTrigger = !trigger.equals("music");
        E().pos(x, y - (tallTrigger ? 5000 : 0)).bounds(0, 0, 16, (tallTrigger ? 10000 : 16)).trigger(trigger).triggerParameter(parameter);
    }

    private void assembleBatterySlot(float x, float y, boolean b, String batteryType) {
        E socket = E();
        socket.anim()
                .pos(x, y)
                .socketAnimSocketed("socket_on_" + batteryType)
                .socketAnimEmpty("socket_off_" + batteryType)
                .type(batteryType)
                .render(G.LAYER_PLAYER - 1)
                .bounds(0, 0, G.CELL_SIZE, G.CELL_SIZE);

        if (b) {
            spawnBatteryInSocket(batteryType, socket);
        } else {
            powerSystem.powerMapCoordsAround((int) (x / G.CELL_SIZE + 0.5f), (int) (y / G.CELL_SIZE + 0.5f), false);
        }


    }

    public void spawnBatteryInSocket(String batteryType, E socket) {
        socketSystem.socket(assembleBattery(socket.posX(), socket.posY(), batteryType), socket);
        powerSystem.powerMapCoordsAround((int) (socket.posX() / G.CELL_SIZE + 0.5f), (int) (socket.posY() / G.CELL_SIZE + 0.5f), true);
    }

    private void assembleWormSlug(float x, float y, String destination, String sfx) {
        E().anim("wormslug-idle")
                .pos(x, y)
                .render(G.LAYER_GREMLIN)
                .gravity(0,-20)
                .bounds(0, 0, 160, 128)
                .telegulp(destination)
                .telegulpSfx(sfx)
                .deadly()
                .wallSensor()
                .teamTeam(G.TEAM_MONSTERS)
                .originX(0.5F);
    }

    private void assembleAnus(float x, float y, String destination, String sfx) {
        E().anim("anus-wall")
                .pos(x, y)
                .render(G.LAYER_GREMLIN)
                .bounds(0, 0, 64, 32)
                .telegulp(destination)
                .telegulpSfx(sfx)
                .wallSensor()
                .originX(0.5F);
    }

    private void assembleGlorb(float x, float y) {
        E().anim("glorb-idle")
                .pos(x, y)
                .render(G.LAYER_GREMLIN)
                .gravity(0,-20)
                .bounds(0, 0, 64, 64)
                .deadly()
                .wallSensor()
                .teamTeam(G.TEAM_MONSTERS)
                .originX(0.5F);
    }


    private void assemblePlayer(float x, float y) {
        E().anim("player-idle")
                .pos(x, y)
                .physics()
                .physicsFriction(0)
                .render(G.LAYER_PLAYER)
                .evictFromWall()
                .mortal()
                .gravity(0,-60)
                .bounds(8, 0, 16, 12)
                .wallSensor()
                .controls()
                .cameraFocus()
                .teamTeam(G.TEAM_PLAYERS)
                .originX(0.5F)
                .footsteps()
                .footstepsCount(8)
                .footstepsSfx("footstep ")
                .tag("player")
                .playerControlled();
    }

    private void assembleExit(float x, float y) {
        E().anim("exit")
                .pos(x, y)
                .render(G.LAYER_PLAYER - 100)
                .exit()
                .bounds(0, 0, 16, 16);
    }

    private E assembleBattery(float x, float y, String batteryType) {
        return E().anim(batteryType)
                .pos(x, y)
                .physics().pickup()
                .type(batteryType)
                .render(G.LAYER_PLAYER - 1)
                .gravity()
                .bounds(-8, -8, 24, 24)
                .wallSensor();
    }

    private E assembleRobot(float x, float y) {
        E robot = E().anim("robot-idle")
                .pos(x, y)
                .physics()
                .charge()
                .socketAnimSocketed(null)
                .socketAnimEmpty(null)
                .socketSfxSocketed("battery_eaten")
                .type("battery2")
                .robot()
                .teamTeam(G.TEAM_PLAYERS)
                .render(G.LAYER_PLAYER_ROBOT)
                .follow()
                .footstepsStepSize(8)
                .footstepsSfx("footsteps_robot")
                .gravity()
                .platform()
                .bounds(0, 0, 39, 43)
                .tag("robot")
                .wallSensor();

        E().anim("robot-idle")
                .tag("robot-charge").pos(x, y).bounds(0, 0, 25, 12);

        return robot;
    }

    public void spawnGremlin(float x, float y) {
        E robot = E().anim("gremlin-1-idle")
                .pos(x, y)
                .physics()
                .mortal()
                .jumpAttack()
                .deadly()
                .render(G.LAYER_GREMLIN)
                .footstepsStepSize(4)
                .gravity()
                .bounds(0, 0, 24, 24)
                .wallSensor();
    }
}

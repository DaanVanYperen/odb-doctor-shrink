package net.mostlyoriginal.game.screen;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.link.EntityLinkManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.SingletonPlugin;
import net.mostlyoriginal.api.manager.FontManager;
import net.mostlyoriginal.api.screen.core.WorldScreen;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.physics.*;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.Telegulp;
import net.mostlyoriginal.game.system.*;
import net.mostlyoriginal.game.system.detection.*;
import net.mostlyoriginal.game.system.map.*;
import net.mostlyoriginal.game.system.render.*;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;
import net.mostlyoriginal.game.system.view.GameScreenSetupSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;

/**
 * Example main game screen.
 *
 * @authorDaan van Yperen
 */
public class GameScreen extends WorldScreen {

    public static final String BACKGROUND_COLOR_HEX = "0000FF";
    private Color BACKGROUND_COLOR= Color.valueOf("031D1E");

    @Override
    protected World createWorld() {
        RenderBatchingSystem renderBatchingSystem;
        return new World(new WorldConfigurationBuilder()
                .dependsOn(EntityLinkManager.class, OperationsPlugin.class, SingletonPlugin.class)
                .with(
                        new SuperMapper(),
                        new TagManager(),
                        new FontManager(),
                        new PulsatingFramebufferManager(),

                        new EntitySpawnerSystem(),
                        new MapSystem(),
                        new ParticleSystem(),
                        new PowerSystem(),

                        new GameScreenAssetSystem(),
                        new GameScreenSetupSystem(),

                        new CameraSystem(G.CAMERA_ZOOM),

                        // sensors.
                        new WallSensorSystem(),
                        new CollisionSystem(),

                        // spawn
                        new TriggerSystem(),
                        new FarewellSystem(),
                        new SpoutSystem(),

                        // Control and logic.
                        //new FollowSystem(),
                        new PlayerControlSystem(),
                        new PlayerAnimSystem(),
                        new BirdBrainSystem(),

                        // Physics.
                        new GravitySystem(),
                        //new MapCollisionSystem(),
                        new WallEvictionSystem(),
                        new MapCollisionSystem2(),
                        new PlatformCollisionSystem(),
                        new PhysicsSystem(),

                        // Interactions
                        new DeathSystem(),
                        new CurableSystem(),
                        new SwallowSystem(),

                        // Effects.
                        new FootstepSystem(),
                        new CarriedSystem(),
                        new SocketSystem(),

                        // Camera.
                        new CameraFollowSystem(),
                        //new CameraShakeSystem(),
                        new CameraClampToMapSystem(),
                        new PriorityAnimSystem(),

                        new JumpAttackSystem(),

                        new ClearScreenSystem(BACKGROUND_COLOR),
                        new MapRenderSystem(),

                        renderBatchingSystem = new RenderBatchingSystem(),
                        new MyAnimRenderSystem(renderBatchingSystem),
                        new MapRenderInFrontSystem(),
                        new TerminalSystem(),
                        new ExitSystem(),
                        new DialogSystem(),
                        new SoundPlaySystem( new String[]{
                                "jump",
                                "spray",
                                "mucus_door_opens",
                                "snail_roar",
                                "fart",
                                "shrink_down",
                                "shrink_up",
                                "jump_landing",
                                "footstep 1",
                                "footstep 2",
                                "footstep 3",
                                "footstep 4",
                                "footstep 5",
                                "footstep 6",
                                "footstep 7",
                                "footstep 8",
                                "MOWV", //
                                "voice1",
                                "VWOM",
                                "deathsound", // done
                                "battery_eaten", // robot gets battery
                                "deepsound", //
                                "door_break", //
                                "door_openclose", // electrical doors.
                                "footsteps_girl",
                                "footsteps_robot",
                                "landing_girl",
                                "boss_sound_1",
                                "poop_pipe",
                                "robot_attack",
                                "death_jingle",
                                "robot_fly",
                                "gremlin_death",
                                "splat1",
                                "splat2",
                                "splat3",
                                "splat4",
                        }),
                        new TransitionSystem(GdxArtemisGame.getInstance())
                ).build());
    }

}

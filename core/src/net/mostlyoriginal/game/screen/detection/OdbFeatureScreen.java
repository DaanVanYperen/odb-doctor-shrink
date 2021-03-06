package net.mostlyoriginal.game.screen.detection;

import com.artemis.SuperMapper;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import net.mostlyoriginal.api.SingletonPlugin;
import net.mostlyoriginal.api.screen.core.WorldScreen;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.api.system.render.AnimRenderSystem;
import net.mostlyoriginal.api.system.render.ClearScreenSystem;
import net.mostlyoriginal.game.GdxArtemisGame;
import net.mostlyoriginal.game.system.detection.OdbFeatureDetectionSystem;
import net.mostlyoriginal.game.system.render.MyAnimRenderSystem2;
import net.mostlyoriginal.game.system.render.TransitionSystem;
import net.mostlyoriginal.game.system.view.FeatureScreenAssetSystem;
import net.mostlyoriginal.game.system.view.FeatureScreenSetupSystem;
import net.mostlyoriginal.plugin.OperationsPlugin;

/**
 * Intro screen that also shows all enabled artemis-odb features for a couple of seconds.
 *
 * @author Daan van Yperen
 */
public class OdbFeatureScreen extends WorldScreen {

	protected World createWorld() {

		final RenderBatchingSystem renderBatchingSystem;



		return new World(new WorldConfigurationBuilder()
				.dependsOn(OperationsPlugin.class, SingletonPlugin.class)
				.with(WorldConfigurationBuilder.Priority.HIGH,
						// supportive
						new SuperMapper(),
						new TagManager(),
						new CameraSystem(2),
						new FeatureScreenAssetSystem(),
						new ButtonPressSystem()
				).with(WorldConfigurationBuilder.Priority.LOW,
						// processing
						new TransitionSystem(GdxArtemisGame.getInstance()),
						// animation
						new ClearScreenSystem(Color.valueOf("422433")),
						renderBatchingSystem = new RenderBatchingSystem(),
						new MyAnimRenderSystem2(renderBatchingSystem),
						new FeatureScreenSetupSystem()
				).build());
	}

}

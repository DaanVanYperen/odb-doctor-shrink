package net.mostlyoriginal.game.system.detection;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Deadly;
import net.mostlyoriginal.game.component.G;
import net.mostlyoriginal.game.component.SandSprinkler;
import net.mostlyoriginal.game.system.common.FluidIteratingSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.badlogic.gdx.math.MathUtils.random;
import static net.mostlyoriginal.api.operation.OperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class ParticleSystem extends FluidIteratingSystem {

    private Color BLOOD_COLOR = Color.valueOf("b4202a");
    private Color BLOOD_COLOR2 = Color.valueOf("df3e23");



    private Color COLOR_WHITE = Color.valueOf("FFFFFF");
    public Color COLOR_DUST = Color.valueOf("f5a097");
    public Color COLOR_DUST_BLACK = Color.valueOf("666666");
    public Color COLOR_DUST_BLACK2 = Color.valueOf("888888");
    public Color COLOR_DUST_RED = Color.valueOf("f5c067");
    private Color COLOR_ACID = Color.valueOf("5F411CDD");
    private Color COLOR_SAND = Color.valueOf("D4CFB866");

    private Builder bakery = new Builder();
    private GameScreenAssetSystem assetSystem;
    private CameraSystem cameraSystem;

    public ParticleSystem() {
        super(Aspect.all(SandSprinkler.class));
    }


    public float cooldown = 0;

    public void sprinkleSand(int percentageChance) {
        for (E e : allEntitiesWith(SandSprinkler.class)) {
            if (MathUtils.random(0, 100f) <= percentageChance) {
                triggerSprinkler(e);
            }
        }
    }

    private void triggerSprinkler(E e) {
        for (int i = 0; i < MathUtils.random(1, 2); i++) {
            sand(e.posX() + MathUtils.random(0, e.boundsMaxx()), e.posY(), -90 + MathUtils.random(-2, 2), MathUtils.random(10, 40));
        }
    }


    public void floorDroplet(float x, float y, float angle, Color colorA, Color colorB) {
        bakery
                .color(MathUtils.random(0,100) > 25f ? colorA : colorB)
                .dropletSplatDown()
                .at(x, y)
                .angle(angle, angle)
                .speed(60, 80)
                .fadeAfter(0.4f)
                .rotateRandomly()
                .solid()
                .size(1, 3)
                .friction(1)
                .layer(MathUtils.randomBoolean() ? G.LAYER_PARTICLES : G.LAYER_PLAYER+10000)
                .create(1, 3);
    }

    public void sand(float x, float y, float angle, int force) {
        bakery
                .color(COLOR_SAND)
                .at(x, y)
                .angle(angle, angle)
                .speed(force, force + 5)
                .fadeAfter(1f + MathUtils.random(0f, 2f))
                .rotateRandomly()
                .size(1f, 2f)
                .solid()
                .create(1, 1);
    }


    public void acid(float x, float y, float angle, int force) {
        bakery
                .color(COLOR_ACID)
                .at(x, y)
                .angle(angle, angle)
                .speed(force, force + 5)
                .deadly()
                .fadeAfter(4f)
                .slowlySplatDown()
                .rotateRandomly()
                .size(1, 2)
                .solid()
                .create(1, 5);
    }

    public void smoke(float x, float y, int count) {
        //E.E().playSound("splat" + MathUtils.random(1, 4));
        bakery
                .color(new Color(1, 1, 1,0.5f))
                .at(x-5, y-5)
                .angle(0, 360)
                .speed(50, 80)
                .slowlySplatDown()
                .fadeAfter(0.2f)
                .size(5, 10)
                .rotateRandomly()
                .layer(G.LAYER_PLAYER+10000)
                .create(count);
    }

    public void cureSpray(float x, float y, int count) {
        //E.E().playSound("splat" + MathUtils.random(1, 4));
        bakery
                .color(SpoutSystem.DROP_COLOR_1)
                .at(x-5, y-5)
                .angle(0, 360)
                .speed(50, 80)
                .dropletSplatDown()
                .fadeAfter(0.4f)
                .size(5, 10)
                .solid()
                .rotateRandomly()
                .friction(1)
                .layer(G.LAYER_PLAYER+10000)
                .create(count/2);
        bakery
                .color(SpoutSystem.DROP_COLOR_2)
                .at(x-5, y-5)
                .angle(0, 360)
                .speed(50, 80)
                .dropletSplatDown()
                .fadeAfter(0.4f)
                .size(5, 10)
                .solid()
                .rotateRandomly()
                .friction(1)
                .layer(G.LAYER_PLAYER+10000)
                .create(count/2);
    }

    public void bloodExplosion(float x, float y) {
        E.E().playSound("splat" + MathUtils.random(1, 4));
        bakery
                .color(BLOOD_COLOR2)
                .at(x, y)
                .angle(-180, 180)
                .speed(140, 230)
                .dropletSplatDown()
                .rotateRandomly()
                .fadeAfter(1.2f)
                .size(3, 5)
                .solid()
                .friction(1f)
                .create(60);
        bakery
                .color(BLOOD_COLOR)
                .at(x, y)
                .angle(-180, 180)
                .speed(140, 230)
                .dropletSplatDown()
                .rotateRandomly()
                .fadeAfter(1.2f)
                .size(3, 5)
                .solid()
                .friction(1f)
                .create(60);
    }


    public void bloodSpray(float x, float y, int count) {
        //E.E().playSound("splat" + MathUtils.random(1, 4));
        //E.E().playSound("splat" + MathUtils.random(1, 4));
        bakery
                .color(BLOOD_COLOR)
                .at(x-3, y-3)
                .angle(-180, 180)
                .speed(120, 140)
                .dropletSplatDown()
                .fadeAfter(0.7f)
                .size(3, 5)
                .solid()
                .rotateRandomly()
                .friction(0.9f)
                .layer(G.LAYER_PARTICLES)
                .create(count/2);
        bakery
                .color(BLOOD_COLOR2)
                .at(x-3, y-3)
                .angle(-180, 180)
                .speed(120, 140)
                .dropletSplatDown()
                .fadeAfter(0.7f)
                .size(3, 5)
                .solid()
                .rotateRandomly()
                .friction(0.9f)
                .layer(G.LAYER_PARTICLES)
                .create(count/2);
    }

    Vector2 v2 = new Vector2();

    public E spawnVanillaParticle(float x, float y, float angle, float speed, float scale, int layer, float friction) {

        v2.set(speed, 0).setAngle(angle);

        return E.E()
                .pos(x - scale * 0.5f, y - scale * 0.5f)
                .anim("particle")
                .scale(scale)
                .renderLayer(layer)
                .origin(scale / 2f, scale / 2f)
                .bounds(0, 0, scale, scale)
                .physicsVx(v2.x)
                .physicsVy(v2.y)
                .physicsFriction(friction);
    }

    @Override
    protected void process(E e) {
    }

    @Override
    protected void begin() {
        cooldown -= world.delta;
        if (cooldown <= 0) {
            sprinkleSand(1);
            cooldown = 1f;
        }
    }

    float cooldown2 = 0;

    public void gremlinWave() {
        float borderX = cameraSystem.camera.position.x - (Gdx.graphics.getWidth() / G.CAMERA_ZOOM) * 0.5f - 24;
        float borderY1 = cameraSystem.camera.position.y - (Gdx.graphics.getHeight() / G.CAMERA_ZOOM) * 0.5f;
        float borderY2 = cameraSystem.camera.position.y + (Gdx.graphics.getHeight() / G.CAMERA_ZOOM) * 0.5f;

        cooldown2 -= world.delta;
        if (cooldown2 <= 0) {
            cooldown2 = 0.32f;
            for (int i = 0, s = 10; i < s; i++) {
                float y = MathUtils.random(borderY1, borderY2);
                E.E()
                        .pos(borderX, y)
                        .anim("gremlin-1-idle")
                        .renderLayer(G.LAYER_PARTICLES)
                        .physicsVr(MathUtils.random(-90f, 90f))
                        .origin(0.5f, 0.5f)
                        .angle()
                        .scale(MathUtils.random(1f,2f))
                        .bounds(0, 0, 24, 24)
                        .physicsVx(MathUtils.random(100f, 300f))
                        .physicsVy(MathUtils.random(40f, 200f))
                        .gravityY(-5f)
                        .physicsFriction(0)
                        .script(sequence(
                                delay(0.5f),
                                JamOperationFactory.tintBetween(Tint.WHITE, Tint.TRANSPARENT, 0.5f),
                                deleteFromWorld()
                        ));
            }
        }

    }

    private class Builder {
        private Color color;
        private boolean withGravity;
        private int minX;
        private int maxX;
        private int minY;
        private int maxY;
        private float minAngle;
        private float maxAngle;
        private int minSpeed;
        private int maxSpeed;
        private float minScale;
        private float maxScale;
        private boolean withSolid;
        private float gravityY;
        private float fadeDelay;

        private Tint tmpFrom = new Tint();
        private Tint tmpTo = new Tint();
        private float rotateR = 0;
        private boolean withDeadly;
        private int layer=G.LAYER_PARTICLES;
        private float friction=0;

        public Builder() {
            reset();
        }

        Builder color(Color color) {
            this.color = color;
            return this;
        }

        void create(int count) {
            create(count, count);
        }

        void create(int minCount, int maxCount) {
            for (int i = 0, s = random(minCount, maxCount); i < s; i++) {
                final E e = spawnVanillaParticle(
                        random(minX, maxX),
                        random(minY, maxY),
                        random(minAngle, maxAngle),
                        random(minSpeed, maxSpeed),
                        random(minScale, maxScale), layer, friction)
                        .tint(color.r, color.g, color.b, color.a);

                if (withGravity) {
                    e.gravity();
                    e.gravityY(gravityY);
                }
                if (withSolid) {
                    e.mapWallSensor();
                } else e.ethereal();
                if (withDeadly) {
                    e.deadly();
                }
                if (rotateR != 0) {
                    e.physicsVr(rotateR).angle();
                }
                if (fadeDelay > 0) {
                    e.script(sequence(
                            delay(fadeDelay),
                            remove(Deadly.class),
                            JamOperationFactory.tintBetween(tmpFrom, tmpTo, 0.5f),
                            deleteFromWorld()
                    ));
                }
            }
            reset();
        }

        Builder slowlySplatDown() {
            this.withGravity = true;
            this.gravityY = -0.5f;
            return this;
        }

        Builder dropletSplatDown() {
            this.withGravity = true;
            this.gravityY = -4f;
            return this;
        }

        private void reset() {
            color = COLOR_WHITE;
            withGravity = false;
            minX = 0;
            maxX = 0;
            minY = 0;
            maxY = 0;
            minAngle = 0;
            maxAngle = 0;
            minSpeed = 0;
            maxSpeed = 0;
            withDeadly = false;
            minScale = 1;
            maxScale = 1;
            gravityY = 1;
            fadeDelay = -1;
            withSolid = false;
            rotateR = 0;
        }

        public Builder layer(int layer ){
            this.layer = layer;
            return this;
        }

        public Builder angle(float minAngle, float maxAngle) {
            this.minAngle = minAngle;
            this.maxAngle = maxAngle;
            return this;
        }

        public Builder angle(int angle) {
            this.minAngle = angle;
            this.maxAngle = angle;
            return this;
        }

        public Builder speed(int minSpeed, int maxSpeed) {
            this.minSpeed = minSpeed;
            this.maxSpeed = maxSpeed;
            return this;
        }

        public Builder speed(int speed) {
            this.minSpeed = speed;
            this.maxSpeed = speed;
            return this;
        }

        public Builder size(float minScale, float maxScale) {
            this.minScale = minScale;
            this.maxScale = maxScale;
            return this;
        }

        public Builder size(int size) {
            this.minScale = size;
            this.maxScale = size;
            return this;
        }

        public Builder solid() {
            withSolid = true;
            return this;
        }

        public Builder friction(float friction ) {
            this.friction = friction;
            return this;
        }

        public Builder at(float x, float y) {
            minX = maxX = (int) x;
            minY = maxY = (int) y;
            return this;
        }

        public Builder fadeAfter(float delay) {
            this.fadeDelay = delay;
            tmpFrom = new Tint(color);
            tmpTo = new Tint(color);
            tmpTo.color.a = 0;
            return this;
        }

        public Builder rotateRandomly() {
            rotateR = MathUtils.random(-100f, 100f);
            return this;
        }

        public Builder deadly() {
            withDeadly = true;
            return this;
        }
    }

}

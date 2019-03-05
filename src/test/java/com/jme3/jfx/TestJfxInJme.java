package com.jme3.jfx;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.jfx.injme.JmeFxContainer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import lombok.var;
import org.jetbrains.annotations.NotNull;

/**
 * The test to show how to integrate JavaFX scene in jME Application
 *
 * @author JavaSaBr
 */
public class TestJfxInJme extends SimpleApplication {

    public static void main(@NotNull String[] args) {

        var settings = new AppSettings(true);
        settings.setResizable(true);

        var application = new TestJfxInJme();
        application.setSettings(settings);
        application.setShowSettings(false);
        application.start();
    }

    private JmeFxContainer container;
    private Geometry player;
    private Boolean isRunning;

    public TestJfxInJme() {
        isRunning = true;
    }

    @Override
    public void simpleInitApp() {
        container = JmeFxContainer.install(this, getGuiNode());

        // building javaFX UI
        {
            var button = new Button("BUTTON");
            var rootNode = new Group(button);
            var scene = new Scene(rootNode, 600, 600);
            scene.setFill(Color.TRANSPARENT);

            container.setScene(scene, rootNode);
        }

        // building jME part
        {

            var material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setColor("Color", ColorRGBA.Blue);

            var box = new Box(1, 1, 1);
            player = new Geometry("Player", box);
            player.setMaterial(material);

            rootNode.attachChild(player);

            // load my custom keybinding
            initKeys();
        }

        getFlyByCamera().setEnabled(false);
        getInputManager().setCursorVisible(true);
    }

    @Override
    public void simpleUpdate(final float tpf) {
        super.simpleUpdate(tpf);
        // we decide here that we need to do transferring the last frame from javaFX to jME
        if (container.isNeedWriteToJme()) {
            container.writeToJme();
        }
    }

    /** Custom Keybinding: Map named actions to inputs. */
    private void initKeys() {
        /** You can map one or several inputs to one named mapping. */
        inputManager.addMapping("Pause", new KeyTrigger(keyInput.KEY_P));
        inputManager.addMapping("Mouse", new KeyTrigger(keyInput.KEY_M));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE), // spacebar!
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));        // left click!
        /** Add the named mappings to the action listeners. */
        inputManager.addListener(actionListener, "Pause", "Mouse");
        inputManager.addListener(analogListener, "Left", "Right", "Rotate");
    }

    /** Use this listener for KeyDown/KeyUp events */
    private ActionListener actionListener = (name, keyPressed, tpf) -> {
        if (name.equals("Pause") && !keyPressed) {
            isRunning = !isRunning;
        } else if (name.equals("Mouse") && !keyPressed) {
            getInputManager().setCursorVisible(!getInputManager().isCursorVisible());
        }
    };

    /** Use this listener for continuous events */
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (isRunning) {
                if (name.equals("Rotate")) {
                    player.rotate(0, value, 0);
                }
                if (name.equals("Right")) {
                    player.move((new Vector3f(value, 0, 0)));
                }
                if (name.equals("Left")) {
                    player.move(new Vector3f(-value, 0, 0));
                }
            } else {
                System.out.println("Press P to unpause.");
            }
        }
    };
}

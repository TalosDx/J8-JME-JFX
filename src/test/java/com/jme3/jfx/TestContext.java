package com.jme3.jfx;

import com.jme3.system.AppSettings;
import com.jme3.system.NativeLibraryLoader;
import com.jme3.system.lwjgl.LwjglDisplay;

import com.ss.rlib.common.logging.Logger;
import com.ss.rlib.common.logging.LoggerManager;

/**
 * Created by ronn on 25.07.16.
 */
public class TestContext  extends LwjglDisplay {

    protected static final Logger LOGGER = LoggerManager.getLogger(TestContext.class);

    /**
     * Game thread of render screen
     */
    private Thread thread;

    @Override
    public void create(final boolean waitFor) {

        if ("LWJGL".equals(settings.getAudioRenderer())) {
            NativeLibraryLoader.loadNativeLibrary("openal-lwjgl3", true);
        }

        NativeLibraryLoader.loadNativeLibrary("lwjgl3", true);
        NativeLibraryLoader.loadNativeLibrary("glfw-lwjgl3", true);
        NativeLibraryLoader.loadNativeLibrary("jemalloc-lwjgl3", true);
        NativeLibraryLoader.loadNativeLibrary("jinput", true);
        NativeLibraryLoader.loadNativeLibrary("jinput-dx8", true);

        if (created.get()) return;

        thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setName("LWJGL Renderer Thread");
        thread.start();

        if (waitFor) waitFor(true);
    }

    public Thread getThread() {
        return thread;
    }

    @Override
    protected void createContext(final AppSettings settings) {
        settings.setRenderer(AppSettings.LWJGL_OPENGL3);
        super.createContext(settings);
    }
}

JME3-JFX
========

JFX Gui bridge for JME with usefull utilities for common usecases

License is the New BSD License (same as JME3) 
http://opensource.org/licenses/BSD-3-Clause

##### java compiler options
```bash
--add-exports javafx.graphics/com.sun.javafx.embed=ALL-UNNAMED 
--add-exports javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED 
--add-exports javafx.graphics/com.sun.javafx.cursor=ALL-UNNAMED 
--add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED 
--add-exports javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
```

#### How to add the library to your project

```bash
git clone https://github.com/TalosDx/JME3-JFX.git
./gradlew build
```

```groovy
dependencies {
    compile 'com.jme3:jfx:jre8-2.0.0'
}
```
    
#### Maven

work on it

#### How to integrate jME application to JavaFX ImageView:

```java

    var imageView = new ImageView();
        
    var settings = JmeToJfxIntegrator.prepareSettings(new AppSettings(true), 60);
    var application = new MySomeApplication();
    
    JmeToJfxIntegrator.startAndBindMainViewPort(application, imageView, Thread::new);
```

#### How to integrate javaFX UI to jME application:

```java

    public class MyApplication extends SimpleApplication {
    
        private JmeFxContainer container;
        
        @Override
        public void simpleInitApp() {
            container = JmeFxContainer.install(this, getGuiNode());
    
            var button = new Button("BUTTON");
            var rootNode = new Group(button);
            var scene = new Scene(rootNode, 600, 600);
            scene.setFill(Color.TRANSPARENT);

            container.setScene(scene, rootNode);
            
            getInputManager().setCursorVisible(true);
        }
    
        @Override
        public void simpleUpdate(float tpf) {
            super.simpleUpdate(tpf);
            // we decide here that we need to do transferring the last frame from javaFX to jME
            if (container.isNeedWriteToJme()) {
                container.writeToJme();
            }
        }
    }
```

Also, you can look at some examples in the tests package:

* [jME Application is inside jFX Canvas](https://github.com/JavaSaBr/JME3-JFX/blob/master/src/test/java/com/jme3x/jfx/TestJmeToJFXCanvas.java)
* [jME Application is inside jFX ImageView](https://github.com/JavaSaBr/JME3-JFX/blob/master/src/test/java/com/jme3x/jfx/TestJmeToJFXImageView.java)
* [JavaFX Scene is inside jME Application](https://github.com/JavaSaBr/JME3-JFX/blob/master/src/test/java/com/jme3x/jfx/TestJavaFxInJme.java)

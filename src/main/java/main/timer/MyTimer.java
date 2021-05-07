package main.timer;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

public abstract class MyTimer extends AnimationTimer {

    protected ReadOnlyStringWrapper text = new ReadOnlyStringWrapper(this,
            "text", "Frame count: 0 Average frame interval: N/A");

    protected long firstTime = 0;

    protected long lastTime = 0;

    protected long accumulatedTime = 0;

    protected int frames = 0;

    @Override
    public void handle(long now) {
        if ( lastTime > 0 ) {
            long elapsedTime = now - lastTime;
            accumulatedTime += elapsedTime;
            update(elapsedTime / 1000000000.0f);
        } else {
            firstTime = now;
        }
        lastTime = now;

        if ( accumulatedTime >= 1000000000L ) {
            accumulatedTime -= 1000000000L;
            text.set(String.format("FPS: %,d", frames));
            frames = 0;
        }
        // Aquí es donde se debería de imprimir la pantalla
        frames++;
    }

    protected void update(float elapsedTime) {
        // UPDATE THINGS...
    }

    public ReadOnlyStringProperty getFpsText() {
        return text.getReadOnlyProperty();
    }

}

package utilites;

import java.util.function.Consumer;

import javafx.animation.AnimationTimer;

public class AnimationTimerFactory {
	public AnimationTimer CreateTimer(float timeStep, Consumer<Float> action){
		return new AnimationTimer() {
			private long last = -1;

			@Override
			public void handle(long now) {
				if (last >= 0) {
					long milliseconds = (now - last) / 1_000_000;
					if (milliseconds > timeStep) {
						action.accept(timeStep);
						last = now;
					}
				} else {
					last = now;
				}
			}
		};
	}
}

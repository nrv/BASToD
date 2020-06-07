/*
 * Copyright 2012, 2020 Nicolas HERVE
 *
 * This file is part of BASToD.
 *
 * BASToD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BASToD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BASToD. If not, see <http://www.gnu.org/licenses/>.
 */
package name.herve.game.engine.simu;

import java.text.DecimalFormat;

import com.esotericsoftware.minlog.Log;

import name.herve.game.tools.Constants;

/**
 * @author Nicolas HERVE - n.herve@laposte.net
 */
public class SimulationThread extends Thread {
	private long currentTick;
	private int ticksPerSesond;
	private Simulated simulated;

	public SimulationThread(int ticksPerSesond, Simulated simulated) {
		super();
		this.ticksPerSesond = ticksPerSesond;
		this.simulated = simulated;
	}

	@Override
	public void run() {
		Log.debug("simulation", "Thread started");
		long tickDurationNano = Constants.NANO / ticksPerSesond;
		long tickDurationMilli = tickDurationNano / Constants.NANO_MILLI;

		long afterStepTimeNano = 0;
		long beforeStepTimeNano = 0;
		long lastTickDurationNano = 0;
		long sleepAccumulatorNano = 0;

		DecimalFormat df = new DecimalFormat("0.00");

		while (!simulated.isGameStarted()) {
			sleepMilli(tickDurationMilli);
		}

		Log.debug("simulation", "Simulation started");
		currentTick = 0;

		long startTimeNano = System.nanoTime();
		long lastDisplayTimeNano = startTimeNano;
		long lastDisplayNbTicks = currentTick;

		afterStepTimeNano = startTimeNano;
		while (simulated.isGameStarted()) {
			beforeStepTimeNano = System.nanoTime();
			simulated.step(currentTick, beforeStepTimeNano - afterStepTimeNano);
			afterStepTimeNano = System.nanoTime();

			lastTickDurationNano = afterStepTimeNano - beforeStepTimeNano;
			sleepAccumulatorNano += (tickDurationNano - lastTickDurationNano);

			long sleepTimeMilli = sleepAccumulatorNano / Constants.NANO_MILLI;
			if (sleepTimeMilli >= 50) {
				sleepMilli(sleepTimeMilli);
				sleepAccumulatorNano -= (sleepTimeMilli * Constants.NANO_MILLI);
			}

			if (Log.DEBUG && (currentTick % 25) == 0) {
				long currentTimeNano = System.nanoTime();
				float simulationDurationSeconds = (float) (currentTimeNano - startTimeNano) / (float) Constants.NANO;
				float tps = (currentTick - lastDisplayNbTicks) / ((currentTimeNano - lastDisplayTimeNano) / (float) Constants.NANO);
				float gtps = currentTick / ((currentTimeNano - startTimeNano) / (float) Constants.NANO);
				Log.debug("simulation", "Time : " + df.format(simulationDurationSeconds) + " s - " + currentTick + " t - " + df.format(tps) + " / " + df.format(gtps) + " TPS");
				lastDisplayTimeNano = currentTimeNano;
				lastDisplayNbTicks = currentTick;
			}

			currentTick++;
		}
		Log.debug("simulation", "Thread ended");
	}

	private void sleepMilli(long ms) {
		try {
			sleep(ms);
		} catch (InterruptedException e) {
			// ignore
		}
	}
}

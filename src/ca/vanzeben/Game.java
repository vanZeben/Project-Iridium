package ca.vanzeben;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

  private static Game           _instance;
  private static final String   TITLE       = "Project Iridium";
  private static final int      WIDTH       = 650;
  private static final int      HEIGHT      = WIDTH * 3 / 4;
  public static final Dimension SIZE        = new Dimension(WIDTH, HEIGHT);
  private static final int      UPDATE_RATE = 30;
  private static final int      RENDER_RATE = 60;

  private JFrame                _frame;
  private Thread                _thread;
  private boolean               _running    = false;

  private int                   _tps        = 0;
  private int                   _fps        = 0;
  private int                   _totalTicks = 0;

  public Game() {
    _instance = this;

    setPreferredSize(SIZE);
    setMinimumSize(SIZE);
    setMaximumSize(SIZE);

    _frame = new JFrame(TITLE);
    _frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    _frame.setLayout(new BorderLayout());
    _frame.add(_instance, BorderLayout.CENTER);
    _frame.pack();

    _frame.setResizable(false);
    _frame.setLocationRelativeTo(null);
    _frame.setVisible(true);
  }

  public synchronized void start() {
    _running = true;
    _thread = new Thread(this, TITLE + "_main");
    _thread.start();
  }

  public synchronized void stop() {
    _running = false;
    if (_thread != null) {
      try {
        _thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void run() {
    double lastUpdateTime = System.nanoTime();
    double lastRenderTime = lastUpdateTime;

    final int ns = 1000000000;
    final double nsPerUpdate = (double) ns / UPDATE_RATE;
    final double nsPerRender = (double) ns / RENDER_RATE;
    final int maxUpdatesBeforeRender = 5;

    int lastSecond = (int) (lastUpdateTime / ns);
    int tickCount = 0;
    int renderCount = 0;
    while (_running) {

      long currTime = System.nanoTime();
      int tps = 0;

      while ((currTime - lastUpdateTime) > nsPerUpdate && tps < maxUpdatesBeforeRender) {
        update();
        tickCount++;
        _totalTicks++;
        tps++;
        lastUpdateTime += nsPerUpdate;
      }

      if (currTime - lastUpdateTime > nsPerUpdate) {
        lastUpdateTime = currTime - nsPerUpdate;
      }

      float interpolation = Math.min(1.0F, (float) ((currTime - lastUpdateTime) / nsPerUpdate));
      render(interpolation);
      renderCount++;
      lastRenderTime = currTime;

      int currSecond = (int) (lastUpdateTime / ns);
      if (currSecond > lastSecond) {
        _tps = tickCount;
        _fps = renderCount;
        tickCount = 0;
        renderCount = 0;
        lastSecond = currSecond;
        System.out.println(_tps + " TPS " + _fps + " FPS");
      }

      while (currTime - lastRenderTime < nsPerRender && currTime - lastUpdateTime < nsPerUpdate) {
        Thread.yield();
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        currTime = System.nanoTime();
      }
    }
  }

  public void update() {

  }

  public void render(float interpolation) {

  }
}
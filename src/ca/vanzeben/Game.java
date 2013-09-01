package ca.vanzeben;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

  private static Game           _instance;
  private static final String   TITLE    = "Project Iridium";
  private static final int      WIDTH    = 650;
  private static final int      HEIGHT   = WIDTH * 3 / 4;
  public static final Dimension SIZE     = new Dimension(WIDTH, HEIGHT);

  private JFrame                _frame;
  private Thread                _thread;
  private boolean               _running = false;

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
  }
}

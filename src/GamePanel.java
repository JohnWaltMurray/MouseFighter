import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import javax.swing.*;

public class GamePanel extends JFrame {
	
	public GamePanel() {
		super("Mouse Fighter");
		GameCanvas canvas = new GameCanvas();
		add(canvas, BorderLayout.CENTER);
		
		setBounds(700, 300, 500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		
		canvas.createBufferStrategy(2);
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GamePanel();
			}
		});
	}
}

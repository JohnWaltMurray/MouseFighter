import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameRate implements ActionListener {

	GameCanvas canvas;
	
	FrameRate(GameCanvas canvas) {
		this.canvas = canvas;
	}
	
	public void actionPerformed(ActionEvent e) {
		canvas.UpdateCanvas();
	}
}

import model.BD;
import view.GuiLogin;

public class Main {

	public static void main(String[] args) {

	    BD bd = new BD();

	    if (bd.connect()) {
	        
	        java.awt.EventQueue.invokeLater(() -> {
	            new GuiLogin().setVisible(true);
	        });
	    }

	}

}

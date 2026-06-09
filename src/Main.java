import model.BD;
import script.BancoSetup;
import view.GuiLogin;

public class Main {

	public static void main(String[] args) {

	    BD bd = new BD();

	    if (bd.connect()) {

	        BancoSetup setup = new BancoSetup(bd.getConnection());

	        setup.inicializar();
	        
	        java.awt.EventQueue.invokeLater(() -> {
	            new GuiLogin(bd).setVisible(true);
	        });
	    }

	}

}

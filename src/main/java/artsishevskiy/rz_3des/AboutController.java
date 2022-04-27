package artsishevskiy.rz_3des;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ButtonOK;

    @FXML
    void OnActionButtonOK(ActionEvent event) {
        Stage st = (Stage) ButtonOK.getScene().getWindow();
        st.close();
    }

    @FXML
    void initialize() {
        assert ButtonOK != null : "fx:id=\"ButtonOK\" was not injected: check your FXML file 'About.fxml'.";

    }
}
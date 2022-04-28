package artsishevskiy.rz_3des;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainController {

    Cryptoanalysis analyzer;
    Stage stageAbout;

    // конструируем вывод вариантов расшифровок
    private String MakeOutput(ArrayList<String> vars) throws NoSuchAlgorithmException {
        StringBuilder output = new StringBuilder("");
        MessageDigest md = MessageDigest.getInstance("md5");

        vars.forEach(var -> {
            output.append("Key: ");
            output.append(var);
            output.append("---> Decrypted: ");

            byte[] digestOfPassword = md.digest(var.getBytes(StandardCharsets.UTF_8));

            try {
                output.append(TripleDesBouncyCastle.decode(analyzer.GetCryptedText(), digestOfPassword));
            } catch (IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException
                    | NoSuchProviderException | NoSuchAlgorithmException | BadPaddingException e) {
                e.printStackTrace();
                output.append("Exeption in decrypt");
            }

            output.append("\n");
        });

        return new String(output);
    }

    // конструируем вывод вариантов расшифровок без исключений
    private String MakeOutputNoExeption(ArrayList<String> vars) throws NoSuchAlgorithmException {
        StringBuilder output = new StringBuilder("");
        MessageDigest md = MessageDigest.getInstance("md5");

        vars.forEach(var -> {
            byte[] digestOfPassword = md.digest(var.getBytes(StandardCharsets.UTF_8));

            try {
                String result = TripleDesBouncyCastle.decode(analyzer.GetCryptedText(), digestOfPassword);
                output.append("Key: ");
                output.append(var);
                output.append("---> Decrypted: ");
                output.append(result);
                output.append("\n");
            } catch (IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException
                    | NoSuchProviderException | NoSuchAlgorithmException | BadPaddingException e) {
                e.printStackTrace();
            }
        });

        if(output.isEmpty()) output.append("* No results without exeption *");
        return new String(output);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label label_encrypt_error;

    @FXML
    private Label label_decrypt_error;

    @FXML
    private CheckBox isSpecSymbols;

    @FXML
    private CheckBox isLatin;

    @FXML
    private CheckBox isDigits;

    @FXML
    private CheckBox ignoreExeptions;

    @FXML
    private TextField text_decrypt_passLength;

    @FXML
    private TextField text_encrypt_passphrase;

    @FXML
    private TextArea text_encrypt_open;

    @FXML
    private MenuItem MenuButtonAbout;

    @FXML
    private TextArea text_decrypt_coded;

    @FXML
    private TextArea text_decrypt_open;

    @FXML
    private MenuItem MenuButtonClearFields;

    @FXML
    private Button ButtonEncrypt;

    @FXML
    private Button ButtonNext;

    @FXML
    private TextArea text_encrypt_coded;

    @FXML
    private Button ButtonDecrypt;

    @FXML
    private Button ButtonDone;

    @FXML
    private Button ButtonStart;

    @FXML
    private MenuItem MenuButtonClose;

    @FXML
    void OnActionButtonNext(ActionEvent event) throws NoSuchAlgorithmException {
        ArrayList<String> variants = analyzer.MakeStep();
        if(ignoreExeptions.isSelected()){
            text_decrypt_open.setText(MakeOutputNoExeption(variants));
        }
        else{
            text_decrypt_open.setText(MakeOutput(variants));
        }
    }

    @FXML
    void OnActionButtonDone(ActionEvent event) {
        ButtonStart.setDisable(false);
        ButtonNext.setDisable(true);
        ButtonDone.setDisable(true);
        ButtonEncrypt.setDisable(false);
        if(!text_encrypt_coded.getText().equals("")) ButtonDecrypt.setDisable(false);

        label_decrypt_error.setTextFill(Color.GREEN);
        label_decrypt_error.setText("Криптоанализ завершен.");
    }

    @FXML
    void OnActionButtonDecrypt(ActionEvent event) {
        text_decrypt_coded.setText(text_encrypt_coded.getText());
    }

    @FXML
    void OnActionButtonStart(ActionEvent event) {
        try {
            if (text_decrypt_coded.getText().equals("")) {
                label_decrypt_error.setTextFill(Color.RED);
                label_decrypt_error.setText("Шифротекст не может быть пустым!");
            } else if (!isDigits.isSelected() && !isLatin.isSelected() && !isSpecSymbols.isSelected()) {
                label_decrypt_error.setTextFill(Color.RED);
                label_decrypt_error.setText("Необходимо выбрать хотя бы один пункт!");
            } else if (text_decrypt_passLength.getText().equals("")) {
                label_decrypt_error.setTextFill(Color.RED);
                label_decrypt_error.setText("Длина пароля не может быть пустой!");
            } else if (Integer.parseInt(text_decrypt_passLength.getText()) == 0) {
                label_decrypt_error.setTextFill(Color.RED);
                label_decrypt_error.setText("Длина пароля не может нулевой!");
            } else {
                label_decrypt_error.setTextFill(Color.GREEN);
                label_decrypt_error.setText("Криптоанализ начат...");
                ButtonStart.setDisable(true);
                ButtonNext.setDisable(false);
                ButtonDone.setDisable(false);
                ButtonEncrypt.setDisable(true);
                ButtonDecrypt.setDisable(true);

                int len = Integer.parseInt(text_decrypt_passLength.getText());
                analyzer = new Cryptoanalysis(len,
                                            isLatin.isSelected(),
                                            isDigits.isSelected(),
                                            isSpecSymbols.isSelected(),
                                            text_decrypt_coded.getText(),
                                            1000
                );
                ArrayList<String> variants = analyzer.MakeStep();
                if(ignoreExeptions.isSelected()){
                    text_decrypt_open.setText(MakeOutputNoExeption(variants));
                }
                else{
                    text_decrypt_open.setText(MakeOutput(variants));
                }
            }
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            label_decrypt_error.setTextFill(Color.RED);
            label_decrypt_error.setText("Длина пароля должна быть числом!");
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    @FXML
    void OnActionButtonEncrypt(ActionEvent event) throws Exception {
        // testing --------------------------------------------------------------------------
//        String test = "111111";
//
//        String testRandomKey = "111";
//        MessageDigest md_test = MessageDigest.getInstance("md5");
//        byte[] digestOfPassword = md_test.digest(testRandomKey.getBytes(StandardCharsets.UTF_8));
//
//        System.out.println("Line to encode: "+test);
//        System.out.println("Key: "+testRandomKey);
//        System.out.println("Key in bytes: "+ Arrays.toString(digestOfPassword) + "\n");
//
//        String encodedLineString = TripleDesBouncyCastle.encode(test, digestOfPassword);
//        System.out.println("Encoded line: "+ encodedLineString + "\n");
//
//        String decodedLine = TripleDesBouncyCastle.decode(encodedLineString, digestOfPassword);
//        System.out.println("Decoded line: "+ decodedLine);
//
//        byte[] wdigestOfPassword = md_test.digest("000qqq".getBytes(StandardCharsets.UTF_8));
//        String wdecodedLine = TripleDesBouncyCastle.decode(encodedLineString, wdigestOfPassword);
//        System.out.println("Wrong decoded line: "+ wdecodedLine);
        // testing --------------------------------------------------------------------------

        if(!text_encrypt_passphrase.getText().equals("")){
            label_encrypt_error.setText("");

            // create secret key
            String passphrase = text_encrypt_passphrase.getText();
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] secretKey = md.digest(passphrase.getBytes(StandardCharsets.UTF_8));

            // encrypt text
            String openText = text_encrypt_open.getText();
            String cryptText = TripleDesBouncyCastle.encode(openText, secretKey);

            text_encrypt_coded.setText(cryptText);
            ButtonDecrypt.setDisable(false);
        }
        else{
            text_encrypt_coded.setText("");
            ButtonDecrypt.setDisable(true);
            label_encrypt_error.setTextFill(Color.RED);
            label_encrypt_error.setText("Парольная фраза не может быть пустой!");
        }

    }

    @FXML
    void OnActionMenuButtonClose(ActionEvent event) {
        Stage st = (Stage) ButtonDone.getScene().getWindow();
        st.close();
    }

    @FXML
    void OnActionMenuButtonClearFields(ActionEvent event) {
        text_decrypt_coded.setText("");
        text_decrypt_open.setText("");
        text_encrypt_coded.setText("");
        text_encrypt_open.setText("");
        label_decrypt_error.setText("");
        label_encrypt_error.setText("");

        ButtonDecrypt.setDisable(true);
        ButtonDone.setDisable(true);
        ButtonNext.setDisable(true);
        ButtonStart.setDisable(false);
    }

    @FXML
    void OnActionMenuButtonAbout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("About.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 706);
        stageAbout = new Stage();
        stageAbout.setTitle("О программе");
        stageAbout.setScene(scene);
        stageAbout.show();
    }

    @FXML
    void initialize() {
        assert isSpecSymbols != null : "fx:id=\"isSpecSymbols\" was not injected: check your FXML file 'main.fxml'.";
        assert text_decrypt_passLength != null : "fx:id=\"text_decrypt_passLength\" was not injected: check your FXML file 'main.fxml'.";
        assert MenuButtonAbout != null : "fx:id=\"MenuButtonAbout\" was not injected: check your FXML file 'main.fxml'.";
        assert text_decrypt_coded != null : "fx:id=\"text_decrypt_coded\" was not injected: check your FXML file 'main.fxml'.";
        assert text_decrypt_open != null : "fx:id=\"text_decrypt_open\" was not injected: check your FXML file 'main.fxml'.";
        assert MenuButtonClearFields != null : "fx:id=\"MenuButtonClearFields\" was not injected: check your FXML file 'main.fxml'.";
        assert ignoreExeptions != null : "fx:id=\"ignoreExeptions\" was not injected: check your FXML file 'main.fxml'.";
        assert ButtonEncrypt != null : "fx:id=\"ButtonEncrypt\" was not injected: check your FXML file 'main.fxml'.";
        assert ButtonNext != null : "fx:id=\"ButtonNext\" was not injected: check your FXML file 'main.fxml'.";
        assert text_encrypt_passphrase != null : "fx:id=\"text_encrypt_passphrase\" was not injected: check your FXML file 'main.fxml'.";
        assert ButtonDecrypt != null : "fx:id=\"ButtonDecrypt\" was not injected: check your FXML file 'main.fxml'.";
        assert isLatin != null : "fx:id=\"isLatin\" was not injected: check your FXML file 'main.fxml'.";
        assert ButtonStart != null : "fx:id=\"ButtonStart\" was not injected: check your FXML file 'main.fxml'.";
        assert text_encrypt_open != null : "fx:id=\"text_encrypt_open\" was not injected: check your FXML file 'main.fxml'.";
        assert label_encrypt_error != null : "fx:id=\"label_encrypt_error\" was not injected: check your FXML file 'main.fxml'.";
        assert isDigits != null : "fx:id=\"isDigits\" was not injected: check your FXML file 'main.fxml'.";
        assert text_encrypt_coded != null : "fx:id=\"text_encrypt_coded\" was not injected: check your FXML file 'main.fxml'.";
        assert ButtonDone != null : "fx:id=\"ButtonDone\" was not injected: check your FXML file 'main.fxml'.";
        assert MenuButtonClose != null : "fx:id=\"MenuButtonClose\" was not injected: check your FXML file 'main.fxml'.";
        assert label_decrypt_error != null : "fx:id=\"label_decrypt_error\" was not injected: check your FXML file 'main.fxml'.";

        ButtonDecrypt.setDisable(true);
        ButtonDone.setDisable(true);
        ButtonNext.setDisable(true);
        ButtonStart.setDisable(false);
    }
}
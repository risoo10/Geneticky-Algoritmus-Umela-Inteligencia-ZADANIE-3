package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {

    @FXML
    private JFXTextField mriezkaY;

    @FXML
    private JFXTextField mriezkaX;

    @FXML
    private JFXTextField pocetPokladov;

    @FXML
    private JFXButton submit;

    @FXML
    private JFXTextField startY;

    @FXML
    private JFXTextField startX;

    @FXML
    private JFXTextArea suradnicePokladov;

    @FXML
    private JFXTextField mutaciaField;

    @FXML
    private JFXComboBox<?> selekciaCombobox;

    @FXML
    void najdiRiesenie(ActionEvent event) {

        Mapa mapa = new Mapa(
                Integer.parseInt(pocetPokladov.getText()),
                Integer.parseInt(mriezkaX.getText()),
                Integer.parseInt(mriezkaY.getText())
        );

        mapa.parsujPoklady(suradnicePokladov.getText());

        HladacPokladov h = new HladacPokladov(Integer.parseInt(startX.getText()), Integer.parseInt(startY.getText()), mapa);

        GenetickyAlgoritmus g = new GenetickyAlgoritmus( mapa, h, 0.2 );

        Jedinec j = g.proces();

        System.out.println("KONIEC : JEDINEC [ fit: " + j.getFitness() +
                            " || poklady: " + j.getPocetNajdenychPokladov() +
                            " || kroky: " + j.getPocetKrokov() + " ]");



    }

}

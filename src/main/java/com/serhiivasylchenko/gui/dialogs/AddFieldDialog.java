package com.serhiivasylchenko.gui.dialogs;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.gui.GUIUpdater;
import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.utils.FieldType;
import com.serhiivasylchenko.utils.Validator;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Serhii Vasylchenko
 */
public class AddFieldDialog extends VBox implements Initializable, IDialog {
    @FXML
    private TextField fieldName;
    @FXML
    private TextArea fieldDescription;
    @FXML
    private ChoiceBox<FieldType> fieldType;
    @FXML
    private VBox mainVBox;

    private VBox choiceStringsBox;
    private VBox insideVBox;

    private Dialog<List<String>> dialog;

    private GUIUpdater guiUpdater = GUIUpdater.getInstance();
    private PersistenceBean persistenceBean = PersistenceBean.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceStringsBox = new VBox();
        choiceStringsBox.setAlignment(Pos.CENTER);
        choiceStringsBox.setSpacing(14);

        insideVBox = new VBox();
        insideVBox.setAlignment(Pos.CENTER);
        insideVBox.setSpacing(14);
        insideVBox.setFillWidth(false);
    }

    public AddFieldDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/dialogs/addFieldDialog.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error("FXML loading error in class '" + this.getClass().getSimpleName() + "'", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showDialog(TechnicalEntity entity) {
        dialog = new Dialog<>();
        dialog.setTitle("Add new field");
        dialog.setContentText(null);

        fieldType.setItems(FXCollections.observableList(Arrays.asList(FieldType.values())));
        fieldType.getSelectionModel().selectFirst();

        // Set the button types.
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Validation on fields
        final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.addEventFilter(ActionEvent.ACTION, event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong fields!");
            alert.setHeaderText(null);

            if (fieldName.getText().isEmpty()) {
                event.consume();
                alert.setContentText("Please set the name of the field");
                alert.showAndWait();
            } else if (fieldType.getValue() == null) {
                event.consume();
                alert.setContentText("Please choose the type");
                alert.showAndWait();
            } else if (!Validator.validateFieldName(entity, fieldName.getText())) {
                event.consume();
                alert.setContentText("Field name should unique per entity");
                alert.showAndWait();
            }
        });

        dialog.getDialogPane().setContent(this);

        // Request focus on the name field by default.
        Platform.runLater(fieldName::requestFocus);

        // Send the result to workflowManager when the add button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Field field = new Field(entity.getParameterList(), fieldName.getText(), fieldDescription.getText(), fieldType.getValue());
                if (fieldType.getValue() == FieldType.CHOICE_BOX) {
                    List<String> choiceStrings = insideVBox.getChildren().stream()
                            .map(child -> (HBox) child)
                            .map(hBox -> (TextField) hBox.getChildren().get(0))
                            .map(TextField::getText)
                            .collect(Collectors.toList());
                    field.setChoiceStrings(choiceStrings);
                }
                persistenceBean.persist(field);
//                guiUpdater.updateParameters(entity);
            }
            fieldName.setText("");
            fieldDescription.setText("");
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void onFieldTypeChange() {
        if (fieldType.getSelectionModel().getSelectedItem() == FieldType.CHOICE_BOX) {
            buildChoiceStringsBox();
        } else {
            mainVBox.getChildren().remove(choiceStringsBox);
            dialog.getDialogPane().getScene().getWindow().sizeToScene();
        }
    }

    private void buildChoiceStringsBox() {
        choiceStringsBox.getChildren().clear();
        insideVBox.getChildren().clear();

        Button addChoiceButton = new Button("Add option");

        addChoiceButton.setOnAction(event -> {
            insideVBox.getChildren().add(buildChoice());
            dialog.getDialogPane().getScene().getWindow().sizeToScene();
        });

        insideVBox.getChildren().add(buildChoice());

        choiceStringsBox.getChildren().addAll(insideVBox, addChoiceButton);

        mainVBox.getChildren().add(choiceStringsBox);

        dialog.getDialogPane().getScene().getWindow().sizeToScene();
    }

    private HBox buildChoice() {
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);

        TextField choiceTextField = new TextField();
        choiceTextField.setPromptText("Write choice string here");

        Button deleteChoiceButton = new Button();
        deleteChoiceButton.setTooltip(new Tooltip("delete choice"));
        deleteChoiceButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        deleteChoiceButton.setPrefSize(20, 20);
        deleteChoiceButton.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        deleteChoiceButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.REMOVE, "16px"));
        deleteChoiceButton.setOnAction(event -> {
            insideVBox.getChildren().remove(hBox);
            dialog.getDialogPane().getScene().getWindow().sizeToScene();
        });

        hBox.getChildren().addAll(choiceTextField, deleteChoiceButton);

        return hBox;
    }
}

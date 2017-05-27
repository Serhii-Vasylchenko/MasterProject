package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.core.WorkflowManager;
import com.serhiivasylchenko.persistence.*;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Parameters;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */
public class ParametersPaneController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ParametersPaneController.class);

    @FXML
    private ImageView iconImage;
    @FXML
    private Label name;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane parameterGridPane;
    @FXML
    private VBox mainVBox;

    private DialogController dialogController = DialogController.getInstance();
    private GUIUpdater guiUpdater = GUIUpdater.getInstance();
    private PersistenceBean persistenceBean = PersistenceBean.getInstance();
    private WorkflowManager workflowManager = WorkflowManager.getInstance();

    private TechnicalEntity entity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guiUpdater.setParametersGridPane(parameterGridPane);
    }

    public void showEntityParameters(TechnicalEntity entity) {
        this.entity = entity;
        mainVBox.setDisable(false);

        if (entity instanceof System) {
            iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/system1_32.png")));
        } else if (entity instanceof Component) {
            iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/component1_32.png")));
        } else if (entity instanceof ComponentGroup) {
            iconImage.setImage(new Image(getClass().getResourceAsStream("/icons/group1_32.png")));
        } else {
            LOGGER.error("Unknown TechnicalEntity! - " + entity.toString());
        }

        name.setText(entity.toString());
        ContextMenu contextMenu = new ContextMenu();
        descriptionTextArea.setText(entity.getDescription());
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(event -> {
            entity.setDescription(descriptionTextArea.getText());
            persistenceBean.persist(entity);
            Notifications.create()
                    .text("Description for the entity '" + entity.getName() + "' is saved!")
                    .graphic(GlyphsDude.createIcon(FontAwesomeIcon.CHECK_CIRCLE, "32px"))
                    .hideAfter(Duration.seconds(4))
                    .owner(parameterGridPane)
                    .position(Pos.BASELINE_RIGHT)
                    .show();
        });
        contextMenu.getItems().add(saveMenuItem);
        descriptionTextArea.setContextMenu(contextMenu);

        updateParameters();
    }

    @FXML
    private void addNewField() {
        dialogController.addFieldToEntity(entity);
        updateParameters();
    }

    public void updateParameters() {
        // Clear previous grid first
        parameterGridPane.getChildren().clear();

        List<Field> fields = persistenceBean.find(Field.class, Field.NQ_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", entity.getParameterList()));

        // Create field labels, setters adn control buttons

        int i = 0;
        for (Field field : fields) {
            // Label for the field
            Label fieldName = new Label(field.getName());
            fieldName.setTooltip(new Tooltip(field.getDescription()));

//            TitledPane titledPane = new TitledPane();
//            Accordion accordion = new Accordion();
//            titledPane.setText(field.getName());
//            titledPane.setTooltip(new Tooltip(field.getDescription()));
//            titledPane.setContent(new TextArea(field.getDescription()));
//            accordion.getPanes().add(titledPane);

            // Create buttons for save, edit and delete
            Button saveFieldValueButton = new Button();
            Button editFieldNameButton = new Button();
            Button deleteFieldButton = new Button();

            saveFieldValueButton.setTooltip(new Tooltip("Save value"));
            editFieldNameButton.setTooltip(new Tooltip("Change name"));
            deleteFieldButton.setTooltip(new Tooltip("Delete field"));

            saveFieldValueButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            editFieldNameButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            deleteFieldButton.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            saveFieldValueButton.setPrefSize(20, 20);
            editFieldNameButton.setPrefSize(20, 20);
            deleteFieldButton.setPrefSize(20, 20);
            saveFieldValueButton.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            editFieldNameButton.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            deleteFieldButton.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

            saveFieldValueButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.SAVE, "16px"));
            editFieldNameButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.EDIT, "16px"));
            deleteFieldButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.REMOVE, "16px"));

            HBox buttonBox = new HBox();
            buttonBox.setSpacing(4);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(saveFieldValueButton, editFieldNameButton, deleteFieldButton);

            editFieldNameButton.setOnAction(event -> {
                dialogController.changeName(field);
            });
            deleteFieldButton.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Confirm deletion");
                alert.setContentText("Are you sure you want to delete this field? There would be no way back!");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        persistenceBean.delete(field);
                        updateParameters();
                    } catch (Exception e) {
                        Logger.getLogger(this.getClass()).error(null, e);
                    }
                }

            });

            // Value setter for this field
            switch (field.getFieldType()) {
                case INT_NUMBER:
                    TextField intField = new TextField(); // TODO: custom integer field
                    intField.setText(String.valueOf(field.getIntValue()));
                    intField.setTooltip(new Tooltip("integer value"));

                    // Saving the value on enter
                    intField.setOnKeyPressed(event -> {
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            field.setIntValue(Integer.valueOf(intField.getText()));
                            persistenceBean.persist(field);
                            showSuccessNotification(field);
                        }
                    });

                    // Saving the value on button click
                    saveFieldValueButton.setOnAction(event -> {
                        field.setIntValue(Integer.valueOf(intField.getText()));
                        persistenceBean.persist(field);
                        showSuccessNotification(field);
                    });

                    parameterGridPane.addRow(i, fieldName, intField, buttonBox);
                    break;

                case FLOAT_NUMBER:
                    TextField floatField = new TextField(); // TODO: custom float field
                    floatField.setText(String.valueOf(field.getFloatValue()));
                    floatField.setTooltip(new Tooltip("float value"));
                    floatField.setPromptText("float value");

                    // Saving the value on enter
                    floatField.setOnKeyPressed(event -> {
                        if (event.getCode().equals(KeyCode.ENTER)) {
                            field.setFloatValue(Float.valueOf(floatField.getText()));
                            persistenceBean.persist(field);
                            showSuccessNotification(field);
                        }
                    });

                    // Saving the value on button click
                    saveFieldValueButton.setOnAction(event -> {
                        field.setFloatValue(Integer.valueOf(floatField.getText()));
                        persistenceBean.persist(field);
                        showSuccessNotification(field);
                    });

                    parameterGridPane.addRow(i, fieldName, floatField, buttonBox);
                    break;

                case CHOICE_BOX:
                    ChoiceBox<String> choiceBox = new ChoiceBox<>();
                    if (field.getChoiceStrings() != null) {
                        choiceBox.setItems(FXCollections.observableList(field.getChoiceStrings()));
                        choiceBox.getSelectionModel().select(field.getSelectedStringIndex());
                    }

                    // Saving the value on change of selected line
                    choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        field.setSelectedStringIndex(choiceBox.getSelectionModel().getSelectedIndex());
                        persistenceBean.persist(field);
                        showSuccessNotification(field);
                    });

                    // Saving the value on button click
                    saveFieldValueButton.setOnAction(event -> {
                        field.setSelectedStringIndex(choiceBox.getSelectionModel().getSelectedIndex());
                        persistenceBean.persist(field);
                        showSuccessNotification(field);
                    });

                    parameterGridPane.addRow(i, fieldName, choiceBox, buttonBox);
                    break;
            }

            i++;
        }
    }

    @FXML
    private void editEntityName() {
        this.dialogController.changeName(entity);
    }

    private void showSuccessNotification(Field field) {
        Notifications.create()
                .text("Value for field '" + field.getName() + "' is saved!")
                .graphic(GlyphsDude.createIcon(FontAwesomeIcon.CHECK_CIRCLE, "32px"))
                .hideAfter(Duration.seconds(4))
                .owner(parameterGridPane)
                .position(Pos.BASELINE_RIGHT)
                .show();
    }

    @FXML
    private void deleteEntity() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm deletion");
        alert.setContentText("Are you sure you want to delete this element and all its children? There would be no way back!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.workflowManager.deleteEntity(entity);
            this.guiUpdater.updateComponentTree();
            this.reset();
        }
    }

    public void reset() {
        this.mainVBox.setDisable(true);
        this.iconImage.setImage(null);
    }
}

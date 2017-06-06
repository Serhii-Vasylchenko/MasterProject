package com.serhiivasylchenko.learners;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.Field;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Parameters;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Serhii Vasylchenko
 */
public class LearningUtils {

    private static final PersistenceBean persistenceBean = PersistenceBean.getInstance();

    public static Map<String, Field> fieldWithNameMap(System system) {
        Map<String, Field> fieldMap = new LinkedHashMap<>();

        // Add all system fields first
        List<Field> systemFields = persistenceBean.find(Field.class, Field.NQ_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", system.getParameterList()));
        systemFields.forEach(field -> fieldMap.put(system.toString() + "->" + field.getName(), field));

        // Then all component groups fields
        persistenceBean.find(ComponentGroup.class, ComponentGroup.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(group -> group.getParameterList().getFields()
                        .forEach(field -> fieldMap.put(group.toString() + "->" + field.getName(), field))
                );

        // Then all component fields
        persistenceBean.find(Component.class, Component.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(component -> component.getParameterList().getFields()
                        .forEach(field -> fieldMap.put(component.toString() + "->" + field.getName(), field))
                );

        return fieldMap;
    }

    /**
     * Get all fields of system
     * Order of fields: system, component groups (ordered by id), components (order by id)
     *
     * @param system
     * @return list of all fields
     */
    public static List<Field> getAllFields(System system) {

        List<Field> fields = new ArrayList<>();

        // Add all system fields first
        List<Field> systemFields = persistenceBean.find(Field.class, Field.NQ_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", system.getParameterList()));
        fields.addAll(systemFields);

        // Then all component groups fields
        persistenceBean.find(ComponentGroup.class, ComponentGroup.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(group -> fields.addAll(group.getParameterList().getFields()));

        // Then all component fields
        persistenceBean.find(Component.class, Component.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(component -> fields.addAll(component.getParameterList().getFields()));

        return fields;
    }

    /**
     * Get all fields of system marked as not target
     * Order of fields: system, component groups (ordered by id), components (order by id)
     *
     * @param system
     * @return list of all fields
     */
    public static List<Field> getInputFields(System system) {

        List<Field> fields = new ArrayList<>();

        // Add all system fields first
        List<Field> systemFields = persistenceBean.find(Field.class, Field.NQ_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", system.getParameterList()));
        fields.addAll(systemFields);

        // Then all component groups fields
        persistenceBean.find(ComponentGroup.class, ComponentGroup.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(group -> group.getParameterList().getFields()
                        .stream()
                        .filter(field -> !field.isTarget())
                        .forEach(fields::add)
                );

        // Then all component fields
        persistenceBean.find(Component.class, Component.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(component -> component.getParameterList().getFields()
                        .stream()
                        .filter(field -> !field.isTarget())
                        .forEach(fields::add)
                );

        return fields;
    }

    /**
     * Get all fields of system marked as target
     * Order of fields: system, component groups (ordered by id), components (order by id)
     *
     * @param system
     * @return list of all fields
     */
    public static List<Field> getTargetFields(System system) {

        List<Field> fields = new ArrayList<>();

        // Add all system fields first
        persistenceBean.find(Field.class, Field.NQ_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", system.getParameterList()))
                .stream()
                .filter(Field::isTarget)
                .forEach(fields::add);

        // Then all component groups fields
        persistenceBean.find(ComponentGroup.class, ComponentGroup.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(group -> group.getParameterList().getFields()
                        .stream()
                        .filter(Field::isTarget)
                        .forEach(fields::add)
                );

        // Then all component fields
        persistenceBean.find(Component.class, Component.NQ_BY_SYSTEM, new Parameters().add("system", system))
                .forEach(component -> component.getParameterList().getFields()
                        .stream()
                        .filter(Field::isTarget)
                        .forEach(fields::add)
                );

        return fields;
    }

    public static List<String> collectFieldValues(GridPane gridPane) {

        List<String> fieldValues = new ArrayList<>();

        gridPane.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                fieldValues.add(((TextField) node).getText());
            } else if (node instanceof ChoiceBox) {
                fieldValues.add(String.valueOf(((ChoiceBox) node).getSelectionModel().getSelectedIndex()));
            }
        });

        return fieldValues;
    }

    public static void writeCSV(File file, List<String> fieldValues) {
        try {
            DataOutput dataOutput = new DataOutputStream(new FileOutputStream(file, true));

            int i = 0;
            for (String fieldValue : fieldValues) {
                dataOutput.writeChars(fieldValue);
                if (i != fieldValues.size() - 1) {
                    dataOutput.write(",".getBytes());
                    i++;
                }
            }
            dataOutput.write("\n".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

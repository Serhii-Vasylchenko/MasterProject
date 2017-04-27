package com.serhiivasylchenko.persistence;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class ComponentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private ArrayList<Component> componentChildren;

    @Column
    private ArrayList<ComponentGroup> componentGroupChildren;


}

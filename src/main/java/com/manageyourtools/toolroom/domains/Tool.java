package com.manageyourtools.toolroom.domains;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    private Long currentCount;

    @Enumerated(value = EnumType.STRING)
    private UnitOfMeasure unitOfMeasure;
    private Long allCount;

    @ManyToMany
    @JoinTable(name = "tool_category",
            joinColumns = @JoinColumn(name = "tool_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    private Long minimalCount;

    @NotNull
    private Boolean isUnique;

    @NotNull
    private Boolean isToReturn;
    private LocalDate warrantyDate;
    private String location;

    @Lob
    private String image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    private List<BoughtTool> boughtTools;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    private List<DestroyedTool> destroyedTools;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    private List<PickAndReturnTool> pickAndReturnTools;

    public Tool addBoughtTool(BoughtTool boughtTool){
        boughtTool.setTool(this);
        this.boughtTools.add(boughtTool);
        return this;
    }

    public Tool addDestroyedTool(DestroyedTool destroyedTool){
        destroyedTool.setTool(this);
        this.destroyedTools.add(destroyedTool);
        return this;
    }

    public Tool addPickAndReturnTool(PickAndReturnTool pickAndReturnTool){
        pickAndReturnTool.setTool(this);
        this.pickAndReturnTools.add(pickAndReturnTool);
        return this;
    }
}

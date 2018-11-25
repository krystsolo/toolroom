package com.manageyourtools.toolroom.domains;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
    private List<BuyOrderTool> buyOrderTools;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    private List<DestructionOrderTool> destructionOrderTools;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    private List<LendingOrderTool> lendingOrderTools;

    public Tool addBoughtTool(BuyOrderTool buyOrderTool){
        buyOrderTool.setTool(this);
        this.buyOrderTools.add(buyOrderTool);
        return this;
    }

    public Tool addDestroyedTool(DestructionOrderTool destructionOrderTool){
        destructionOrderTool.setTool(this);
        this.destructionOrderTools.add(destructionOrderTool);
        return this;
    }

    public Tool addPickAndReturnTool(LendingOrderTool lendingOrderTool){
        lendingOrderTool.setTool(this);
        this.lendingOrderTools.add(lendingOrderTool);
        return this;
    }
}

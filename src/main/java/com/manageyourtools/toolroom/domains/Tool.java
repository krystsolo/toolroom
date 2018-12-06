package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Tool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String name;

    private Long currentCount;

    @Enumerated(value = EnumType.STRING)
    private UnitOfMeasure unitOfMeasure;
    private Long allCount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "category_id")
    private Category category;

    private Long minimalCount;

    @NotNull
    private Boolean isUnique;

    @NotNull
    private Boolean isToReturn;
    private LocalDate warrantyDate;
    private String location;

    @NotNull
    private Boolean isEnable = true;

    @Lob
    private String image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    @JsonIgnore
    private List<BuyOrderTool> buyOrderTools = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    @JsonIgnore
    private List<DestructionOrderTool> destructionOrderTools = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tool")
    @JsonIgnore
    private List<LendingOrderTool> lendingOrderTools = new ArrayList<>();

    public Tool addBuyOrderTool(BuyOrderTool buyOrderTool){
        buyOrderTool.setTool(this);
        this.buyOrderTools.add(buyOrderTool);
        return this;
    }

    public Tool addDestructionOrderTool(DestructionOrderTool destructionOrderTool){
        destructionOrderTool.setTool(this);
        this.destructionOrderTools.add(destructionOrderTool);
        return this;
    }

    public Tool addLendingOrderTool(LendingOrderTool lendingOrderTool){
        lendingOrderTool.setTool(this);
        this.lendingOrderTools.add(lendingOrderTool);
        return this;
    }

    public Tool addCategory(Category category){
        this.category = category;
        List<Tool> tools = category.getTools();
        tools.add(this);
        category.setTools(tools);
        return this;
    }
}

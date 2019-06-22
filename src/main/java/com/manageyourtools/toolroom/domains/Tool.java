package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
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

    @ManyToOne(fetch = FetchType.EAGER)
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
    private byte[] image;

    @OneToMany(fetch = FetchType.LAZY,  mappedBy = "tool")
    @JsonIgnore
    private Set<BuyOrderTool> buyOrderTools = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tool")
    @JsonIgnore
    private Set<DestructionOrderTool> destructionOrderTools = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tool")
    @JsonIgnore
    private Set<LendingOrderTool> lendingOrderTools = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tool")
    @JsonIgnore
    private Set<LendingReturnOrderTool> lendingReturnOrderTools = new HashSet<>();

    public void addBuyOrderTool(BuyOrderTool buyOrderTool){
        buyOrderTool.setTool(this);
        this.buyOrderTools.add(buyOrderTool);
    }

    public void addDestructionOrderTool(DestructionOrderTool destructionOrderTool){
        destructionOrderTool.setTool(this);
        this.destructionOrderTools.add(destructionOrderTool);
    }

    public void addLendingOrderTool(LendingOrderTool lendingOrderTool){
        lendingOrderTool.setTool(this);
        this.lendingOrderTools.add(lendingOrderTool);
    }

    public void addLendingReturnOrderTool(LendingReturnOrderTool lendingReturnOrderTool) {
        lendingReturnOrderTool.setTool(this);
        this.lendingReturnOrderTools.add(lendingReturnOrderTool);
    }

}

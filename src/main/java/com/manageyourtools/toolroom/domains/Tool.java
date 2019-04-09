package com.manageyourtools.toolroom.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tool")
    @JsonIgnore
    private List<BuyOrderTool> buyOrderTools = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tool")
    @JsonIgnore
    private List<DestructionOrderTool> destructionOrderTools = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tool")
    @JsonIgnore
    private List<LendingOrderTool> lendingOrderTools = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tool")
    @JsonIgnore
    private List<LendingReturnOrderTool> lendingReturnOrderTools = new ArrayList<>();

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

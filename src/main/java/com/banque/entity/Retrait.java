package com.banque.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("R")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Retrait extends Operation {
    // Inherits all fields from Operation
}


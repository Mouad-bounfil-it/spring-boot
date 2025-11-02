package com.banque.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("V")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Versement extends Operation {
    // Inherits all fields from Operation
}


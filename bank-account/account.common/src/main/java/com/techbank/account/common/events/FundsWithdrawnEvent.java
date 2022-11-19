package com.techbank.account.common.events;

import com.techbank.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=true)
public class FundsWithdrawnEvent extends BaseEvent {
    private double amount;
}

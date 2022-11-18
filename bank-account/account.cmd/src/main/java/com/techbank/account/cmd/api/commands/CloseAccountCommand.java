package com.techbank.account.cmd.api.commands;

import com.techbank.cqrs.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CloseAccountCommand extends BaseCommand {
    public  CloseAccountCommand(String id) {
        super(id);
    }
}

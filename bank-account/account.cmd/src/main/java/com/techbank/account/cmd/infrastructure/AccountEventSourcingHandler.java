package com.techbank.account.cmd.infrastructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.techbank.cqrs.core.infrastructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    @Autowired
    private EventStore eventStore;

    @Autowired
    private EventProducer eventProducer;

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(),aggregate.getUncommittedChanges(),aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events =eventStore.getEvents(id);
        if (events != null && !events.isEmpty()) {
            aggregate.replayEvents(events);
            var lastVersion = events.stream().map(x -> x.getVersion()).max(Comparator.naturalOrder());
            aggregate.setVersion(lastVersion.get());
        }

        return aggregate;
    }

    @Override
    public void rePublishEvents() {
        var aggregateIds = eventStore.getAggregateIds();
        for (var aggregateId: aggregateIds) {
            var aggregate= getById(aggregateId);
            if (aggregate == null || !aggregate.getActive())
                continue;

            var events = eventStore.getEvents(aggregateId);
            for (var event: events) {
                eventProducer.produce(event.getClass().getSimpleName(),event);
            }
        }
    }
}

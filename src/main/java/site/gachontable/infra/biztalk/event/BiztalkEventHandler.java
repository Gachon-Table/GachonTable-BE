package site.gachontable.infra.biztalk.event;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import site.gachontable.infra.biztalk.SendBiztalk;

@Component
@RequiredArgsConstructor
public class BiztalkEventHandler {

    private final SendBiztalk sendBiztalk;

    @Async
    @TransactionalEventListener
    public void execute(SentBiztalkEvent event) {
        sendBiztalk.execute(event.getTemplateCode(), event.getUserTel(), event.getVariables());
    }
}

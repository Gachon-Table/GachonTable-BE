package site.gachontable.gachontablebe.domain.shared.event;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import site.gachontable.gachontablebe.global.biztalk.SendBiztalk;

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

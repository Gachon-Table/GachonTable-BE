package site.gachontable.gachontablebe.domain.shared.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashMap;

@Getter
@NoArgsConstructor
public class SentBiztalkEvent {

    private String templateCode;
    private String userTel;
    private HashMap<String, String> variables;

    public SentBiztalkEvent(String templateCode, String userTel, HashMap<String, String> variables) {
        this.templateCode = templateCode;
        this.userTel = userTel;
        this.variables = variables;
    }

    public static SentBiztalkEvent of(String templateCode, String userTel, HashMap<String, String> variables) {
        return new SentBiztalkEvent(templateCode, userTel, variables);
    }
}
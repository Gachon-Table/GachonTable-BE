package site.gachontable.domain.admin.port.in;

import site.gachontable.presentation.admin.dto.request.ExitUserRequest;
import site.gachontable.infra.security.principal.AuthDetails;

public interface ExitUser {

    String execute(AuthDetails authDetails, ExitUserRequest request);
}

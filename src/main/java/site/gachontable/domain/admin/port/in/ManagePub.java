package site.gachontable.domain.admin.port.in;

import site.gachontable.infra.security.principal.AuthDetails;
import site.gachontable.presentation.admin.dto.request.PubManageRequest;

public interface ManagePub {

    String execute(AuthDetails authDetails, PubManageRequest request);
}

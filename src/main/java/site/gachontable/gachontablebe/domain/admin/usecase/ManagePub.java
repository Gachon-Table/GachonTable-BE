package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.PubManageRequest;

public interface ManagePub {

    String execute(AuthDetails authDetails, PubManageRequest request);
}

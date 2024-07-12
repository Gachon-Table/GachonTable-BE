package site.gachontable.gachontablebe.domain.pub.usecase;

import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.request.PubManageRequest;

public interface ManagePub {
    String execute(AuthDetails authDetails, PubManageRequest request);
}

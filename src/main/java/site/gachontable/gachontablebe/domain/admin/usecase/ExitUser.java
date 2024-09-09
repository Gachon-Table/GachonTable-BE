package site.gachontable.gachontablebe.domain.admin.usecase;

import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.ExitUserRequest;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;

public interface ExitUser {
    String execute(AuthDetails authDetails, ExitUserRequest request);
}

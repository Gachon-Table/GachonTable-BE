package site.gachontable.gachontablebe.domain.admin.presentation.dto.request;

public record RegisterRequest(String username,
                              String password,
                              String tel,
                              Integer pubId) {
}

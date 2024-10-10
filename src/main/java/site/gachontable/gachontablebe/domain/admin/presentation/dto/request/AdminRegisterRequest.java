package site.gachontable.gachontablebe.domain.admin.presentation.dto.request;

public record AdminRegisterRequest(String username,
                                   String password,
                                   String tel,
                                   Integer pubId) {
}

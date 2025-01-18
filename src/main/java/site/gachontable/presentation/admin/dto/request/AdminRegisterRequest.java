package site.gachontable.presentation.admin.dto.request;

public record AdminRegisterRequest(String username,
                                   String password,
                                   String tel,
                                   Integer pubId) {
}

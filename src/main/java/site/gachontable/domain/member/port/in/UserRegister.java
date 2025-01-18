package site.gachontable.domain.member.port.in;

import site.gachontable.domain.shared.dto.response.RegisterResponse;

public interface UserRegister {

    RegisterResponse execute(String username, String password, String tel);
}

package site.gachontable.domain.member.port.in;

import site.gachontable.infra.security.jwt.dto.JwtResponse;

public interface UserLogin {

    JwtResponse execute(String id, String password);
}

package site.gachontable.gachontablebe.domain.waiting.usecase;

import site.gachontable.gachontablebe.domain.user.domain.User;
import site.gachontable.gachontablebe.domain.waiting.presentation.dto.response.OrderResponse;

import java.util.List;

public interface GetOrder {
    List<OrderResponse> execute(User user);
}

package site.gachontable.gachontablebe.domain.pub.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.menu.domain.repository.MenuRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.presentation.dto.request.PubManageRequest;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagePubImpl implements ManagePub {
    private final AdminRepository adminRepository;
    private final MenuRepository menuRepository;
    private final PubRepository pubRepository;

    @Override
    public String execute(AuthDetails authDetails, PubManageRequest request) {
        Admin admin = adminRepository.findByUsername(authDetails.getUsername())
                .orElseThrow(AdminNotFoundException::new);
        Pub pub = admin.getPub();

        List<Menu> menus = createMenus(request, pub);

        pub.updatePubInfo(request.thumbnail(), request.oneLiner(), request.studentCard(), menus);
        pubRepository.save(pub);

        return SuccessCode.MANAGE_PUB_SUCCESS.getMessage();
    }

    private List<Menu> createMenus(PubManageRequest request, Pub pub) {
        return new ArrayList<>(
                request.menuRequests().stream()
                .map(menuRequest ->
                        Menu.create(pub, menuRequest.menuName(), menuRequest.price(), menuRequest.oneLiner()))
                .map(menuRepository::save)
                .toList());
    }
}

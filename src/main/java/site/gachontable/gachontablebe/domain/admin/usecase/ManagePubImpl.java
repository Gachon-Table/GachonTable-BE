package site.gachontable.gachontablebe.domain.admin.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gachontable.gachontablebe.domain.admin.domain.repository.AdminRepository;
import site.gachontable.gachontablebe.domain.admin.exception.AdminNotFoundException;
import site.gachontable.gachontablebe.domain.auth.domain.AuthDetails;
import site.gachontable.gachontablebe.domain.menu.domain.Menu;
import site.gachontable.gachontablebe.domain.menu.domain.repository.MenuRepository;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.admin.presentation.dto.request.PubManageRequest;
import site.gachontable.gachontablebe.global.success.SuccessCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagePubImpl implements ManagePub {

    private final AdminRepository adminRepository;
    private final MenuRepository menuRepository;
    private final PubRepository pubRepository;

    @Override
    @Transactional
    public String execute(AuthDetails authDetails, PubManageRequest request) {
        Pub pub = adminRepository.findById(authDetails.getUuid())
                .orElseThrow(AdminNotFoundException::new)
                .getPub();

        List<Menu> menus = manageMenus(request, pub);

        pub.updatePubInfo(request.thumbnails(), menus);
        pubRepository.save(pub);

        return SuccessCode.MANAGE_PUB_SUCCESS.getMessage();
    }

    private List<Menu> manageMenus(PubManageRequest request, Pub pub) {
        Map<Integer, Menu> existingMenus = menuRepository.findAllByPub(pub).stream()
                .collect(Collectors.toMap(Menu::getMenuId, menu -> menu));

        List<Menu> updatedMenus = request.menuRequests().stream()
                .map(menuRequest -> {
                    Menu menu = existingMenus.get(menuRequest.menuId());
                    if (menu != null) {
                        menu.update(
                                menuRequest.menuName(),
                                menuRequest.price(),
                                menuRequest.oneLiner(),
                                menuRequest.thumbnail());
                        return menu;
                    }
                    return Menu.create(
                            pub,
                            menuRequest.menuName(),
                            menuRequest.price(),
                            menuRequest.oneLiner(),
                            menuRequest.thumbnail());
                }).toList();

        return menuRepository.saveAll(updatedMenus);
    }
}

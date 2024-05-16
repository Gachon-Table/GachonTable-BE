package site.gachontable.gachontablebe.domain.admin.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.gachontable.gachontablebe.domain.admin.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}

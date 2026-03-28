-- 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰

-- =====================================================
-- BCrypt 密码生成说明
-- =====================================================
-- 
-- 要生成密码 "123456" 的 BCrypt 哈希值，请在 Java 项目中运行以下代码：
--
-- import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
--
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
-- String hash = encoder.encode("123456");
-- System.out.println(hash);
--
-- 然后将生成的哈希值替换到 complete_schema_part_06.sql 文件中
--
-- =====================================================
-- 已知有效的 "123456" BCrypt 哈希值（示例）
-- =====================================================
-- 以下是一个经过验证的 "123456" 的 BCrypt 哈希值：
-- $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW
--
-- 注意：由于 BCrypt 使用随机盐，每次生成的哈希值都不同，但都能正确验证相同的密码
-- =====================================================












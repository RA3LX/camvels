package com.camvels.infrastructure.config;

import com.camvels.application.port.in.AuthenticateUserPort;
import com.camvels.application.port.in.CategoryPort;
import com.camvels.application.port.in.DashboardPort;
import com.camvels.application.port.in.GenerateReportPort;
import com.camvels.application.port.in.MovementQueryPort;
import com.camvels.application.port.in.ProductPort;
import com.camvels.application.port.in.RegisterMovementPort;
import com.camvels.application.port.in.SendEmailPort;
import com.camvels.application.port.in.SupplierPort;
import com.camvels.application.port.in.UserPort;
import com.camvels.application.usecase.AuthenticateUserUseCase;
import com.camvels.application.usecase.CategoryUseCase;
import com.camvels.application.usecase.DashboardUseCase;
import com.camvels.application.usecase.GenerateReportUseCase;
import com.camvels.application.usecase.MovementQueryUseCase;
import com.camvels.application.usecase.ProductUseCase;
import com.camvels.application.usecase.RegisterMovementUseCase;
import com.camvels.application.usecase.SendEmailUseCase;
import com.camvels.application.usecase.SupplierUseCase;
import com.camvels.application.usecase.UserUseCase;
import com.camvels.domain.port.out.EmailPort;
import com.camvels.domain.port.out.ReportGeneratorPort;
import com.camvels.infrastructure.adapter.out.mail.SmtpEmailAdapter;
import com.camvels.infrastructure.adapter.out.pdf.ItextReportGeneratorAdapter;
import com.camvels.infrastructure.persistence.JdbcConnectionProvider;
import com.camvels.infrastructure.persistence.JdbcTransactionManager;
import com.camvels.infrastructure.persistence.jdbc.JdbcCategoriaRepository;
import com.camvels.infrastructure.persistence.jdbc.JdbcMovimientoRepository;
import com.camvels.infrastructure.persistence.jdbc.JdbcProductoRepository;
import com.camvels.infrastructure.persistence.jdbc.JdbcProveedorRepository;
import com.camvels.infrastructure.persistence.jdbc.JdbcUsuarioRepository;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamvelsConfiguration {

    @Bean
    JdbcConnectionProvider jdbcConnectionProvider(DataSource dataSource) {
        return new JdbcConnectionProvider(dataSource);
    }

    @Bean
    JdbcProductoRepository jdbcProductoRepository(JdbcConnectionProvider provider) {
        return new JdbcProductoRepository(provider);
    }

    @Bean
    JdbcMovimientoRepository jdbcMovimientoRepository(JdbcConnectionProvider provider) {
        return new JdbcMovimientoRepository(provider);
    }

    @Bean
    JdbcUsuarioRepository jdbcUsuarioRepository(JdbcConnectionProvider provider) {
        return new JdbcUsuarioRepository(provider);
    }

    @Bean
    JdbcCategoriaRepository jdbcCategoriaRepository(JdbcConnectionProvider provider) {
        return new JdbcCategoriaRepository(provider);
    }

    @Bean
    JdbcProveedorRepository jdbcProveedorRepository(JdbcConnectionProvider provider) {
        return new JdbcProveedorRepository(provider);
    }

    @Bean
    JdbcTransactionManager jdbcTransactionManager(JdbcConnectionProvider provider) {
        return new JdbcTransactionManager(provider);
    }

    @Bean
    EmailPort emailPort(
            @Value("${camvels.mail.host}") String host,
            @Value("${camvels.mail.port}") String port,
            @Value("${camvels.mail.user}") String user,
            @Value("${camvels.mail.password}") String password) {
        return new SmtpEmailAdapter(host, port, user, password);
    }

    @Bean
    ReportGeneratorPort reportGeneratorPort() {
        return new ItextReportGeneratorAdapter();
    }

    @Bean
    AuthenticateUserPort authenticateUserPort(JdbcUsuarioRepository usuarioRepository) {
        return new AuthenticateUserUseCase(usuarioRepository);
    }

    @Bean
    RegisterMovementPort registerMovementPort(
            JdbcMovimientoRepository movimientoRepository,
            JdbcProductoRepository productoRepository,
            JdbcTransactionManager transactionManager) {
        return new RegisterMovementUseCase(movimientoRepository, productoRepository, transactionManager);
    }

    @Bean
    MovementQueryPort movementQueryPort(
            JdbcMovimientoRepository movimientoRepository,
            JdbcProductoRepository productoRepository) {
        return new MovementQueryUseCase(movimientoRepository, productoRepository);
    }

    @Bean
    ProductPort productPort(
            JdbcProductoRepository productoRepository,
            JdbcMovimientoRepository movimientoRepository,
            JdbcProveedorRepository proveedorRepository) {
        return new ProductUseCase(productoRepository, movimientoRepository, proveedorRepository);
    }

    @Bean
    CategoryPort categoryPort(JdbcCategoriaRepository categoriaRepository) {
        return new CategoryUseCase(categoriaRepository);
    }

    @Bean
    SupplierPort supplierPort(JdbcProveedorRepository proveedorRepository) {
        return new SupplierUseCase(proveedorRepository);
    }

    @Bean
    UserPort userPort(JdbcUsuarioRepository usuarioRepository) {
        return new UserUseCase(usuarioRepository);
    }

    @Bean
    DashboardPort dashboardPort(
            JdbcProductoRepository productoRepository,
            JdbcMovimientoRepository movimientoRepository) {
        return new DashboardUseCase(productoRepository, movimientoRepository);
    }

    @Bean
    GenerateReportPort generateReportPort(
            JdbcProductoRepository productoRepository,
            JdbcMovimientoRepository movimientoRepository,
            JdbcProveedorRepository proveedorRepository,
            ReportGeneratorPort reportGeneratorPort) {
        return new GenerateReportUseCase(productoRepository, movimientoRepository, proveedorRepository, reportGeneratorPort);
    }

    @Bean
    SendEmailPort sendEmailPort(JdbcProveedorRepository proveedorRepository, EmailPort emailPort) {
        return new SendEmailUseCase(proveedorRepository, emailPort);
    }
}

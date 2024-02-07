package pavlina.EShop.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;
import pavlina.EShop.service.ProductService;

@Component
public class SessionExpireListener implements ApplicationListener<SessionDestroyedEvent> {

    private final ProductService productService;

    public SessionExpireListener(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        productService.releaseProductsForExpiredSession(event.getId());
    }
}

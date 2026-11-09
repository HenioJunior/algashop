package contracts.shoppingCart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method DELETE()
        urlPath("/api/v1/shopping-carts/01226N0640J7Q/items/0S66P0865N43H")
    }
    response {
        status 204
    }
}
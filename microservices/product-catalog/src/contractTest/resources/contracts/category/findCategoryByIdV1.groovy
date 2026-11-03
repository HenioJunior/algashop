package contracts.category

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url("/api/v1/categories/f5ab7a1e-37da-41e1-892b-a1d38275c2f2")
    }
    response {
        status 200
        headers {
            contentType 'application/json'
        }
        body([
                id: fromRequest().path(3),
                name: "Notebook",
                enabled: true
        ])
    }
}
package com.henio.algashop.product.catalog.contract.base;

import com.henio.algashop.product.catalog.application.ResourceNotFoundException;
import com.henio.algashop.product.catalog.application.category.management.CategoryInput;
import com.henio.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.henio.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.henio.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.henio.algashop.product.catalog.application.product.query.PageModel;
import com.henio.algashop.product.catalog.application.query.CategoryDetailOutputTestDataBuilder;
import com.henio.algashop.product.catalog.presentaion.CategoryController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CategoryQueryService categoryQueryService;

    @MockitoBean
    private CategoryManagementApplicationService categoryManagementApplicationService;

    public static final UUID validCategoryId = UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2");

    public static final UUID invalidCategoryId = UUID.fromString("21651a12-b126-4213-ac21-19f66ff4642e");

    public static final UUID createdCategoryId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockValidCategoryById();
        mockFilterCategories();
        mockCreateCategory();
        mockInvalidCategoryFindById();
        mockUpdateCategory();
        mockDisableCategory();
    }

    private void mockDisableCategory() {
        Mockito.doNothing()
                .when(categoryManagementApplicationService)
                .disable(Mockito.eq(validCategoryId));
    }

    private void mockUpdateCategory() {
        Mockito.doNothing()
                .when(categoryManagementApplicationService)
                .update(
                        Mockito.eq(validCategoryId),
                        Mockito.any(CategoryInput.class)
                );

        Mockito.when(categoryQueryService.findById(createdCategoryId))
                .thenReturn(CategoryDetailOutputTestDataBuilder.aCategory().build());
    }

    private void mockInvalidCategoryFindById() {
        Mockito.when(categoryQueryService.findById(invalidCategoryId))
                .thenThrow(new ResourceNotFoundException());
    }

    private void mockCreateCategory() {
        Mockito.when(categoryManagementApplicationService.create(any(CategoryInput.class)))
                .thenReturn(createdCategoryId);

        Mockito.when(categoryQueryService.findById(createdCategoryId))
                .thenReturn(CategoryDetailOutputTestDataBuilder.aCategory().build());
    }

    private void mockFilterCategories() {
        Mockito.when(categoryQueryService.filter(anyInt(), anyInt()))
                .then((answer) -> {
                    Integer size = answer.getArgument(0);
                    return PageModel.<CategoryDetailOutput>builder()
                            .number(0)
                            .size(size)
                            .totalPages(1)
                            .totalElements(2)
                            .content(
                                    List.of (
                                            CategoryDetailOutputTestDataBuilder.aCategory().build(),
                                            CategoryDetailOutputTestDataBuilder.aCategoryAlt().build()
                                    )
                            ).build();
                });
    }

    private void mockValidCategoryById() {
        Mockito.when(categoryQueryService.findById(validCategoryId))
                .thenReturn(CategoryDetailOutputTestDataBuilder.aCategory().id(validCategoryId).build());
    }
}

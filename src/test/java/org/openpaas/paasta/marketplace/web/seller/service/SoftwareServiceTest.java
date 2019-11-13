package org.openpaas.paasta.marketplace.web.seller.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openpaas.paasta.marketplace.api.domain.Category;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.openpaas.paasta.marketplace.api.domain.SoftwareHistory;
import org.openpaas.paasta.marketplace.api.domain.SoftwarePlan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SuppressWarnings({ "unchecked" })
public class SoftwareServiceTest extends AbstractMockTest {

    SoftwareService softwareService;

    @Mock
    ResponseEntity<CustomPage<Software>> softwearPageResponse;

    @Mock
    CustomPage<Software> softwareCustomPage;

    @Mock
    Page<Software> softwarePage;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        softwareService = new SoftwareService(paasApiRest);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createSoftware() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);

        when(paasApiRest.postForObject(eq("/softwares"), any(Software.class), eq(Software.class)))
                .thenReturn(software1);
        Software result = softwareService.createSoftware(software1);
        assertEquals(software1, result);
    }

    @Test(expected = RuntimeException.class)
    public void createSoftwareError() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);

        when(paasApiRest.postForObject(eq("/softwares"), any(Software.class), eq(Software.class)))
                .thenThrow(new IllegalStateException());
        softwareService.createSoftware(software1);
    }

    @Test
    public void getCategories() {
        List<Category> categoryList = new ArrayList<>();
        Category category1 = category(1L, "category-01");
        categoryList.add(category1);
        Category category2 = category(2L, "category-02");
        categoryList.add(category2);

        when(paasApiRest.getForObject(startsWith("/categories"), eq(List.class))).thenReturn(categoryList);

        List<Category> result = softwareService.getCategories();
        assertEquals(categoryList, result);
    }

    @Test(expected = RuntimeException.class)
    public void getCategoriesError() {
        when(paasApiRest.getForObject(startsWith("/categories"), eq(List.class)))
                .thenThrow(new IllegalStateException());

        softwareService.getCategories();
    }

    @Test
    public void getSoftwareList() {
        Category category = category(1L, "category-01");
        List<Software> softwareList = new ArrayList<>();
        Software software1 = software(1L, "software-01", category);
        softwareList.add(software1);
        Software software2 = software(2L, "software-02", category);
        softwareList.add(software2);

        when(paasApiRest.exchange(startsWith("/softwares/my/page"), eq(HttpMethod.GET), eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(softwearPageResponse);
        when(softwearPageResponse.getBody()).thenReturn(softwareCustomPage);
        when(softwareCustomPage.getContent()).thenReturn(softwareList);

        CustomPage<Software> result = softwareService.getSoftwareList("?nameLike=software");
        assertEquals(softwareList, result.getContent());
    }

    @Test
    public void getSoftware() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);

        when(paasApiRest.getForObject(eq("/softwares/1"), eq(Software.class))).thenReturn(software1);

        Software result = softwareService.getSoftware(1L);
        assertEquals(software1, result);
    }

    @Test
    public void updateSoftware() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);

        doNothing().when(paasApiRest).put(eq("/softwares/1"), any(Software.class));

        Software result = softwareService.updateSoftware(1L, software1);
        assertEquals(software1, result);
    }

    @Test
    public void updateSoftware2() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);

        doNothing().when(paasApiRest).put(startsWith("/softwares/1"), any(Software.class));

        Software result = softwareService.updateSoftware(1L, "1\\^2\\^3\\^4", software1);
        assertEquals(software1, result);
    }

    @Test
    public void getSoftwarePlan() {
        Category category = category(1L, "category-01");
        software(1L, "software-01", category);
        SoftwarePlan softwarePlan1 = softwarePlan(1L, 1L);

        when(paasApiRest.getForObject(startsWith("/softwares/plan/1"), eq(SoftwarePlan.class)))
                .thenReturn(softwarePlan1);

        SoftwarePlan result = softwareService.getSoftwarePlan(1L);
        assertEquals(softwarePlan1, result);
    }

    @Test
    public void getSoftwarePlanList() {
        Category category = category(1L, "category-01");
        software(1L, "software-01", category);
        List<SoftwarePlan> softwarePlanList = new ArrayList<>();
        SoftwarePlan softwarePlan1 = softwarePlan(1L, 1L);
        softwarePlanList.add(softwarePlan1);
        SoftwarePlan softwarePlan2 = softwarePlan(2L, 1L);
        softwarePlanList.add(softwarePlan2);

        when(paasApiRest.getForObject(startsWith("/softwares/plan/1/list"), eq(List.class)))
                .thenReturn(softwarePlanList);

        List<SoftwarePlan> result = softwareService.getSoftwarePlanList(1L);
        assertEquals(softwarePlanList, result);
    }

    @Test
    public void getHistoryList() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);
        List<SoftwareHistory> softwareHistoryList = new ArrayList<>();
        SoftwareHistory softwareHistory1 = softwareHistory(1L, software1);
        softwareHistoryList.add(softwareHistory1);
        SoftwareHistory softwareHistory2 = softwareHistory(2L, software1);
        softwareHistoryList.add(softwareHistory2);

        when(paasApiRest.getForObject(startsWith("/softwares/1/histories"), eq(List.class)))
                .thenReturn(softwareHistoryList);

        List<SoftwareHistory> result = softwareService.getHistoryList(1L, "?sort=id,asc");
        assertEquals(softwareHistoryList, result);
    }

    @Test
    public void getPlanHistoryList() {
        Category category = category(1L, "category-01");
        software(1L, "software-01", category);
        List<SoftwarePlan> softwarePlanList = new ArrayList<>();
        SoftwarePlan softwarePlan1 = softwarePlan(1L, 1L);
        softwarePlanList.add(softwarePlan1);
        SoftwarePlan softwarePlan2 = softwarePlan(2L, 1L);
        softwarePlanList.add(softwarePlan2);

        when(paasApiRest.getForObject(startsWith("/softwares/plan/1/histories"), eq(List.class)))
                .thenReturn(softwarePlanList);

        List<SoftwarePlan> result = softwareService.getPlanHistoryList(1L, "?sort=id,asc");
        assertEquals(softwarePlanList, result);
    }

    @Test
    public void getApplyMonth() {
        Category category = category(1L, "category-01");
        software(1L, "software-01", category);
        List<SoftwarePlan> softwarePlanList = new ArrayList<>();
        SoftwarePlan softwarePlan1 = softwarePlan(1L, 1L);
        softwarePlanList.add(softwarePlan1);
        SoftwarePlan softwarePlan2 = softwarePlan(2L, 1L);
        softwarePlanList.add(softwarePlan2);

        when(paasApiRest.getForObject(startsWith("/softwares/plan/1/applyMonth"), eq(List.class)))
                .thenReturn(softwarePlanList);

        List<SoftwarePlan> result = softwareService.getApplyMonth(1L, "201911");
        assertEquals(softwarePlanList, result);
    }

    @Test
    public void deleteSwpId() {
        doNothing().when(paasApiRest).delete(eq("/softwares/plan/1"));

        softwareService.deleteSwpId(1L);
    }

}

package org.openpaas.paasta.marketplace.web.seller.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openpaas.paasta.marketplace.api.domain.Category;
import org.openpaas.paasta.marketplace.api.domain.CustomPage;
import org.openpaas.paasta.marketplace.api.domain.Instance;
import org.openpaas.paasta.marketplace.api.domain.Software;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.Silent.class)
@SuppressWarnings("unchecked")
public class StatsServiceTest extends AbstractMockTest {

    StatsService statsService;

    @Mock
    SoftwareService softwareService;

    @Mock
    ResponseEntity<CustomPage<Software>> softwarePageResponse;

    @Mock
    ResponseEntity<CustomPage<Instance>> instancePageResponse;

    @Mock
    ResponseEntity<Map<Long, Long>> longLongMapResponse;

    @Mock
    CustomPage<Software> softwareCustomPage;

    @Mock
    CustomPage<Instance> instanceCustomPage;

    boolean instanceListEmpty;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        statsService = new StatsService(softwareService, paasApiRest);

        instanceListEmpty = false;
    }

    @Test
    public void getCategories() {
        List<Category> categoryList = new ArrayList<>();
        Category category1 = category(1L, "category-01");
        categoryList.add(category1);
        Category category2 = category(2L, "category-02");
        categoryList.add(category2);

        when(paasApiRest.getForObject(startsWith("/categories"), eq(List.class))).thenReturn(categoryList);

        List<Category> result = statsService.getCategories();
        assertEquals(categoryList, result);
    }

    @Test(expected = RuntimeException.class)
    public void getCategoriesError() {
        when(paasApiRest.getForObject(startsWith("/categories"), eq(List.class)))
                .thenThrow(new IllegalStateException());

        statsService.getCategories();
    }

    @Test
    public void getCountsOfInsts() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        Map<Long, Long> map = new TreeMap<>();
        map.put(1L, 7L);
        map.put(2L, 13L);

        when(paasApiRest.getForObject(startsWith("/stats/instances/my/counts/ids"), eq(Map.class))).thenReturn(map);

        Map<Long, Long> result = statsService.getCountsOfInsts(ids);
        assertEquals(map, result);
    }

    @Test
    public void countsOfInstsProviderMonthly() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        Map<Long, Long> map = new TreeMap<>();
        map.put(1L, 7L);
        map.put(2L, 13L);

        when(paasApiRest.getForObject(startsWith("/stats/instances/my/counts/months"), eq(Map.class))).thenReturn(map);

        Map<String, Object> result = statsService.countsOfInstsProviderMonthly(ids);
        assertEquals(map, result);
    }

    @Test
    public void getCountOfInstsUsing() {
        when(paasApiRest.getForObject(eq("/stats/instances/my/counts/sum"), eq(long.class))).thenReturn(7L);

        long result = statsService.getCountOfInstsUsing();
        assertEquals(7L, result);
    }

    @Test
    public void getInstanceListBySwId() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);
        List<Instance> instanceList = new ArrayList<>();
        Instance instance1 = instance(1L, software1);
        instanceList.add(instance1);
        Instance instance2 = instance(2L, software1);
        instanceList.add(instance2);

        when(paasApiRest.exchange(startsWith("/instances/page"), eq(HttpMethod.GET), eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(instancePageResponse);
        when(instancePageResponse.getBody()).thenReturn(instanceCustomPage);
        when(instanceCustomPage.getContent()).thenReturn(instanceList);

        CustomPage<Instance> result = statsService.getInstanceListBySwId("?nameLike=instance");
        assertEquals(instanceList, result.getContent());
    }

    @Test
    public void getCountOfUsersUsing() {
        when(paasApiRest.getForObject(eq("/stats/users/my/counts/sum"), eq(long.class))).thenReturn(7L);

        long result = statsService.getCountOfUsersUsing();
        assertEquals(7L, result);
    }

    @Test
    public void getSoftwareList() {
        Category category = category(1L, "category-01");
        List<Software> softwareList = new ArrayList<>();
        Software software1 = software(1L, "software-01", category);
        softwareList.add(software1);
        Software software2 = software(2L, "software-02", category);
        softwareList.add(software2);

        // FIXME: url
        when(paasApiRest.exchange(startsWith(" /stats/softwares/my"), eq(HttpMethod.GET), eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(softwarePageResponse);
        when(softwarePageResponse.getBody()).thenReturn(softwareCustomPage);
        when(softwareCustomPage.getContent()).thenReturn(softwareList);

        CustomPage<Software> result = statsService.getSoftwareList("?nameLike=software");
        assertEquals(softwareList, result.getContent());
    }

    @Test
    public void getInstance() {
        Category category = category(1L, "category-01");
        Software software1 = software(1L, "software-01", category);
        Instance instance1 = instance(1L, software1);

        when(paasApiRest.getForObject(eq("/instances/1"), eq(Instance.class))).thenReturn(instance1);

        Instance result = statsService.getInstance(1L);
        assertEquals(instance1, result);
    }

    @Test
    public void soldInstanceByProvider() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        Map<Long, Long> map = new TreeMap<>();
        map.put(1L, 7L);
        map.put(2L, 13L);

        when(paasApiRest.getForObject(startsWith("/stats/instances/sold/count"), eq(Map.class))).thenReturn(map);

        Map<Long, Object> result = statsService.soldInstanceByProvider(ids);
        assertEquals(map, result);
    }

    @Test
    public void countOfSoldSw() {
        Category category = category(1L, "category-01");
        List<Software> softwareList = new ArrayList<>();
        Software software1 = software(1L, "software-01", category);
        softwareList.add(software1);
        Software software2 = software(2L, "software-02", category);
        softwareList.add(software2);

        List<Instance> instanceList = new ArrayList<>();
        if (!instanceListEmpty) {
            Instance instance1 = instance(1L, software1);
            instanceList.add(instance1);
            Instance instance2 = instance(2L, software2);
            instanceList.add(instance2);
        }

        when(softwareService.getSoftwareList(any(String.class))).thenReturn(softwareCustomPage);
        when(softwareCustomPage.getContent()).thenReturn(softwareList);

        when(paasApiRest.exchange(startsWith("/instances/page"), eq(HttpMethod.GET), eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(instancePageResponse);
        when(instancePageResponse.getBody()).thenReturn(instanceCustomPage);
        when(instanceCustomPage.getContent()).thenReturn(instanceList);
       
        when(statsService.countOfSoldSw(any(String.class))).thenReturn(instanceList.size());

        long result = statsService.countOfSoldSw(userId);
        if (!instanceListEmpty) {
            assertEquals(2L, result);
        } else {
            assertEquals(0L, result);
        }
    }

    @Test
    public void countOfSoldSwInstanceListEmpty() {
        instanceListEmpty = true;

        countOfSoldSw();
    }

    @Test
    public void soldInstanceCountOfSw() {
        when(paasApiRest.getForObject(eq("/stats/software/1/sold/counts/sum"), eq(long.class))).thenReturn(7L);

        long result = statsService.soldInstanceCountOfSw(1L);
        assertEquals(7L, result);
    }

    @Test
    public void getSalesAmount() {
        Map<Long, Long> map = new TreeMap<>();
        map.put(1L, 7L);
        map.put(2L, 13L);
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        when(paasApiRest.exchange(startsWith("/stats/softwares/sales-amount"), eq(HttpMethod.GET), eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(longLongMapResponse);
        when(longLongMapResponse.getBody()).thenReturn(map);

        Map<Long, Long> result = statsService.getSalesAmount(ids, "?sort=id,asc");
        assertEquals(map, result);
    }

    @Test
    public void getSoftwareUsagePriceTotal() {
        // FIXME: url
        when(paasApiRest.getForObject(eq("/stats/1/softwareUsagePriceTotal"), eq(long.class))).thenReturn(7L);

        long result = statsService.getSoftwareUsagePriceTotal(1L);
        assertEquals(7L, result);
    }

}

package org.openpaas.paasta.marketplace.web.seller.storageApi.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class ObjectMapperUtils {
    public static <T> T parseObject(String string, Class<T> clazz) throws IOException {
        assertNotNull(string, clazz);
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(string.getBytes(), clazz);
    }
    
    public static <T> String writeValueAsString(T object) throws IOException {
        assertNotNull(object);
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString( object );
    }
}

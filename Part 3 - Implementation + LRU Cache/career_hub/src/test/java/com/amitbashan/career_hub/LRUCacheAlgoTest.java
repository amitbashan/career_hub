package com.amitbashan.career_hub;

import com.amitbashan.career_hub.caching.LRUCacheAlgo;
import org.junit.jupiter.api.Test;

public class LRUCacheAlgoTest {
    @Test
    public void testMyCacheAlgo() {
        LRUCacheAlgo<String, String> cacher = new LRUCacheAlgo<>(2);

        cacher.showCacheContent();

        cacher.put("Senior Software Engineer", "Write professional code!");
        cacher.put("Junior Software Engineer", "Vibe code!");

        cacher.get("Junior Software Engineer");
        cacher.get("Junior Software Engineer");

        cacher.get("Senior Software Engineer");

        cacher.showCacheContent();

        cacher.put("Troll", "haha");

        cacher.showCacheContent();

        cacher.put("LOL", "LMAO");

        cacher.showCacheContent();
    }
}

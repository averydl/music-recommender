package com.derek.recommender;
import org.junit.*;

import java.util.Set;

import static org.junit.Assert.*;

public class RecommenderTest {
    public Recommender r;
    @Before
    public void setup() {
        r = new Recommender(
                "src/test/dataset/test_friends.dat",
                "src/test/dataset/test_artists.dat",
                "src/test/dataset/test_listens.dat"
        );
    }

    @Test
    public void testFriendGraphSize() {
        // there are 10 total users
        assertEquals(10, r.userSize());
    }

    @Test
    public void testTop10() {
        Set<Integer> top10 = r.listTop10();
        // artists 1 through 10 should be top 10
        for(int i = 1; i <= 10; i++) {
            assert(top10.contains(i));
        }
    }

    @Test
    public void testListFriends() {
        // user 1 should have two friends (users 2 and 3)
        Set<Integer> friends = r.friends(1);
        assert(friends.contains(2));
        assert(friends.contains(3));
    }

    @Test
    public void testListArtists() {
        // users 1 and 2 listen to artist 2
        assert(r.artists(1, 2).contains(2));
    }

    @Test
    public void testRecommend10() {
        // top artists for user 5 should be those in the array 'sharedTop10'
        int[] sharedTop10 = {1, 11, 2, 12, 3, 13, 4, 5, 15};
        Set<Integer> artists = r.recommend10(1);
        System.out.println(artists);
        for(Integer artist : sharedTop10) {
            assert(artists.contains(artist));
        }

    }

}
